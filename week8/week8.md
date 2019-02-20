## Week 8 Project : 25.02 - 01.03

**WARNING: DESCRIPTION UNDER CONSTRUCTION**

### Organisation

Week 8 is devoted to project work which is to be undertaken in **groups of 2-3 students**. Discussions between groups are allowed, but the code handed in by the group should be the work of the group members are not members of other groups.

There will be no lectures on Wesnesday and Friday, but there will be labs Thursday/Friday at the normal timeslots. You also are strongly encouraged to use the [discussion forum](https://hvl.instructure.com/courses/6156/discussion_topics/XXX) in Canvas throughout the project woek.

### Overview

The aim of the project is to implement a publish-subscribe messaging-oriented middleware (PB-MOM) on top of the messaging transport layer and TCP from project 1. You are not required to implement the messaging transport layer, but you are given an implementation as part of the start code. There should not be any need to directly use TCP/UDP transport services, only indirectly via the messaging transport layer.

You are assumed to have read Chapter 4 in the distributed systems book and be familiar with the concepts of publisher clients, subscriber clients, topics, and brokers.

The client-side of PB-MOM consists of a publishers/subscribers that can create/delete topics, subscribe/unsubscribe to topics, and publish on topics. When a publishers publish a message on a topic, then all clients subscribing to the topic is to receive the message. The server-side is comprised of a broker that manages the connected clients, topics, and subscriptions and which is responsible for publishing messages to the subscribers of a given topic.  

The project is comprised of X main tasks

1. Implement classes for the messages to be used in the publish-subscribe protocol.

2. Implement the storage of topics and subscriptions in the broker and the processing of publish-subscibe messages received from connected clients.

3. Application of the PB-MOM for implementing a small IoT system in which a sensor publishes the current temperature on a topic to which a display is describing (see Exercise Y from week Z)

4. Application of the PB-MOM for implementing the ChApp (Chat Application) where users can send short messages to each on topics

5. Extend the broker from being single-threaded to being multi-threaded having a thread for handling each connected client.

6. Extend the broker so that if a subscribing client is currently disconnect and later reconnects, then the client will be provided with the messages that may have been published on the topic while the client was disconnected.

It is only required to done one of the tasks 5 and 6 - not both.

### Getting Started

You should start by cloning the Java code which can be found in the github repository

https://github.com/selabhvl/dat110-project2-startcode.git

which contains an Eclipse-project with start-code. In addition, it also contains a number of unit tests which can be used for some basic testing of the implemented functionality. The unit-tests should not be modified/removed as they will be used for evaluation of the submitted solution.

When opening the project in Eclipse there will be some compile-errors, but these will go away as you complete the implementation of the tasks below.

In order for the group to use their own git-repository for the further work on the codebase, one member of the group must create an empty repository on github/bitbucket without a README file and without a `.gitignore` file, and then perform the following operations

`git remote remove origin`

`git remote add origin <url-to-new-empty-repository>`

`git push -u origin master`

The other group members can now clone this new repository and work with a shared repository as usual.

### Taks 1: Publish-subscribe Protocol Messages

The messages to be exchanged between the clients and broker is to be defined as classes in the `no.hvl.dat110.messages` package. The base message class is `Message` and all messages classes must be subclasses of this class. Any message will contain information about a `user` and have a `type` as defined in `MessageType.java`

The communication between the client and the broker is to be based on the transport messaging layer/service from project 1. An implementation of this layer is provided a part of the start-code in the `no.hvl.dat110.messagetransport`

The `no.hvl.dat110.messages` already contains classes implementing the following messages for the publish-subscribe protocol:

- `ConnectMsg.java` - sent by the client as the first message after having establish the underlying connection to the broker.

- `DisconnectMsg.java` - sent from the client in order to disconnect from the broker.

You are required to complete the implementation of the classes below.

- `CreateTopicMsg.java` - sent by the client in order to have the broker create a `topic`. A topic is to be identified by means of a `String`

- `DeleteTopicMsg.java` - sent by the client in order to have a `topic` deleted.

- `SubscribeMsg.java` - sent by the client in order to subscribe to a `topic`.

- `UnsubscribeMsg.java` - sent by the client in order to publish a `message` (`String`) on a topic and sent by the broker in order to deliver the message to subscribing clients.

The message-classes must have a constructor that can give a value to all object-variables, getter/setter methods for all object-variables, and they must implement a `toString`-method to be used for logging purposes.

### Task 2: Broker Implementation

The implementation of the broker can be found in the `no.hvl.dat110.broker` package. You will have to study the code of the broker which is comprised of the following subclasses

- `ClientSesssion.java` which is used to represent a session with a currently connected client on the broker side. Whenever a client (user) connects, a corresponding `ClientSession`-object will be created on the broker-side encapsulating the underlying message transport connection.

- `Storage.java` which is to implement the storage of currently connected clients and manage the subscription of clients (users) to topics. **You will complete the implementation of this class in Task 2.1 below.**

- `Broker.java` implementing a `Stopable`-thread as introduced in the lecture on transport protocols. The `doProcess`-methods of the broker runs in a loop accepting incoming message transport connections (sessions) from clients.

- `Dispatcher.java` implementing a `Stopable`-thread that is responsible for processing the messages received from clients. The `doProcess()`-methods of the dispatcher check the client session for incoming messages and then invokes the `dispatcher`-method which depending on the type of received message will invoke the corresponding handler method. **You will complete the implementation of the dispatcher in Task 2.2 below.**

- `BrokerServer.java` which contains the main-method of the broker. It is responsible for starting up the server and creating the storage and dispatcher of the broker.

#### Task 2.1 Broker Storage

The `Storage`-class of the broker implements an in-memory storage where the broker can store information about connected clients and the subscription of user (clients) to topics. The start of the class is already provided:

```Java
public class Storage {

	private ConcurrentHashMap<String, Set<String>> subscriptions;
	private ConcurrentHashMap<String, ClientSession> clients;

	public Storage() {
		subscriptions = new ConcurrentHashMap<String, Set<String>>();
		clients = new ConcurrentHashMap<String, ClientSession>();
	}
 [...]
```

The basic idea is to use a hash-map mapping from users (`String`) to a set of topics (`String`) for managing which user are subscribed to which topics. Similarly, the currently connected client are stored in a hash-map mapping from a user `(String)` to a `ClientSession`-object representing the connection with the client.

You are required to complete the implementation of the following methods in the classes

- `public void addClientSession(String user, Connection connection)`

- `public void removeClientSession(String user)`

- `public void createTopic(String topic)`

- `public void deleteTopic(String topic)`

- `public void addSubscriber(String user, String topic)`

- `public void removeSubscriber(String user, String topic)`

- `public Set<String> getSubscribers(String topic)`

The TODO-comments in `Storage.java` gives more detailed information about what the individual methods are supposed to do. The package `no.hvl.dat110.storage.tests` contains some unit tests that can be used to test the implementation of the storage methods.

#### Task 2.2 Broker Message Processing

All communication with a connected client will be done via the `send`, `receive`, and `hasData`-methods of the corresponding `ClientSession`-object. The message themselves

TODO

### Task 3: IoT application

In this task you will use the PB-MOM middleware to implement a small IoT system comprised of a (temperature) sensor, and a display. The start of the implementation of the IoT-system can be found in the `no.hvl.dat110.iotsystem` package

#### Sensor device implementation

The start of the sensor device implementation can be found in the `SensorDevice.java` class. You are required to
complete the implementation such that the sensor device connects to a broker, runs in a loop `COUNT`-times where it publishes to a *temperature* topic. After that the sensor device should disconnect from the broker.

#### Display device Implementation

The start of the display device implementation can be found in the `DisplayDevice.java` class. You are required to complete the implementation of the display device such that it connects to the same broker as the sensor device, creates a *temperature* topic, subscribes to this topic and then receives the same number of messages as the sensor device is sending on the topic. Upon completion, the display device should disconnect from the broker.

Try to start a broker and have the display device and then the sensor device connect. Check that the display device is correctly receiving the temperature-messages published by the sensor device.

### Task 4: ChApp - Chat application

TODO

### Task 5a - Message Buffering

When a client disconnect from the broker the corresponding `ClientSession-object` is removed from the storage which means that if the client is subscribing to a topic and messages are published in that topic while the client is disconnected, then the client will not receive the published messages. If the client later reconnects, it will only receive those message that were published after the reconnect.

The aim of this task is to extend the implementation of the broker such that the broker will buffer any messages for client until the point where the client connects again. At that point, the broker should then publish the buffered message to the client. Implementing this extension will involve  

- changing the implementation of how a disconnect from the client is handled by the dispatcher.
- augmenting the broker storage such that buffering of messages for the clients becomes possible.
- changing the implementation of how a connect from a client is handled by the dispatcher.    

### Task 5b - Multi-threaded Broker

The implementation of the dispatcher in the `Dispatcher.java` runs as a single `Stopable`-thread which succesively check the current client sessions for incoming messages using the `hasData`-method. This means that it is not possible to exploit multiple-cores when running the broker, and this may degrade the performance of the server as perceived by the clients.

The aim of this task is to change the implementation of the dispatcher such that each client session has an associated thread which processes the incoming message from the corresponding client. Solving this task means that a new thread has to be spawned whenever a client connects. This thread will then wait for incoming messages from the client and handles these accordingly. When the client disconnect the corresponding thread should be terminated.

It should also be possible to stop/termination the execution of all current threads. In the current implementation, the single threaded dispatcher can be stopped by invoking the `doStop`-method on the dispatcher.

### Handing in the project

Each group must hand in a link on Canvas to a git-repository containing their implementation. You should keep the unit-test in the project as they are as we will use these for testing your implementation.

Please remember to hand-in as a member of a group in Canvas: https://hvl365-my.sharepoint.com/:w:/g/personal/akv_hvl_no/EdkQXNKVjmhPrHNtD3n5r74B6KSb7DwmVYf9MA3SIUA4Sw?e=hC5Q9i
