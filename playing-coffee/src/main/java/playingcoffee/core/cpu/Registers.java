package playingcoffee.core.cpu;

public class Registers {

	private int a, b, c, d, e, h, l;
	
	private int pc, sp;
	
	private Flags flags;
	
	public Registers() {
		flags = new Flags();
	}
	
	public int getA() { return a; }
	public int getB() { return b; }
	public int getC() { return c; }
	public int getD() { return d; }
	public int getE() { return e; }
	public int getH() { return h; }
	public int getL() { return l; }
	
	public void setA(int value) { a = value & 0xFF; }
	public void setB(int value) { b = value & 0xFF; }
	public void setC(int value) { c = value & 0xFF; }
	public void setD(int value) { d = value & 0xFF; }
	public void setE(int value) { e = value & 0xFF; }
	public void setH(int value) { h = value & 0xFF; }
	public void setL(int value) { l = value & 0xFF; }
	
	public Flags getFlags() { return flags; }
	
	public int getAF() { return (a << 8) | flags.get(); }
	public int getBC() { return (b << 8) | c; }
	public int getDE() { return (d << 8) | e; }
	public int getHL() { return (h << 8) | l; }
	
	public int getPC() { return pc; }
	public int getSP() { return sp; }
	
	public void setAF(int value) { setA(value >> 8); flags.set(value); }
	public void setBC(int value) { setB(value >> 8); setC(value); }
	public void setDE(int value) { setD(value >> 8); setE(value); }
	public void setHL(int value) { setH(value >> 8); setL(value); }

	public void setPC(int value) { pc = value & 0xFFFF; }
	public void setSP(int value) { sp = value & 0xFFFF; }

	public void addPC(int value) { setPC(getPC() + value); };
	public void addSP(int value) { setSP(getSP() + value); };

	public void incPC() { addPC(1); }
	public void incSP() { addSP(1); }

	public void decPC() { addPC(-1); }
	public void decSP() { addSP(-1); }
}
