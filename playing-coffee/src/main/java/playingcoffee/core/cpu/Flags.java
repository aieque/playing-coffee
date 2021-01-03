package playingcoffee.core.cpu;

public class Flags {

	private int f; // Technically the "correct" notation
	
	public static final int ZERO = 1 << 7;
	public static final int NEGATIVE = 1 << 6;
	public static final int HALF_CARRY = 1 << 5;
	public static final int CARRY = 1 << 4;
	
	public void set(int value) {
		f = value & 0xF0;
	}
	
	public int get() {
		return f;
	}
	
	public void set(int flag, boolean value) {
		if (value) f |= flag;
		else f &= ~flag;
	}
	
	public boolean get(int flag) {
		return (f & flag) != 0;
	}
	
}
