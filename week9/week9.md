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

#### Exercise 6 - Lamport clock

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
Implement withdrawal operation in the withdrawal method. You can check other opperations (requestDeposit) to get an idea how to fix it. Lastly, you should fix the 'TODO' for the withdrawal operation in the applyOperation method. After these, run the program and check that the test passed.
 
 
