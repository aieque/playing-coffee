package playingcoffee.ppu;

import playingcoffee.core.InterruptManager;
import playingcoffee.core.MMU;

public class PPU {

	private final MMU mmu;
	private final InterruptManager interruptManager;
	
	private PPURegisters registers;
	private VRAM vram;
	
	private int clockCount = 0;
	
	private int[] framebuffer;
	
	public static final int HBLANK = 0;
	public static final int VBLANK = 1;
	public static final int OAM_SEARCH = 2;
	public static final int PIXEL_TRANSFER = 3;
	
	public PPU(final MMU mmu, final InterruptManager interruptManager) {
		this.mmu = mmu;
		this.interruptManager = interruptManager;
		
		registers = new PPURegisters(this);
		vram = new VRAM(this);
		
		framebuffer = new int[160 * 144];
		
		this.mmu.connectMemorySpace(registers);
		this.mmu.connectMemorySpace(vram);
	}
	
	public void onLCDDisable() {
		clockCount = 0;
	}
	
	public void OAMSeach() {
		registers.setLCDCMode(OAM_SEARCH);
	}
	
	public void pixelTransfer() {
		registers.setLCDCMode(PIXEL_TRANSFER);
	}
	
	public void HBlank() {
		registers.setLCDCMode(HBLANK);
	}	
	
	public void VBlank() {
		registers.setLCDCMode(VBLANK);
	}
	
	public void clock() {
		registers.lcdcYCoord = clockCount / 114;
		
		if (clockCount == 144 * 114) renderToFramebuffer();
		if (clockCount == 114 * 144) {
			interruptManager.requestInterrupt(InterruptManager.VBLANK);
		}

		
		if (clockCount < 114 * 144) {
			if (clockCount % 114 < 20) OAMSeach();
			else if (clockCount % 114 < 43) pixelTransfer();
			else HBlank();
			
		} else {
			VBlank();
		}
		
		clockCount++;
		
		if (clockCount == 17556) {
			clockCount = 0;
		}
	}

	private void putPixel(int pixel, int x, int y) {
		if (x >= 0 && x < 160 && y >= 0 && y < 144) framebuffer[x + y * 160] = pixel;
	}
	
	private void putTile(int address, int screenX, int screenY) {
		for (int y = 0; y < 8; y++) {
			for (int n = 0; n < 8; n++) {
				int value = ((vram.vram[address + y * 2] >> (7 - n)) & 1) |
						   (((vram.vram[address + y * 2 + 1] >> (7 - n)) & 1) << 1);
			
				int color = (value * 64) | ((value * 64) << 8) | ((value * 64) << 16);
				
				putPixel(color, screenX + n, screenY + y);
			}
		}
	}
	
	private void renderToFramebuffer() {
		boolean backgroundTileDataSelect = (registers.lcdControl & 0x10) != 0;
		int[] backgroundMap = (registers.lcdControl & 0x8) != 0 ? vram.background1 : vram.background0;
		
		for (int tileY = 0; tileY < 32; tileY++) {
			for (int tileX = 0; tileX < 32; tileX++) {
				int tileIndex = backgroundMap[tileX + tileY * 32];
				
				int tileAddress = tileIndex * 16;
				
				if (!backgroundTileDataSelect) {
					if (tileIndex >= 128) tileIndex -= 256;
					tileAddress = 0x1000 + tileIndex * 16;
				}
				
				putTile(tileAddress, tileX * 8 - registers.scrollX, tileY * 8 - registers.scrollY);
			}
		}
	}
	
	public PPURegisters getRegisters() {
		return registers;
	}

	public VRAM getVRAM() {
		return vram;
	}

	public int[] getFrameBuffer() {
		return framebuffer;
	}
	
}
