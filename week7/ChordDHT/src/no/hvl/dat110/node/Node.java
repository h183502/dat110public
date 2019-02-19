package no.hvl.dat110.node;


/**
 * exercise/demo purpose in dat110
 * @author tdoy
 *
 */

import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import no.hvl.dat110.rpc.interfaces.ChordNodeInterface;
import no.hvl.dat110.util.Hash;
import no.hvl.dat110.util.Util;

public class Node extends UnicastRemoteObject implements ChordNodeInterface {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private BigInteger nodeID;		// BigInteger value of hash of IP address of the Node
	private String nodeIP;			// IP address of node 
	private ChordNodeInterface successor;
	private ChordNodeInterface predecessor;
	private List<ChordNodeInterface> fingerTable;
	private Set<BigInteger> fileKey;
	
	
	public Node() throws RemoteException, UnknownHostException {
		super();

		fingerTable = new ArrayList<ChordNodeInterface>();
		fileKey = new HashSet<BigInteger>();
		setNodeIP(InetAddress.getLocalHost().getHostAddress());	// use the IP address of the host
		BigInteger hashvalue = Hash.customHash(getNodeIP());	// use custom hash for testing purposes with few (8-16) peers
		//BigInteger hashvalue = Hash.hashOf(getNodeIP());		// use the SHA-1 
		setNodeID(hashvalue);
		
		setSuccessor(null);
		setPredecessor(null);
	}
	
	public BigInteger getNodeID() {
		return nodeID;
	}
	
	public void setNodeID(BigInteger nodeID) {
		this.nodeID = nodeID;
	}
	
	public String getNodeIP() {
		return nodeIP;
	}

	public void setNodeIP(String nodeIP) {
		this.nodeIP = nodeIP;
	}

	public ChordNodeInterface getSuccessor() {
		return successor;
	}
	
	public void setSuccessor(ChordNodeInterface successor) {
		this.successor = successor;
	}
	
	public ChordNodeInterface getPredecessor() {
		return predecessor;
	}
	public void setPredecessor(ChordNodeInterface predecessor) {
		this.predecessor = predecessor;
	}
	
	public List<ChordNodeInterface> getFingerTable() {
		return fingerTable;
	}
	
	public void addToFingerTable(ChordNodeInterface finger) {
		this.fingerTable.add(finger);
	}
	
	public void removeFromFingerTable(ChordNodeInterface finger) {
		this.fingerTable.remove(finger);
	}
	
	public void setFingerTable(List<ChordNodeInterface> fingerTable) {
		this.fingerTable = fingerTable;
	}

	public Set<BigInteger> getFileKey() {
		return fileKey;
	}
	
	public void addToFileKey(BigInteger fileKey) {
		this.fileKey.add(fileKey);
	}
	
	public void removeFromFileKey(BigInteger fileKey) {
		this.fileKey.remove(fileKey);
	}

	@Override
	public ChordNodeInterface findSuccessor(BigInteger keyid) throws RemoteException {
		
		// ask this node to find the successor of id
		ChordNodeInterface succ = this.getSuccessor();			// last known successor of this node
			
		ChordNodeInterface succstub = registryHandle(succ); 	// issue a remote call
			
		if(succstub != null) {
			
			//System.out.println("Successor for Node: successor("+this.getNodeIP()+") = "+succstub.getNodeIP());
			
			BigInteger succID = succstub.getNodeID();
			BigInteger nodeID = this.getNodeID();

			// check that keyid is a member of the set {nodeid+1,...,succID}
			Boolean cond = Util.computeLogic(keyid, nodeID.add(new BigInteger("1")), succID);
			if(cond) {
				return succstub;
			} else {
				// search the local finger table of this node for the highest predecessor of id
				ChordNodeInterface highest_pred = findHighestPredecessor(keyid);
				return highest_pred.findSuccessor(keyid);							// a remote call
			}
		}
				
		return null;	
	}
	
	private ChordNodeInterface findHighestPredecessor(BigInteger ID) throws RemoteException {
		
		BigInteger nodeID = getNodeID();
		List<ChordNodeInterface> fingers = getFingerTable();			
		
		int size = fingers.size() - 1;
		//System.out.println("FingerTable size: "+fingers.size());
		for(int i=0; i<fingers.size()-1; i++) {
			int m = size-i;
			ChordNodeInterface ftsucc = fingers.get(m);
			try {
				BigInteger ftsuccID = ftsucc.getNodeID();
				
				Registry registry = Util.locateRegistry(ftsucc.getNodeIP());
				
				if(registry == null)
					return this;

				ChordNodeInterface ftsuccnode = (ChordNodeInterface) registry.lookup(ftsuccID.toString());

				// check that ftsuccID is a member of the set {nodeID+1,...,ID-1}
				boolean cond = Util.computeLogic(ftsuccID, nodeID.add(new BigInteger("1")), ID.subtract(new BigInteger("1")));
				if(cond) {
					return ftsuccnode;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}			
		}
		
		return (ChordNodeInterface) this;			
	}
	
	@Override
	public void notifySuccessor(ChordNodeInterface pred_new) throws RemoteException {
		
		ChordNodeInterface successor = this;						// 
		ChordNodeInterface pred_old = successor.getPredecessor();
		
		if(pred_old == null) {
			this.setPredecessor(pred_new);		// accept the new predecessor
			return;
		}
		
		BigInteger succID = successor.getNodeID();
		BigInteger pred_oldID = pred_old.getNodeID();
		
		BigInteger pred_newID = pred_new.getNodeID();
				
		if(pred_newID.compareTo(pred_oldID) == 1 && pred_newID.compareTo(succID) == -1) {
			this.setPredecessor(pred_new);		// accept the new predecessor
		}		
		
	}

	@Override
	public void leaveRing(ChordNodeInterface node) throws RemoteException {
		// TODO
		// transfer keys to successor before departure
		// update the successor and predecessor accordingly
		System.exit(0);				
	}
	
	private ChordNodeInterface registryHandle(ChordNodeInterface node) {
		ChordNodeInterface nodestub = null;
		Registry registry = null;
		try {
			registry = Util.locateRegistry(node.getNodeIP());		
			
			if(registry == null) {
				return null;
			}
			
			nodestub = (ChordNodeInterface) registry.lookup(node.getNodeID().toString());	// remote stub
		} catch (NotBoundException | RemoteException e) {
			return null;		// successor has left the ring...or can't connect
		}
		
		return nodestub;
	}

}
