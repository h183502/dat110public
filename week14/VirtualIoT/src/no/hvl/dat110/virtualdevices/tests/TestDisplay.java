package no.hvl.dat110.virtualdevices.tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import no.hvl.dat110virtualdevices.Display;

class TestDisplay {

	@Test
	void test() {
		
		Display display = new Display();
		
		display.write("TEST MESSAGE");
	}

}
