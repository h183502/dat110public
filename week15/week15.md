## Lab Week 15: 23/4 - 16/4

The assignment this week constitute the second part of the mandatory project 4 in the course. Project 4 consists of the following parts

- Part A: Hardware/software co-design of an access control device (lab week 14: https://github.com/selabhvl/dat110public/blob/master/week14/week14.md)
- Part B: Connecting IoT devices to the cloud (this lab)
- Part C: Writing a 5-page report on Parts A and B (course week 16)

### Project 4 - Part A

In this part you will be connecting your access control device from Part A to the cloud. In part A you implemented the control part of the access control device using the TinkerCAD: https://www.tinkercad.com/ simulator.

As TinkerCAD does not provide support for connecting the circuit design to the Internet and as  we do not have sufficient physical Arduino-devices with network cards available for use, you will be provided with a virtual access control device implemented using Java.

You are then required to implement the network part of the virtual access control device which will use the HTTP protocol to connect to a REST-based cloud service implementing using the [Spark/Java micro-service framework](http://sparkjava.com). At the end of the you should have a completed IoT-cloud system solution in which the access control IoT device is connected to a cloud service that makes it possible to track when the system has been locked/unlocked and also change the access code.

The principles that you will apply to develop your IoT-cloud solution similar to what was demonstrated in the lectures in IoT using the red-green counters examples.

### Security and Storage

In a complete implementation of the system, we would also make sure that the cloud service is secure by:

- confidentiality using transport layer security to encrypt the communication between the IoT device and the cloud service

- authentication by making sure that only authenticated software could access the cloud service

- authorisation by making sure that only an owner could retrieve the access log and that only an administrator would be able to make modification to the access codes.

As we have not yet covered security in the course, we will omit this for now. Once you have learned about security you are encouraged to revisit your implementation and implement security.

In a complete implementation, the cloud service would also use a database for storing the information such that it is made persistent across starts and stops of the service. For this project, we will simply stored the information in memory so be aware that information will be lost between restarts.

### Step 1: Virtual Access Control Device

The implementation of the sensor-actuator control for the access device can be found in the following Eclipse-project:

TODO: LINK

The project implements a virtual access control device with the same functionality as in Part A of project 4. The project is organised into the following packages:

- `no.hvl.dat110.aciotdevice.main` contains the main-method for the virtual device.

- `no.hvl.dat110.aciotdevice.ui` implements the sensors and actuators as JavaFX-elements.

- `no.hvl.dat110.aciotdevice.pins` implements the input/output pins of the virtual device.

- `no.hvl.dat110.aciotdevice.controller` implements the control-loop of the virtual device.

- `no.hvl.dat110.aciotdevice.client` is to contain the implementation of the network part of the virtual device acting as a client (consumer) of the REST-based cloud service.

Running the main-method in the `Main.java` should result in the following window where you can interact with the PIR-sensor and also push the button labelled `1` and `2`. The green, yellow, and red LEDs are represented by the accordingly coloured boxes.

The only difference compared to Project 4 - part A is that the device has an extra button and a blue led. The extra button and led is to be used when connecting the device to the cloud service (TODO).

The implementation of the logic for controlling the sensors and actuators can be found in the `AccessController.java` class and follows the same reactive programming model as on teh Arduino-device. This means that the `setup`-method is executed once at startup and the `loop`-method is continiously executed.

The `AccessController` class implements one possible solution for project 4 - Part A. Of you want you can replace the implementation with your own implementation. Few changes to the C/C++ code should be required in order to make it run under Java.

### Step 2: Cloud service and REST API.

In this step you will be implementing the cloud service that the access control device can by providing a REST API implemented using the [Spark/Java micro-service framework](http://sparkjava.com).

The Eclipse-project available via:

TODO

provides the basic setup required to implement the service. It is organised as a Maven-project in order to automatically download externally libraries required. The Java source code is available in the `src/main/java` folder.

The `main-method` of the service is in the `App.java` class.

- Running the main-method will start the REST service.

- Point your browser to the following URL

 ```
http://localhost:8080/hello
```

and you should see `"IoT Access Control Device"` in the browser window as a result of having executed the HTTP GET request on the service.

As a next step you are to implement the operations to be supported by the cloud-service. The cloud-service should make it possible to for the access control device to register an access attempt and to obtain a new access (pin) code for entry. Furthermore, the cloud-service should make it possible to change the pin-code for entry.  

Specifically, the following HTTP operations should be supported:

- `POST /accessdevice/log/` should record an attempt to entry by registering the time and also with entry was successfull (device became unlocked) or failed. The access attempts should be collected in memory in an `ArrayList` and each access attempt should have a unique identifier. The time and status should be contained as JSON in the body of the request.

- `GET /accessdevice/log/` should return a JSON-representation of all access attempts to the system.

- `GET /accessdevice/log/{id}` should return a JSON representation of the access entry identified by {id}. TODO: check spark documentation

- `PUT /accessdevice/pin` should update the access code stored in the server to a new combination of the `1` and `2` buttons. The new access code is to be contained in JSON in the body of the request.

- `GET /accessdevice/pin` should return a JSON-representation of the current access code stored in the server. This is what the access device will used in order to update the access code.

In order to implement the above API you will have to use the primitives available in the Spark/Java framework. The documentation is available here:

http://sparkjava.com/documentation

It should be sufficient to read the parts on *Routes*, *Requests*, and *Responses*.

For the JSON-representation we will use the GSON-library:

https://github.com/google/gson

The Maven-setup of the cloud-service makes sure to download this library.

The examples on Spark/Java and also GSON from the lecture on IoT is available from here:

 https://github.com/selabhvl/dat110public/blob/master/week14/rest/counters/counters/src/main/java/no/hvl/dat110/rest/counters/App.java

 https://github.com/selabhvl/dat110public/blob/master/week14/rest/counters/counters/src/main/java/no/hvl/dat110/rest/counters/Counters.java

### Step 3: Testing the Cloud service

The operations of the cloud service relying on HTTP GET can easily be tested from a browser, but to conduct a more complete test of the services implemented in the above step you should download and install the [Postman-tool](https://www.getpostman.com/tools). The tool makes it possible to construct HTTP Requests and run these against a service as was also demonstrated at the lectures on IoT.

You should create request in the Postman-tool and test all the operations that were implemented in the previous step.  

### Step 4: Implementing Device Network Communication

The next step is now to implement the network communication in the access control device which is to use some of the services implemented in step 2. The implementation of the network communication is to be provided in the `RestClient.java` by completing the implementation of the following methods:

- AccessCode GetAccessCode()

- void PostAccessLogEntry ()

Both of the methods should establish a connection to the cloud-service and issue the appropriate HTTP GET and HTTP POST requests, i.e.., following the format for HTTP request messages which can be found on pages X and Y in the networking book.

The code from the IoT lectures illustrating one way of constructing HTTP request can be found via:

TODO

Test that the implementation works properly by attempting to access the device and see that the log in the cloud service is properly updated. Try to also change the access code by sending a request from Postman and see that the access control device correctly obtains the new access code.

### Step 5: Cloud Deployment - Optional

As a final step you may deploy you cloud service. There are several options two of which are listed below.

- Deploy the service on [Amazon EC2](https://aws.amazon.com/ec2/) by creating a virtual machine in the free-tier. One way to deploy the Spark/Java service is to

 - Install Java, Maven, and git on the virtual machine
 - Check out the code using git
 - Build the REST-service using maven: ```mvn package```
 - Start the service   
 - In order to be able to connect to the service you have to make sure that the TCP port used for the service is open in the security setting of the virtual machine and tha the port is not blocked by the firewall of the virtual machine

- Deploy the service on [Heroku](https://www.heroku.com) following the instruction that can be found here: https://sparktutorials.github.io/2015/08/24/spark-heroku.html
