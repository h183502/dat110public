### Week 4 Project : 28.01 - 01.02

** WARNING: assignment under construction - nothing final yet

##### Organisation

Week 4 is devoted to project work which is to be undertaken in groups of 2-3 students.

There will be no lecture and no lab Wednesday/Thursday due to travelling. The lectures and teaching assistants will be available on Monday 28.1 and on Friday 1.2 in the normal lecture and lab hours.

You are therefore also strongly encouraged to use the discussion forum at Canvas through out the week

##### Overview

The project builds on socket programming and network applications and aims to consolidate important concepts that have been covered until now in the course: layering, services, protocol design, headers, encapsulation/decapsulation, remote procedure calls (RPC), and marshalling/unmarshalling of parameters.

The project is comprised of three main parts

- implementation of a messaging layer on top of TCP socket for exchanging short messages between a client and a server
- implementation of a light-weight RPC layer/middleware on top of the messaging layers
- application of the RPC layer for realising an IoT network application comprised of a sensor, and display, and a controller

##### Getting Started

You should start by cloning the Java code which can be found in the github repository

TODO: provide link

TODO: describe how to import this into an own repository for the group.

In addition to providing start-code in the form of an Eclipse project, it also contains a number of unit tests which can be used for some basic testing of the implemented functionality.

##### Exercise 1: Messaging layer

- describe protocol
- describe what has to be implemented
- add some basic unit tests

##### Exercise 2: RPC layer

- protocol to be designed on to of the messaging layers
- optional - keep alive feature

##### Exercise 3: IoT network application

- controller, temperature device, heating element ?, and display - with the controller acting as a coordinating client

##### Hand-in

The running application should be demonstrated - in the lab? - and the code checked in to the github repository.
