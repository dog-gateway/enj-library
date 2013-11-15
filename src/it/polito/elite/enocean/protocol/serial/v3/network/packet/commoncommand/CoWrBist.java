package it.polito.elite.enocean.protocol.serial.v3.network.packet.commoncommand;

public class CoWrBist{
	/**
	 * 
	 */
	private final CommonCommand commonCommand;

	@SuppressWarnings("null")
	public CoWrBist(CommonCommand commonCommand){		
		this.commonCommand = commonCommand;
		byte header[] = null;
		this.commonCommand.syncByte = 0x55;
		this.commonCommand.dataLenght[0]=0x00;
		this.commonCommand.dataLenght[1]=0x01;
		this.commonCommand.optLenght = 0x00;
		this.commonCommand.packetType = 0x05;
		header[0] = this.commonCommand.dataLenght[0];
		header[1] = this.commonCommand.dataLenght[1];
		header[2] = this.commonCommand.optLenght;
		header[3] = this.commonCommand.packetType;
		//this.crc8h = CRC8.calc(header, 3);
		//this.data[0] = 0x06; //Command code
		//this.optData ;       QUESTO CAMPO NON LO METTO O LO INIZIALIZZO A NULL?
		//this.crc8d = CRC8.calc(data, dataLenght);
	}
}