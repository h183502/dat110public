## Week 8 Project : 25.02 - 01.03

### Organisation

Week 8 is devoted to project work which is to be undertaken in **groups of 2-3 students**. Discussions among the groups are allowed, but the code handed in by the group should be the work of the group members - and not members of other groups.

There will be no lectures on Wednesday 27/2 and Friday 1/3, but there will be labs Thursday/Friday at the normal time-slots. You also are strongly encouraged to use the [discussion forum](https://hvl.instructure.com/courses/6156/discussion_topics/34796) in Canvas throughout the project week.

### Overview

The aim of the project is to implement a publish-subscribe messaging-oriented middleware (PB-MOM) on top of the TCP-based message transport layer from project 1. You are **not** required to implement the messaging transport layer, but you are given an implementation as part of the start code. There should not be any need to directly use TCP/UDP transport services, only indirectly via the provided message transport service implementation.

You are assumed to have read Chapter 4 (Communication) in the distributed systems book and be familiar with the concepts of publisher clients, subscriber clients, topics, and brokers. You are also assumed to be familiar with the service provided by the message transport layer that we implemented as part of project 1.

The client-side of the PB-MOM consists of *publishers and subscribers* that can create/delete topics, subscribe/unsubscribe to *topics*, and *publish* messages to topics. When a publisher publishes a message on a given topic, then all connected clients subscribing to the topic is to receive the message.

The server-side is comprised of a *broker* that manages the connected clients, topics and subscriptions, and which acts as an intermediate responsible for publishing messages to the subscribers of a given topic.  

The project is comprised of the following main tasks:

1. Implement classes for the messages to be used in the publish-subscribe protocol between clients and the broker.

2. Implement the storage of topics and subscriptions in the broker, and the processing of publish-subscribe messages received from connected clients.

3. Application of the PB-MOM for implementing a small IoT system in which a sensor publishes the current temperature on a temperature topic to which a display is describing (see also lab-exercises from earlier weeks).

4. Experiment with PB-MOM for implementing the ChApp (Chat Application) where users can send short messages to each other via topics.

5. Extend the broker from being single-threaded to being multi-threaded having a thread for handling each connected client.

6. Extend the broker such that if a subscribing client is currently disconnected and later reconnects, then the client will be provided with the messages that may have been published on the topic while the client was disconnected.

**It is only required to do one of the tasks 5 and 6 - not both.**

### Getting Started

You should start by cloning the Java code which can be found in the github repository

https://github.com/selabhvl/dat110-project2-startcode.git

which contains an Eclipse-project with start-code. In addition, it also contains a number of unit tests which can be used for some basic testing of the implemented functionality. The unit-tests should not be modified/removed as they will be used for evaluation of the submitted solution.

When opening the project in Eclipse, there will be some compile-errors. These will go away as you complete the implementation of the tasks below.

In order for the group to use their own git-repository for the further work on the codebase, one member of the group must create an empty repository on github/bitbucket without a README file and without a `.gitignore` file, and then perform the following operations

`git remote remove origin`

`git remote add origin <url-to-new-empty-repository>`

`git push -u origin master`

The other group members can now clone this new repository and work with a shared repository as usual.

### Taks 1: Publish-subscribe Protocol Messages

The messages to be exchanged between the clients and the broker is to be defined as classes in the `no.hvl.dat110.messages` package. The base message class is `Message` and all messages classes must be subclasses of this class. All messages will contain information about a `user` and have a `type` as defined in `MessageType.java`. The `user` is assumed to uniquely identify a connected client.

The communication between the client and the broker is to be based on the message transport layer/service implemented as part of project 1. An implementation of this layer is provided a part of the start-code in the `no.hvl.dat110.messagetransport` package

The `no.hvl.dat110.messages` already contains classes implementing the following messages for the publish-subscribe protocol:

- `ConnectMsg.java` - sent by the client as the first message after having established the underlying message transport connection to the broker.

- `DisconnectMsg.java` - sent from the client in order to disconnect from the broker.

You are required to complete the implementation of the remaining message-classes.

- `CreateTopicMsg.java` - sent by the client in order to have the broker create a `topic`. A topic is to be identified by means of a `String`

- `DeleteTopicMsg.java` - sent by the client in order to have a `topic` deleted.

- `SubscribeMsg.java` - sent by the client in order to subscribe to a `topic`.

- `UnsubscribeMsg.java` - sent by the client in order to publish a `message` (`String`) on a topic and sent by the broker in order to deliver the message to subscribing clients.

The message-classes must have a constructor that can give a value to all object-variables, getter/setter methods for all object-variables, and they must implement a `toString`-method to be used for logging purposes.

### Task 2: Broker Implementation

The implementation of the broker can be found in the `no.hvl.dat110.broker` package. You will have to study the code of the broker which is comprised of the following subclasses

- `ClientSesssion.java` which is used to represent a *session* with a currently connected client on the broker side. Whenever a client (user) connects, a corresponding `ClientSession`-object will be created on the broker-side encapsulating the underlying message transport connection.

- `Storage.java` which is to implement the storage of currently connected clients and manage the subscription of clients (users) to topics. **You will complete the implementation of this class in Task 2.1 below.**

- `Broker.java` implementing a `Stopable`-thread as introduced in the lecture on transport protocols. The `doProcess`-methods of the broker runs in a loop accepting incoming message transport connections (sessions) from clients.

- `Dispatcher.java` implementing a `Stopable`-thread that is responsible for processing the messages received from clients. The `doProcess()`-methods of the dispatcher checks (polls) the client sessions for incoming messages and then invokes the `dispatcher`-method which, depending on the type of received message, will invoke the corresponding handler method. **You will complete the implementation of the dispatcher in Task 2.2 below.**

- `BrokerServer.java` which contains the `main`-method of the broker. It is responsible for starting up the server and creating the storage and dispatcher of the broker.

#### Task 2.1 Broker Storage

The `Storage`-class of the broker implements an in-memory storage where the broker can store information about connected clients and the subscription of user (clients) to topics. The start of the class is already provided:

```java
public class Storage {

	private ConcurrentHashMap<String, Set<String>> subscriptions;
	private ConcurrentHashMap<String, ClientSession> clients;

	public Storage() {
		subscriptions = new ConcurrentHashMap<String, Set<String>>();
		clients = new ConcurrentHashMap<String, ClientSession>();
	}
 [...]
```

The basic idea is to use a hash-map mapping from topics (`String`) to a set of users (`String`) for managing which users are subscribed to which topics. Similarly, the currently connected clients are stored in a hash-map mapping from a user (`String`) to a `ClientSession`-object representing the connection/session with the client.

You are required to complete the implementation of the following methods in the classes

- `public void addClientSession(String user, Connection connection)`

- `public void removeClientSession(String user)`

- `public void createTopic(String topic)`

- `public void deleteTopic(String topic)`

- `public void addSubscriber(String user, String topic)`

- `public void removeSubscriber(String user, String topic)`

- `public Set<String> getSubscribers(String topic)`

The TODO-comments in `Storage.java` class provides more detailed information about what the individual methods are supposed to do. The package `no.hvl.dat110.broker.storage.tests` contains some basic unit tests that can be used to test the implementation of the storage methods.

#### Task 2.2 Broker Message Processing

All communication between the broker and the connected clients will be done via the `send`, `receive`, and `hasData`-methods of the corresponding `ClientSession`-object. The encapsulation of the underlying message transport connection has been already implemented in the `ClienSession.java` class

The messages exchanged between the broker and the client will be a JSON-representation of the objects of the message-classes implemented in Task 1.  As an example, a `ConnectMsg`-object will be represented as follows:

```java
{"type":"CONNECT","user":"testuser"}
```

The conversion to/from the JSON format has already been implemented using the [gson-library](https://github.com/google/gson) library in the `MessageUtils.java` class.

The aim of this task it to implement the broker-side processing of the messages received from clients in the `Dispatcher.java` class. The `doProcess`-method of the dispatcher runs in a loop where it in turn checks the current client sessions for an incoming message using the `hasData`-method. If the client has sent a message, then it will invoke the `dispatch`-method which in turn will invoke a method named on the form `onX` for a processing a message of type `X`.

The dispatcher contains an implementation of the `onConnect` and on `onDisconnect`-methods. Your task is to complete the implementation of the remaining methods:

- `public void onCreateTopic(CreateTopicMsg msg)`

- `public void onDeleteTopic(DeleteTopicMsg msg)`

- `public void onSubscribe(SubscribeMsg msg)`

- `public void onUnsubscribe(UnsubscribeMsg msg)`

- `public void onPublish(PublishMsg msg)`

in order to be able to also process the remaining types of messages.

The tests found in the `no.hvl.dat110.broker.processing.tests` package can be used to test the implemented methods.

### Task 3: IoT application

In this task you will use the PB-MOM middleware to implement a small IoT system comprised of a (temperature) sensor, and a display. The start of the implementation of the IoT-system can be found in the `no.hvl.dat110.iotsystem` package.

#### Sensor device implementation

The skeleton of the sensor device implementation can be found in the `SensorDevice.java` class. You are required to
complete the implementation such that the sensor device connects to a broker, runs in a loop `COUNT`-times where it publishes to a *temperature* topic. After that the sensor device should disconnect from the broker.

#### Display device Implementation

The skeleton of the display device implementation can be found in the `DisplayDevice.java` class. You are required to complete the implementation of the display device such that it connects to the same broker as the sensor device, creates a *temperature* topic, subscribes to this topic and then receives the same number of messages as the sensor device is sending on the topic. Upon completion, the display device should disconnect from the broker.

Try to start a broker and have the display device and then the sensor device connect. Check that the display device is correctly receiving the temperature-messages published by the sensor device. The test in the package `no.hvl.dat110.iotsystem.tests` can be used to run the IoT system.

### Task 4: ChApp - Chat application

The purpose of this task is to connect multiple JavaFX-based GUI clients to a broker, and in this way implement a short messaging system. The figure below show a screenshot of the client. The application client makes it possible to connect to a broker, create/delete topics, subscribe/unsubscribe to topics, and to publish messages on topics.

![](assets/chapp.png)

Clone the implementation of the client which is available as an Eclipse-project from here:

https://github.com/selabhvl/dat110-project2-chapp.git

If using the Java 11 SDK, then you will have to download JavaFX for your platform and then configure the project as describe here:

https://openjfx.io/openjfx-docs/

For Java 8/9/10 JavaFX is included as part of JDK, but you may need to configure the build path of the project.

In order to compile the client you will in addition have to add the project containing your implementation of the PB-MOM middelware to the build path of the project for the GUI client.

Start a broker on one machine and let each group member run the ChApp-client on their machine. Try creating topics and then publish some messages. If you are not able to connect to the broker it may be due to firewall issues on the host running the broker. Make sure that the port on which the broker is running is not blocked by the firewall.

### Task 5: Message Buffering

When a client disconnects from the broker, the corresponding `ClientSession-object` is removed from the storage. This means that if the client is subscribing to a topic and messages are published on that topic while the client is disconnected, then the client will not receive the published messages. If the client later reconnects, it will only receive those message that were published after the reconnect.

The aim of this task is to extend the implementation of the broker such that the broker will buffer any messages for a subscribed client until the point where the client connects again. At that point, the broker should then publish the buffered message to the client. Implementing this extension will involve  

- changing the implementation of how a disconnect-message from the client is processed by the dispatcher.
- augmenting the broker storage such that buffering of messages for the clients becomes possible.
- changing the implementation of how a connect from a client is handled by the dispatcher.    

You may use the ChApp-application to test the buffering implementation or alternatively write a unit test similar to the ones found in the `no.hvl.dat110.broker.processing.tests` package to create a scenario where a client (subscriber) disconnects for a while.

### Task 6: Multi-threaded Broker

The implementation of the dispatcher in the `Dispatcher.java` class runs as a single `Stopable`-thread which in turn checks the current client sessions for incoming messages using the `hasData`-method. This means that it is not possible to exploit multiple-cores when running the broker, and this may degrade the performance of the broker as perceived by the clients.

The aim of this task is to change the implementation of the dispatcher such that each client session has an associated thread which processes the incoming message from the corresponding client.

Solving this task means that a new thread has to be spawned whenever a client connects. This thread will then wait for incoming messages from the client and handles these accordingly. When the client disconnect the corresponding thread should be terminated. It should also be possible to stop/terminate the execution of all current threads. In the current implementation, the single threaded dispatcher can be stopped by invoking the `doStop`-method on the dispatcher.

### Handing in the project

Each group must hand in a link on Canvas to a git-repository containing their implementation. You should keep the unit-test in the project as they are as we will use these for testing your implementation.

Please remember to hand-in as a member of a group in Canvas: https://hvl365-my.sharepoint.com/:w:/g/personal/akv_hvl_no/EdkQXNKVjmhPrHNtD3n5r74B6KSb7DwmVYf9MA3SIUA4Sw?e=hC5Q9i
