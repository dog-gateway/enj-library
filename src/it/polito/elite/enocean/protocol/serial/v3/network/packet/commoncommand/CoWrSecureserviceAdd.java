package it.polito.elite.enocean.protocol.serial.v3.network.packet.commoncommand;

import it.polito.elite.enocean.protocol.serial.v3.network.packet.ESP3Packet;

/**
 * Add secure device to coltroller. It is possible to add ony one or more rocker with this function
 *
 * @author Andrea Biasi <biasiandrea04@gmail.com>
 *
 */
public class CoWrSecureserviceAdd extends ESP3Packet {
/**
 * @param slf : Security Level Format
 * @param id : Device ID
 * @param privateKey : 16 bytes private key of the device
 * @param rollingCode : If a 16 bit rolling code is defined in SLF, the MSB is undefined
 * @param direction : Add device security information to: 0x00 = Inbound table (default) 
 * 														  0x01 = Outbound table ID = Device ID 
 * 														  0x02 = Outbound table broadcast ID = Gateway SourceID which can be ChipID or one of BaseIDs 0x02..0xFF = not used
 */
public CoWrSecureserviceAdd(byte slf, int id, byte[] privateKey, int rollingCode , byte direction) {
		super();
		this.packetType = COMMON_COMMAND;
		this.data[0] = 0x19;
		this.data[1] = slf;

		this.data[2] = (byte) (id & 0xff);
		this.data[3] = (byte) ((id & 0xff00) >> 8);
		this.data[4] = (byte) ((id & 0xff0000) >> 16);
		this.data[5] = (byte) ((id & 0xff000000) >> 32);

		for (int i = 0; i < privateKey.length; i++) {
			this.data[6 + i] = privateKey[i];
		}
		
		this.data[6 + privateKey.length] = (byte) (rollingCode & 0xff00);
		this.data[6 + privateKey.length + 1] = (byte) ((rollingCode & 0xff00) >> 8);
		this.data[6 + privateKey.length + 2] = (byte) ((rollingCode & 0xff0000) >> 32);
		this.optData [0] = direction;
		this.buildPacket();
	}

}