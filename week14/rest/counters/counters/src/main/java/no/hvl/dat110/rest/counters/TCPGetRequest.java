package no.hvl.dat110.rest.counters;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.Socket;

public class TCPGetRequest {

	private static int port = 8080;
    private static String host = "localhost";
    private static String uri = "/counters";
    
	public static void main(String[] args) {

		try (Socket s = new Socket(host, port)) {
         
         OutputStream output = s.getOutputStream();
         // no auto-flushing
         
         PrintWriter pw = new PrintWriter(output, false);
         
         pw.print("GET " + uri + " HTTP/1.1\r\n");
         pw.print("Accept: application/json\r\n");
         pw.print("Host: localhost\r\n");
         pw.print("\r\n");
         pw.flush();
         
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
