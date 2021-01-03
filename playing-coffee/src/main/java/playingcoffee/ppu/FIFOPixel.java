package playingcoffee.ppu;

public class FIFOPixel {

	private int color;
	private int palette;
	private int spritePriority;
	private int backgroundPriority;
	
	public FIFOPixel() {
		this(0, 0, 0, 0);
	}
	
	public FIFOPixel(int color, int palette, int spritePriority, int backgroundPriority) {
		this.color = color;
		this.palette = palette;
		this.spritePriority = spritePriority;
		this.backgroundPriority = backgroundPriority;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public int getPalette() {
		return palette;
	}

	public void setPalette(int palette) {
		this.palette = palette;
	}

	public int getSpritePriority() {
		return spritePriority;
	}

	public void setSpritePriority(int spritePriority) {
		this.spritePriority = spritePriority;
	}

	public int getBackgroundPriority() {
		return backgroundPriority;
	}

	public void setBackgroundPriority(int backgroundPriority) {
		this.backgroundPriority = backgroundPriority;
	}
	
	
	
	/*private int colorIndex;
	private int source;
	
	public static final int BACKGROUND = 0;
	public static final int SPRITE0 = 1;
	public static final int SPRITE1 = 2;
	
	public FIFOPixel() {
		this(0, 0);
	}
	
	public FIFOPixel(int colorIndex, int source) {
		this.colorIndex = colorIndex;
		this.source = source;
	}
	
	public int getColorIndex() {
		return colorIndex;
	}
	
	public void setColorIndex(int colorIndex) {
		this.colorIndex = colorIndex;
	}
	
	public int getSource() {
		return source;
	}
	
	public void setSource(int source) {
		this.source = source;
	}*/
	
}
