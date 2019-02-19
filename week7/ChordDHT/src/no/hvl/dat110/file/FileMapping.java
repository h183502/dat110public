package no.hvl.dat110.file;

/**
 * @author tdoy
 * dat110 - demo/exercise
 */

import java.math.BigInteger;
import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.Random;

import no.hvl.dat110.rpc.interfaces.ChordNodeInterface;
import no.hvl.dat110.util.Hash;

public class FileMapping extends Thread {
	
	private BigInteger[] files;
	private int nfiles = 5;								// let's assume each node manages nfiles (5 for now) - can be changed from the constructor
	private ChordNodeInterface chordnode;
	
	public FileMapping(ChordNodeInterface chordnode, int nfiles) throws RemoteException {
		this.nfiles = nfiles;
		files = new BigInteger[nfiles];
		this.chordnode = chordnode;
		randomFiles();
	}
	
	public void run() {
		
		while(true) {
			try {
				distributeFiles();
				Thread.sleep(2000);
			} catch (RemoteException | InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void randomFiles() throws RemoteException {
		Random r = new Random();
		String filename = "filename"+r.nextInt();
		
		for(int i=0; i<nfiles; i++){
			//files[i] = Hash.hashOf(filename);
			files[i] = Hash.customHash(filename);
			filename = "filename"+r.nextInt();
			chordnode.getFileKey().add(files[i]);
		}
		
		System.out.println("Generated File keyids for "+chordnode.getNodeIP()+" => "+Arrays.asList(files));
		
	}
	
	private void staticFiles() {
		
		// TODO create some filenames (n - number) similar to what randomFiles() is doing. But this time, use static filenames
		// Purpose is to be able to search for the node responsible for holding this file from NodeClient class.
	}
	
	private void distributeFiles() throws RemoteException {
		
		// lookup(keyid) operation
		// findSuccessor() function should be invoked to find the node with identifier id >= keyid
		Object[] keys = chordnode.getFileKey().toArray();
		for(int i=0; i<keys.length; i++) {
			BigInteger fileID = (BigInteger) keys[i];
			ChordNodeInterface succOfFileID = chordnode.findSuccessor(fileID);
			
			// if we find the successor node of fileID, we can assign the file to the successor. This should always work even with one node
			if(succOfFileID != null) {
				chordnode.getFileKey().remove(fileID);
				succOfFileID.addToFileKey(fileID);
			}			
		}
	}

}
