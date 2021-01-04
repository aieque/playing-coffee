package playingcoffee.ppu.mode;

import playingcoffee.ppu.PPU;
import playingcoffee.ppu.PPURegisters;

public class PixelTransferMode implements PPUMode {

	private PPURegisters registers;
	
	@Override
	public void start(PPU ppu) {
		registers = ppu.getRegisters();
		
		registers.lx = 0;
	}

	@Override
	public void clock(PPU ppu) {
		registers.lx++;
	}

	@Override
	public int nextMode() {
		// This will soon be redone, however this value is the minimum amount of clocks.
		if (registers.lx < 172) {
			return PPU.NONE;
		} else {
			return PPU.HBLANK;
		}
	}
	
	@Override
	public boolean firesLCDCStatusInterrupt() {
		return false;
	}
}
