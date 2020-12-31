package playingcoffee.core;

public interface MemorySpace {

	public int read(int address);
	public void write(int value, int address);
	public boolean inMemorySpace(int address);
	
}
