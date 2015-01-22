import serial
import getopt
import sys
from dog import DogGateway

def usage():
    print "EnOcean Packet Sniffer"
    print "Usage: enOceanSniffer -p port -r baudrate"

    
def decode_message(message_bytes):
    '''
    Decodes EnOcean messages, only RPS messages are currently supported
    '''
    action = -1
    if(len(message_bytes) > 0):
        lenght = ord(message_bytes[1])
        # print ("Data Lenght:%d")%lenght
        rorg = message_bytes[6]
        # print ("Rorg:%s")%rorg.encode("hex")
        if(rorg.encode("hex") == "f6"):
            # print "Type: RPS"
            data = message_bytes[7] 
            # print ("Data: %s")%data.encode("hex")
            status = message_bytes[12]
            
            # print ("Status: %s")%(status.encode("hex"))
            print ("Sender id:"),
            for i in range(8,12):
                print message_bytes[i].encode("hex"),
            print "\n"
            
            senderId = message_bytes[8:12]
            status_byte = ord(status)
            nu = (status_byte & 0x10) >> 4
            t21 = (status_byte & 0x20) >> 5
            
            if(t21 == 1 and nu == 1):
                # get the data
                action = (ord(data) & 0xE0) >> 5
                
    return action


def main():
    
    # the serial port to attach to
    port = ""
    # the baudrate to use
    rate = 0
    # optional 
    dog_gateway = None
    dog_device_uri = None
    
    # parse arguments
    try:
        opts, args = getopt.getopt(sys.argv[1:], "h:p:r:a:d:", ["help", "port=", "rate=", "address=", "device="])
    except getopt.GetoptError as err:
        # print help information and exit:
        print str(err)  # will print something like "option -a not recognized"
        usage()
        sys.exit(2)
        
    # handle command-line options
    for o, a in opts:
        # handle help
        if o in ("-h", "--help"):
            
            # show usage
            usage()
            
            # exit with success code
            sys.exit(0)
            
        # handle configuration data
        elif o in ("-p", "--port"):
            
            # store the port value
           port = a
        
        elif o in ("-r", "--rate"):
            
           # store the rate
           rate = a
            
        elif o in ("-a", "--address"):
            
           # store the dog address
           dog_gateway = DogGateway(("http://%s/api/v1") % a);
        
        elif o in ("-d", "--device"):
            
           # store the dog device
           dog_device_uri = a
            
         # handle not supported options
        else:
            assert False, "unhandled option"

    if((port != "") and (rate > 0)):
            
        # open the serial port
        ser = serial.Serial(port, rate)
        ser.setStopbits(1)
        ser.setByteSize(8)
        ser.setParity(serial.PARITY_NONE)
        
    
        # set the running flag at true
        can_run = True
        printed = False  
        message_bytes = []
        message_chars = []
        
        # sampling cycle
        while can_run:

            try:  
                       
                if(ser.inWaiting() > 0):    
                    # read a line from the serial port
                    byte = ser.read()
            
                    # debug / log print the read line
                    byte_value = byte.encode('hex')
                    if(byte_value == "55"):
                        # print the current message value
                        print("%s\n---------") % message_bytes
                        sys.stdout.flush()
                        
                        # interpret the data
                        action = decode_message(message_chars)
                        if (action == 0):
                            print "Button 1 OFF"
                            if((dog_gateway != None) and (dog_device_uri != None)):
                                dog_gateway.sendCommand(dog_device_uri, "off")
                        elif(action == 1):
                            print "Button1 ON"
                            if((dog_gateway != None) and (dog_device_uri != None)):
                                dog_gateway.sendCommand(dog_device_uri, "on")
                        elif (action == 2):
                            print "Button 2 OFF"
                        elif(action == 3):
                            print "Button 2 ON"
                            
                        # reset buffers
                        message_bytes = []
                        message_chars = []
                        
                    # append content to buffers    
                    message_bytes.append(byte_value);
                    message_chars.append(byte);
                    
                    
                else:
                    # sleep a bit                       
                    sleep(.1)
                        
                        
            except KeyboardInterrupt:
                can_run = False
            
                # debug
                print "stopped"
                sys.stdout.flush()
        
            except:
                pass

if __name__ == '__main__':
    # start main
    main() 
