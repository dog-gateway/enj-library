/**
 * 
 */
package it.polito.elite.enocean.protocol.serial.v3.network.packet.response;

import it.polito.elite.enocean.protocol.serial.v3.network.packet.ESP3Packet;

/**
 * @author Andrea Biasi <biasiandrea04@gmail.com>
 *
 */
public class Response extends ESP3Packet{
	public Response(byte respCode){
		super();
		this.packetType = RESPONSE;
		this.data[0]= respCode;
		this.buildPacket();
	}

	//---------------- Response type -----------------
		
	public Response() {
		super();
		// TODO Auto-generated constructor stub
	}

	public static byte RET_OK = 0;
	public static byte RET_ERROR = 1;
	public static byte RET_NOT_SUPPORTED = 2;
	public static byte RET_WRONG_PARAM = 3;
	public static byte RET_OPERATION_DENIED = 4;
	
	/**
	 * @return
	 */
	public boolean retOk(){
		return data[0]== RET_OK;
	}

	/**
	 * @return
	 */
	public boolean retError(){
		return data[0] == RET_ERROR;
	}

	/**
	 * @return
	 */
	public boolean retNotSupported(){
		return data[0] == RET_NOT_SUPPORTED;
	}

	/**
	 * @return
	 */
	public boolean retWronParam(){
		return data[0] == RET_WRONG_PARAM;
	}

	/**
	 * @return
	 */
	public boolean retOperationDenied(){
		return data[0] == RET_OPERATION_DENIED;
	}
}