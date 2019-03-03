### Lab Week 9: 04/03 - 08/03

#### Exercise 1 - Reliable Data Transfer Framework

Exercises 2 and 3 below is concerned with implementation of transport layer protocols based on the RDT Java framework that has been introduced and discussed in the lectures on the transport layer.

To undertake the following two exercises it is assumed that you have read pages 234-245 in the networking book. You may also want to review the lecture notes from the transport layer I and transport layer II lectures, in particular the parts that cover the RDT implementation framework.

Make sure that you have pulled the most recent version of the framework available as an Eclipse-project at:

https://github.com/selabhvl/dat110public/tree/master/week6/rdt

If you have not already done so earlier, then start by importing the project into your IDE (Eclipse) and make sure that the classes and the tests compiles.

The project is organised into the following packages

- `no.hvl.dat110.application` implements a sender process that sends messages to a receiver process using the underlying transport protocol implementation.

- `no.hvl.dat110.network` implements a simulated network that can connect a sender and a receiver by means of two channels (one in each direction). By changing the type of channel and the associated adversary, we can simulate reliable and unreliable networks and experiment with different transport protocol implementations.

- `no.hvl.dat110.transport` contains the base classes for implementing transport protocols for reliable data transfer of segments and defines the basic primitives of `rdt_send`, `deliver_data`, `udt_send`, and `rdt_recv`.

- `no.hvl.dat110.transport.rdt1` implements the RDT1.0 protocol for reliable data transfer over a perfectly reliable channel as described on pages 236-237 in the networking book.

- `no.hvl.dat110.trasport.tests` implements some JUnit-tests for testing the transport protocols. The basic correctness criteria is that the receiver must receive all data from the sender and in correct order.

#### Exercise 2 - RDT 2.2 Implementation

The  `no.hvl.dat110.transport.rdt2` package  implements the RDT 2.1 transport protocol for reliable data transfer over a channel with bit errors as described on pages 237-239 in the networking book. The implementation of the sender and receiver are in the classes `TransportSenderRDT21.java` and `TransportReceiverRDT21.java` as presented at the lectures.

The transport receiver of RDT2.1 uses a NAK (negative acknowledgement) to signal to the sender that a corrupt data segment has been received. As described on page 242 in the networking book, then it is also possible to replace the use of NAKs with the use of an ACK (acknowledgement) in combination with a sequence number.

Modify the RDT 2.1 implementation (i.e., the classes `TransportSenderRDT21.java` and `TransportReceiverRDT21.java`) to become an RDT2.2 implementation as described on page 242 in the networking book.

The test `TestRDT21Adversary21.java` can be used to test your protocol implementation.

#### Exercise 3 - Reliable Transport and Overtaking

The  `no.hvl.dat110.transport.rdt2` package implements the RDT 3.0 transport protocol for reliable data transfer over a channel with bit errors and loss of segments as described on pages 242-245 in the networking book.

##### Task-1: Study the RDT 3.0 Implementation

The implementation of the sender in `TransportSenderRDT3.java` uses a timer to implement timeouts, and thereby recover from possible loss of data and acknowledgements. Study the implementation of the transport sender and transport receiver in order to understand how it implements retransmission and the state machines shown in Figures 3.15 and 3.16.

The test in `TestRDT3Adversary3.java` can be used to run the implementation.  

##### Task-2: Implementation of the RDT 4.0 Protocol

The RDT3.0 protocol cannot recover from overtaking of segments, i.e., that segments may arrive in a different order from which they were sent.

The `no.hvl.dat110.transport.rdt4` package contains templates for implementing a RDT 4.0 protocol that can recover from overtaking of segments. The protocol is to replace the alternating bit sequence number of RDT3.0 with an increasing sequence number and operates as follows:

- The sender keeps sending the same data segment (having the same sequence number) until an acknowledgement with a sequence number which is one higher is received. This should include retransmission similar to RDT 3.0. This means that the sender interprets a sequence number received in an acknowledge which is one higher that its current sequence number as indication that the receiver has received the data segment with the current sequence number.

- The receiver keeps an internal sequence number indicating the data segment expected next. If a data segment with the expected sequence number is received, then the internal sequence number is incremented and a corresponding acknowledgement segment is sent back to the sender. If the receiver receives a data segment with the wrong (non-expected) sequence number, then the receiver replies with an acknowledgement segment containing the sequence number of the data segment expected next.

The test in `TestRDT4Adversary4.java` can be used to test and run the protocol implementation.

##### Task-3: Variant of the RDT4.0 Protocol

Modify the implementation of the receiver side of the RDT4.0 protocol in Task-2 such that an acknowledgement is only sent if the data segment with the expected sequence number is received. Is the protocol still correct?

#### Exercise 4 - Lamport Clock

This exercise is based on Section 6.2 of the distributed system book. The goal is to show how event ordering in distributed systems can be achieved by using logical clock as proposed by Lamport.

The implementation is based on a totally-ordered multicasting where every process has the same copy of the queue first before performing their operations. This is the most basic implementation but can be optimized. A totally-ordered multicasting is crucial for replicated services to keep replicas consistent by ensuring that each replica execute the same operations in the same order.

To get started, download the project and import it to your Eclipse IDE. Project can be downloaded here: https://github.com/selabhvl/dat110public/tree/master/week9/LamportClock-exercise

You can also download a default implementation of inter-process communications where replica processes simply send updates to each other that results in an inconsistent state. Project can be found here: https://github.com/selabhvl/dat110public/tree/master/week9/LamportClock-Inconsistent-demo

The processes communicate using the Java RMI. Current implementation uses three separate processes each with a main thread on a single computer to simulate inter-process communication in a distributed system. You can use multiple computers by changing the registry configuration and specifying the IP addresses of the communicating hosts in the Util/ProcessContainer classes

The project is organized as follows:

- no.hvl.dat110.clients: contains three client processes (process1, process2, & process3). Each client initiates some operations such as requestDeposit, requestInterest, or requestWithdrawal to the replica process. The main class (SimulateReplicas) simulates the interprocess communication, and the JUnit test (LamportClockTest) is used to test the result whether the state of the resource (balance) is consistent across the processes after the operations.

- no.hvl.dat110.process: contains the 'process' class that implements the Lamport clock and its application using three operations (deposit, interest, and withdrawal). ProcessContainer class is the 'server' for the 'process' class. This is where the registry is started and where the binding of the process implementation to the registry is done. Message class is used to construct the message to be passed between the processes.

- no.hvl.dat110.process.iface: contains the process interface (ProcessInterface) where remotely-invocable methods are defined and OperationType

- no.hvl.dat110.util: contains the Util class with various methods for obtaining registry or performing conversion.

##### Task-1: Run the project
Run the project using the LamportClockTest junit test and confirm that the the test fails because the final balance is different from each replica. You will find that a default value is used for balance in the 'Process' class (private double balance = 1000;	// default balance). You can also run the project by first running 'ProcessContainer' and then run SimulateReplicas.

##### Task-2: Implement sortQueue and sendAcknowledgement
Recall that sorting the queue based on the logical timestamp and the processid (to break ties) is important to achieving a totally-ordered events (queue). Your task is to sort the queue first on the clock and then on the processid.

Next, you should implement the sendAcknowledgement method. When a process receives a message from another process, it multicasts acknowledgement to other processes. After these, run the program and check that the test passed.

##### Task-3: Implement Withdrawal operation
Implement withdrawal operation in the withdrawal method. You can check other operations (requestDeposit) to get an idea how to fix it. Lastly, you should fix the 'TODO' for the withdrawal operation in the applyOperation method. After these, run the program and check that the test passed.
