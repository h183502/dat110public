### Lab Week 6: 11/02 - 15/02

#### Exercise 1 - Transport Layer - Warmup

For doing some practical experiments with implementation of transport layer protocols for reliable data transfer (rdt), we will use the framework which can be found in the Eclipse project at:

https://github.com/selabhvl/dat110public/tree/master/week6/rdt

The main goal of this exercise is to start becoming familiar with this framework.

##### Exercise 1.1

Start by importing the project into your IDE (Eclipse) and make sure that the classes and the tests compiles.

The project is organised into the following packages

- `no.hvl.dat110.application` implements a sender process that sends messages to a receiver process using the underlying transport protocol implementation.

- `no.hvl.dat110.transport` contains the classes that implements the transport protocols for reliable data transfer of segments. The `TransportSender` and `TransportReceiver` classes in this package, implements the reliable data transfer over a perfectly reliable channel (rdt1.0) as described on pages 236-237 in the networking book.

- `no.hvl.dat110.network` implements a simulated network that can connect a sender and a receiver by means of two channels (one in each direction). By changing the type of channel we can simulate reliable and unreliable networks and experiments with different transport protocol implementations.

- `no.hvl.dat110.transport.rdt2` implements the rdt2.0 transport protocol for reliable data transfer over a channel with bit errors as described on pages 237-239 in the networking book

- `no.hvl.dat110.trasport.tests` implements some JUnit-tests for testing the transport protocols. The basic requirement is that the receiver must receive all data from the sender and in correct order.

##### Exercise 1.2

Perform the following experiments

1. Run the main-method in `Main.java` where a sender process sends three messages to the receiver. Try to understand the output generated in the console and where it is generated from.

2. Run the `TestRDT1.java` which tests the rdt1.0 transport protocol over a perfect channel.

3. Run the `TestRDT2.java` which tests the rdt2.0 transport protocol over a perfect channel.

4. Run the `TestRDT2BitErrors.java` which test the rdt2.0 transport protocol over a channel with bit-errors.

#### Exercise 2 - Handling Corrupt ACK/NAK segments

The `Adversary2.java` class implements a network adversary that at random sets the checksum in the transmitted segment to `0` thereby simulating a transmission errors. This in will case the `doProcess()` methods in `TransportReceiverRDT2.java` to detect that the receive segment has a checksum errors and therefore send a NAK segment.

Methods for calculating and checking checksums can be found in the `Segment.java` class.

As a checksum of `0` is the correct checksum for ACK and NAK segments (see Segment-constructors), then this effectively mean that only DATA segments can have transmission errors.

##### Exercise 2.1

Modify the implementation of the `Segment.java` class such that the correct checksum simulating a transmission error is set to 1. This means that also ACK/NAK can have checksum errors.

##### Exercise 2.2

Augment the implementation of the sender side in the rdt2.0 transport protocol in `TransportSenderRDT2.java` such that the sender checks for any transmission errors on ACK and NAK segments.

What could/should the sender do in case a corrupt ACK / NAK segment is received? 

Implement you proposed solution and use the `TestRDT2BitErrors.java` test test the solution. You can modify the probability of transmission errors by adjusting the value `CORRUPTPB` in `TestRDT2BitErrors.java`

#### Exercise 3 - CloudMQTT

- description is underway
