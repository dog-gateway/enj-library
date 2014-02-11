/**
 * 
 */
package packagemain;


import it.polito.elite.enocean.enj.EEP2_5.communication.EnjCommunicator;
import it.polito.elite.enocean.enj.EEP2_5.primitives.Device;
import it.polito.elite.enocean.enj.knowndevices.EnjDevices;
import it.polito.elite.enocean.protocol.serial.v3.network.connection.EnjConnection;

/**
 * @author Andrea Biasi <biasiandrea04@gmail.com>
 *
 */
public class Enocean {


	/**
	 * @param args
	 */
	public static void main(String[] args) {

		String lettura = null;
		EnjDevices devices = new EnjDevices();

		// Reload devices
		//   devices.load(); // C'e exception perch non c' una minchia da caricare
		// end reload

		/*
		System.out.println("Select operation:");
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

		try {
			lettura = in.readLine();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}*/
		
		EnjConnection connection = new EnjConnection();
		EnjCommunicator communicator = new EnjCommunicator(connection);
		
		try {
			connection.startConnection();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		lettura = "teach";
		if(lettura.equals("teach")){
			Device newDevice = communicator.teach();
			devices.add(newDevice);
		}
		else{
			if(lettura.equals("command")){
				communicator.sendCommand();
				/*
				 * 
				 * COMMUNICATION
				 * 
				 */
			}
		}
		//Alla fine salva i dispositivi --> devices.save
	}
}