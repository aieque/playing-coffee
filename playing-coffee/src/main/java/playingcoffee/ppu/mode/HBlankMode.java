package playingcoffee.ppu.mode;

import playingcoffee.ppu.PPU;
import playingcoffee.ppu.PPURegisters;

public class HBlankMode implements PPUMode {

	private PPURegisters registers;
	
	@Override
	public void start(PPU ppu) {
		registers = ppu.getRegisters();
	}

	@Override
	public void clock(PPU ppu) {
		if (registers.lx < 376) { // 456 (clocks per scanline) - 80 (clocks for OAM search) = 376
			registers.lx++;
		}
		
		if (registers.lx == 376) {
			registers.ly++;
		}
	}
	
	@Override
	public int nextMode() {
		if (registers.lx < 376) {
			return PPU.NONE;
		} else {
			if (registers.ly == 144) { // Scanlines per frame
				return PPU.VBLANK;
			} else {
				return PPU.OAM_SEARCH;
			}
		}
	}
}
