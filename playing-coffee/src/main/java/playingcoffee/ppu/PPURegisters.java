package playingcoffee.ppu;

import playingcoffee.core.MMU;
import playingcoffee.core.MemorySpace;

public class PPURegisters implements MemorySpace {

	public int lcdControl; // 0xFF40
	public int lcdcStatus; // 0xFF41
	public int scy, scx; // 0xFF42 - 0xFF43
	public int ly; // 0xFF44
	public int lyc; // 0xFF45
	public int dmaTransferStart; // 0xFF46
	public int bgPalette; // 0xFF47
	public int objPalette0, objPalette1; // 0xFF48 - 0xFF49
	public int wy, wx; // 0xFF4A - 0xFF4B
	
	public int lx; // NOTE: This is not an actual register, but I put it here for convinience and structure.
	
	private final PPU ppu;
	
	public PPURegisters(PPU ppu) {
		this.ppu = ppu;
	}

	public void setLCDCMode(int mode) {
		lcdcStatus = (lcdcStatus & 0xFC) | mode;
	}
	
	public int getLCDCMode() {
		return lcdcStatus & 0x3;
	}
	
	public int read(int address) {
		switch (address) {
			case 0xFF40: return lcdControl;
			case 0xFF41: return lcdcStatus;
			case 0xFF42: return scy;
			case 0xFF43: return scx;
			case 0xFF44: return ly;
			case 0xFF45: return lyc;
			case 0xFF46: return dmaTransferStart;
			case 0xFF47: return bgPalette;
			case 0xFF48: return objPalette0;
			case 0xFF49: return objPalette1;
			case 0xFF4A: return wy;
			case 0xFF4B: return wx;
		}
		
		throw new IllegalArgumentException("Invalid address");
	}
	
	public void write(int value, int address) {
		switch (address) {
			case 0xFF40: lcdControl = value; return;
			case 0xFF41: lcdcStatus = value; return;
			case 0xFF42: scy = value; return;
			case 0xFF43: scx = value; return;
			case 0xFF44: return;
			case 0xFF45: lyc = value; return;
			case 0xFF46:
				dmaTransferStart = value; // TODO: Redo the DMA transfer
				
				for (int i = 0; i < 0x100; i++) {
					MMU mmu = ppu.getMMU();
					
					mmu.write(mmu.read((value << 8) + i), 0xFE00 + i);
				}
				return;
			case 0xFF47: bgPalette = value; return;
			case 0xFF48: objPalette0 = value; return;
			case 0xFF49: objPalette1 = value; return;
			case 0xFF4A: wy = value; return;
			case 0xFF4B: wx = value; return;
		}
	
		throw new IllegalArgumentException("Invalid address");
	}
	
	public boolean inMemorySpace(int address) {
		return (address >= 0xFF40 && address <= 0xFF4B);
	}
}
