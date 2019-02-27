package no.hvl.dat110.transport.rdt4;

import java.util.Timer;
import java.util.TimerTask;

import no.hvl.dat110.network.Channel;
import no.hvl.dat110.network.Datagram;

public class DelayChannel extends Channel {

	public DelayChannel(String name, AdversaryRDT4 adversary) {
		super(name, adversary);
	}

	private String getNetwork() {
		return super.name;
	}

	class DelayDatagram extends TimerTask {

		private Datagram datagram;

		public DelayDatagram(Datagram datagram) {
			this.datagram = datagram;
		}

		public void run() {

			try {
				
				System.out.print("[Network:" + getNetwork() + "] delayed arrival: " + datagram.toString());
				
				datagramqueue.put(datagram);
				
			} catch (InterruptedException ex) {

				System.out.println("Delay channel send " + ex.getMessage());
				ex.printStackTrace();
			}

		}
	}

	@Override
	public void send(Datagram datagram) {

		System.out.print("[Network:" + super.name + "] transmit: " + datagram.toString());

		datagram = observer.process(datagram);

		if (datagram != null) {

			AdversaryRDT4 adversary = (AdversaryRDT4) observer;

			int delay = adversary.delay();

			System.out.println();
			
			if (delay > 0) {

				Timer timer = new Timer();
				timer.schedule(new DelayDatagram(datagram), delay);

			} else {

				try {

					datagramqueue.put(datagram);

				} catch (InterruptedException ex) {

					System.out.println("Delay channel send " + ex.getMessage());
					ex.printStackTrace();
				}
			}
		}
	}
}
