package playingcoffee.cartridge;

import playingcoffee.log.Log;

public class ROMOnlyMBC extends MBC {

	protected ROMOnlyMBC(int[] rom) {
		super(rom);
	}

	@Override
	public int read(int address) {
		return rom[address];
	}

	@Override
	public void write(int value, int address) {
		// Don't do anything
		Log.warn("Attempting to write to rom at address: 0x%4x.", address);
	}

	@Override
	public boolean inMemorySpace(int address) {
		return address >= 0x0000 && address <= 0x7FFF;
	}

}
