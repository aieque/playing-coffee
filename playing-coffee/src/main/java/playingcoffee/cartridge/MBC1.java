package playingcoffee.cartridge;

public class MBC1 extends MBC {

	private boolean ram;
	private boolean battery;
	
	private int bank = 1;
	
	public MBC1(boolean hasRAM, boolean hasBattery, int[] rom) {
		super(rom);
		
		this.ram = hasRAM;
		this.battery = hasBattery;
	}

	@Override
	public int read(int address) {
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

	@Override
	public boolean inMemorySpace(int address) {
		return address >= 0x0000 && address <= 0x7FFF;
	}

	
}
