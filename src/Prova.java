/**
 * 
 */

/**
 * @author bonino
 *
 */
public class Prova
{
	
	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		// TODO Auto-generated method stub
		byte  lowByte = (byte)0x80;
		byte  highByte = (byte)0x80;
		
		int number = ((highByte<<8) & 0xff00)+ ((lowByte) & 0xff);

	
		
		System.out.println("LowByte (0x80): "+lowByte+" HighByte (0x80):"+highByte+" Number (32896): "+number);
	}
	
}
