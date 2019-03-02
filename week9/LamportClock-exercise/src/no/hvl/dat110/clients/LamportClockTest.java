package no.hvl.dat110.clients;

import java.util.Random;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import no.hvl.dat110.process.ProcessContainer;

class LamportClockTest {
	
	private SimulateReplicas sr;
	
	@BeforeEach
	void setUp() throws Exception {
		Random r = new Random();
		
		new ProcessContainer("process1", r.nextInt(10000));
		new ProcessContainer("process2", r.nextInt(10000));
		new ProcessContainer("process3", r.nextInt(10000));
		sr = new SimulateReplicas();
		sr.create();
	}

	@Test
	void test() {
		Assert.assertEquals(sr.getP1Bal(), sr.getP2Bal(), 0);
		Assert.assertEquals(sr.getP1Bal(), sr.getP3Bal(), 0);
		
	}

}
