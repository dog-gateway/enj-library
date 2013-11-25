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
	/*
	 * The byte vector optional may contains the optional data, in this packet
	 * type is empty
	 */
	private static byte[] optional = null;
	/*
	 * The byte vector dataValue contains the response code
	 */
	private static byte[] dataValue;
	public Response(byte respCode){
		super((byte)0x02, dataValue, optional);
		dataValue[0] = respCode;
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