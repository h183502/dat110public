package no.hvl.dat110.concurrency.demo;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;

public class CountConnectionReqHandler extends AbstractHandler {
	
	/* 
	 * Variable for counting requests handled
	 */
	private long count = 0;

	@Override
	public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		
		response.setContentType("text/plain");
		response.setStatus(200);
		baseRequest.setHandled(true);
		
		final long current; 
		
		//concurrency - uncomment to check the counts when the critical region is synchronised		
		synchronized(this) {
			current = ++count;
		}

		response.getWriter().println(" "+current);
		
		
	}
	
	public static void main (String[] args) throws Exception{
		Server server = new Server(9091);
		server.setHandler(new CountConnectionReqHandler());
		
		server.start();
		server.join();
	}

}
