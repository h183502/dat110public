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

#### Exercise 3 - CloudMQTT (Message-Oriented Middleware)

These exercises are based on Message-Oriented Middleware

In this exercise, you will implement the IoT system using a message-oriented middleware based in the cloud and optionally, a broker installed locally on your machine.

To get started with the exercise, you should perform tasks 1 and 2.

##### Task 1 - Setup CloudMQTT
We will use CloudMQTT as the broker for subscribing to and publishing messages to topics. You have to register for a free account (Cute Cat) on CloudMQTT (Broker/Server) by going to https://www.cloudmqtt.com

##### Task 2 - Test Connection to CloudMQTT

To test whether you can connect to CloudMQTT, you need to provide the following information in the Config class (located in no.hvl.dat110.mqtt.brokerclient.test) which are then used by the publisher and subscriber classes. These can be obtained from your CloudMQTT account:

broker: tcp://your-cloudmqtt-instance:port
username:
password:
Test that you can connect to CloudMQTT and publish/subscribe to the ‘Temp’ topic by running the main method in the MQTTSubTest and MQTTPubTest classes.

##### Exercise 3.1 - IoT System with Message Broker
You will be implementing the virtual IoT devices as clients using the Eclipse Paho MQTT https://www.eclipse.org/paho/ for publishing and subscribing. That is: the TemperatureDevice publishes the temperature reading to the CloudMQTT using the topic "Temp" while the DisplayDevice subscribes to the topic "Temp" on CloudMQTT from where it receives the temperature reading.

To get you started, you are provided with an initial implementation of the system which is available from here:

https://github.com/selabhvl/dat110public/tree/master/week6/CloudMQTT-iotthreads-exercise

as an Eclipse project.
The Paho client jar is located under the lib folder. Make sure you import the jar as an external jar into your eclipse project (Configure build path).

The project contains implementations of a virtual temperature sensor and a display that you have used in previous exercises. Your task is to implement the missing parts in the TemperatureDevice and DisplayDevice classes. 

Run the IoTSystem class located in the package "no.hvl.dat110.simulation" to test that your implementation is working.

A short tutorial on Paho-MQTT client that explains how MQTT works and the meanings of the configuration parameters can be found here: https://github.com/selabhvl/dat110public/blob/master/week6/mqtt-paho-client-tutorial.pdf

##### Exercise 3.2 (Optional) - Use Mosquitto broker instead of CloudMQTT

Instead of using CloudMQTT broker, you should now download and configure Mosquitto message broker on your machine.
Download from: http://mosquitto.org/download/
Follow the instructions for installation and starting the broker. 

To switch the broker to Mosquitto in your source code using default configurations:
- Start the Mosquitto broker on your machine
- Go to the Config class and change the 'broker = "tcp://<hostname:port>";' to something like: broker = "tcp://localhost:1883";
- Clear the username and password fields.
- Run the IoTSystem to see that everything is properly configured.
