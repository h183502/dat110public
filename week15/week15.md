## Lab Week 15: 23/4 - 26/4

The lab this week is constitute the second part of the mandatory project 4 in the course. Project 4 consists of the following parts

- Part A: Hardware/software co-design of an access control device (lab week 14: https://github.com/selabhvl/dat110public/blob/master/week14/week14.md)
- Part B: Connecting IoT devices to the cloud (this lab / week)
- Part C: Writing a 5-page report on Parts A and B (course week 16)

### Project 4 - Part A

In this part you will be connecting your access control device from Part A to the cloud.

In part A you implemented the control part of the access control device using the TinkerCAD: https://www.tinkercad.com/ simulator. As TinkerCAD does not provide support for connecting the circuit design to the Internet, and as we do not have sufficient physical Arduino devices with network cards available for use, you will be provided with a virtual access control device implemented using Java.

You are then required to implement the network part of the virtual access control device which will use the HTTP protocol to connect to a REST-based cloud service implementing using the [Spark/Java micro-service framework](http://sparkjava.com). At the end of the you should have a completed IoT-cloud system solution in which the IoT access control device is connected to a cloud service that makes it possible to track when the system has been locked/unlocked and also change the current access code.

The principles that you will apply to develop your IoT-cloud solution is similar to what was demonstrated in the lectures on IoT by means of the red-green counters examples.

### Security and Storage

In a completed implementation of the system, we would also ensure that the service is secure by means of

- *confidentiality* using transport layer security to encrypt the communication between the IoT access control device and the cloud service

- *authentication* such that only authenticated software could access the cloud service

- *authorisation* such that only an owner could retrieve the access log and such that only an administrator would be able to change the access code.

As we have not yet covered security in the course, we will omit this for now. Once you have learned about security, you are encouraged to revisit your implementation and implement security mechanism as outlined above.

In a complete implementation, the cloud service would also use a database for storing the information such that it becomes persistent across starts and stops of the service. It would also implement fault-tolerance such that the access log was updated even if the acccess control device may have been without network connectivity for a while. For this project, we will simply stored the information in memory so be aware that information will be lost between restarts.

### Step 1: Virtual Access Control Device

The implementation of the sensor-actuator control for the access control device can be found in the following Eclipse-project:

**TODO** ADD LINK

The project implements a virtual access control device with similar as in Part A. The project is organised into the following packages:

- `no.hvl.dat110.aciotdevice.main` contains the main-method for the virtual device.

- `no.hvl.dat110.aciotdevice.ui` implements the sensors and actuators as JavaFX-elements.

- `no.hvl.dat110.aciotdevice.pins` implements the input/output pins of the virtual device.

- `no.hvl.dat110.aciotdevice.controller` implements the control-loop of the virtual device.

- `no.hvl.dat110.aciotdevice.client` will contain the implementation of the network part of the virtual device acting as a client (consumer) of the REST-based cloud service. This part is to be implemented in Step 4 below.

Running the main-method in the `Main.java` should result in the following window where you can interact with the PIR-sensor and also push the button labelled `1` and `2`. The green, yellow, and red LEDs are represented by the accordingly coloured boxes.

The only difference compared to Part A is that the device has an extra button labelled `N` and a blue LED. The extra button can be pressed in order to put the device into network mode in which case the blue LED will be turned on.

![](img/ui.png)

The implementation of the logic for controlling the sensors and actuators can be found in the `AccessController.java` class. It follows the same reactive programming model as on the Arduino-device. This means that the `setup`-method is executed once at startup, and the `loop`-method is continiously executed.

The `AccessController.java` class implements one possible solution for Part A. If you want, you can replace the implementation with your own implementation from Part A. Few changes to the C/C++ code should be required in order to make it run under Java.

### Step 2: Cloud service and REST API.

In this step you will be implementing the cloud service that the access control device can use by providing a REST API implemented using the [Spark/Java micro-service framework](http://sparkjava.com).

The Eclipse-project available via:

**TODO** ADD LINK

provides the basic setup required to implement the service. It is organised as a [Maven-project](https://maven.apache.org) in order to automatically download the externally libraries required. The externa dependencies are declared in the `pom.xml` file. The Java source code is available in the `src/main/java` folder.

Start by testing that you are able to run the server for the REST service locally on your machine:

- The `main-method` of the service is in the `App.java` class

- Run the main-method which will start the server and the basic REST service.

- Point your browser to the following URL

 ```
http://localhost:8080/hello
```

and you should see `"IoT Access Control Device"` in the browser window as a result of having executed the HTTP GET request in the browser.

As a next step you are to implement the operations to be supported by the cloud-service. The cloud-service should make it possible to for the access control device to register attempt to access the system by registering this in an access log. Furthermore, the cloud-service should make it possible to change the access code entry and the access control device should be able to retreive the current access code.    

Specifically, the following HTTP operations should be supported:

- `POST /accessdevice/log/` should record an access attempt by registering the log-message in JSON format which is to be contained in the body of the HTTP request.  The access attempts should be collected in memory and each log message should be given a unique identifier. You may use a [Concurrent HashMap](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/ConcurrentHashMap.html)  for storing the log-messages received from the device and an [Atomic Integer](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/atomic/AtomicInteger.html) for keeping track of the identifier. The hash-map should use the identifier as the key for the log-message.

- `GET /accessdevice/log/` should return a JSON-representation of all access log entries in the system.

- `GET /accessdevice/log/{id}` should return a JSON representation of the access entry identified by {id}. TODO: check spark documentation

- `PUT /accessdevice/code` should update the access code stored in the server to a new combination of the `1` and `2` buttons. The new access code is to be contained in JSON in the body of the request.

- `GET /accessdevice/code` should return a JSON-representation of the current access code stored in the server. This is what the access device will used in order to update the access code.

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
