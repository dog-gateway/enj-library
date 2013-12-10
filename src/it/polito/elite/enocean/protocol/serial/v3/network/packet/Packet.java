package it.polito.elite.enocean.protocol.serial.v3.network.packet;

import it.polito.elite.enocean.protocol.serial.v3.network.crc8.Crc8;

/**
 * A class for representing EnOcean Serial Protocol version 3 packets
 * 
 * @author Andrea Biasi <biasiandrea04@gmail.com>
 *
 */

// ATTENZIONE, per i metodi setLenght e getLenght meglio usare tipo int?

public abstract class Packet
{
	// serial synchronization byte
	protected byte syncByte; // Il problema ï¿½ che byte ï¿½ signed

	// number of bytes in the data part of the packet (DATA_LENGHT)
	protected byte dataLenght[]; // 2 byte

	// number of bytes of optional data (OPTIONAL_LENGHT)
	protected byte optLenght;

	// identifies the packet type
	protected byte packetType;

	// checksum for bytes DATA_LENGHT, OPTIONAL_LENGHT and TYPE
	protected byte crc8h;

	// data payload (DATA)
	protected byte data[];

	// additional data extending the data payload (OPTIONAL_DATA)
	protected byte[] optData;

	// checksum for DATA and OPTIONAL_DATA
	protected byte crc8d;

	// --------------- Constructors --------------------

	/**
	 * Empty constructor (to support the bean instantiation pattern)
	 */
	public Packet()
	{
		// empty
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
	public  Packet(byte packetType, byte[] data, byte[] optData)
	{
		this.syncByte = 0x55;
		this.packetType = packetType;
		this.data = data;
		this.optData = optData;
		this.buildPacket();
	}

	public void buildPacket(){
		this.dataLenght[0] = (byte) (data.length & 0xff); //Parte bassa dei 2 byte
		this.dataLenght[1] = (byte) ((data.length & 0xff00)>>8); //Parte alta dei due byte
		this.optLenght = (byte) (optData.length);
		byte header[] = new byte[4];
		header[0] = this.dataLenght[0];
		header[1] = this.dataLenght[1];
		header[2] = this.optLenght;
		header[3] = this.packetType;

		this.crc8h = Crc8.calc(header);		
		byte vectData[] = new byte[data.length+optData.length];
		for(int i=0 ; i<data.length; i++){
			vectData[i] = data[i];
		}
		// Se non ho dati opzionali non metto pi nulla nel vettore
		if(optLenght != 0){
			for(int i = data.length ; i<optData.length+data.length; i++){
				vectData[i] = optData[i];
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

	public byte[] getBytes()
	{
		// 1 syncByte + 2 dataLenght + 1 optLenght + 1 packetType + 1 crcr8h +
		// 1crc8d + dataLength + optDataLenght
		int packetLengthInBytes = 5 + this.dataLenght.length + this.data.length + this.optData.length;

		byte[] packetAsBytes = new byte[packetLengthInBytes]; 
		
		packetAsBytes[0] = this.syncByte;
		packetAsBytes[1] = this.dataLenght[0];
		packetAsBytes[1] = this.dataLenght[1];
		packetAsBytes[3] = this.optLenght;
		packetAsBytes[4] = this.packetType;
		packetAsBytes[5] = this.crc8h;

		//byte array to unsigned int conversion
		int dataLenght = ((this.dataLenght[1] << 8) & 0xff00) + ((this.dataLenght[0]) & 0xff);

		//byte to unsigned int conversion
		int optLenght = this.optLenght & 0xFF;

		for (int i = 0; i < dataLenght; i++)
		{
			packetAsBytes[6 + i] = this.data[i];
		}
		for (int i = 0; i < optLenght; i++)
		{
			packetAsBytes[6 + dataLenght + i] = this.optData[i];
		}

		//return the packet as byte array
		return packetAsBytes;
	} 

	/*
	 * ------------- Metodi di GET ---------------------------------
	 */

	public void parsePacket(byte[] buffer)
	{ 
		// "Inpacchetto" cio che arriva in ingresso

		this.syncByte = buffer[0];
		this.dataLenght[0] = buffer[1]; 
		this.dataLenght[1] = buffer[2];
		this.optLenght = buffer[3];
		packetType = buffer[4];
		this.crc8h = buffer[5];

		//byte array to unsigned int conversion
		int dataLenght = ((this.dataLenght[1] << 8) & 0xff00) + ((this.dataLenght[0]) & 0xff);

		//byte to unsigned int conversion
		int optLenght = this.optLenght & 0xFF;


		for (int i = 0; i < dataLenght; i++)
		{
			this.data[i] = buffer[6 + i];
		}
		for (int i = 0; i < optLenght; i++)
		{
			this.optData[i] = buffer[6 + dataLenght + i];
		}
		this.crc8d = buffer[6 + dataLenght + optLenght];

	} // Fine parsePacket

	// Metodi per discriminare che tipo di pacchetto ho ricevuto
	public boolean isResponse()
	{
		return packetType == 0x02;
	}

	public boolean isEvent()
	{
		return packetType == 0x04;
	}
	
	public boolean isRadio(){
		return packetType == 0x01;
	}

}