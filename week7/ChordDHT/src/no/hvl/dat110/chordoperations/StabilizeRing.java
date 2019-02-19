package no.hvl.dat110.chordoperations;

/**
 * @author tdoy
 * dat110 - demo/exercise
 */

import java.math.BigInteger;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;

import no.hvl.dat110.node.Node;
import no.hvl.dat110.rpc.interfaces.ChordNodeInterface;
import no.hvl.dat110.util.Util;


public class StabilizeRing extends Thread {
	
	private Node node;
	
	public StabilizeRing(Node node) {
		this.node = node;
	}
	
	public void run() {
		while (true) {
			
			try {
				stabilize();
				Thread.sleep(1000);
			}catch(InterruptedException | RemoteException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void stabilize() throws RemoteException {
		
		System.out.println("Stabilizing ring from "+node.getNodeIP()+"...");
		ChordNodeInterface succ = node.getSuccessor();				// get the successor of node
		
		ChordNodeInterface succnode = null;
		ChordNodeInterface predsucc = null;
		Registry registry = Util.locateRegistry(succ.getNodeIP());
		
		if(registry == null)
			return;
		try {
			succnode = (ChordNodeInterface) registry.lookup(succ.getNodeID().toString());	// get a remote handle
			predsucc = succnode.getPredecessor();
		} catch (Exception e) {
			predsucc = null;													// if we can't get a remote handle, pred does not exist
			//e.printStackTrace();
		}
		
		BigInteger nodeID = node.getNodeID();
		BigInteger succID = succnode.getNodeID();

		BigInteger predID = null;
		
		if(predsucc != null) {
			predID = predsucc.getNodeID();
			
			//predID.compareTo(nodeID)==1 && predID.compareTo(succID)==-1
			boolean cond = Util.computeLogic(predID, nodeID.add(new BigInteger("1")), succID.add(new BigInteger("1")));
			if(cond) {
				node.setSuccessor(predsucc);
											
				try {
					predsucc.notifySuccessor(node);			// notify successor (pred) that it has a new predecessor (node)
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
			}
		}
		System.out.println("Finished stabilizing chordring from "+node.getNodeIP());
	}
}
