"""Receiver prgram for getting data and 
    writing it to a file
"""
import argparse
import socket
import os
import sys
import struct
import select

IP = '127.0.0.1'
MAGICNO = 0x497E
DATATYPE = 0
ACKTYPE = 1
PACFOMAT = 'iiii{size}s'
HDR_LEN = 16

def receive(r_inPort, r_outPort, c_inPort, filename):
    receivedPack = 0
    #build senders sockets
    sockIn = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
    sockIn.bind((IP, r_inPort))
    sockOut = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
    sockOut.bind((IP, r_outPort))
    
    #sets up where to send
    sockOut.connect((IP, c_inPort))    
    
    #checks to see if the file already exists
    if os.path.isfile(filename):
        print("File already exists")
        sockIn.close()
        sockOut.close()        
        sys.exit()
    file = open(filename, "wb")
 
    expected = 0
    
    while True:
        try:
            readable,writeable,exceptional = select.select([sockIn],[],[], 1)
            if readable: 
                receivedPack += 1
                newpack, address = sockIn.recvfrom(1024)
                fmt = PACFOMAT.format(size=len(newpack)-HDR_LEN)
                mno, typ, seq, dal, ans = struct.unpack(fmt, newpack)
                if mno != MAGICNO:
                    continue
                elif typ != DATATYPE:
                    continue
                elif seq != expected:
                    packet = struct.pack('iiii0s', MAGICNO, 
                                        ACKTYPE, seq, 0,
                                                   b'')
                    sockOut.send(packet)
                    continue
                else:
                    packet = struct.pack('iiii0s', MAGICNO, 
                                         ACKTYPE, seq, 0, b'')
                    sockOut.send(packet)
                    expected = 1 - expected
                    if dal > 33:
                        #data = ans.decode("utf-8")
                        file.write(ans)#data[:dal-33])
                        continue
                    else:
                        print(receivedPack)
                        sockIn.close()
                        sockOut.close()
                        file.close()
                        sys.exit()
        except socket.error:
            print("Socket error. Shuting down.")
            sockIn.close()
            sockOut.close()            
            file.close()
            sys.exit()
        

def main():
    #gets the port numbers from command line
    parser = argparse.ArgumentParser()
    parser.add_argument("R_inPort", type=int, nargs='?',
                        help="the port sender.py \
                        listens on")    
    parser.add_argument("R_outPort", type=int,
                            help="the port \
                            sender.py connects through")
    parser.add_argument("C_inPort", type=int,
                            help="the port \
                            channel.py connects through")
    parser.add_argument("fileName", type=str,
                            help="file name to send")    
    args = parser.parse_args()
    

    
    #checks to see if they are valid and trys to
    #send them if they are
    if isinstance(args.R_inPort, int) and \
       isinstance(args.R_outPort, int) and \
       isinstance(args.C_inPort, int) and \
       1024 <= args.R_inPort <= 64000 and \
       1024 <= args.R_outPort <= 64000 and \
       1024 <= args.C_inPort <= 64000:
        receive(args.R_inPort, args.R_outPort,
             args.C_inPort, args.fileName)    
    

if __name__ == '__main__':
    main()