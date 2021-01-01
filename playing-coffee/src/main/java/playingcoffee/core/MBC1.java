package playingcoffee.core;

public class MBC1 implements MBC {

	private int bank = 1;
	
	@Override
	public int read(int[] rom, int address) {
		if (address >= 0x0000 && address <= 0x3FFF) {
			return rom[address];
		} else if (address >= 0x4000 && address <= 0x7FFF) {
			return rom[(address % 0x4000) + bank * 0x4000];
		}
		return 0;
	}

	@Override
	public void write(int value, int address) {
		if (address >= 0x2000 && address <= 0x3FFF) {
			bank = ((value & 0x1F) == 0) ? 1 : (value & 0x1F);
		}
	}

}
