package no.hvl.dat110.transport.rdt2;

public class SegmentRDT21 extends SegmentRDT2 {

	private int seqnr;
	
	public SegmentRDT21(byte[] data, int seqnr) {
		super(data);
		this.seqnr = seqnr;
	}
	
	public SegmentRDT21(SegmentType type) {
		super(type);
	}
	
	public int getSeqnr () {
		return this.seqnr;
	}
	
	public String toString() {
		
		String str = super.toString();
		
		if (type == SegmentType.DATA) {
			str = str + "[" + type + "]";
		}
		
		return str;
	}
}
