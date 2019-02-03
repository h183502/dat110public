### Lab Week 5: 05/02 - 08/02

These exercises are based on Java Remote Method Invocation (RMI) implementation of RPC

#### Exercise 1 - Synchronous RPC 

In this lab exercise, you will implement the IoT system using Java RMI.
https://github.com/selabhvl/dat110public/tree/master/week5/JavaRMI-iotthreads-synchronous

The main task is to implement the RPC server that accepts (stores) temperature readings from the TemperatureDevice and that allows the DisplayDevice to read/get the temperature values from its storage.

- The TemperatureDevice will send its temperature readings to the RPC server storage.

- The Display device will read the temperature reading from the RPC server storage.

You can use the example code here as inspiration:
https://github.com/selabhvl/dat110public/tree/master/week5/JavaRMI-Synchronous


#### Exercise 2 - Asynchronous RPC with a callback

In this exercise, you will construct an asynchronous version of the client-server RPC IoT system in Exercise 1 by using a callback mechanism. The idea is that the RPC server should notify/forward the temperature value to the Display device as soon as it receives the reading from the Temperature device.

You will need to implement a callback function for the Display device which must be registered on the RPC server.

An idea of how to construct such asynchronous RPC client-server system can be found in the example code: https://github.com/selabhvl/dat110public/tree/master/week5/JavaRMI-Asynchronous-Client

#### Exercise 3 - Asynchronous RPC Server

Consider the example code: https://github.com/selabhvl/dat110public/tree/master/week5/JavaRMI-Asynchronous-Client

This example code demonstrates asynchronicity from the client side. That is, the client provides unblocking mechanism by spawning a new thread to wait for the result from the server while the client continues its work.

Modify the example code to construct an asynchronous RPC server such that the RPC server can accept multiple RPC clients connections without blocking.