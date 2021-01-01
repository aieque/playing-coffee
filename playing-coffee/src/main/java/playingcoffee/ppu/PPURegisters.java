package playingcoffee.ppu;

import playingcoffee.core.MemorySpace;

public class PPURegisters implements MemorySpace {

	public int lcdControl; // 0xFF40
	public int lcdcStatus; // 0xFF41
	public int scrollY, scrollX; // 0xFF42 - 0xFF43
	public int lcdcYCoord; // 0xFF44
	public int lyCompare; // 0xFF45
	public int dmaTransferStart; // 0xFF46
	public int bgPalette; // 0xFF47
	public int objPalette0, objPalette1; // 0xFF48 - 0xFF49
	public int windowY, windowX; // 0xFF4A - 0xFF4B
	
	@SuppressWarnings("unused")
	private final PPU ppu;
	
	public PPURegisters(PPU ppu) {
		this.ppu = ppu;
	}

	public void setLCDCMode(int mode) {
		lcdcStatus = (lcdcStatus & 0xFC) | mode;
	}
	
	public int read(int address) {
		switch (address) {
			case 0xFF40: return lcdControl;
			case 0xFF41: return lcdcStatus;
			case 0xFF42: return scrollY;
			case 0xFF43: return scrollX;
			case 0xFF44: return lcdcYCoord;
			case 0xFF45: return lyCompare;
			case 0xFF46: return dmaTransferStart; // TODO: Do DMA (I have no idea how to do it though...)
			case 0xFF47: return bgPalette;
			case 0xFF48: return objPalette0;
			case 0xFF49: return objPalette1;
			case 0xFF4A: return windowY;
			case 0xFF4B: return windowX;
		}
		
		throw new IllegalArgumentException("Invalid address");
	}
	
	public void write(int value, int address) {
		switch (address) {
			case 0xFF40: lcdControl = value; return;
			case 0xFF41: lcdcStatus = value; return;
			case 0xFF42: scrollY = value; return;
			case 0xFF43: scrollX = value; return;
			case 0xFF44: return;
			case 0xFF45: lyCompare = value; return;
			case 0xFF46: dmaTransferStart = value; return; // TODO: Do DMA (I have no idea how to do it though...)
			case 0xFF47: bgPalette = value; return;
			case 0xFF48: objPalette0 = value; return;
			case 0xFF49: objPalette1 = value; return;
			case 0xFF4A: windowY = value; return;
			case 0xFF4B: windowX = value; return;
		}
	
		throw new IllegalArgumentException("Invalid address");
	}
	
	public boolean inMemorySpace(int address) {
		return (address >= 0xFF40 && address <= 0xFF4B);
	}
}
