package no.hvl.dat110.chordoperations;

/**
 * @author tdoy
 * dat110 - demo/exercise
 */
import java.math.BigInteger;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;

import no.hvl.dat110.node.Node;
import no.hvl.dat110.rpc.interfaces.ChordNodeInterface;
import no.hvl.dat110.util.Util;

public class CheckPredecessor extends Thread {
	
	private Node node;
	
	
	public CheckPredecessor(Node node) {
		this.node = node;
	}
	
	public void run() {
		
		while(true) {
			
			try {
				checkpred();
				Thread.sleep(1000);
			} catch (InterruptedException | RemoteException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void checkpred() throws RemoteException {
		System.out.println("Checking the predecessor for Node: "+node.getNodeIP());
		BigInteger predID = null;
		try {
			predID = node.getPredecessor().getNodeID();
		}catch(Exception e) {
			predID = null;
		}
		
		try {
			Registry registry = Util.locateRegistry(node.getPredecessor().getNodeIP());
			if(registry == null) {
				node.setPredecessor(null);		// object not available remove predecessor
				return;
			}
			
			ChordNodeInterface predNode = null;
			try {
				predNode = (ChordNodeInterface) registry.lookup(predID.toString());
			} catch (RemoteException e) {
				node.setPredecessor(null);
				//e.printStackTrace();
			}
			
			BigInteger succofpred = predNode.getSuccessor().getNodeID();
			if (succofpred.compareTo(node.getNodeID()) != 0) { 	// if node a that succeeds b where b preceeds c is not the same as c, then c has no predecessor
				node.setPredecessor(null);
			}
		} catch (NotBoundException | NullPointerException | RemoteException e) {		
			node.setPredecessor(null);		// if error occurs - predecessor can't be reached.. set the reference to null
			//e.printStackTrace();
		}
		String predip = null;
		try{
			predip = node.getPredecessor().getNodeIP();
		}catch(Exception e) {}
		//System.out.println("predecessor for Node: predecessor("+node.getNodeIP()+") = "+predip);
	}
}
