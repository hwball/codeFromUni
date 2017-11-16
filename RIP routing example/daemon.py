import socket
import select
import sys
import pickle
import time
import random
import errno
import copy
from collections import namedtuple
from socket import error as socket_error


class RipPacket(object):  # RIPv2
    def __init__(self, command, version, rip_entry, addr_family_iden,\
                 address, next_hop, metric):
        self.command = command
        self.version = version
        self.rip_entry = rip_entry
        self.addr_family_iden = addr_family_iden
        self.address = address
        self.next_hop = next_hop
        self.metric = metric
        
        if isinstance(self.command, int) == False:
            raise Exception("Error: command")
        if self.command not in [1, 2]:
            raise Exception("Error: command")
        if self.version != 0x02:  # for RIPv2
            raise Exception("Error: version")
        if self.addr_family_iden != 2: # for IP protocol
            raise Exception("Error: address family identified")
        if 0 < self.metric > 16:
            raise Exception("Error: metric")        


class RipRouting(object):
    def __init__(self, file):
        """This is where the program is set up and called. Most of 
        the function initialisations (such as neighbour addresses) 
        can be found here so that they can be accessed by other 
        functions."""
        
        # intial setup **********************************************
        self.processed_config = self.process_config(file)
        
        # the format of the config file is wrong
        if self.processed_config == []:
            return 
        
        self.router_id = self.processed_config[0][0]
        self.routing_table = self.build_routing_table( \
            self.processed_config)
        print("Starting routing table:")
        self.print_routing_table(self.routing_table)
        
        # sockets ***************************************************
        self.input_sockets = {}
        self.input_addresses = {}
        self.output_addresses = {}
        self.neighbour_addresses = {} 
        
        self.inputs_list = []
        self.outputs_list = []
        self.data_buffer = []  
        
        self.send_socket = None  
        
        # trigger ***************************************************
        self.do_triggered_update = False
        
        # trigger intialisation
        self.trigger_interval = None  
        self.trigger_time_start = 0
        self.trigger_time_passed = 0        
        
        # timer intialisation ***************************************
        self.router_timeout = 4
        self.router_metric = 1
        
        # create an offset for the timers so they are not all
        # updating at the same time
        self.offset = random.randint(-1,1)
        self.timeout = 8 + self.offset 
        self.expire_time = 48           
        self.garbage_time = 32        
    
        # time tracked by checking time at start and updateing
        # time passed when it is checked again
        self.time_start = time.time()
        self.time_passed = 0           

        # run program ***********************************************
        self.run()        

    def process_config(self, file):
        """This function processes the configuration file. It ignores 
        comments and empty lines in the file. It returns the 
        router-id, input-ports and outputs in a list with a length of 
        3. Otherwise it will print an error message and return an 
        empty list."""
        config_list = [[], [], []]
        router_id = ""
        input_ports = [] 
        outputs = []
        
        for parameter in file.readlines():  
            parameter = parameter.split()
            if '#' in parameter:
                index = parameter.index('#')
                # take any item up to the comment '#'
                parameter = parameter[:index]   
            if 'router-id' in parameter:
                found_index = parameter.index('router-id')
                router_id = parameter[found_index + 1]
            elif 'input-ports' in parameter:
                found_index = parameter.index('input-ports')
                for port in parameter[found_index + 1:]:
                    if "," in port:
                        port = port.replace(",", "")
                    input_ports.append(port)
            elif 'outputs' in parameter:
                found_index = parameter.index('outputs')
                for output in parameter[found_index + 1:]:
                    output = output.split('-')
                    if "," in output[2]:
                        output[2] = output[2].replace(",", "")
                    outputs.append(tuple(output))
        
        results = [self.check_parameter(router_id, 'router_id'), \
                   self.check_parameter(input_ports, 'input_ports'), \
                   self.check_parameter(outputs, 'outputs')]
        
        if all(results):
            config_list[0].append(int(router_id))
            
            for port in input_ports:
                config_list[1].append(int(port))
            
            for output in outputs:
                output = (int(output[0]), int(output[1]), \
                          int(output[2]))
                config_list[2].append(output)
        else:
            print("ERROR: The configuration file is wrong!")
            config_list = [] 
            file.close()
            return config_list

        file.close()                    
        return config_list      
        
    def check_parameter(self, parameter, param_type):
        """Checks if the everything in the parameter can be converted 
        from a string to integer. Also, checks if the different port
        numbers satisfy specific conditions. Returns True or False."""
        result = None
        
        if param_type == 'router_id':
            try:
                if (1 <= int(parameter) <= 64000):
                    result = True
                else:
                    result = False
            except ValueError:
                return False
            
        elif param_type == 'input_ports':
            ports = []
            try:
                for port in parameter:
                    if (1024 <= int(port) <= 64000) and \
                       result == None:
                        ports.append(port)
                        result = True
                    elif result == None:
                        result = False
                    elif port in ports:
                        result = False
            except ValueError:
                return False
            
        elif param_type == 'outputs':
            outputs = []
            try:
                for output in parameter:
                    if (1024 <= int(output[0]) <= 64000) and\
                        output[1] and int(output[2]):
                        outputs.append(output)
                        result = True
                    elif int(output[0]) > 64000 or \
                         int(output[0]) < 1024:
                        result = False
                    elif result == None:
                        result = False
                    elif output in outputs:
                        result = False
            except ValueError:
                return False
        
        # determines if the parameters in the config file are right    
        if result:
            return True
        else:
            return False    
            
    def build_routing_table(self, config_list):
        """Builds a routing table from the information gained from 
           the config file. The format of the table is a dictionary
           with a key of the destination_address and values of
           [destination_address, metric(cost), next_hop, 
           router_change_flag, timers]
        """
        routing_table = {}
        router_id = config_list[0]
        input_ports = config_list[1]
        output_ports = config_list[2]
        for output_port in output_ports:
            routing_table[output_port[2]] = [output_port[2],
                                             output_port[1], 
                                        output_port[2], False, 0, 0]
        return routing_table
    
    def print_routing_table(self, routing_table):
        """Prints a given routing table out"""
        print("--------------------------------------------------\
------------")
        print("|  Dest  | Cost | Next Hop | Change Flag | Timeout \
| Garbage |")
        print("---------------------------------------------------\
-----------")
        for entry, info in sorted(routing_table.items()):
            print("| {:>6} | {:>4} | {:>8} | {:>11} | {:>7} | {:>7} \
|".format(info[0],
                 info[1],info[2],str(info[3]),str(round(info[4], 2)),
                 str(round(info[5], 2) )))
        print("---------------------------------------------------\
-----------") 
    
    def check_response_message(self, response):
        """In section 3.9.2. This function processes response 
        messages. Returns if the response message is in the wrong 
        format. The response message is either an response (1), 
        triggered (1) or a request (2)."""
        r_addr = 0
        r_metric = 1
        r_id = 1
        next_hop = 2
        flag = 3
        timeout = 4
        garbage_time = 5
        
        # this is the input address of the response packet
        router_addr = None  
        infinity = 16  # 16 hops represent infinity 
        
        # validity checks response message
        if response.command not in [1, 2]:
            print("Response Message Error: command is incorrect")
            return
        if response.version != 0x02:
            print("Response Message Error: version is incorrect")
            return
        if isinstance(response.rip_entry, RipPacket):
            print("Response Message Error: entry is not a rip packet")
            return       
        if response.metric > 16:
            print("Response Message Error: metric > 16")
            return 
                
        # check if the sender is a neighbour, if not, print message 
        #and return back
        found = False
        for info in self.neighbour_addresses.items():
            if response.address == info[1][r_id] and\
               router_addr == None:
                router_addr = info[r_addr]
                found = True
                
        if found == False:
            print("Reponse Message Error: {} is not a \
            neighbour".format(response.address))
            
            if response.address == self.router_id:
                print("-> cannot send to itself")             
            return
        
        # process routing table entries *****************************
        
        # Note: the neighbour does not put itself in the routing 
        #table. So we reset the timeout of the neighbour separately.
        if response.address in self.routing_table:
            self.routing_table[response.address][r_metric] = \
                response.metric
            self.routing_table[response.address][flag] = False
            self.routing_table[response.address][timeout] = 0
            self.routing_table[response.address][garbage_time] = 0
            
        # if neighbour is not in the router table 
        #(as it was previously removed) then...
        else:
            self.routing_table[response.address] = [response.address,\
                                                    response.metric, \
                                                    response.address,\
                                                    True, 0, 0]
            self.add_data("Triggered", self.routing_table)
            self.do_triggered_update = True
            
        # finally process the entries now
        for addr, entry in response.rip_entry.items():
            if 1 <= addr <= 64000 and \
               1 <= entry[r_metric] <= infinity:
                
                # calculate the metric
                metric = min(entry[r_metric] + response.metric, \
                             infinity)
                
                # Note: addr != self.router_id = not to include 
                #itself in the routing table too.
                if metric != infinity and addr not in\
                   self.routing_table and addr != self.router_id:
                    self.routing_table[addr] = [entry[0], metric,
                                                response.address, 
                                                False, 0, 0]
                    
                elif addr in self.routing_table:
                    
                    # if response message is from the same router as
                    #the existing route
                    if self.routing_table[addr][next_hop] == \
                       response.address:
                        self.routing_table[addr][timeout] = 0
                        
                        # if the new metric is different than the 
                        #old one
                        if self.routing_table[addr][r_metric] != \
                           metric:
                            self.routing_table[addr][r_metric] = \
                                metric

                            # if not infinity
                            # Note: the trigger_loop function will 
                            #take care of the
                            # deletion process if metric is infinity
                            if metric != infinity:
                                self.routing_table[addr][timeout] = 0 
                                
                                # do garbage timer as well if it is 
                                #set or not.
                                self.routing_table[addr]\
                                    [garbage_time] = 0
                               
                            else:
                                self.routing_table[addr][flag] = True
                                self.routing_table[addr][timeout] = \
                                    self.expire_time
                                
                                # do triggered update
                                self.add_data("Triggered", \
                                              self.routing_table)
                                self.do_triggered_update = True                                
                    
                    # or if the new metric is lower than the old one
                    elif self.routing_table[addr][r_metric] > metric \
                         and addr not in self.neighbour_addresses:
                        self.routing_table[addr][r_metric] = metric
                        self.routing_table[addr][next_hop] = \
                            response.address

                        if metric != infinity:
                            self.routing_table[addr][timeout] = 0 
                            
                             # do garbage timer as well if it is set
                             #or not.
                            self.routing_table[addr][garbage_time] = 0 
                            
                        else:
                            self.routing_table[addr][flag] = True
                            self.routing_table[addr][timeout] = \
                                self.expire_time   
                            
                            # do triggered update
                            self.add_data("Triggered", \
                                          self.routing_table)
                            self.do_triggered_update = True                            
              
    def separate_routing_table(self, routing_table):
        """This function separares the routing table if it has more
        than 25 entries. It returns a list of routing_tables"""
        rt_list = []    #rt for routing table
        new_routing_table = {}
        entry_count = 1 #counts the number of entries added to rt_list
        
        for addr, entry in routing_table.items():
            if entry_count <= 25:
                new_routing_table[addr] = entry
                entry_count += 1
            else:
                # add to list and reset
                rt_list.append(new_routing_table)
                entry_count = 1
                new_routing_table = {}
                
                # begin adding again
                new_routing_table[addr] = entry
                entry_count += 1
        
        if entry_count <= 25:
            rt_list.append(new_routing_table)
                
        return rt_list
            
    def add_data(self, p_type, data, addr=None):
        """This function creates packets (with data) then adds it to
        the data buffer to be sent to its neighbour. The number of 
        send_sockets added to the outputs_list will be the same as 
        the number of packets needed to be sent.
        p_type will either be a "Response", "Request" or a "Triggered" 
        update."""
        cost = 1
        flag = 3
        
        if p_type == "Response":
            for rt in self.separate_routing_table(data):
                
                for addr in self.neighbour_addresses.items():
                    # check if neighbour is in routing table
                    if addr[1][1] in data: 
                        new_packet = RipPacket(2,       # command
                                               0x02,    # version
                                               rt,      # rip_entry
                                               2,   # addr_family_iden
                                               self.router_id,#address
                                               addr[1][0][1],#next_hop
                                               data[addr[1][1]][cost])   
                                               #metric
                                            
                        self.outputs_list.append(self.send_socket)
                        self.data_buffer.append(new_packet)
                    
        elif p_type == "Triggered":
            self.time_passed += time.time() - self.time_start
            triggered_routing_table = {}
        
            # add all routing table entries with flags set to true 
            #to a new table to send
            for addr, entry in data.items():
                if entry[flag] == True:
                    triggered_routing_table[addr] = entry
                    entry[flag] = False     # flags cleared
            
            # do triggered update only if a regular update is not 
            # due and there are no messages to send in the queue
            if self.time_passed < self.router_timeout and \
               self.outputs_list == []:   
                
                # begin creating the packets
                for rt in self.separate_routing_table(\
                    triggered_routing_table):
                    for addr in self.neighbour_addresses.items():
                        # check if neighbour is in routing table
                        if addr[1][1] in data: 
                            new_packet = RipPacket(2,       
                                                   0x02,           
                                                   rt,                   
                                                   2,                   
                                                   self.router_id,        
                                                   addr[1][0][1],        
                                                   data[addr[1][1]]\
                                                   [cost]) 
                            self.outputs_list.append(self.send_socket)
                            self.data_buffer.append(new_packet) 

                # if routing_table is empty (because neighbours is 
                # not reachable), then dont't do a triggered update
                if self.routing_table == {}:
                    self.do_triggered_update = False           
        
    def timer_loop(self):
        """Checks for timeouts for routers"""
        cost = 1
        flag = 3
        garbage = 5

        self.time_passed += time.time() - self.time_start
        
        # update every router in the table's timeout timer and 
        # garbage collection timer
        if self.time_passed >= self.timeout:
            for router, info in list(self.routing_table.items()):
                
                # check to see if router timer over 180
                if info[self.router_timeout] >= self.expire_time or\
                   info[self.router_metric] == 16:
                    
                    if info[self.router_metric] == 16 and \
                       info[self.router_timeout] < self.expire_time:
                        info[self.router_timeout] = self.expire_time
                    
                    # update the garbage timer
                    info[garbage] += round(self.time_passed, 1)
                    
                    # set the cost to this router to infinity (16)
                    info[cost] = 16
                    
                    # update change flag
                    info[flag] = True
                    self.do_triggered_update = True
                    
                    # check new garbage timer to see if it is over 
                    # self.expire_time if so remove it
                    if info[garbage] > round(self.garbage_time, 1):
                        del self.routing_table[router]
                else:
                    info[self.router_timeout] += round(\
                        self.time_passed, 1)
                
            self.time_passed = 0     # reset timer
            
            # prepare packet for sending if the routing table is 
            # not empty
            if self.routing_table != {}:
                self.add_data("Response", self.routing_table)

            #prints the rt every 8 seconds
            self.print_static_view_of_routing_table()
            
        self.time_start = time.time() 
    
    def triggered_update(self):
        """section 3.10.1 and 3.4.4."""
        if self.do_triggered_update == True:
            
            # if an interval is set, calculate the time
            if self.trigger_interval != None:
                self.trigger_time_passed += time.time() - \
                    self.trigger_time_start
            
            # if interval is not set, prepare packet for sending
            if self.trigger_interval == None:
                self.add_data("Triggered", self.routing_table)
            
            # if interval is set and time has passed, prepare packet 
            #for sending
            elif self.trigger_time_passed > self.trigger_interval:
                self.trigger_time_passed = 0
                self.trigger_interval = None
                self.add_data("Triggered", self.routing_table)        
    
    def print_static_view_of_routing_table(self):
        #creates a copy of routing table so the real one is not 
        #effected
        static_routing_table = copy.deepcopy(self.routing_table) 
        #need to import copy
       
        #update the timers for the static routing table
        for router, info in list(static_routing_table.items()):
            info[self.router_timeout] += round(self.time_passed, 2)
       
        #prints the static routing table
        self.print_routing_table(static_routing_table)        
    
    def run(self):
        """This function creates UDP sockets using information from 
        a processed configution file. It binds one socket to each 
        input port. Then it enters into an infinite loop"""
        # socket initialisation
        packet = 0
        inputs = 1
        outputs = 2
        send_addr = 1
        
        UDP_IP = "127.0.0.1"   
        select_timeout = 1
        
        # create sockets for input ports
        for input_port in self.processed_config[inputs]:
            self.input_sockets[input_port] = \
                socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
            self.input_addresses[input_port] = \
                (UDP_IP, input_port) # add input port to dict
            self.inputs_list.append(\
                self.input_sockets[input_port]) # for select()
            
            # assign an input socket that can be used for sending
            if self.send_socket == None:
                self.send_socket = self.input_sockets[input_port]
                
        # bind socket to each port (from outputs in the config file)
        for input_socket in self.input_sockets:
            self.input_sockets[input_socket].bind(\
                self.input_addresses[input_socket])
        
        # add each output (neighbours) port to dict
        for output_port in self.processed_config[outputs]:
            self.neighbour_addresses[output_port[0]] = \
                [(UDP_IP, output_port[0]), output_port[2]]
            self.output_addresses[output_port[0]] = \
                (UDP_IP, output_port[0])             
        
        # enters into an infinite loop
        while self.inputs_list:   
            read_list, write_list, x_list = select.select(\
                self.inputs_list, self.outputs_list, \
                self.inputs_list, select_timeout)
            
            # read all sockets from the inputs_list
            for read_socket in read_list:
                try:
                    data = read_socket.recv(1024)
                    decode_data = pickle.loads(data)  # not complete
                    if decode_data.version in [1, 2]:
                        # used the data to update routing table
                        self.check_response_message(decode_data)
                except socket_error as serr:
                    if serr.errno != errno.ECONNREFUSED:
                        pass
    
            # send data if there is data in the packet_buffer
            for write_socket in write_list:
                data = self.data_buffer
                try:
                    encoded_packet = pickle.dumps(data[0]) 
                    # get first data in the queue
                    write_socket.sendto(encoded_packet, \
                                        self.output_addresses[\
                                            data[0].next_hop])
                    self.data_buffer.pop(0)
                    self.outputs_list.pop(0)   
                except socket_error as serr:
                    if serr.errno != errno.ECONNREFUSED:
                        pass                    
            
            # if a triggered_update has been sent: turn off 
            # triggered update start random interval if it is not set.
            if self.do_triggered_update == True:
                if self.trigger_interval == None:
                    self.trigger_interval = random.randint(1, 5)
                    self.trigger_time_start = time.time()
                self.do_triggered_update = False
            
            self.timer_loop()
            self.triggered_update()         
        
def main():
    """Main method to run when program starts"""
    config_file = open(sys.argv[1], 'r')
    RipRouting(config_file)
    
if __name__ == "__main__":
    main()
