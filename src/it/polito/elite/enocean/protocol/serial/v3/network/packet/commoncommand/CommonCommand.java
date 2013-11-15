/**
 * 
 */
package it.polito.elite.enocean.protocol.serial.v3.network.packet.commoncommand;

import it.polito.elite.enocean.protocol.serial.v3.network.packet.Packet;


/**
 * @author andreabiasi
 *
 */

//public class CommonCommand extends Packet{
//}

// TYPE 4 - Non so se va fatta una classe Event dal momento che possono solo essere ricevuti
/*public class Event extends Packet{

		FORSE SI POSSONO FARE DEI METODI BOOLEAN PER VERIFICARE DI CHE TIPO DI EVENTO SI TRATTA


		public Event(byte CRC8H, byte event_code, byte CRC8D){
			sync_byte = 0x55;
			dataLenght = 0x01;
			optLenght = 0x00;
			packetType = 0x04;
			this.CRC8H = CRC8H;
			this.data[0] = event_code; //EVENT CODE
			//this.optData ;       STO CAMPO NON LO METTIAMO ?
			this.CRC8D = CRC8D;		
		}		
	}*/

// TYPE 5
public class CommonCommand extends Packet{
}