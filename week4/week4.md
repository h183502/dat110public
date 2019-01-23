### Week 4 Project : 28.01 - 01.02

** WARNING: assignment under construction - nothing final yet

#### Organisation

Week 4 is devoted to project work which is to be undertaken in groups of 2-3 students.

There will be no lecture and no lab Wednesday/Thursday due to travelling. The lectures and teaching assistants will be available on Monday 28.1 and on Friday 1.2 in the normal lecture and lab hours.

You are therefore also strongly encouraged to use the discussion forum at Canvas through out the week

#### Overview

The project builds on socket programming and network applications and aims to consolidate important concepts that have been covered until now in the course: layering, services, protocol design, headers, encapsulation/decapsulation, remote procedure calls (RPC), and marshalling/unmarshalling of parameters.

The project is comprised of three main parts

- implementation of a messaging layer on top of TCP socket for exchanging short messages between a client and a server
- implementation of a light-weight RPC layer/middleware on top of the messaging layers
- application of the RPC layer for realising an IoT network application comprised of a sensor, and display, and a controller

#### Getting Started

You should start by cloning the Java code which can be found in the github repository

TODO: provide link


In addition to providing start-code in the form of an Eclipse project, it also contains a number of unit tests which can be used for some basic testing of the implemented functionality.

It should not be necessary to add additional classes in order to complete the project and the unit-tests should not be modified as they will be used for evaluation of the submitted solution.

In order for the group to use their own repository for the further work on the codebase, one member of the group members must create an empty repository on github/bitbucket without a README file and without a `.gitignore` file and the perform the following operations

`git remote remove origin`

`git remote add origin <url-to-new-empty-repository>`

`git push -u origin master`

The other group members can now clone this new repository and work with the shared repository as usual.

#### Taks 1: Messaging layer

The messaging layer is to be implemented on top of TCP sockets and provide a service for connection-oriented, reliable, and bidirectional exchange of (short) messages carrying up to 127 bytes of data.

The messaging layer is to be based on a client-server architecture supporting a client in establishing a connection to a server on top of which the messages can be exchanged.

The messaging protocol is based on sending segments of 128 bytes on the underlying TCP connection such that the first byte of the segment is to be interpreted as an integer in the range 0..127 specifying how many of the subsequent 127 bytes is payload data. Any remaining bytes is simply considered to be padding and can be ignored.

The implementation of the messaging service is to be located in the `no.hvl.dat110.messaging` package. You are required to implement the methods marked with `TODO` in the following classes

- `Message.java` implementing methods for encapsulation and decapsulation of payload data according to the segment format described above.

- `Connection.java` implemented the connection abstraction linking the connection to the underlying TCP socket and associated input and output data strems that is to be used for sending and receiving message.

- `MessagingClient.java` implementing the methods for the client-side of the messaging service and responsible for creating the underlying TCP sockets.

- `MessagingServer.java` implementing the methods for the server-side of the messaging service. In the current project a server is only required to handle a single connection to a client.

Unit-test for the messaging layer can be found in the `no.hvl.dat110.messaging` package.

**Optional challenge:** If you have time you may consider implementing a messaging protocol that supports the exchange of arbitrarily long messages and without use of padding.

#### Task 2: RPC layer

- protocol to be designed on to of the messaging layers
- optional - keep alive feature

#### Task 3: IoT network application

- controller, temperature device, heating element ?, and display - with the controller acting as a coordinating client

#### Hand-in

Each group must hand in a link to a github repository containing their implementation.

The running application should be demonstrated - in the lab? - and the code checked in to the github repository.
