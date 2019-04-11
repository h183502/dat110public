package no.hvl.dat110virtualdevices;

public class TemperatureSensor {

	static final int RANGE = 100;
	
	public int read () {
		
		long seconds = System.currentTimeMillis();
		
		double temp = RANGE * Math.sin(seconds / 1000);
		
		return (int) Math.ceil(temp);
	}
}
