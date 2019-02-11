package no.hvl.dat110.udp.multiplexing;

import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;


public class SenderProcess {
	
	public static void main(String[] args) throws SocketException, UnknownHostException {
		
		if (args.length != 2) {
			throw new RuntimeException("usage: SenderProcess <remotehost> <remoteport>");
		}
		
		String host = args[0];	
		int port = Integer.parseInt(args[1]);
		
		UDPSender sender = new UDPSender(host,port);
		
		System.out.println("SenderProcess to " + host + ":" + port);
		
		String message = null;
		Scanner input;
		
		do {
			
			System.out.println("!");
			
			input = new Scanner(System.in);
			
			message = input.nextLine();
			
			sender.send(message.getBytes());
			
		} while (message != null && !(message.equals("")));
		
		input.close();
		
		System.out.println("SenderProcess terminate");
		
	}	
}
