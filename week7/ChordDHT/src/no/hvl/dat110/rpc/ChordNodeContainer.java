package no.hvl.dat110.rpc;

import java.net.UnknownHostException;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import no.hvl.dat110.chordoperations.CheckPredecessor;
import no.hvl.dat110.chordoperations.JoinRing;
import no.hvl.dat110.chordoperations.StabilizeRing;
import no.hvl.dat110.chordoperations.UpdateSuccessor;
import no.hvl.dat110.file.FileMapping;
import no.hvl.dat110.chordoperations.FixFingerTable;
import no.hvl.dat110.node.Node;
import no.hvl.dat110.node.NodeInformation;
import no.hvl.dat110.rpc.interfaces.ChordNodeInterface;

/**
 * @author tdoy
 * exercise/demo - dat110
 */

public class ChordNodeContainer {
	
	// the server for the ChordNode instance
	
	// joinRing()
	// createRing() if joinRing() fails
	// bind to the registry
	// keep running

	
	public static void main(String[] args) throws RemoteException, UnknownHostException {
		
		// create a registry to hold the chordnode stub and start it on the port located in the StaticTracker class
		Registry registry = null;
		try {
			registry = LocateRegistry.getRegistry(StaticTracker.PORT);
			System.out.println("Registry "+registry.list());			// the method registry.list() must be invoked for the exception to work
			
		} catch(RemoteException e) {
			try {
				registry = LocateRegistry.createRegistry(StaticTracker.PORT);
				System.out.println("Registry created...");				
			} catch (RemoteException e1) {
				//
			}
			//e.printStackTrace();
		}
		
		ChordNodeInterface chordnode = new Node();		// create a new Chord Node
		
		// attempt to join the ring
		JoinRing join = new JoinRing(chordnode);
		join.join();
		
		try {
			registry.bind(chordnode.getNodeID().toString(), chordnode);
			System.out.println("Registry "+registry.list());
		} catch (RemoteException | AlreadyBoundException e1) {
			e1.printStackTrace();
		}
		
			
		// print out info about the node periodically
		NodeInformation nodeinfo = new NodeInformation(chordnode);
		nodeinfo.start();
		
		
		// distribute own files across the node - 
		int numfiles = 4;							// let's assume this node has 4 files to distribute to others
		FileMapping filemapping = new FileMapping(chordnode, numfiles);
		filemapping.start();
		
		/**
		 *  Chord's stabilization protocols
		 */
		
		// Schedule updateSuccessor => run this thread periodically to set the first successor pointer to the correct node
		UpdateSuccessor updatesucc = new UpdateSuccessor(chordnode);
		updatesucc.start();
		
		// Scheduler to stabilize ring => we run this thread periodically to stabilize the ring
		StabilizeRing stabilize = new StabilizeRing((Node) chordnode);
		stabilize.start();
		
		// Scheduler to update fingers => schedule updateFinger to run periodically
		FixFingerTable updatefingers = new FixFingerTable(chordnode);
		updatefingers.start();
		
		// Scheduler to check predecessor => schedule check predecessor to run periodically
		CheckPredecessor checkpred = new CheckPredecessor((Node) chordnode);
		checkpred.start();
		
		// leave the ring after 5secs
		try {
			Thread.sleep(5000);
			chordnode.leaveRing();
		}catch(Exception e) {
			
		}
		
		
	}

}
