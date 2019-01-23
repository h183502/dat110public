package no.hvl.dat110.concurrency.demo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class Client extends Thread {
	
	public void run() {
		
		char[] buffer = new char[1024];
		int i = 0;
		do {
			try {
				
				URL server = new URL("http://localhost:9091");
				URLConnection con = server.openConnection();
				
				BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
				br.read(buffer, 0, buffer.length);
				
				System.out.println(new String(buffer));
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			i++;
		} while (i < 10);
			
	}
	
	public static void main(String[] args) throws InterruptedException {
		
		Client c1 = new Client();
		Client c2 = new Client();
		
		c1.start();
		c2.start();
		
		c1.join();
		c2.join();
		
		
	}

}
