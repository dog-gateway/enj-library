import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
// Documentation: http://users.frii.com/jarvi/rxtx/doc/index.html

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;

//import javax.xml.crypto.Data;

//Documentation: http://jflac.sourceforge.net/apidocs/org/kc7bfi/jflac/util/CRC8.html
import org.kc7bfi.jflac.util.CRC8;

import java.lang.Number;

//Per una corretta interpretazione � meglio mettere solo le classi con lettere maiuscole? Esiste uno standard in questo?

public class ENOCEAN_prova {
	
	void connect ( String portName ) throws Exception //Preso dall'esempio su RXTX
    {      
    	CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName); // Da alla porta che si chima portName identificatore?
    	
        if ( portIdentifier.isCurrentlyOwned() )
        {
            System.out.println("Error: Port is currently in use");
        }
        else
        {
            CommPort commPort = portIdentifier.open(this.getClass().getName(),2000);   //Cos'� sto 2000?
            if ( commPort instanceof SerialPort )
            {
                SerialPort serialPort = (SerialPort) commPort; 
                // Istanzio l'oggetto serialPort facendo il cast di commPort ma visto che serial port � una sottoclasse 
                //	di commPort non posso direttamente crearla di tipo serialPort
                
                // Quale velocit� impostare?
                // La velocit� della UART del modulo 58823 bit/s ma si usa di solito 57600 bit/s
                serialPort.setSerialPortParams(57600,SerialPort.DATABITS_8,SerialPort.STOPBITS_1,SerialPort.PARITY_NONE);
                
                InputStream in = serialPort.getInputStream();
                OutputStream out = serialPort.getOutputStream();
            
                (new Thread(new SerialReader(in,buffer))).start();    //Thread RX     
                (new Thread(new SerialWriter(out))).start();  //Thread TX
            }
            else
            {
                System.out.println("Error: Only serial ports are handled by this example.");
            }
        }     
    }
    
    public class SerialReader implements Runnable 
    {
    	// Se voglio che il codice necessario all'invio del pacchetto sia all'interno della classe packet questa classe dovrebbe servire 
    	// da interfaccia per implementare il thread
    	
    	//Forse l'invio del buffer di byte va fatto qua dentro e nel metodo sendPacket della classe packet semplicemente "riempire il buffer"
    	
        InputStream in; 
        byte[] buffer = new byte[65547];
        
        public SerialReader ( InputStream in , byte buffer[])  //COSTRUTTORE passa all'oggetto la variabile di tipo InputStream
        {
            this.in = in;
            //this.buffer = buffer;
        }
        
        public void run ()
        {
        	int len = 0;        	        	
        	while ( len > -1 ) //Finch� c'� qualcosa da leggere
	        {
	        	// Leggo da in che � di tipo InputStream e lo metto in un buffer di tipo byte, sar� giusto?
	        	len = in.read(buffer);
	        }
        }
    }
    
    public class SerialWriter implements Runnable 
    {
        OutputStream out;
        byte buffer[];
        public SerialWriter ( OutputStream out, byte[] buffer )
        {
            this.out = out;
            this.buffer = buffer;
        }
        
        public void run ()
        {
			out.write(buffer);
        }
        
        //Creare metodo che restituisca il buffer? restuire il buffer come una funzione?
        
    }
	
	 // Fine Class Pacchetto	
	
	// TYPE 1
	public class RADIO extends Packet{
		public RADIO(short data_lenght, byte data[], byte SubTelNum, byte[] DestinationID , byte dBm, byte Securitylevel){
			byte[] header;
			int i;
			sync_byte = 0x55;
			this.data_lenght = data_lenght;
			opt_lenght = 0x07;
			packet_type = 0x01;
			header[0] = data_length;
			header[1] = opt_lenght;
			header[2] = packet_type;
			this.CRC8H = CRC8.calc(header, 4);
			this.data = data;
			opt_data[0] = SubTelNum;
			for(i=0 ; i<4 ; i++) {
				opt_data[i+1] = DestinationID[i]; // Mettere int o vettore di 4 byte?
			}
			opt_data[5] = dBm;
			opt_data[6] = Securitylevel;			
			//Come concatenare due vettori? Usare la classe vector?
			this.CRC8D = CRC8.calc(data+opt_data, data_lenght+7);		
		}
	}
	
	// TYPE 2
	public class RESPONSE extends Packet{
		public RESPONSE(byte data){ //Piu che data andrebbe messo un tipo risposta
			byte[] header;
			sync_byte = 0x55;
			data_lenght = 0x01;
			opt_lenght = 0x00;
			packet_type = 0x02;
			header[0] = data_length;
			header[1] = opt_lenght;
			header[2] = packet_type;
			this.CRC8H = CRC8.calc(header, 4);
			this.data[0] = data;
			//this.opt_data ;       QUESTO CAMPO NON LO METTO O LO INIZIALIZZO A NULL?
			this.CRC8D = CRC8.calc(data, data_lenght);		
		}
		
		// Metodi che restituiscono TRUE o FALSE per stabilire velocemente di che risposta si stratta
		
		boolean RET_OK(){
			return this.data[0] == 0x00;
		}
		
		boolean RET_ERROR(){
			return this.data[0] == 0x01;
		}
		
		boolean RET_NOT_SUPPORTED(){
			return this.data[0] == 0x02;
		}
		
		boolean RET_WRONG_PARAM(){
			return this.data[0] == 0x03;
		}
		
		boolean RET_OPERATION_DENIED(){
			return this.data[0] == 0x04;
		}
		
		/*
		Se >128 usati per comandi con informazioni di ritorno speciali 
		
		boolean ALTRO(){
			return this.data[0] > 0x80;
		}
		*/	
	}
	
	// TYPE 3
	public class RADIO_SUB_TEL extends Packet{
		public RADIO_SUB_TEL(short data_lenght, byte opt_lenght, byte data[], byte opt_data[]){
			byte[] header;
			sync_byte = 0x55;
			this.data_lenght = data_lenght;
			opt_lenght = 0x07;
			packet_type = 0x03;
			header[0] = data_length;
			header[1] = opt_lenght;
			header[2] = packet_type;
			this.CRC8H = CRC8.calc(header, 4);
			this.data = data;
			this.opt_data = opt_data;
			this.CRC8D = CRC8.calc(data+optdata, data.length+opt_data.length);		
		}
	}
	
	
	// TYPE 4 - Non so se va fatta una classe Event dal momento che possono solo essere ricevuti
	/*public class Event extends Packet{
		
		FORSE SI POSSONO FARE DEI METODI BOOLEAN PER VERIFICARE DI CHE TIPO DI EVENTO SI TRATTA
		
		
		public Event(byte CRC8H, byte event_code, byte CRC8D){
			sync_byte = 0x55;
			data_lenght = 0x01;
			opt_lenght = 0x00;
			packet_type = 0x04;
			this.CRC8H = CRC8H;
			this.data[0] = event_code; //EVENT CODE
			//this.opt_data ;       STO CAMPO NON LO METTIAMO ?
			this.CRC8D = CRC8D;		
		}		
	}*/
	
	
	// TYPE 5
	public class COMMON_COMMAND extends Packet{
		public void CO_WR_SLEEP(byte deep_sleep_period[]){
			//deep_sleep_period in int? poi bisogna mettere l'int a gruppi di 1 byte alla volta in data, perch� data � di tipo byte
			byte header[];
			this.sync_byte = 0x55;
			data_lenght = 0x05;
			opt_lenght = 0x00;
			packet_type = 0x05;
			header[0] = data_length;
			header[1] = opt_lenght;
			header[2] = packet_type;
			this.CRC8H = CRC8.calc(header, 3);
			this.data[0] = 0x01; //Command code
			int i;
			for(i=0 ; i<5 ; i++){
				this.data[i+1] = deep_sleep_period[i];
			}
			//this.opt_data ;       QUESTO CAMPO NON LO METTO O LO INIZIALIZZO A NULL?
			this.CRC8D = CRC8.calc(data, data_lenght);
		}
		
		public void CO_WR_RESET(){
			byte header[];
			sync_byte = 0x55;
			data_lenght = 0x01;
			opt_lenght = 0x00;
			packet_type = 0x05;
			header[0] = data_length;
			header[1] = opt_lenght;
			header[2] = packet_type;
			this.CRC8H = CRC8.calc(header, 3);
			this.data[0] = 0x02; //Command code
			//this.opt_data ;       QUESTO CAMPO NON LO METTO O LO INIZIALIZZO A NULL?
			this.CRC8D = CRC8.calc(data, data_lenght);
		}
		
		public void CO_RD_VERSION(){
			this.data[0] = 0x03;
			byte header[];
			sync_byte = 0x55;
			data_lenght = 0x01;
			opt_lenght = 0x00;
			packet_type = 0x05;
			header[0] = data_length;
			header[1] = opt_lenght;
			header[2] = packet_type;
			this.CRC8H = CRC8.calc(header, 3);
			this.data[0] = 0x03; //Command code
			//this.opt_data ;       QUESTO CAMPO NON LO METTO O LO INIZIALIZZO A NULL?
			this.CRC8D = CRC8.calc(data, data_lenght);
		}
		
		public void CO_RD_SYS_LOG(){
			this.data[0] = 0x04;
			byte header[];
			sync_byte = 0x55;
			data_lenght = 0x01;
			opt_lenght = 0x00;
			packet_type = 0x05;
			header[0] = data_length;
			header[1] = opt_lenght;
			header[2] = packet_type;
			this.CRC8H = CRC8.calc(header, 3);
			this.data[0] = 0x04; //Command code
			//this.opt_data ;       QUESTO CAMPO NON LO METTO O LO INIZIALIZZO A NULL?
			this.CRC8D = CRC8.calc(data, data_lenght);
		}
		
		public void CO_WR_SYS_LOG(){
			byte header[];
			sync_byte = 0x55;
			data_lenght = 0x01;
			opt_lenght = 0x00;
			packet_type = 0x05;
			header[0] = data_length;
			header[1] = opt_lenght;
			header[2] = packet_type;
			this.CRC8H = CRC8.calc(header, 3);
			this.data[0] = 0x01; //Command code
			//this.opt_data ;       QUESTO CAMPO NON LO METTO O LO INIZIALIZZO A NULL?
			this.CRC8D = CRC8.calc(data, data_lenght);	
		}
		
		public void CO_WR_BIST(){		
			byte header[];
			sync_byte = 0x55;
			data_lenght = 0x01;
			opt_lenght = 0x00;
			packet_type = 0x05;
			header[0] = data_length;
			header[1] = opt_lenght;
			header[2] = packet_type;
			this.CRC8H = CRC8.calc(header, 3);
			this.data[0] = 0x06; //Command code
			//this.opt_data ;       QUESTO CAMPO NON LO METTO O LO INIZIALIZZO A NULL?
			this.CRC8D = CRC8.calc(data, data_lenght);
		}
		
		public void CO_WR_IDBASE(){
			byte header[];
			sync_byte = 0x55;
			data_lenght = 0x05;
			opt_lenght = 0x00;
			packet_type = 0x05;
			header[0] = data_length;
			header[1] = opt_lenght;
			header[2] = packet_type;
			this.CRC8H = CRC8.calc(header, 3);
			this.data[0] = 0x07; //Command code
			//this.opt_data ;       QUESTO CAMPO NON LO METTO O LO INIZIALIZZO A NULL?
			this.CRC8D = CRC8.calc(data, data_lenght);
		}
		
		public void CO_RD_IDBASE(){
			byte header[];
			sync_byte = 0x55;
			data_lenght = 0x01;
			opt_lenght = 0x00;
			packet_type = 0x05;
			header[0] = data_length;
			header[1] = opt_lenght;
			header[2] = packet_type;
			this.CRC8H = CRC8.calc(header, 3);
			this.data[0] = 0x08; //Command code
			//this.opt_data ;       QUESTO CAMPO NON LO METTO O LO INIZIALIZZO A NULL?
			this.CRC8D = CRC8.calc(data, data_lenght);
		}
		
		public void CO_WR_REPEATER(byte COMMAND_code, byte REP_Enable, byte REP_level){
			byte header[];
			sync_byte = 0x55;
			data_lenght = 0x03;
			opt_lenght = 0x00;
			packet_type = 0x05;
			header[0] = data_length;
			header[1] = opt_lenght;
			header[2] = packet_type;
			this.CRC8H = CRC8.calc(header, 3);
			this.data[0] = 0x09; //Command code
			int i;
			for(i=0 ; i<data_lenght ; i++){
				this.data[i+1] = deep_sleep_period[i];
			}
			//this.opt_data ;       QUESTO CAMPO NON LO METTO O LO INIZIALIZZO A NULL?
			this.CRC8D = CRC8.calc(data, data_lenght);
		}
					
		public void CO_RD_REPEATER(){			
			byte header[];
			sync_byte = 0x55;
			data_lenght = 0x01;
			opt_lenght = 0x00;
			packet_type = 0x05;
			header[0] = data_length;
			header[1] = opt_lenght;
			header[2] = packet_type;
			this.CRC8H = CRC8.calc(header, 3);
			this.data[0] = 0x0A; //Command code
			//this.opt_data ;       QUESTO CAMPO NON LO METTO O LO INIZIALIZZO A NULL?
			this.CRC8D = CRC8.calc(data, data_lenght);		
		}
		
		public void CO_WR_FILTER_ADD(byte filter_type, byte[] filter_value, byte filter_kind){
			byte header[];
			sync_byte = 0x55;
			data_lenght = 0x07;
			opt_lenght = 0x00;
			packet_type = 0x05;
			header[0] = data_length;
			header[1] = opt_lenght;
			header[2] = packet_type;
			this.CRC8H = CRC8.calc(header, 3);
			data[0] = 0x0B; //Command code
			data[1] = filter_type;
			data[2] = 
			int i;
			for(i=0 ; i<data_lenght ; i++){
				this.data[i+3] = filter_value[i];
			}
			//this.opt_data ;       QUESTO CAMPO NON LO METTO O LO INIZIALIZZO A NULL?
			this.CRC8D = CRC8.calc(data, data_lenght);	
		}
		
		public void CO_WR_FILTER_DEL(byte[] filter_value){
			byte header[];
			sync_byte = 0x55;
			data_lenght = 0x06;
			opt_lenght = 0x00;
			packet_type = 0x05;
			header[0] = data_length;
			header[1] = opt_lenght;
			header[2] = packet_type;
			this.CRC8H = CRC8.calc(header, 3);
			this.data[0] = 0x0C; //Command code
			int i;
			for(i=0 ; i<data_lenght ; i++){
				this.data[i+1] = filter_value[i];
			}
			//this.opt_data ;       QUESTO CAMPO NON LO METTO O LO INIZIALIZZO A NULL?
			this.CRC8D = CRC8.calc(data, data_lenght);
		}
		
		public void CO_WR_FILTER_DEL_ALL(){
			byte header[];
			sync_byte = 0x55;
			data_lenght = 0x01;
			opt_lenght = 0x00;
			packet_type = 0x05;
			header[0] = data_length;
			header[1] = opt_lenght;
			header[2] = packet_type;
			this.CRC8H = CRC8.calc(header, 3);
			this.data[0] = 0x0D; //Command code
			//this.opt_data ;       QUESTO CAMPO NON LO METTO O LO INIZIALIZZO A NULL?
			this.CRC8D = CRC8.calc(data, data_lenght);
		}
		
		public void CO_WR_FILTER_ENABLE(byte filter_onoff, byte filter_operator){
			byte header[];
			sync_byte = 0x55;
			data_lenght = 0x03;
			opt_lenght = 0x00;
			packet_type = 0x05;
			header[0] = data_length;
			header[1] = opt_lenght;
			header[2] = packet_type;
			this.CRC8H = CRC8.calc(header, 3);
			data[0] = 0x0E; //Command code
			data[1] = filter_onoff;
			data[2] = filter_operator;
			//this.opt_data ;       QUESTO CAMPO NON LO METTO O LO INIZIALIZZO A NULL?
			this.CRC8D = CRC8.calc(data, data_lenght);
		}
		
		public void CO_RD_FILTER(){
			byte header[];
			sync_byte = 0x55;
			data_lenght = 0x01;
			opt_lenght = 0x00;
			packet_type = 0x05;
			header[0] = data_length;
			header[1] = opt_lenght;
			header[2] = packet_type;
			this.CRC8H = CRC8.calc(header, 3);
			this.data[0] = 0x0F; //Command code
			int i;
			//this.opt_data ;       QUESTO CAMPO NON LO METTO O LO INIZIALIZZO A NULL?
			this.CRC8D = CRC8.calc(data, data_lenght);
		}
		
		public void CO_WR_WAIT_MATURITY(byte wait_end_maturity){
			byte header[];
			sync_byte = 0x55;
			data_lenght = 0x02;
			opt_lenght = 0x00;
			packet_type = 0x05;
			header[0] = data_length;
			header[1] = opt_lenght;
			header[2] = packet_type;
			this.CRC8H = CRC8.calc(header, 3);
			data[0] = 0x10; //Command code
			data[1] = wait_end_maturity;
			//this.opt_data ;       QUESTO CAMPO NON LO METTO O LO INIZIALIZZO A NULL?
			this.CRC8D = CRC8.calc(data, data_lenght);
		}
		
		public void CO_WR_SUBTEL(){
			byte header[];
			sync_byte = 0x55;
			data_lenght = 0x02;
			opt_lenght = 0x00;
			packet_type = 0x05;
			header[0] = data_length;
			header[1] = opt_lenght;
			header[2] = packet_type;
			this.CRC8H = CRC8.calc(header, 3);
			this.data[0] = 0x11; //Command code
			//this.opt_data ;       QUESTO CAMPO NON LO METTO O LO INIZIALIZZO A NULL?
			this.CRC8D = CRC8.calc(data, data_lenght);
		}
		
		public void CO_WR_MEM(short data_lenght, byte memory_type, byte[] memory_address, byte[] memory_data){
			byte header[];
			sync_byte = 0x55;
			this.data_lenght = data_lenght;
			opt_lenght = 0x00;
			packet_type = 0x05;
			header[0] = data_length;
			header[1] = opt_lenght;
			header[2] = packet_type;
			this.CRC8H = CRC8.calc(header, 3);
			data[0] = 0x12; //Command code
			data[1] = memory_type; 
			int i;
			for(i=0 ; i<4 ; i++){
				this.data[i+2] = memory_address[i];
			}
			for(i=0 ; i<memory_data.length ; i++){
				this.data[i+2] = memory_data[i];
			}
			//this.opt_data ;       QUESTO CAMPO NON LO METTO O LO INIZIALIZZO A NULL?
			this.CRC8D = CRC8.calc(data, data_lenght);
		}
		
		public void CO_RD_MEM(byte memory_type, byte[] memory_address, byte[] data_lenght){
			byte header[];
			sync_byte = 0x55;
			data_lenght = 0x08;
			opt_lenght = 0x00;
			packet_type = 0x05;
			header[0] = data_length;
			header[1] = opt_lenght;
			header[2] = packet_type;
			this.CRC8H = CRC8.calc(header, 3);
			data[0] = 0x13; //Command code
			data[1] = memory_type
			for(i=0 ; i<4 ; i++){
				this.data[i+2] = memory_address[i];
				}
			for(i=0 ; i<2 ; i++){
				this.data[i+6] = data_lenght[i];
				}
			//this.opt_data ;       QUESTO CAMPO NON LO METTO O LO INIZIALIZZO A NULL?
			this.CRC8D = CRC8.calc(data, data_lenght);			
		}
		
		public void CO_RD_MEM_ADDRESS(byte memory_area){
			byte header[];
			sync_byte = 0x55;
			data_lenght = 0x02;
			opt_lenght = 0x00;
			packet_type = 0x05;
			header[0] = data_length;
			header[1] = opt_lenght;
			header[2] = packet_type;
			this.CRC8H = CRC8.calc(header, 3);
			data[0] = 0x14; //Command code
			data[1] = memory_area;
			this.CRC8D = CRC8.calc(data, data_lenght);			
		}
		
		public void CO_RD_SECURITY(){	
			byte header[];
			sync_byte = 0x55;
			data_lenght = 0x01;
			opt_lenght = 0x00;
			packet_type = 0x05;
			header[0] = data_length;
			header[1] = opt_lenght;
			header[2] = packet_type;
			this.CRC8H = CRC8.calc(header, 3);
			data[0] = 0x15; //Command code
			this.CRC8D = CRC8.calc(data, data_lenght);		
		}
		
		public void CO_WR_SECURITY(byte sec_level, byte[] key){
			byte header[];
			sync_byte = 0x55;
			data_lenght = 0x0A;
			opt_lenght = 0x00;
			packet_type = 0x05;
			header[0] = data_length;
			header[1] = opt_lenght;
			header[2] = packet_type;
			this.CRC8H = CRC8.calc(header, 3);
			data[0] = 0x16; //Command code
			data[1] = sec_level;
			for(i=0 ; i<4 ; i++){
				this.data[i+2] = key[i];
				}
			for(i=0 ; i<4 ; i++){
				this.data[i+6] = 0x00;
				}
			this.CRC8D = CRC8.calc(data, data_lenght);	
		}
		
		public void CO_WR_LEARNMODE(byte enable, byte[] timeout, byte channel){
			byte header[];
			sync_byte = 0x55;
			data_lenght = 0x06;
			opt_lenght = 0x01; // <-- ATTENZIONE ERRORE NEL DATASHEET
			packet_type = 0x05;
			header[0] = data_length;
			header[1] = opt_lenght;
			header[2] = packet_type;
			this.CRC8H = CRC8.calc(header, 3);
			data[0] = 0x17; //Command code
			data[1] = enable;
			for(i=0 ; i<4 ; i++){
				this.data[i+2] = timeout[i];
				}
			opt_data[0] = channel;
			this.CRC8D = CRC8.calc(data + opt_data, data_lenght);
		}
		
		public void CO_RD_LEARNMODE(){
			byte header[];
			sync_byte = 0x55;
			data_lenght = 0x01;
			opt_lenght = 0x00;
			packet_type = 0x05;
			header[0] = data_length;
			header[1] = opt_lenght;
			header[2] = packet_type;
			this.CRC8H = CRC8.calc(header, 3);
			data[0] = 0x18; //Command code
			this.CRC8D = CRC8.calc(data, data_lenght);		
		}
		
		public void CO_WR_SECUREDEVICE_ADD(byte slf, byte id, byte[] private_key, byte rolling_code){
			byte header[];
			sync_byte = 0x55;
			data_lenght = 0x0019;
			opt_lenght = 0x00;
			packet_type = 0x05;
			header[0] = data_length;
			header[1] = opt_lenght;
			header[2] = packet_type;
			this.CRC8H = CRC8.calc(header, 3);
			data[0] = 0x19; //Command code
			data[1] = slf;
			for(i=0 ; i<4 ; i++){
				this.data[i+2] = id[i];
				}
			for(i=0 ; i<16 ; i++){
				this.data[i+6] = private_key;
				}
			for(i=0 ; i<3 ; i++){
				this.data[i+23] = rolling_code;
				}
			this.CRC8D = CRC8.calc(data, data_lenght);
		}
		
		public void CO_WR_SECUREDEVICE_DEL(byte[] id){
			byte header[];
			sync_byte = 0x55;
			data_lenght = 0x05;
			opt_lenght = 0x00;
			packet_type = 0x05;
			header[0] = data_length;
			header[1] = opt_lenght;
			header[2] = packet_type;
			this.CRC8H = CRC8.calc(header, 3);
			data[0] = 0x1A; //Command code
			for(i=0 ; i<4 ; i++){
				this.data[i+1] = id[i];
				}
			this.CRC8D = CRC8.calc(data, data_lenght);		
		}
		
		public void CO_RD_SECUREDEVICE(byte index){
			byte header[];
			sync_byte = 0x55;
			data_lenght = 0x02;
			opt_lenght = 0x00;
			packet_type = 0x05;
			header[0] = data_length;
			header[1] = opt_lenght;
			header[2] = packet_type;
			this.CRC8H = CRC8.calc(header, 3);
			data[0] = 0x1B; //Command code
			data[1] = index;
			this.CRC8D = CRC8.calc(data, data_lenght);
		}
		
		public void CO_WR_MODE(byte mode){
			byte header[];
			sync_byte = 0x55;
			data_lenght = 0x02;
			opt_lenght = 0x00;
			packet_type = 0x05;
			header[0] = data_length;
			header[1] = opt_lenght;
			header[2] = packet_type;
			this.CRC8H = CRC8.calc(header, 3);
			data[0] = 0x1C; //Command code
			data[1] = mode;
			this.CRC8D = CRC8.calc(data, data_lenght);
			}
		
		public void CO_RD_NUMSECUREDEVICE(){
			byte header[];
			sync_byte = 0x55;
			data_lenght = 0x01;
			opt_lenght = 0x00;
			packet_type = 0x05;
			header[0] = data_length;
			header[1] = opt_lenght;
			header[2] = packet_type;
			this.CRC8H = CRC8.calc(header, 3);
			data[0] = 0x1D; //Command code
			this.CRC8D = CRC8.calc(data, data_lenght);
		}
			
		}
	}
	

	// TYPE 6 : ci sono 29 metodi, forse prima di iniziare a scriverli conviene chiedere
	public class SMART_ACK_COMMAND extends Packet
	{
		public SMART_ACK_COMMAND(){
			this.sync_byte = 0x55;
			this.packet_type = 0x06;
		}
		
		public void SA_WR_LEARNMODE(byte enable, byte extended, byte[] timeout){
			int i;
			byte header[];
			this.data_lenght = 0x0007;
			this.opt_lenght = 0x00;
			header[0] = data_lenght;
			header[1] = opt_lenght;
			header[2] = packet_type;
			this.CRC8H = CRC8.calc(header , 3);
			this.data[0] = 0x01;
			this.data[1] = enable;
			this.data[2] = extended;
			for(i=0 ; i<4 ; i++){
				this.data[3+i] = timeout[i];
			}			
			this.CRC8D = CRC8.calc(data, data_length); 
		}
		
		public void SA_RD_LEARNMODE(){
			int i;
			byte header[];
			this.data_lenght = 0x0001;
			this.opt_lenght = 0x00;
			header[0] = data_lenght;
			header[1] = opt_lenght;
			this.CRC8H = CRC8.calc(header, header.length);
			this.data[0] = 0x02;
			this.CRC8D = CRC8.calc(data, data_length); 
		}
		
		public void SA_WR_LEARNCONFIRM(byte[] response_time, byte confirm_code, byte[] postmaster_candidate_ID, byte sartack_client_ID){
			byte header[];
			sync_byte = 0x55;
			data_lenght = 0x0C;
			opt_lenght = 0x00;
			packet_type = 0x06;
			header[0] = data_length;
			header[1] = opt_lenght;
			header[2] = packet_type;
			this.CRC8H = CRC8.calc(header, 3);
			data[0] = 0x03; //Command code
			for(i=0 ; i<3 ; i++){
				this.data[i+2] = response_time[i];
				}
			data[3] = confirm_code;
			for(i=0 ; i<4 ; i++){
				this.data[i+4] = postmaster_candidate_ID;
				}
			for(i=0 ; i<4 ; i++){
				this.data[i+8] = smartack_client_ID;
				}
			this.CRC8D = CRC8.calc(data, data_lenght);
		}
		
		public void SA_WR_CLIENTLEARNRQ(byte campo_sconosciuto, byte manufactorID, byte[] EEP){
			byte header[];
			sync_byte = 0x55;
			data_lenght = 0x06;
			opt_lenght = 0x00;
			packet_type = 0x06;
			header[0] = data_length;
			header[1] = opt_lenght;
			header[2] = packet_type;
			this.CRC8H = CRC8.calc(header, 3);
			data[0] = 0x04; //Command code
			data[1] = campo_sconosciuto; // <-- Cosa fa questo campo?
			data[2] = manufactorID;
			for(i=0 ; i<3 ; i++){
				this.data[i+8] = EEP[i];
				}
			this.CRC8D = CRC8.calc(data, data_lenght);
		}
		
		public void SA_WR_RESET(byte SmartAckCientID[]){
			byte header[];
			sync_byte = 0x55;
			data_lenght = 0x05;
			opt_lenght = 0x00;
			packet_type = 0x06;
			header[0] = data_length;
			header[1] = opt_lenght;
			header[2] = packet_type;
			this.CRC8H = CRC8.calc(header, 3);
			data[0] = 0x05; //SmartAck code;
			for(i=0 ; i<4 ; i++){
				this.data[i+1] = SmartAckCientID[i];
				}
			this.CRC8D = CRC8.calc(data, data_lenght);
		}
		
		public void SA_WR_LEARNCLIENTS(){
			byte header[];
			sync_byte = 0x55;
			data_lenght = 0x01;
			opt_lenght = 0x00;
			packet_type = 0x06;
			header[0] = data_length;
			header[1] = opt_lenght;
			header[2] = packet_type;
			this.CRC8H = CRC8.calc(header, 3);
			data[0] = 0x06; //Command code;
			this.CRC8D = CRC8.calc(data, data_lenght);		
			}
		
		public void SA_WR_RECLAIMS(byte reclaimCount){
			byte[] header;
			sync_byte = 0x55;
			this.data_lenght = data.length;
			this.opt_lenght = 0x00;
			this.packet_type = 0x06;
			header[0] = data.length;
			header[1] = opt_lenght;
			header[2] = packet_type;
			this.CRC8H = CRC8.calc(header, 4);
			data[0] = 0x07;
			data[1] = reclaimCount;
			this.CRC8D = CRC8.calc(data, data_lenght);	
		}
		
		public void SA_WR_POSTMASTER(byte mailboxCount){
			byte header[];
			sync_byte = 0x55;
			data_lenght = 0x02;
			opt_lenght = 0x00;
			packet_type = 0x06;
			header[0] = data_length;
			header[1] = opt_lenght;
			header[2] = packet_type;
			this.CRC8H = CRC8.calc(header, 3);
			data[0] = 0x08; //Command code;
			data[1] = mailboxCount;
			this.CRC8D = CRC8.calc(data, data_lenght);
		}
	}

	// TYPE 7
	public class REMOTE_MAN_COMMAND extends Packet{
		public REMOTE_MAN_COMMAND(byte data[]){
			byte[] header;
			this.sync_byte = 0x55;
			this.data_lenght = data.length;
			this.opt_lenght = 0x00;
			this.packet_type = 0x07;
			header[0] = data.length;
			header[1] = opt_lenght;
			header[2] = packet_type;
			this.CRC8H = CRC8.calc(header, 4);
			this.data = data;
			this.CRC8D = CRC8.calc(data, data_lenght);
		}
	}

	// TYPE 9
	public class RADIO_MESSAGE extends Packet{
		public RADIO_MESSAGE(byte data[], byte opt_data[]){
			byte[] header;
			byte[] d;
			this.sync_byte = 0x55;
			this.data_lenght = data.length;
			this.opt_lenght = 0x0A;
			this.packet_type = 0x09;
			header[0] = data_length;
			header[1] = opt_lenght;
			header[2] = packet_type;
			this.CRC8H = CRC8.calc(header, 4);
			this.data = data;
			this.opt_data = opt_data;
			// d : concatenare in d data+opt_data
			this.CRC8D = CRC8.calc(d, data_lenght);
		}
	}
	
	// TYPE 10
	public class RADIO_ADVANCED extends Packet{
		public RADIO_ADVANCED(byte data, byte SubTelNum, byte dBm){
			this.sync_byte = 0x55;
			this.data_lenght = data.length;
			this.packet_type  = 0x0A;
			this.CRC8H = CRC8.calc(header, 4);
			this.data = data;
			this.opt_data[0] = SubTelNum; 
			this.opt_data[1] = dBm;
			this.CRC8D = CRC8.calc(data, data.lenght+2);
		}
	}


	public static void main(String[] args){
		try
        {
			(new ENOCEAN_prova()).connect("ttyAMA0"); // ttyAMA0 : UART RaspberryPi
			//Voglio che i thread tengano aperto il canale di comunicazione in modo tale da mandare a mio piacimento packet.sendPacket quando ne ho bisogno
			/*
			Radio mexradio = new Radio();
			mexradio.sendPacket()
			*/
			
			/*
			 * CORPO DEL PROGRAMMA
			 */
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
	}
} //Fine ENOCEAN_prova