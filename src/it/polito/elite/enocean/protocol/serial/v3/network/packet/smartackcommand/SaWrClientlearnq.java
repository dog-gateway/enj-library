package it.polito.elite.enocean.protocol.serial.v3.network.packet.smartackcommand;

import it.polito.elite.enocean.protocol.serial.v3.network.packet.ESP3Packet;
/**
 * Code 4 : Sends smart ack learn request telegram to smart ack controller. This function will only be in a Smart Ack Client
 * 
 * @author andreabiasi
 *
 */
public class SaWrClientlearnq extends ESP3Packet{
	/**
	 * @param MsbManufactorId : nnn = Most significant 3 bits of the Manufacturer ID 11111 = reserved / default values
	 * @param LsbManufactorId : Least significant bits of the Manufacturer ID
	 * @param EEP EEP of the Smart Ack client, who wants to Teach IN.
	 */
	public SaWrClientlearnq(byte MsbManufactorId, byte LsbManufactorId, int EEP){
		super();
		this.packetType = SMART_ACK_COMMAND;
		//Smart ack code
		this.data[0] = 0x04; 
		this.data[1] = MsbManufactorId;
		this.data[2] = LsbManufactorId;
		this.data[4] = (byte) (EEP & 0xff);
		this.data[5] = (byte) ((EEP & 0xff00)>>8);
		this.data[6] = (byte) ((EEP & 0xff0000)>>16);
		this.buildPacket();
	}
}