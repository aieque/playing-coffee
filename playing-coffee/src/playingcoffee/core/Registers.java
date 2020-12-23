package playingcoffee.core;

public class Registers {

	private int a, f;
	private int b, c;
	private int d, e;
	private int h, l;
	
	public int pc;
	public int sp;
	
	public static final int FLAG_Z = 1 << 7;
	public static final int FLAG_N = 1 << 6;
	public static final int FLAG_H = 1 << 5;
	public static final int FLAG_C = 1 << 4;
	
	public void setFlag(int flag, boolean value) {
		if (value)
			f |= flag;
		else
			f &= ~flag;
	}
	
	public boolean getFlag(int flag) {
		return (f & flag) != 0;
	}
	
	public int readAF() {
		return a << 8 | f;
	}
	
	public int readBC() {
		return b << 8 | c;
	}
	
	public int readDE() {
		return d << 8 | e;
	}
	
	public int readHL() {
		return h << 8 | l;
	}
	
	public void writeAF(int af) {
		a = (af >> 8) & 0xFF;
		f = af & 0xFF;
	}
	
	public void writeBC(int bc) {
		b = (bc >> 8) & 0xFF;
		c = bc & 0xFF;
	}
	
	public void writeDE(int de) {
		d = (de >> 8) & 0xFF;
		e = de & 0xFF;
	}
	
	public void writeHL(int hl) {
		h = (hl >> 8) & 0xFF;
		l = hl & 0xFF;
	}
	
	public int readA() {
		return a;
	}
	
	public void writeA(int a) {
		this.a = a;
	}
	
	public int readF() {
		return f;
	}
	
	public void writeF(int f) {
		this.f = f;
	}
	
	public int readB() {
		return b;
	}
	
	public void writeB(int b) {
		this.b = b;
	}
	
	public int readC() {
		return c;
	}
	
	public void writeC(int c) {
		this.c = c;
	}
	
	public int readD() {
		return d;
	}
	
	public void writeD(int d) {
		this.d = d;
	}
	
	public int readE() {
		return e;
	}
	
	public void writeE(int e) {
		this.e = e;
	}
	
	public int readH() {
		return h;
	}
	
	public void writeH(int h) {
		this.h = h;
	}
	
	public int readL() {
		return l;
	}
	
	public void writeL(int l) {
		this.l = l;
	}
	
}