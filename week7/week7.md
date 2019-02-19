### Lab Week 7: 18/02 - 22/02

This exercise focuses on the Chord implementation of Distributed Hash Table (DHT). The aim is to use this exercise to further understand the concepts of name space, addressing, identifier, entities and scalable lookups in peer-to-peer distributed systems

#### Exercise 1 - Chord-Distributed Hash Table

To get started you need to download and import the ChordDHT project into your eclipse IDE. You can find the project here:https://github.com/selabhvl/dat110public/tree/master/week7/ChordDHT
The project is divided into six packages:

##### no.hvl.dat110.rpc.interfaces
- The interface class (ChordNodeInterface) where remote functions for each node are defined.

##### no.hvl.dat110.node
contains the 'Node' and NodeInformation classes.
- Node: implements the ChordNodeInterface with its attributes and methods. For example, a node has an IP address, an identifier (hash of the IP address), finger table list, key id list, and so on. In addition, a node can be called to lookup any key id. Therefore, the method findSuccessor(keyid) and findHighestPredecessor(keyid) are directly implemented in node. In addition, the predecessor P of the successor S of node N can be notified that it has a new predecessor N, if P is between N and S after remote calls. This is implemented as P.notifySuccessor(N) and it's usually called during stabilize ring operation.

- NodeInformation: used to print out information about the status of the node (IP, ID, finger table entries, current keys)

##### no.hvl.dat110.node.client
- NodeClientTester: class that can be run to lookup a keyid of a resource and obtain the node that is responsible for it. The file id needs to be specified in the class.

##### no.hvl.dat110.chordoperations
This package contains five classes responsible specific chord protocols: 
- StabilizeRing: Checks whether a node P's successor is still valid. If P's successor has predecessor Q which is different from P, then P needs to accept Q as its new successor and Q needs to accept P as its new predecessor (via notifySuccessor)
- CheckPredecessor: runs periodically and makes a remote call to a node's predecessor and checks whether it's still valid. If call fails, predecessor is removed
- FixFingerTable: runs periodically to update the finger table for each node.
- UpdateSuccessor: runs periodically to set the first pointer of the finger table to the correct successor
- JoinRing: calls once when the node is being created to determine whether to join an existing ring or to start a new ring. It uses initial addresses from the StaticTracker class to determine who and where to join a ring.

##### no.hvl.dat110.file
- FileMapping: used to simulate cooperative mirroring by using random files and distributing those files among existing chord nodes.

##### no.hvl.dat110.rpc
- ChordNodeContainer: This is the 'server' for the node where the registry is started and where the binding of the remote stub object for the Node is done. In addition, all periodic chord operations are started in this class currently.

- StaticTracker: class that defines the ip addresses of possible active nodes in a ring. In addition, the port for the registry is specified in this class.

##### no.hvl.dat110.util
- Hash: implements hash function method and converts the hash value to big integer. Also, it implements a custom modulo 2^mbit function for testing purposes (where mbit = 4).
- Util: contains various utility methods for obtaining registry or performing conversion.

##### Task 1: Run the Chord program alone
1. Specify your IP address in the StaticTracker class
2. Run the ChordNodeContainer class
3. Specify a random file name in the NodeClientTester and run it
4. Check the result and see that the keyid of the file is mapped to your node

If you want the program to run forever, comment out the leave ring section at the bottom of ChordNodeContainer. Or increase the sleep time to make it run longer.


##### Task 2: Implement static files in the FileMapping.staticFiles method 
1. Implement static files in the staticFiles() method where you should define fixed filenames and obtain identifiers for each.
2. Replace the call to randomFiles() in the constructor with the staticFiles().
4. Specify one of the files in the NodeClientTester. Or modify to handle multiple files.
3. Start the ChordNodeContainer and later start the NodeClientTester. Check the result to see whether the right node is printed.

##### Task-3: Group testing to simulate a peer-to-peer DHT operation.
Here you need to form groups of at least 3 members each to be able to simulate a distributed P2P system. 
1. One of the group members will start its own node client (ChordNodeContainer)
2. The rest should include the IP address of the first group member or another member whose instance is already running in their StaticTracker class. You can include many IP addresses of active nodes in the StaticTracker class.
3. Each member of the group should run the NodeClientTester and check the result. 

What important improvements can you make to the system?


#### Exercise 2 - Leave Ring Implementation
A node can leave the ring by shutting down its client. You will implement a graceful departure behaviour from the ring. This behaviour should be implemented in the Node.leaveRing() method and should do the following.
1. When a node leaves, it should notify its successor and hands over all its keys to the successor. At this point, the successor must set its predecessor to the predecessor of the leaving node.
2. It should also inform its predecessor so that it can update its references. At this point, the predecessor must set its successor pointer to the successor of the node.
3. Simulate the behaviour and check your result.

