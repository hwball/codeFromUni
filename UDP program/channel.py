"""channel program, processes packets from sender and 
   decide whether to forward to receiver via sockets.  
   Vise versa."""

import socket
import sys
import select
import struct
import random


HOST = "127.0.0.1"
PACFORMAT = 'iiii{size}s'
HDR_LEN = 16

#input 7 port numbers and a P number
cs_in_port= int(sys.argv[1])
cs_out_port = int(sys.argv[2])
cr_in_port = int(sys.argv[3])
cr_out_port = int(sys.argv[4])
s_in_port = int(sys.argv[5])
r_in_port = int(sys.argv[6])
p = float(sys.argv[7])
         

def check_port(port_number, name):
    """check if the input port number is valid"""
    if port_number <1024 or port_number > 6000:
        print("port number of " + name + " is not valid")
        sys.exit()

     
def check_p(p):
    """check if the input p value is valid""" 
    if p >=1 or p < 0:
        print("p value must be 0 <= p < 1")
        sys.exit()

check_port(cs_in_port, "cs_in_port")
check_port(cs_out_port,"cs_out_port")
check_port(cr_in_port, "cr_in_port")
check_port(cr_out_port, "cr_out_port")
check_port(s_in_port, "s_in_port")
check_port(r_in_port, "r_in_port")

check_p(p)

#create sockets, bind them to the corresponding port
cs_in = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
cs_in.bind((HOST,cs_in_port))

cr_out = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
cr_out.bind((HOST,cr_out_port))

#set the default receiver of cr_out to r_in 
cr_out.connect((HOST,r_in_port)) 

cr_in = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
cr_in.bind((HOST,cr_in_port))

cs_out = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
cs_out.bind((HOST,cs_out_port))

#set the default receiver of cs_out to s_in
cs_out.connect((HOST, s_in_port))  

    
while True:
    try:
        readable,writeable,exceptional = select.select([cs_in,cr_in],[],[],0.5)
        for s in readable:
            if s == cs_in:
                packet, address = cs_in.recvfrom(1024)
                fmt = PACFORMAT.format(size=len(packet)-HDR_LEN)
                MAGICNO,DATATYPE,nexti,data_size,data = struct.unpack(fmt,packet) #deserialize
                print("MAGICNO: ",MAGICNO)
                print("DATATYPE: ",DATATYPE)
                print("nexti: " ,nexti)
                print("data_size ",data_size)
                print("data ",data)
                if MAGICNO == 0x497E:
                    u = random.uniform(0,1)
                    if u < p:
                        print("number u should not be less than p")
                    else:
                        packet = struct.pack(fmt, MAGICNO,DATATYPE,nexti,data_size,data) #serialize
                        print("sending pack to rec")
                        cr_out.send(packet)
                        print("sended to rec")
                else:
                    print("MAGICNO number is not 0x497E")
            
            elif s == cr_in:
                packet, address = cr_in.recvfrom(1024)
                fmt = PACFORMAT.format(size=len(packet)-HDR_LEN)
                MAGICNO,DATATYPE,nexti,data_size,data = struct.unpack(fmt,packet)
                print("MAGICNO: ",MAGICNO)
                print("DATATYPE: ",DATATYPE)
                print("nexti: " ,nexti)
                print("data_size ",data_size)
                print("data ",data)      
                if MAGICNO == 0x497E:
                    u = random.uniform(0,1)
                    if u < p:
                        print("number u should not be less than p")
                    else:
                        packet = struct.pack(fmt, MAGICNO,DATATYPE,nexti,data_size,data)     
                        print("sending pack to send")
                        cs_out.send(packet)
                        print("sended to sender")
                else:
                    print("MAGICNO number is not 0x497E")
                       
    except socket.error:
        print("sock error,shut down")
        cs_in.close()
        cr_out.close()
        cr_in.close()
        cs_out.close()
        sys.exit()


