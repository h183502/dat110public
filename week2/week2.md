## DAT110: Distributed Systems and Network Technology

### Lab Week 2: 14/01 - 18/01

#### Exercise 1 - Threads

Distributed and networked applications involve implementing exchange of message between processes that runs on different computers. In many cases, however, implementation of the application of a distributed system also involves writing multi-threaded applications where threads synchronise and communicate using shared memory. This is for instance the case when implementing multi-threaded servers and when implementing sending and reception of messages in communication protocols.

The main purpose of this exercise is to briefly recap inter-thread communication and synchronisation in Java before starting on network programming next week. The exercise uses the API for concurrent programming in Java https://docs.oracle.com/javase/tutorial/essential/concurrency/

We consider a small (emulated) IoT system consisting of a temperature sensor device and a display device. The sensor device and the display device runs as individual threads where the sensor-thread with periodic intervals reads the current temperature and the display-threads periodically display the current temperature.

1. Pull the code for the system from the github repository
2. Study the code for the SensorDevice, DisplayDevice, IoTSystem, TemperatureSensor, and TemperatureMeasurement. How does the sensor-thread and the display-thread exchange temperature?
3. Augment the IoT system from such that multiple display threads are created each display the current temperature.
4. Modify the IoT system from such that multiple temperature devices can update the temperature measurement, but make sure that only one of them is doing it at a time (hint: what does the modified synchronised do in java?)
5. Modify the IoT system from 2. such that instead of using a sleep() in the display-thread, then notify and wait are used such that the sensor-threads wakeups the display-thread when there is a new temperature measurement available.
6. Modify the IoT system from 3. such that it uses wait and notifyAll to wakeup all displays.

#### Exercise 2 - Wireshark

Do the first Wireshark lab described at http://www-net.cs.umass.edu/wireshark-labs/Wireshark_Intro_v7.0.pdf

#### Exercise 3 - Communication Protocols

Problem P1 in Kurose and Ross.

#### Exercise 4 - Communication Metrics

Problems P6 and P10 in Kurose and Ross.
