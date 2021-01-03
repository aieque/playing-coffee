package playingcoffee.ppu;

public class FIFOPixelFetcher {

	private int mode;
	
	public static final int GET_TILE      = 0;
	public static final int GET_TILE_LOW  = 1;
	public static final int GET_TILE_HIGH = 2;
	public static final int SLEEP         = 3;
	public static final int PUSH          = 4;
	
	private final PixelFIFO pixelFIFO;

	private int tile;
	private int high, low;
	
	public FIFOPixelFetcher(final PixelFIFO pixelFIFO) {
		this.pixelFIFO = pixelFIFO;
		
		mode = GET_TILE;
	}
	
	// To be called just before PIXEL_TRANSFER.
	public void prepare() {
		mode = GET_TILE;

		tile = high = low = 0;
	}
		
	public void clock(VRAM vram) {
		switch (mode) {
			case GET_TILE: {
				if (tile == 0) {
					
				}
				break;
			}
			case GET_TILE_LOW: {
				
				break;
			}
			case GET_TILE_HIGH: {
				
				break;
			}
			case SLEEP: {
				
				break;
			}
			case PUSH: {
				
				break;
			}
		}
	}
}
