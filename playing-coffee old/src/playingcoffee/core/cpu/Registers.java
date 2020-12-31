package playingcoffee.core.cpu;

public class Registers {

	private int a, b, c, d, e, h, l;
	
	private int pc, sp;
	
	public int getA() { return a & 0xFF; }
	public int getB() { return b & 0xFF; }
	public int getC() { return c & 0xFF; }
	public int getD() { return d & 0xFF; }
	public int getE() { return e & 0xFF; }
	public int getH() { return h & 0xFF; }
	public int getL() { return l & 0xFF; }
	
	public void setA(int value) { a = value & 0xFF; }
	public void setB(int value) { b = value & 0xFF; }
	public void setC(int value) { c = value & 0xFF; }
	public void setD(int value) { d = value & 0xFF; }
	public void setE(int value) { e = value & 0xFF; }
	public void setH(int value) { h = value & 0xFF; }
	public void setL(int value) { l = value & 0xFF; }

	public int getPC() { return pc & 0xFFFF; }
	public int getSP() { return sp & 0xFFFF; }
	
	public void setPC(int value) { pc = value & 0xFFFF; }
	public void setSP(int value) { sp = value & 0xFFFF; }
	
	public void incPC(int value) { setPC(pc + value); }
	public void incSP(int value) { setSP(sp + value); }
}
