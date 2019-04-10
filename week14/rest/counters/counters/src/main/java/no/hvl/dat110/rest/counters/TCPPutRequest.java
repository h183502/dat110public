package no.hvl.dat110.rest.counters;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class TCPPutRequest {

	private static int port = 8081;
    private static String host = "ec2-3-19-66-128.us-east-2.compute.amazonaws.com";
    private static String uri = "/counters";
    
	public static void main(String[] args) {

		Counters counters = new Counters(2,4);
		
		try (Socket s = new Socket(host, port)) {
         
         OutputStream output = s.getOutputStream();
         // no auto-flushing
         
         PrintWriter pw = new PrintWriter(output, false);
         
         pw.print("PUT " + uri + " HTTP/1.1\r\n");
         pw.print("Host: " + host + "\r\n");
         pw.print("Content-type: application/json\r\n");
         
         String body = counters.toJson();
         pw.print("Content-length: " + body.length() + "\r\n");
         pw.print("Connection: close\r\n");
         pw.print("\r\n");
         pw.print(body);
         pw.print("\r\n");
         
         pw.flush();
         
         System.out.print(body);
         System.out.println(body.length());
         InputStream in = s.getInputStream();
         InputStreamReader isr = new InputStreamReader(in);
         BufferedReader br = new BufferedReader(isr);
         
         int c;
         while ((c = br.read()) != -1) {
           System.out.print((char) c);
         }
		}
		catch (IOException ex) {
        System.err.println(ex);
      }

    }
}
