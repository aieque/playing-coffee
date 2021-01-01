package playingcoffee.core;

import playingcoffee.log.Log;

public class ROMOnlyMBC implements MBC {

	@Override
	public int read(int[] rom, int address) {
		return rom[address];
	}

	@Override
	public void write(int value, int address) {
		// Don't do anything
		Log.warn("Attempting to write to rom at address: 0x%4x.", address);
	}

}
