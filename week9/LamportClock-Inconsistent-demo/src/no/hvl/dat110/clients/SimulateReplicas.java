package no.hvl.dat110.clients;

public class SimulateReplicas {
	
	private	Process1 p1;
	private	Process2 p2;
	private	Process3 p3;
	
	public SimulateReplicas() throws InterruptedException {
		p1 = new Process1();
		p2 = new Process2();
		p3 = new Process3();
		
		p1.start();
		p2.start();
		p3.start();
		
		p1.join();
		p2.join();
		p3.join();
	}
	
	
	public double getP1Bal() {
		return p1.getFinalbalance();
	}


	public double getP2Bal() {
		return p2.getFinalbalance();
	}


	public double getP3Bal() {
		return p3.getFinalbalance();
	}


	public static void main(String[] args) throws InterruptedException {
		
		Process1 p1 = new Process1();
		Process2 p2 = new Process2();
		Process3 p3 = new Process3();
		
		p1.start();
		p2.start();
		p3.start();
		
		p1.join();
		p2.join();
		p3.join();

	}

}
