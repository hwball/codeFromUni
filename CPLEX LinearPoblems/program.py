import sys
import numpy
import subprocess

def start_up():
    """runs on start up of the program, gets the
    needed inputs from the user"""
    print("Start.")
    num_of_source_nodes = get_number()
    num_of_transit_nodes = get_number()
    num_of_destination_nodes = get_number()
    
    return num_of_source_nodes, \
           num_of_transit_nodes,\
           num_of_destination_nodes
            
def get_number():
    """gets a positive integer from the user"""
    needs_input = True
    
    #runs a loop untill a valid number is gotten
    while(needs_input):
        input_number = input("Enter a number: ")
        try:
            number = int(input_number)
            if number < 0:
                print("The number {} is not positive. \
Please try again".format(input_number))
                continue
            needs_input = False
        except ValueError:
            print("The value {} is not a number. \
Please try again".format(input_number))           
    return number
    
def build_lp_file(x, y, z):
    """ takes the input from the user and 
    generates a LP file from it and outputs the file name"""
    ##NOTE: This program works for x, y, z < 25
    all_links = []
    dest_nodes = [] 
    source_nodes = []
    transit_nodes = []    
    
    #builds a list of source nodes
    for i in range(x):
        source_nodes.append(str(i+1))
    #builds a list of transit nodes
    for i in range(y):
        transit_nodes.append(str(chr(i+1+64)))    
    #builds a list of transit nodes
    for i in range(z):
        dest_nodes.append(str(chr(i+1+96)))      
              
    #build a list of auxiliary variables (the load on transit nodes)
    aux_var = []
    for transit in transit_nodes:
        aux_var.append("y"+transit)      
            
    #builds a list of all decision variables
    links = []
    for source in source_nodes:
        for transit in transit_nodes:
            for dest in dest_nodes:
                links.append("x"+source+transit+dest)  
                
    #builds a list of demand volumes
    demand_vol = []
    demand_vol_values = {}
    for source in source_nodes:
        for dest in dest_nodes:    
            demand_vol.append("h"+source+dest)
            
    #builds a list of binaries variables for all decision variables
    binaries = []
    for k in demand_vol:
        k_source = []
        k_links = []
        for link in links:
            if k[1] == link[1]:
                k_source.append(link)
            if k[1] == link[3]:
                k_source.append(link)
        for k_s in k_source: 
            if k_s[-1] == k[-1]:
                k_links.append(k_s) 
                
        for p in k_links:
            binaries.append("u"+k+p)
    ########################################################
    lp_file_str = "Minimize\n"
    lp_file_str += "  " + "r\n"
    
    ########################################################
    lp_file_str += "Subject to\n"
    #build list of demand volume constraits
    for h in demand_vol:
        #gets a list of all links that have source nodes
        #the same as h source node
        h_source = []
        for link in links:
            if h[1] == link[1]:
                h_source.append(link)
        #gets a list of all links that have dest nodes
        #the same as h dest node  
        h_string = ''
        for link in h_source:
            if h[-1] == link[-1]:
                h_string += link + " + "
        h_string = h_string[:-2] + "= " +  str( int(h[1]) + (ord(h[2]) -96))
        demand_vol_values[h] = str( int(h[1]) + (ord(h[2]) -96))
        lp_file_str += "  " + h_string + "\n"
    
    #total flow on transit node
    for var in aux_var:
        var_source = []
        var_links = []
        for link in links:
            if var[1] == link[2]:
                var_links.append(link)       
        str_var = ""
        for var_l in var_links:
            str_var += var_l + " + "
        lp_file_str += "  " + str_var[:-3] + " - " +var+" = 0" + "\n"       
    
    #build capacity list for source to tansit
    st_cap_list = []
    for node in source_nodes:
        for node2 in transit_nodes:
            cap = "c" + node + node2
            st_cap_list.append(cap)
        
    #build capacity list for tansit to dest
    td_cap_list = []
    for node in transit_nodes:
        for node2 in dest_nodes:
            cap = "d" + node + node2
            td_cap_list.append(cap) 
    
    #builds a list with all capacities
    cap_list = []
    for i in range(len(st_cap_list)): 
        cap_list.append((st_cap_list[i], td_cap_list[i]))
        
    #add in the auxiliary variable constraints to the file
    for var in aux_var:
        aux_str = '  ' + var + ' - r <= 0\n'
        lp_file_str += aux_str
    
    #flow is less than capacity 
    for cap in cap_list:
        c_cap_str = '  '
        for link in links:
            if cap[0][1:] == link[1:-1]:
                c_cap_str += link + " + "
        c_cap_str = c_cap_str[:-3] + " - " + cap[0] + " = 0\n"
        
        d_cap_str = '  '
        for link in links:
            if cap[1][1:] == link[2:]:
                d_cap_str += link + " + "
        d_cap_str = d_cap_str[:-3] + " - " + cap[1] + " = 0\n"
        
        lp_file_str += c_cap_str
        lp_file_str += d_cap_str
        
    #only 3 transit nodes can be used to split the demand vol
    for k in demand_vol:
        binary_str = ''
        for binary in binaries:
            if k == binary[1:4]:
                binary_str += binary + " + "
        lp_file_str += "  " + binary_str[:-3] + " = 3\n"
                       
    #splits the flow over the links in use equally
    for link in links:
        for u in binaries:
            if u[4:] == link:
                lp_file_str += "  3 " + link + " - " +\
                    demand_vol_values[u[1:4]] + " " +\
                    u + " = 0" + "\n"
                  
                  
    ########################################################
    lp_file_str += "Bounds\n"
    
    for link in links:
        lp_file_str += "  " + "0 <= " + link  + "\n"  
    for var in aux_var:
        lp_file_str += "  " + "0 <= " + var  + "\n" 
    for cap in cap_list:
        lp_file_str += "  " + "0 <= " + cap[0]  + "\n" 
    for cap in cap_list:
        lp_file_str += "  " + "0 <= " + cap[1]  + "\n"         
        
    lp_file_str += "  0 <= r\n"
    
    
    ########################################################  
    lp_file_str +=  "Binaries\n" 
    
    for binary in binaries:
        lp_file_str += "  " + binary + "\n"
        
        
    ########################################################  
    lp_file_str += "End"
    
    #creates a lp file from the string
    file_name = "cosc364_a2.lp"
    file = open(file_name,"w") 
    file.write(lp_file_str) 
    file.close()
    
    return file_name

def use_cplex(file_name):
    """Takes a lp file name and preforms the cplex 
    calculations"""
    ##NOTE: the cplex dir is the one on my computer, 
    ##you will need to change it for yours
    args = \
    ['/home/cosc/student/hba56/CPLEX/cplex/bin/x86-64_linux/cplex',
            '-c', 'read '+ file_name, 'optimize', 
            'display solution variables -' ]
    p = subprocess.Popen(args) 

def main():
    #These are used to create a lp file with numbers entered after 
    #the program has started
    x, y, z = start_up() 
    file_name = build_lp_file(x, y, z)
    
    use_cplex(file_name)

if __name__ == "__main__":
    main()