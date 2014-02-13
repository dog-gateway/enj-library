/**
 * 
 */
package it.polito.elite.enocean.enj.EEP2_5.communication;

import java.util.concurrent.ConcurrentLinkedQueue;

import it.polito.elite.enocean.enj.EEP2_5.D2.D201.D20108;
import it.polito.elite.enocean.enj.EEP2_5.primitives.Device;
import it.polito.elite.enocean.enj.EEP2_5.primitives.EnoceanEquipmentProfile;
import it.polito.elite.enocean.enj.EEP2_5.primitives.Rorg;
import it.polito.elite.enocean.enj.EEP2_5.receiveEvent.PacketReceiverListener;
import it.polito.elite.enocean.enj.EEP2_5.receiveEvent.PacketEventSender;
import it.polito.elite.enocean.protocol.serial.v3.network.connection.EnjConnection;
import it.polito.elite.enocean.protocol.serial.v3.network.packet.Packet;
import it.polito.elite.enocean.protocol.serial.v3.network.packet.commoncommand.CoWrLearnmore;
import it.polito.elite.enocean.protocol.serial.v3.network.packet.radio.Radio;
import it.polito.elite.enocean.protocol.serial.v3.network.serialcommunication.ElementQueue;

/**
 * @author Andrea Biasi <biasiandrea04@gmail.com>
 *
 */
public class EnjCommunicator{

	/**
	 * 
	 */
	public EnjCommunicator(EnjConnection connection, ConcurrentLinkedQueue<ElementQueue> lowPriorityRxQueue){
		super();
		this.connection = connection;
		this.lowPriorityRxQueue = lowPriorityRxQueue;
	}

	EnjConnection connection;

	Device device;

	EnoceanEquipmentProfile eep;

	Rorg rorg;

	Packet pkt;
	
	ConcurrentLinkedQueue<ElementQueue> lowPriorityRxQueue = new ConcurrentLinkedQueue<ElementQueue>();

	// Coda per accumulare i pacchetti in ricezione e visualizzarli in fase di debug
	ConcurrentLinkedQueue<Packet> codaRicevuti = new ConcurrentLinkedQueue<Packet>();
	// Fine coda


	// Teach field-------------------------------------------
	public static byte TEACHIN_REQUEST =(byte) 0x00;
	public static byte TEACHIN_DELECTION_REQUEST =(byte) 0x01;
	public static byte TEACHIN_NOTSPECIFIED =(byte) 0x02;
	//-------------------------------------------------------


	public Device teach(){

		//Response response;

		// Inizializzo il listener per i dati in ricezione dal livello piu basso
		PacketReceiverListener incomingDataListener = new PacketReceiverListener(this.connection);


		//PacketEventSender dataSender = new PacketEventSender();

		PacketEventSender dataSender = this.connection.packetSender;

		dataSender.addEventListener(incomingDataListener);
		// Fine inizializzazione listener

		byte[]	payload = new byte[7];
		byte[] address = new byte[4];
		byte[] manId = new byte [2];		

		// Manda comando a TCM310 : "CO_WR_LEARNMODE"
		//CoWrLearnmore cmdLearnmode = new CoWrLearnmore(CoWrLearnmore.START_LEARNMODE, 0, (byte)1);
		//this.connection.send(cmdLearnmode);
		//System.out.println("Ho inviato sulla seriale CO_WR_LEARNMODE");
		// Fine invio comando
		
		

		boolean flag=true;
		while( flag == true){
			//System.out.println("");
			//System.out.print("Pacchetto: ");
			//pkt = incomingDataListener.getPacket();
			
			//pkt= connection.receive();
			//this.codaRicevuti.add(pkt);
			//for(int i=0 ; i<pkt.getPacketAsBytes().length ; i++){
				//System.out.print("" + String.format("%x", pkt.getPacketAsBytes()[i]));
			//}
			
			pkt=this.lowPriorityRxQueue.poll().getPkt();
					
			if(pkt.getData()[0]==(byte)0xD4){ 
				flag=false;
			}


			/* 
			 * Attendo finch non  un UTE TEACH IN, scarto tutto il resto
			 */
		}	
		//this.codaRicevuti.add(pkt);


		switch (pkt.getData()[0]){
		//case (byte)(Rorg.UTE): this.teachUte(payload,address,manId);
		case (byte) 0xD4 : this.teachUte(payload, address, manId);
		break;
		}


		return device;
	}


	public void sendCommand(){
		D20108 plug = new D20108();
		byte[] address = device.getAddress();
		//									  byte data[], byte subTelNum, int destinationId , byte dBm, byte securityLevel
		Radio radioToSend = new Radio(plug.actuatorSetOutput(D20108.ON), (byte)0x03, address, (byte)0x00, (byte)0x00);

		// 				 public Radio(byte data[], byte subTelNum, byte[] destinationId , byte dBm, byte securityLevel)
		
		this.connection.send(radioToSend);
	}

	/**
	 * @return
	 */
	private void teachUte(byte[] payload, byte[] address, byte[] manId){

		//pkt = this.connection.receive();

		// Salvo i 7 byte UTE payload
		payload[6] = pkt.getData()[1];
		payload[5] = pkt.getData()[2];
		payload[4] = pkt.getData()[3];
		payload[3] = pkt.getData()[4];
		payload[2] = pkt.getData()[5];
		payload[1] = pkt.getData()[6];
		payload[0] = pkt.getData()[7];

		// Attenzione controllare molto bene l'ordine dei byte
		address[3] = pkt.getData()[8];
		address[2] = pkt.getData()[9];
		address[1] = pkt.getData()[10];
		address[0] = pkt.getData()[11];
		//----------------------------------------------------

		//byte status = pkt.getData()[12];

		//Aggiungo solo DB_6.BIT2 , DB_6.BIT1 , DB_6.BIT0 facendo un end con una maschera di 00000111
		manId[0]=(byte) (payload[3]&(0x07));
		manId[1] = payload[4];

		this.eep = new EnoceanEquipmentProfile(new Rorg(payload[0]), payload[1], payload[2]);

		// Se il bit 4 e 5 di DB_6 sono 00 una teach-in query 
		if(	((payload[6] & (0x30)) == (byte)0x00) || ((payload[6] & (0x30)) == (byte)0x20) ){ // Faccio and con una maschiera 00110000 per vedere solo i bit 4 e 5
			// Aggiungo il device
			device = new Device(eep, address, manId);
		}
		else{
			if( (payload[6] & (0x30)) == TEACHIN_DELECTION_REQUEST){
				// E una teach in deletion request
			}
			//Nessuna delle due precedenti
			System.out.println("Sticazzi non  ne teach in ne delection teach in");
		}

		// Se il bit 6 del byte 6 = 0 allora il messaggio richede una risposta
		if( (payload[6] & (0x40)) == 0x00){
			byte[] payloadResp = new byte[13];

			/*
			 * TEACH IN QUERY
			 */
			
			//RORG UTE
			payloadResp[0] = (byte)0xD4;
			
			// Ho invertito perch in fase di invio devo mandare prima DB6 etc
			payloadResp[1] = (byte)0x91;
			payloadResp[2] = payload[5];
			payloadResp[3] = payload[4];
			payloadResp[4] = payload[3];
			payloadResp[5] = payload[2];
			payloadResp[6] = payload[1];
			payloadResp[7] = payload[0];
			
			//ADDRESS
			payloadResp[8] = (byte)0x00;
			payloadResp[9] = (byte)0xFF;
			payloadResp[10] = (byte)0xFF;
			payloadResp[11] = (byte)0xFF;
			
			//STATUS
			payloadResp[12] = (byte)0x00;

			byte[] opt = new byte[7];
			opt[0] = (byte)0x03;
			opt[1] = (byte)0x00;
			opt[2] = (byte)0x81;
			opt[3] = (byte)0x2A;
			opt[4] = (byte)0x90;
			opt[5] = (byte)0xFF;
			opt[6] = (byte)0x00;
			
			
			/* 
			 * Creare funzione: eepIsPresent() che dica se il profilo del dispositivo  presente
			 */
/*
			//In questa fase do per scontato che il profilo sia presente
			if((payload[6]&(byte)0xF0) == 0x00){
				// Unidirezionale
				payloadResp[6] = (byte)0x11; // 00010001
			}
			else{
				// Bidirezionale
				payloadResp[6] = (byte)0x91; // 10010001
			}	
*/

			//Mando la risposta us ESP3
			this.connection.send(new Packet(Packet.RADIO, payloadResp, opt)); //Attenzione bisogna mandarli invertiti
		}
	}

}