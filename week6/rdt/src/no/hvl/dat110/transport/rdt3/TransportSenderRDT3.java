package no.hvl.dat110.transport.rdt3;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import no.hvl.dat110.transport.*;
import no.hvl.dat110.transport.rdt2.SegmentType;

public class TransportSenderRDT3 extends TransportSender implements ITransportProtocolEntity {

	public enum RDT3SenderStates {
		WAITDATA0, WAITDATA1, WAITACK0, WAITACK1;
	}

	private LinkedBlockingQueue<byte[]> outdataqueue; // move to transport sender base class?
	private LinkedBlockingQueue<SegmentRDT3> recvqueue;
	private RDT3SenderStates state;

	public TransportSenderRDT3() {
		super("TransportSender");
		recvqueue = new LinkedBlockingQueue<SegmentRDT3>();
		outdataqueue = new LinkedBlockingQueue<byte[]>();
		state = RDT3SenderStates.WAITDATA0;
		timeout = new AtomicBoolean(false);
	}

	public void rdt_send(byte[] data) {

		try {

			outdataqueue.put(data);

		} catch (InterruptedException ex) {
			System.out.println("TransportSender thread " + ex.getMessage());
			ex.printStackTrace();
		}
	}

	public void rdt_recv(Segment segment) {

		System.out.println("[Transport:Sender   ] rdt_recv: " + segment.toString());

		try {

			recvqueue.put((SegmentRDT3) segment);

		} catch (InterruptedException ex) {
			System.out.println("TransportSenderRDT2 thread " + ex.getMessage());
			ex.printStackTrace();
		}
	}

	private byte[] data = null;

	public void doProcess() {

		switch (state) {

		case WAITDATA0:

			doWaitData(0);

			break;

		case WAITDATA1:

			doWaitData(1);

			break;

		case WAITACK0:

			doWaitAck(0);

			break;

		case WAITACK1:

			doWaitAck(1);

			break;

		default:
			break;
		}

	}

	private void changeState(RDT3SenderStates newstate) {

		System.out.println("[Transport:Sender   ] " + state + "->" + newstate);
		state = newstate;
	}

	private AtomicBoolean timeout;
	private Timer timer;

	private void stop_timer() {
		timer.cancel();
		timeout.set(false);
	}
	
	public void start_timer() {
		timeout.set(false);
		timer = new Timer(); // CHECK: new timer at each timeout?
		timer.schedule(new TimeOutTask(), 1000);
	}
	
    class TimeOutTask extends TimerTask {
    	
        public void run() {
            System.out.println("[Transport:Sender   ] TIMEOUT");
            timer.cancel(); 
            timeout.set(true);
        }
    }
    
	private void doWaitData(int seqnr) {

		try {

			data = outdataqueue.poll(2, TimeUnit.SECONDS);

			if (data != null) { // something to send

				udt_send(new SegmentRDT3(data, seqnr));

				start_timer();

				if (seqnr == 0) {
					changeState(RDT3SenderStates.WAITACK0);
				} else {
					changeState(RDT3SenderStates.WAITACK1);
				}

			}

		} catch (InterruptedException ex) {
			System.out.println("TransportSenderRDT3 thread " + ex.getMessage());
			ex.printStackTrace();
		}

		try {

			SegmentRDT3 acksegment = recvqueue.poll(2, TimeUnit.SECONDS);

			if (acksegment != null) {
				System.out.println("[Transport:Sender   ] DISCARD: " + acksegment.toString());
			}

		} catch (InterruptedException ex) {
			System.out.println("TransportSenderRDT3 thread " + ex.getMessage());
			ex.printStackTrace();
		}

	}

	private void doWaitAck(int seqnr) {

		if (timeout.get()) {
			
			System.out.println("[Transport:Sender   ] RETRANSMIT ");
			
			udt_send(new SegmentRDT3(data, seqnr)); // retransmit

			start_timer();
		}

		try {

			SegmentRDT3 acksegment = recvqueue.poll(2, TimeUnit.SECONDS);

			if (acksegment != null) { // something received via rdt_recv

				if (acksegment.isCorrect() && (acksegment.getSeqnr() == seqnr)) {

					stop_timer();
					
					System.out.println("[Transport:Sender   ] ACK");

					if (seqnr == 0) {
						changeState(RDT3SenderStates.WAITDATA1);
					} else {
						changeState(RDT3SenderStates.WAITDATA0);
					}

					data = null;
				}

				// if wrong seq nr or corrupt - just wait for timeout
			}
		} catch (InterruptedException ex) {

			System.out.println("TransportSenderRDT2 thread " + ex.getMessage());
			ex.printStackTrace();
		}

	}
}
