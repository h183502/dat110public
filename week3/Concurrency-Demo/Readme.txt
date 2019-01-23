Counting the number of client connections processed by a server
===============================================================

This code demonstrates the problem with concurrency in a client-server environment.

We use the lightweight jetty webserver that accepts incoming HTTP requests from two clients. 
The two clients are simulated using threads and they connect to the server using the URLConnection.

The server uses a handle that processes incoming HTTP request, increments the counter variable and sends the response
to the client via printwriter object.

The critical region is then the section where the count is incremented in the handle method in CountConnectionReqHandler.java

To run this demo:

A. Import the project into eclipse
1. Download from github to your machine
2. From eclipse, click File->Import and Import as existing Maven project.

B. Running this example
1. Start the server by running the CountConnectionReqHandler.java
2. Run the clients many times
3. Comment and uncomment the synchronized region and repeat step 2. Each time, check the total count printed to the output.

Each client should connect ten (10) times. 
Correct output (count) = 20 	//This is the case when the critical region is locked (synchronized) allowing that the clients enter
the region one at a time.