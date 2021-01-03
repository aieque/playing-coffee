package playingcoffee.ppu;

import java.util.LinkedList;
import java.util.Queue;

public class PixelFIFO {

	Queue<FIFOPixel> fifo;
	
	public PixelFIFO() {
		fifo = new LinkedList<FIFOPixel>();
	}
	
	public boolean push(FIFOPixel[] tileData) {
		if (fifo.size() > 8) return false;
		
		for (FIFOPixel pixel : tileData) fifo.add(pixel);
		
		return true;
	}
	
}
