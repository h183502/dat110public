### Lab Week 3: 21/01 - 25/01

#### Exercise 1 - Socket and Network Programming - Warmup

Consider the client-server echo network application covered in the lecture on Friday 18.1. The source Java code for the TCP and the UDP implementation is available from here:

https://github.com/selabhvl/dat110public/tree/master/week2

The implementation uses the Java Socket API as documented here:

https://docs.oracle.com/javase/10/docs/api/java/net/package-summary.html

##### 1.1

Open the two projects in Eclipse and run both the UDP and the TCP client-server example.

##### 1.2

Use Eclipse to build an executable jar-file for the UDP client and server and the TCP client and server. Run the two executable jar-files in a shell/command prompt using

`
java -jar X.jar <command-line arguments>
`

##### 1.3

Team up with one of the other students and try to run the client-side on one machine and the server side on another machine. You need to find the IP address of the machine where you intend to run the server.

##### 1.4

Experiment with the network application.

- What happens if you start two servers on the same port?
- What happens if the TCP client attempts to connect to the server but the server is not running?
- Modify the server such that it sleeps for some number of seconds before returning a response. What happens a TCP client attempts to connect when there is already another TCP client request being served?

##### 1.5

Would it be easy to modify the current TCP implementation with a keep-alive feature such that a TCP connection does not have to be created for each request from the same client?

#### Exercise 2 - Socket and Network Programming

Consider the small IoT example from week 2:

https://github.com/selabhvl/dat110public/tree/master/week2/iotthreads/src/no/hvl/dat110/threading

where communication between the temperature device and the display device was performed using a shared memory object, and where the temperature device and display device were running as two threads in the same JVM.

Revise the implementation such the temperature device and the display device runs as separate processes and uses sockets for communication between the two entities. The temperature device should act as as client reporting temperature, and the display should act as a server receiving request to display the current temperature.

Implement both a TCP and a UDP variant. Use the example code from exercise 1 above for inspiration on how to implement the IoT system as a networked application using sockets.

#### Exercise 3 - DNS and HTTP Wireshark

Perform the Wireshark labs on DNS and HTTP described at http://www-net.cs.umass.edu/wireshark-labs/Wireshark_HTTP_v7.0.pdf and http://www-net.cs.umass.edu/wireshark-labs/Wireshark_TCP_v7.0.pdf

#### Exercise 4 - Layering and Encapsulation

Consider a small network with two hosts H1 and H2 and two routers R1 and R2. H1 is connected to R1, R1 is connected to R2, and R2 is connected to H2 via communication links.

Assume that we have some data in a datagram packet that is to be sent from H1 via R1 and R2 to H2. What encapsulation and decapsulation will happen on the boundaries between the network and link layers along the way?

#### Exercise 5 - Internet Protocols

Problem P3 in Chap. 2 of the networking book with the modification that you should draw a [time sequence diagram]( https://en.wikipedia.org/wiki/Sequence_diagram) showing the interaction between the different protocol entities.

#### Exercise 6 - Domain Name System

Problems P7 and P8 in Chap. 2 of the networking book.
