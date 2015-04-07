package it.polito.elite.enocean.protocol.serial.v3.network.packet;

import it.polito.elite.enocean.protocol.serial.v3.network.crc8.Crc8;
import it.polito.elite.enocean.protocol.serial.v3.network.packet.event.Event;

/**
 * A class for representing EnOcean Serial Protocol version 3 packets
 * 
 * @author Andrea Biasi <biasiandrea04@gmail.com>
 *
 */

// ATTENZIONE, per i metodi setLenght e getLenght meglio usare tipo int?

public class ESP3Packet
{
	/*
	 * Attenzione ho tolto la classe Abstract la uso come una normale, perch�
	 * cosi posso impacchettare come generico Packet e successivamente
	 * discriminarne il tipo
	 */

	// --------------- Packet types -----------------

	public static byte RADIO = 1;
	public static byte RESPONSE = 2;
	public static byte RADIO_SUB_TEL = 3;
	public static byte EVENT = 4;
	public static byte COMMON_COMMAND = 5;
	public static byte SMART_ACK_COMMAND = 6;
	public static byte REMOTE_MAN_COMMAND = 7;
	public static byte RADIO_MESSAGE = 9;
	public static byte RADIO_ADVANCED = 10;

	// ---------------- The packet constants ---------
	public static final byte SYNC_BYTE = 0x55;

	// serial synchronization byte
	protected byte syncByte; // Il problema e che byte e signed

	// number of bytes in the data part of the packet (DATA_LENGHT)
	protected byte dataLenght[] = new byte[2]; // 2 byte

	// number of bytes of optional data (OPTIONAL_LENGHT)
	protected byte optLenght;

	// identifies the packet type
	protected byte packetType;

	// checksum for bytes DATA_LENGHT, OPTIONAL_LENGHT and TYPE
	protected byte crc8h;

	// data payload (DATA)
	protected byte[] data;

	// additional data extending the data payload (OPTIONAL_DATA)
	protected byte[] optData;

	// checksum for DATA and OPTIONAL_DATA
	protected byte crc8d;

	// --------------- Constructors --------------------

	/**
	 * Empty constructor (to support the bean instantiation pattern)
	 */
	public ESP3Packet()
	{
		this.syncByte = ESP3Packet.SYNC_BYTE;
	}

	/**
	 * @param syncByte
	 * @param dataLenght
	 * @param optLenght
	 * @param packetType
	 * @param cRC8H
	 * @param data
	 * @param optData
	 * @param cRC8D
	 */
	public ESP3Packet(byte packetType, byte[] data, byte[] optData)
	{
		this.syncByte = 0x55;
		this.packetType = packetType;
		this.data = data;
		this.optData = optData;
		this.buildPacket();
	}

	public void buildPacket()
	{
		this.dataLenght[1] = (byte) (data.length & 0x00ff); // Parte bassa dei 2
															// byte
		this.dataLenght[0] = (byte) (((data.length & 0xff00) >> 8)& 0x00ff); // Parte alta
																	// dei due
																	// byte
		this.optLenght = (byte) (optData.length & 0x00ff);
		byte header[] = new byte[4];
		header[0] = this.dataLenght[0];
		header[1] = this.dataLenght[1];
		header[2] = this.optLenght;
		header[3] = this.packetType;

		this.crc8h = Crc8.calc(header);
		byte vectData[] = new byte[data.length + optData.length];
		for (int i = 0; i < data.length; i++)
		{
			vectData[i] = data[i];
		}
		// Se non ho dati opzionali non metto piu nulla nel vettore
		if (optLenght != 0)
		{
			for (int i = 0; i < optData.length; i++)
			{
				vectData[i+data.length] = optData[i];
			}
		}
		this.crc8d = Crc8.calc(vectData);
	}

	/**
	 * @return the syncByte
	 */
	public byte getSyncByte()
	{
		return syncByte;
	}

	/**
	 * @param syncByte
	 *            the syncByte to set
	 */
	public void setSyncByte(byte syncByte)
	{
		this.syncByte = syncByte;
	}

	/**
	 * @return the dataLenght
	 */
	public byte[] getDataLenght()
	{
		return dataLenght;
	}

	/**
	 * @param dataLenght
	 *            the dataLenght to set
	 */
	public void setDataLenght(byte[] dataLenght)
	{
		this.dataLenght = dataLenght;
	}

	/**
	 * @return the optLenght
	 */
	public byte getOptLenght()
	{
		return optLenght;
	}

	/**
	 * @param optLenght
	 *            the optLenght to set
	 */
	public void setOptLenght(byte optLenght)
	{
		this.optLenght = optLenght;
	}

	/**
	 * @return the packetType
	 */
	public byte getPacketType()
	{
		return packetType;
	}

	/**
	 * @param packetType
	 *            the packetType to set
	 */
	public void setPacketType(byte packetType)
	{
		this.packetType = packetType;
	}

	/**
	 * @return the crc8h
	 */
	public byte getCrc8h()
	{
		return crc8h;
	}

	/**
	 * @param crc8h
	 *            the crc8h to set
	 */
	public void setCrc8h(byte crc8h)
	{
		this.crc8h = crc8h;
	}

	/**
	 * @return the data
	 */
	public byte[] getData()
	{
		return data;
	}

	/**
	 * @param data
	 *            the data to set
	 */
	public void setData(byte[] data)
	{
		this.data = data;
	}

	/**
	 * @return the optData
	 */
	public byte[] getOptData()
	{
		return optData;
	}

	/**
	 * @param optData
	 *            the optData to set
	 */
	public void setOptData(byte[] optData)
	{
		this.optData = optData;
	}

	/**
	 * @return the crc8d
	 */
	public byte getCrc8d()
	{
		return crc8d;
	}

	/**
	 * @param crc8d
	 *            the crc8d to set
	 */
	public void setCrc8d(byte crc8d)
	{
		this.crc8d = crc8d;
	}

	public byte[] getPacketAsBytes()
	{
		// byte to unsigned int conversion
		int optLenght = this.optLenght & 0x00FF;

		// byte array to unsigned int conversion
		int dataLenght = ((this.dataLenght[0] << 8) & 0xff00)
				+ ((this.dataLenght[1]) & 0x00ff);

		// 1 syncByte + 2 dataLenght + 1 optLenght + 1 packetType + 1 crcr8h +
		// 1crc8d + dataLength + optDataLenght
		int packetLengthInBytes = 7 + dataLenght + optLenght; 

		byte[] packetAsBytes = new byte[packetLengthInBytes];

		// Header
		packetAsBytes[0] = this.syncByte;
		packetAsBytes[1] = this.dataLenght[0]; // Attenzione mando prima la
												// parte alta
		packetAsBytes[2] = this.dataLenght[1];
		packetAsBytes[3] = this.optLenght;
		packetAsBytes[4] = this.packetType;
		packetAsBytes[5] = this.crc8h;

		for (int i = 0; i < dataLenght; i++)
		{
			packetAsBytes[6 + i] = this.data[i];
		}

		if (optLenght > 0)
		{
			for (int i = 0; i < optLenght; i++)
			{
				packetAsBytes[6 + dataLenght + i] = this.optData[i];
			}
		}

		packetAsBytes[6 + dataLenght + optLenght] = this.crc8d;

		// return the packet as byte array
		return packetAsBytes;
	}

	public byte[] getDataPayload()
	{
		byte[] dataPayload = new byte[this.data.length + this.optData.length];
		dataPayload = data;
		if (optData.length > 0)
		{
			for (int i = 0; i < optData.length; i++)
			{
				dataPayload[i + data.length] = optData[i];
			}
		}
		return dataPayload;
	}

	public void parsePacket(byte[] buffer)
	{
		// "Inpacchetto" cio che arriva in ingresso

		this.syncByte = buffer[0];
		this.dataLenght[0] = buffer[1];
		this.dataLenght[1] = buffer[2];
		this.optLenght = buffer[3];
		packetType = buffer[4];
		this.crc8h = buffer[5];

		// byte array to unsigned int conversion
		int dataLenght = ((this.dataLenght[0] << 8) & 0xff00)
				+ ((this.dataLenght[1]) & 0xff);

		// byte to unsigned int conversion
		int optLenght = this.optLenght & 0xFF;

		// Inizializzo il vettore dei dati alla lunghezza effettiva
		this.data = new byte[dataLenght];

		for (int i = 0; i < dataLenght; i++)
		{
			this.data[i] = buffer[6 + i];
		}

		// Inizializzo il vettore dati opzionali alla lunghezza effettiva
		this.optData = new byte[optLenght];

		for (int i = 0; i < optLenght; i++)
		{
			this.optData[i] = buffer[6 + dataLenght + i];
		}
		this.crc8d = buffer[6 + dataLenght + optLenght];

	} // Fine parsePacket

	// Metodi per discriminare che tipo di pacchetto ho ricevuto
	public boolean isResponse()
	{
		return packetType == RESPONSE;
	}

	public boolean isEvent()
	{
		return packetType == EVENT;
	}

	public boolean isRadio()
	{
		return packetType == RADIO;
	}

	public boolean requiresResponse()
	{
		return this.isEvent()
				&& ((this.packetType == Event.SA_RECLAIM_NOT_SUCCESSFUL)
						|| (this.packetType == Event.SA_CONFIRM_LEARN) || (this.packetType == Event.SA_LEARN_ACK));
	}

	/**
	 * Computes the lenght of an ESP3Packet packet given the first 4 bytes
	 * 
	 * @param partialBuffer
	 * @return
	 */
	public static int getPacketLenght(byte partialBuffer[])
	{
		byte dataLenght[] = new byte[2];
		dataLenght[0] = partialBuffer[1];
		dataLenght[1] = partialBuffer[2];
		byte optLenght = partialBuffer[3];

		// byte array to unsigned int conversion
		int dLenght = ((dataLenght[0] << 8) & 0xff00)
				+ ((dataLenght[1]) & 0xff);

		// byte to unsigned int conversion
		int oLenght = optLenght & 0xFF;

		return (7 + dLenght + oLenght);
	}
}