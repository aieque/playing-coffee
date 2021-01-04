package playingcoffee.cartridge;

public class MBC3 extends MBC {

	private boolean ram;
	private boolean battery;
	
	private int bank = 1;
	
	private boolean enableRAM = false;
	private int ramBank = 0;
	
	private int[] extRAM;
	
	public MBC3(boolean hasRAM, boolean hasBattery, int[] rom) {
		super(rom);
		
		this.ram = hasRAM;
		this.battery = hasBattery;
		
		extRAM = new int[0x10000];
	}

	@Override
	public int read(int address) {
		if (address >= 0x0000 && address <= 0x3FFF) {
			return rom[address];
		}
		
		if (address >= 0x4000 && address <= 0x7FFF) {
			return rom[(address % 0x4000) + bank * 0x4000];
		}
		
		if (address >= 0xA000 && address <= 0xBFFF && enableRAM) {
			return extRAM[(address - 0xA000) + ramBank * 0x2000];
		}
		return 0;
	}

	@Override
	public void write(int value, int address) {
		if (address >= 0x0000 && address <= 0x1FFF && ram) {
			if (value == 0x00) enableRAM = false;
			if (value == 0x0A) enableRAM = true;
		}
		
		if (address >= 0x2000 && address <= 0x3FFF) {
			bank = ((value & 0x1F) == 0) ? 1 : (value & 0x1F);
		}
		
		if (address >= 0x4000 && address <= 0x5FFF) {
			ramBank = value & 0x3;
		}
	
		if (address >= 0xA000 && address <= 0xBFFF && enableRAM) {
			extRAM[(address - 0xA000) + ramBank * 0x2000] = value;
		}
	}

	@Override
	public boolean inMemorySpace(int address) {
		return (address >= 0x0000 && address <= 0x7FFF) || (address >= 0xA000 && address <= 0xBFFF && ram);
	}

	public boolean isRAM() {
		return ram;
	}

	public boolean isBattery() {
		return battery;
	}

	
}

/*
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

	public boolean isRAM() {
		return ram;
	}

	public boolean isBattery() {
		return battery;
	}

	
}
*/