package playingcoffee.ppu.mode;

import playingcoffee.ppu.PPU;
import playingcoffee.ppu.PPURegisters;

public class VBlankMode implements PPUMode {

	private PPURegisters registers;
	
	private int vBlankTimer;
	
	@Override
	public void start(PPU ppu) {
		registers = ppu.getRegisters();
		vBlankTimer = 4560; // 10 (scanlines) * 456 (clocks per scanline)
	}

	@Override
	public void clock(PPU ppu) {
		vBlankTimer--;
		
		if (vBlankTimer == 0) {
			registers.ly = 0;
		}
	}

	@Override
	public int nextMode() {
		if (vBlankTimer > 0) {
			return PPU.NONE;
		} else {
			return PPU.OAM_SEARCH;
		}
	}
}
