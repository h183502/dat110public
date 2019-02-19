package no.hvl.dat110.util;

/**
 * exercise/demo purpose in dat110
 * @author tdoy
 *
 */

import java.math.BigInteger;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;

import no.hvl.dat110.rpc.StaticTracker;
import no.hvl.dat110.rpc.interfaces.ChordNodeInterface;

public class Util {
	
	public static String activeIP = null;
	
	public static Registry locateRegistry(String ipaddress) {

		java.rmi.registry.Registry registry = null;
		try {
			registry = LocateRegistry.getRegistry(ipaddress, StaticTracker.PORT);			
			registry.list();
		} catch (RemoteException e) {
			registry = null;
		}
		
		return registry;
	}
	
	
	public static boolean computeLogic(BigInteger id, BigInteger lower, BigInteger upper) {
		
		// a formula to check whether an id falls within the set {lower, upper} using the address size as our bound (modulos operation)
		// it modifies 'upper' and 'id' when lower > upper e.g. set (6, 2) in mod 10 = {6, 7, 8, 9, 0, 1, 2}
		
		boolean cond = false;
		BigInteger nupper = upper;
		BigInteger addresssize = Hash.addressSize();
		if (lower.compareTo(upper)==1) {
			nupper = upper.add(addresssize);
			
			if((id.compareTo(new BigInteger("0"))==1 || id.compareTo(new BigInteger("0"))==0) && 
					((id.compareTo(upper)==-1 || id.compareTo(upper)==0))) {
				id = id.add(addresssize);
			}
		}
		
		upper = nupper;
		cond = (id.compareTo(lower)==1 || id.compareTo(lower)==0) && (id.compareTo(upper)==-1 || id.compareTo(upper)==0);
		
		return cond;
	}
	
	public static List<String> toString(List<ChordNodeInterface> fingers) throws RemoteException {
		List<String> fingerstr = new ArrayList<String>();
		for(int i=0; i<fingers.size(); i++) {
			fingerstr.add(fingers.get(i).getNodeIP());
		}
		
		return fingerstr;
	}
	
	public static Registry tryIPs() {
		
		// try the tracker IP addresses and connect to any one available
		String[] ips = StaticTracker.ACTIVENODES;
		
		Registry registry = null;
		for (String ip : ips) {
			registry = Util.locateRegistry(ip);
			if (registry != null) {
				activeIP = ip;
				return registry;
			}
		}
		
		return registry;

	}

}
