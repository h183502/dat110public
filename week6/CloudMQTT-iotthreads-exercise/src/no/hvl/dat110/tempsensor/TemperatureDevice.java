package no.hvl.dat110.tempsensor;


public class TemperatureDevice extends Thread {

	private TemperatureSensor sn;
	
	public TemperatureDevice() {
		this.sn = new TemperatureSensor();
	}
	
	public void run() {
		
		System.out.println("temperature device started");
		
		// TODO
		//call MQTTPubClient (create a new instance) and make connection
		
		while(true) {			
			
			// read the temp from the TemperatureSensor
			
			System.out.println("Sensor Reading [Temp]: ");
			
			// use the MQTTPubClient instance object to publish the temp to the CloudMQTT Broker 

		}
		
	}
	
}
