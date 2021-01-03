package playingcoffee.ppu;

import playingcoffee.core.MemorySpace;

public class VRAM implements MemorySpace {

	public int[] vram;
	
	public int[] background0;
	public int[] background1;
	
	@SuppressWarnings("unused")
	private final PPU ppu;
	
	public VRAM(final PPU ppu) {
		vram = new int[0x1800];
		
		background0 = new int[32 * 32];
		background1 = new int[32 * 32];
	
		this.ppu = ppu;
	}
	
	@Override
	public int read(int address) {
//		if (ppu.getRegisters().getLCDCMode() == PPU.PIXEL_TRANSFER) return 0xFF;
		
		if (address >= 0x8000 && address <= 0x97FF) return vram[address - 0x8000];
		if (address >= 0x9800 && address <= 0x9BFF) return background0[address % (32 * 32)];
		if (address >= 0x9C00 && address <= 0x9FFF) return background1[address % (32 * 32)];
		
		return 0;
	}

	@Override
	public void write(int value, int address) {
//		if (ppu.getRegisters().getLCDCMode() == PPU.PIXEL_TRANSFER) return;

		if (address >= 0x9C00 && address <= 0x9FFF) background1[address % (32 * 32)] = value;
		if (address >= 0x9800 && address <= 0x9BFF) background0[address % (32 * 32)] = value;
		
		if (address >= 0x8000 && address <= 0x97FF) vram[address - 0x8000] = value;
	}

	@Override
	public boolean inMemorySpace(int address) {
		return address >= 0x8000 && address <= 0x9FFF;
	}

}
