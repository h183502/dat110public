package no.hvl.dat110.transport;

import java.util.Arrays;

public class Segment {

	protected byte[] data;
	protected SegmentType type;
	protected byte checksum;
	
	public Segment () {
		super();
	}
	
	public Segment (byte[] data) {
	
		this.data = data;
		this.type = SegmentType.DATA;
		this.checksum = calcChecksum(data);
	}

	public Segment (SegmentType type) {
		this.data = null;
		this.type = type;
		this.checksum = 0;
	}
	
	public Segment clone () {
	
		Segment segment = new Segment();
		segment.type = this.type;
		
		if (this.data != null) {
		segment.data = this.data.clone();
		} 
		
		segment.checksum = this.checksum;
		
		return segment;
	}
	
	public SegmentType getType() {
		return type;
	}

	public byte[] getData() {
		return data;
	}
	public String toString() {
		
		String str = type.toString();
		
		if (data != null) {
			str = str + "[" + (new String(data)) + "]"; 
					
		}
		
		str = str + String.format("%8s", Integer.toBinaryString(checksum));
				
		return str;
		
		
	}
	
	public byte calcChecksum(byte[] data) {
		
		byte sum = 0;
		
		if (data != null) {
			
			for (int i=0; i<data.length;i++) {
				sum = (byte)(sum + data[i]);
			}
		}
		
		return sum;
	}
	
	public boolean isCorrect() {
		return (calcChecksum(this.data) == this.checksum);
	}

	// used back channel to simulate tranmission error
	public void setChecksum(byte checksum) {
		this.checksum = checksum;
	}
	
}
