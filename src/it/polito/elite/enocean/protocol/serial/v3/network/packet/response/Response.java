/**
 * 
 */
package it.polito.elite.enocean.protocol.serial.v3.network.packet.response;

import it.polito.elite.enocean.enj.util.ByteUtils;
import it.polito.elite.enocean.protocol.serial.v3.network.packet.ESP3Packet;

/**
 * @author Andrea Biasi <biasiandrea04@gmail.com>
 *
 */
public class Response extends ESP3Packet
{
	public Response(byte respCode)
	{
		super();
		this.packetType = RESPONSE;
		this.data[0] = respCode;
		this.buildPacket();
	}

	public Response(ESP3Packet pkt) throws Exception
	{
		super();
		if (pkt.isResponse())
		{
			this.syncByte = pkt.getSyncByte();
			this.packetType = pkt.getPacketType();
			this.data = pkt.getData();
			this.optData = pkt.getOptData();
			this.buildPacket();
		}
		else
			throw (new Exception("Uncompatible packet type"));
	}

	// ---------------- Response type -----------------

	public Response()
	{
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
	public boolean retOk()
	{
		return data[0] == RET_OK;
	}

	/**
	 * @return
	 */
	public boolean retError()
	{
		return data[0] == RET_ERROR;
	}

	/**
	 * @return
	 */
	public boolean retNotSupported()
	{
		return data[0] == RET_NOT_SUPPORTED;
	}

	/**
	 * @return
	 */
	public boolean retWrongParam()
	{
		return data[0] == RET_WRONG_PARAM;
	}

	/**
	 * @return
	 */
	public boolean retOperationDenied()
	{
		return data[0] == RET_OPERATION_DENIED;
	}

	/**
	 * Provides a readable string representation of this object
	 */
	@Override
	public String toString()
	{
		StringBuffer stringThis = new StringBuffer();
		stringThis.append("Response[");
		stringThis.append(" 'value' :");
		if (this.retOk())
			stringThis.append("RET_OK");
		else if (this.retError())
			stringThis.append("RET_ERROR");
		else if (this.retNotSupported())
			stringThis.append("RET_NOT_SUPPORTED");
		else if (this.retWrongParam())
			stringThis.append("RET_WRONG_PARAM");
		else if (this.retOperationDenied())
			stringThis.append("RET_OPERATION_DENIED");
		else
			stringThis.append("RET_UNKNOWN_FORMAT");
		stringThis.append(" , rawPacket: ");
		stringThis.append(ByteUtils.toHexString(this.getPacketAsBytes()));
		stringThis.append(" ]");
		return stringThis.toString();
	}
}