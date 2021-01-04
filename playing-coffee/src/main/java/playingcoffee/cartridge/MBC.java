package playingcoffee.cartridge;

import playingcoffee.core.MemorySpace;

public abstract class MBC implements MemorySpace {

	protected int[] rom;
	
	protected MBC(int[] rom) {
		this.rom = rom;
	}
	
	public static MBC create(int type, int[] rom) {
		switch (type) {
		case 0: return new ROMOnlyMBC(rom);
		case 1: return new MBC1(false, false, rom);
		case 2: return new MBC1(true, false, rom);
		case 3: return new MBC1(true, true, rom);
		case 19: return new MBC3(true, true, rom);
		}
		
		throw new IllegalArgumentException("Unsupported Maapper!");
	}
}
