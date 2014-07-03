/**
 * 
 */
package it.polito.elite.enocean.enj.knowndevices;

import it.polito.elite.enocean.enj.EEP26.Device;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 * @author Andrea Biasi <biasiandrea04@gmail.com>
 *
 */
public class EnjDevices {

	/**
	 * 
	 */
	public EnjDevices() {
		super();
	}

	ArrayList<Device> devices = new ArrayList<Device>(1);

	public void add(Device device){
		devices.add(device);
	}

	// Salva in un file i dispostivi
	public void save(){
		try  
		{  
			FileOutputStream fileOut = new FileOutputStream("/home/pi/devices.ser");  
			ObjectOutputStream outStream = new ObjectOutputStream(fileOut);

			outStream.writeObject(devices);

			System.out.println("Ho salvato i device");
			outStream.close();  
			fileOut.close();  
		}catch(IOException i)  
		{  
			i.printStackTrace();  
		}  
	}

	@SuppressWarnings("unchecked")
	public void load(){
		try  
		{  
			FileInputStream fileIn =new FileInputStream("devices.ser");  
			ObjectInputStream in = new ObjectInputStream(fileIn);  

			this.devices = (ArrayList<Device>) in.readObject(); // C'e un warning soppresso che nn capisco ma funziona

			in.close();  
			fileIn.close();  
		}catch(IOException i)  
		{  
			i.printStackTrace();  
			return;  
		}catch(ClassNotFoundException c)  
		{  
			System.out.println("Employee class not found");  
			c.printStackTrace();  
			return;  
		}  
	}

	public Device peekDevice(){
		return this.devices.get(0);
	}
}