"""sender.py for Introduction to Computer Networks and
   the Internet Assignment. Reads a file and sends it
   to a channel via sockets
"""

import argparse
import socket
import os.path
import sys
import struct
import select


IP = '127.0.0.1'
MAGICNO = 0x497E
DATATYPE = 0
ACKTYPE = 1
PACFOMAT = 'iiii{size}s'
HDR_LEN = 16


def send(s_inPort, s_outPort, c_inPort, filename):
    """Sets up the sockets and sends the file"""
    totalSentPacks = 0
    
    #build senders sockets
    sockIn = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
    sockIn.bind((IP, s_inPort))
    sockOut = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
    sockOut.bind((IP, s_outPort))
    
    #sets up where to send
    sockOut.connect((IP, c_inPort))
    
    #exits program if file does not exist
    if not os.path.isfile(filename):
        sockIn.close()
        sockOut.close()         
        sys.exit()
        
    nexti = 0
    exitFLAG = False
    
    #opens file and starts a loop that reads 512 bytes and
    #trys to send it via the sockets
    file = open(filename, "rb")  
    while exitFLAG == False:
        data = file.read(512)
        if sys.getsizeof(data) <= 33:
            fmt = PACFOMAT.format(size=len(data))
            packet = struct.pack(fmt, MAGICNO, 
                                 DATATYPE, nexti, 0,
                                 b'')
            exitFLAG = True
    
        else:
            fmt = PACFOMAT.format(size=len(data))
            packet = struct.pack(fmt, MAGICNO, 
                                 DATATYPE, nexti, 
                                 sys.getsizeof(data),
                                 data)
            
        #starts an inner loop that listens for the acknowledgement
        #packet and re-transmits if no acknowledgement is recieved 
        inner_exitFLAG = False
        sentPacCount = 0
        while inner_exitFLAG == False:
            sockOut.send(packet)
            sentPacCount += 1
            totalSentPacks += 1
            readable, _, _ = select.select([sockIn], [], [], 1)
            if readable:
                newpac, address = sockIn.recvfrom(1024)
                fmt = PACFOMAT.format(size=len(newpac)-HDR_LEN)
                mno, typ, seq, dal, ans = struct.unpack(fmt, newpac)
                if mno != MAGICNO:
                    continue
                elif typ != ACKTYPE:
                    continue
                elif dal > 33:
                    continue
                elif seq != nexti:
                    continue
                else:
                    nexti = 1 - nexti
                    inner_exitFLAG = True
            else:
                if exitFLAG == True and sentPacCount == 4:
                    print(str(sentPacCount),
                          "packets sent and no response,stopping program")
                    break 
                else:
                    continue
    print(totalSentPacks)
    sockIn.close()
    sockOut.close()     
    file.close()
    sys.exit()
        
    
    
    
    
    
    
def main():
    #gets the port numbers from command line
    parser = argparse.ArgumentParser()
    parser.add_argument("S_inPort", type=int, nargs='?',
                        help="the port sender.py \
                        listens on")    
    parser.add_argument("S_outPort", type=int,
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
    if isinstance(args.S_inPort, int) and \
       isinstance(args.S_outPort, int) and \
       isinstance(args.C_inPort, int) and \
       1024 <= args.S_inPort <= 64000 and \
       1024 <= args.S_outPort <= 64000 and \
       1024 <= args.C_inPort <= 64000:
        send(args.S_inPort, args.S_outPort,
             args.C_inPort, args.fileName)
    
if __name__ == '__main__':
    main()