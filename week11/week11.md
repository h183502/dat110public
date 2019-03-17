### Lab Week 11: 18/03 - 22/03

#### Exercise 1 - Two-phase commit protocol

Consider the CPN model of the [two phase commit protocol](https://github.com/selabhvl/dat110public/blob/master/week11/stopwaitprotocolmodel.cpn) described in the paper: https://hvl.instructure.com/courses/6156/files/folder/material?preview=380233 

Use CPN Tools and simulation to perform some executions of the CPN model. Make sure that you cover both the case where workers votes `Yes`, and where some worker votes `No`.

Modify the CPN model such that the coordinator will instruct the workers to commit the transaction as long as a majority of the workers voted `Yes`.

#### Exercise 2 - CPN model of a Go-back-N transport protocol

Consider the CPN model of the [stop-and-wait protocol](https://github.com/selabhvl/dat110public/blob/master/week11/twophasecommitmodel.cpn) that was presented in the lectures.

Augment the CPN model to become a CPN model of a Go-back N protocol by doing task 1 and 2 described at: https://github.com/lmkr/cpnbook/blob/master/projects/project1.md

You can find a description of the Go-back N protocol in section 3.4.3 of the networking book.

#### Exercise 3 - Java implementation of Go-back-N transport protocol

This exercise is concerned with implementation of transport layer protocols based on the RDT Java framework that has been introduced and discussed in the lectures on the transport layer.

To undertake the following the exercises it is assumed that you have read pages 234-245 in the networking book. You may also want to review the lecture notes from the transport layer I and transport layer II lectures, in particular the parts that cover the RDT implementation framework, and also the [exercises 1-3 from week 8](https://github.com/selabhvl/dat110public/blob/master/week9/week9.md).

Make sure that you have pulled the most recent version of the framework available as an Eclipse-project at:

https://github.com/selabhvl/dat110public/tree/master/week6/rdt

If you have not already done so earlier, then start by importing the project into your IDE (Eclipse) and make sure that the classes and the tests compiles.

The project is organised into the following packages

- `no.hvl.dat110.application` implements a sender process that sends messages to a receiver process using the underlying transport protocol implementation.

- `no.hvl.dat110.network` implements a simulated network that can connect a sender and a receiver by means of two channels (one in each direction). By changing the type of channel and the associated adversary, we can simulate reliable and unreliable networks and experiment with different transport protocol implementations.

- `no.hvl.dat110.transport` contains the base classes for implementing transport protocols for reliable data transfer of segments and defines the basic primitives of `rdt_send`, `deliver_data`, `udt_send`, and `rdt_recv`.

- `no.hvl.dat110.transport.rdt*` implements the RDTX protocols for reliable data transfer over a perfectly reliable channel as described on pages 236-245 in the networking book.

- `no.hvl.dat110.trasport.tests` implements some JUnit-tests for testing the transport protocols. The basic correctness criteria is that the receiver must receive all data from the sender and in correct order.

Make an implementation of the Go-Back-N protocol specified and validated using CPNs in exercise 2 above. As part of this, you may want to augment `SenderProcess.java` such that additional data is being sent from the sender-side.

You should also add a test class in the `no.hvl.dat110.transport.tests` package to test your implementation. You can use the `TestRDT4Adversary4.java` as a starting point.
