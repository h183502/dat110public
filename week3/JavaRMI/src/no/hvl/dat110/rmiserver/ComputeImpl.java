package no.hvl.dat110.rmiserver;

import no.hvl.dat110.rmiinterface.ComputeInterface;

/**
 * For demonstration purpose in dat110 course
 */

public class ComputeImpl implements ComputeInterface{
	
	public ComputeImpl() {
	
	}

	public int addNumbers(int a, int b) {
		
		System.out.println("Sum of 2 integers: a = "+a+" and b = "+b);
		
		int sum = a + b;
		
		return sum;
	}


}
