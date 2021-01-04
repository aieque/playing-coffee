package playingcoffee.ppu.mode;

import playingcoffee.ppu.PPU;

public interface PPUMode {

	public void start(PPU ppu);
	public void clock(PPU ppu);
	
	public int nextMode();
	
	public default boolean firesLCDCStatusInterrupt() {
		return true;
	}
}
