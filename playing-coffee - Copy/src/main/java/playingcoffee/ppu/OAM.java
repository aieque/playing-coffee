package playingcoffee.ppu;

import playingcoffee.core.MemorySpace;
import playingcoffee.log.Log;

public class OAM implements MemorySpace {

	public OAMEntry entries[];
	
	public OAM() {
		entries = new OAMEntry[40];
	}
	
	public int read(int address) {
		int entry = (address - 0xFE00) / 4;
		
		switch (address % 4) {
			case 0: return entries[entry].x;
			case 1: return entries[entry].y;
			case 2: return entries[entry].tileNumber;
			case 3: return entries[entry].flags;
		}
		
		Log.error("how in pete's holy christmas tree did we get here?");
		throw new IllegalArgumentException("Invalid address.");
	}
	
	public void write(int value, int address) {
		int entry = (address - 0xFE00) / 4;
		
		switch (address % 4) {
			case 0: entries[entry].x = value;
			case 1: entries[entry].y = value;
			case 2: entries[entry].tileNumber = value;
			case 3: entries[entry].flags = value;
		}
		
		Log.error("how in pete's holy christmas tree did we get here?");
		throw new IllegalArgumentException("Invalid address.");
	}

	@Override
	public boolean inMemorySpace(int address) {
		return (address >= 0xFE00 && address <= 0xFE9F);
	}
	
	public class OAMEntry {
		public int x, y;
		public int tileNumber;
		public int flags;
	}
}
