/**
 * 
 */
package it.polito.elite.enocean.protocol.serial.v3.network.packet.response;

import it.polito.elite.enocean.protocol.serial.v3.network.packet.Packet;

/**
 * @author Andrea Biasi <biasiandrea04@gmail.com>
 *
 */
public class Response extends Packet{
	public Response(byte respCode){
		super();
		this.data[0]= respCode;
		this.buildPacket();
	}

	/**
	 * @return
	 */
	public boolean retOk(){
		return data[0]==0x00;
	}

	/**
	 * @return
	 */
	public boolean retError(){
		return data[0]==0x01;
	}

	/**
	 * @return
	 */
	public boolean retNotSupported(){
		return data[0]==0x02;
	}

	/**
	 * @return
	 */
	public boolean retWrongParam(){
		return data[0]==0x03;
	}

	/**
	 * @return
	 */
	public boolean retWronParam(){
		return data[0]==0x04;
	}

	/**
	 * @return
	 */
	public boolean retOperationDenied(){
		return data[0]==0x05;
	}
}