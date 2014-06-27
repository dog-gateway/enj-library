/**
 * 
 */
package it.polito.elite.enocean.protocol.serial.v3.network.packet.event;

import it.polito.elite.enocean.protocol.serial.v3.network.packet.ESP3Packet;

/**
 * PACKET TYPE 4 : EVENT
 *
 * An EVENT is primarily a confirmation for processes and procedures, incl. specific data content. Events are currently used only by Smart Ack.
 * In the current version of ESP3 the type EVENT carries no optional data.
 * 
 * @author Andrea Biasi <biasiandrea04@gmail.com>
 *
 */
public class Event extends ESP3Packet{

	/*
	 * ATTENZIONE IL PACCHETTO EVENT PUO SOLO ESSERE RICEVUTO PER CUI CREDO NON SERVA A NULLA IL COSTRUTTORE
	 */
	
	public Event(byte respCode){
		super();
		this.packetType=0x04;
		this.data[0]=respCode;
		this.buildPacket();
	}
	
	// --------- CONSTANT FIELD ----------
	public static int SA_RECLAIM_NOT_SUCCESFUL = 1;
	public static int SA_CONFIRM_LEARN = 2;
	public static int SA_LEARN_ACK = 3;
	public static int CO_READY = 4;
	public static int CO_EVENT_SECUREDEVICE = 5;

	/**
	 * @return TRUE if the event is SA_RECLAIM_NOT_SUCCESSFUL
	 */
	public boolean retSaReclaimNotSuccessful(){
		return data[0]==0x01;
	}

	/**
	 * @return TRUE if the event is SA_CONFIRM_LEARN
	 */
	public boolean retSaConfirmLearn(){
		return data[0]==0x02;
	}

	/**
	 * @return TRUE if the event is SA_LEARN_ACK
	 */
	public boolean retSaLearnAck(){
		return data[0]==0x03;
	}

	/**
	 * @return TRUE if the event is CO_READY
	 */
	public boolean retCoReady(){
		return data[0]==0x04;
	}

	/**
	 * @return TRUE if the event is CO_EVENT_SECUREDEVICES
	 */
	public boolean retCoEventSecuredevice(){
		return data[0]==0x05;
	}

}