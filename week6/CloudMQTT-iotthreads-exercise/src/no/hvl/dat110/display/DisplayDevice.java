package no.hvl.dat110.display;


public class DisplayDevice extends Thread {
		
	
	public void run() {
		
		System.out.println("Display started");	
		
		// TODO 
		
		try {
			// Make a new instance of MQTTSubClient
			
			while (true) {
					
				// use the sub instance to get the temperature
				
				// Print the temp in the console
				System.out.println("Display [Temp]: ");
			}
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
	}

}
