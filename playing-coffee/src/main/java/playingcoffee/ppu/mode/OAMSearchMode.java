package playingcoffee.ppu.mode;

import playingcoffee.ppu.PPU;

public class OAMSearchMode implements PPUMode {

	/* 
	 *  This mode should lookup which sprites cover this scanline.
	 *  But since I'm not rendering sprites at the moment, I'll
	 *  simply wait 80 clock cycles.
	 */
	
	private int clockCycles;
	
	@Override
	public void start(PPU ppu) {
		clockCycles = 0;
	}

	@Override
	public void clock(PPU ppu) {
		clockCycles++;
	}

	@Override
	public int nextMode() {
		if (clockCycles < 80) {
			return PPU.NONE;
		} else {
			return PPU.PIXEL_TRANSFER;
		}
	}
	
}
