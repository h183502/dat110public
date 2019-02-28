### Lab Week 9: 04/03 - 08/03

#### Exercises under construction

#### Exercise 1 - Reliable Data Transfer Framework

For doing some practical experiments with implementation of transport layer protocols for reliable data transfer (rdt), we will use the framework which can be found in the Eclipse project at:

https://github.com/selabhvl/dat110public/tree/master/week6/rdt

and which has also been discussed at the lectures on transport protocols

The main goal of this exercise is to start becoming familiar with this framework.

Start by importing the project into your IDE (Eclipse) and make sure that the classes and the tests compiles.

The project is organised into the following packages

- `no.hvl.dat110.application` implements a sender process that sends messages to a receiver process using the underlying transport protocol implementation.

- `no.hvl.dat110.transport` contains the classes that implements the transport protocols for reliable data transfer of segments. The `TransportSender` and `TransportReceiver` classes in this package, implements the reliable data transfer over a perfectly reliable channel (rdt1.0) as described on pages 236-237 in the networking book.

- `no.hvl.dat110.network` implements a simulated network that can connect a sender and a receiver by means of two channels (one in each direction). By changing the type of channel we can simulate reliable and unreliable networks and experiments with different transport protocol implementations.

- `no.hvl.dat110.transport.rdt2` implements the rdt2.0 transport protocol for reliable data transfer over a channel with bit errors as described on pages 237-239 in the networking book

- `no.hvl.dat110.trasport.tests` implements some JUnit-tests for testing the transport protocols. The basic requirement is that the receiver must receive all data from the sender and in correct order.

#### Exercise 2 - RDT 2.1

Implement the RDT 2.1 from the book

#### Exercise 3 - RDT 3 - study the implementations

#### Exercise 4 - RDT 4

#### Exercise 5 - RDT 4.1
