package playingcoffee.core;

public interface MBC {

	public int read(int[] rom, int address);
	public void write(int value, int address);
	
	public static MBC create(int type) {
		switch (type) {
		case 0: return new ROMOnlyMBC();
		case 1: return new MBC1();
		}
		
		throw new IllegalArgumentException("Unsupported Maapper!");
	}
}
