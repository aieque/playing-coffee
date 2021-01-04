package playingcoffee.ppu;

import playingcoffee.core.MMU;
import playingcoffee.interrupt.InterruptManager;
import playingcoffee.ppu.OAM.OAMEntry;

public class PPUOld {

	private final InterruptManager interruptManager;
	final MMU mmu;
	
	private PPURegisters registers;
	private VRAM vram;
	private OAM oam;
	
	private int clockCount = 0;
	
	private int[] framebuffer;
	
	// TODO: Remove
	private int screenXs[];
	
	public static final int HBLANK = 0;
	public static final int VBLANK = 1;
	public static final int OAM_SEARCH = 2;
	public static final int PIXEL_TRANSFER = 3;
	
	public PPUOld(final MMU mmu, final InterruptManager interruptManager) {
		this.mmu = mmu;
		this.interruptManager = interruptManager;
		
//		registers = new PPURegisters(this);
		vram = new VRAM();
		oam = new OAM();
		
		framebuffer = new int[160 * 144];
		
		screenXs = new int[144];
		
		this.mmu.connectMemorySpace(registers);
		this.mmu.connectMemorySpace(vram);
		this.mmu.connectMemorySpace(oam);
	}
	
	public void onLCDDisable() {
		clockCount = 0;
	}
	
	public void OAMSeach() {}
	
	public void pixelTransfer() {

	}
	
	public void HBlank() { screenXs[registers.ly] = registers.scx; }
	
	public void VBlank() {}
	
	public void clock() {
		registers.ly = clockCount / 456;
		
		if (clockCount == 456 * 144) {
			interruptManager.requestInterrupt(InterruptManager.VBLANK);
			renderToFramebuffer();

			registers.setLCDCMode(VBLANK);

			if ((registers.lcdcStatus & 0x10) != 0) interruptManager.requestInterrupt(InterruptManager.LCDC_STAT);
		}
		
		if (clockCount % 456 == 80 && registers.getLCDCMode() != VBLANK) registers.setLCDCMode(PIXEL_TRANSFER);
		
		if (clockCount % 456 == 240 && registers.getLCDCMode() != VBLANK) {
			registers.setLCDCMode(HBLANK);
			
			if ((registers.lcdcStatus & 0x8) != 0) interruptManager.requestInterrupt(InterruptManager.LCDC_STAT);
		}
		if (clockCount % 456 == 0 && registers.getLCDCMode() != VBLANK) {
			registers.setLCDCMode(OAM_SEARCH);

			if ((registers.lcdcStatus & 0x20) != 0) interruptManager.requestInterrupt(InterruptManager.LCDC_STAT);
		}

		switch (registers.getLCDCMode()) {
			case OAM_SEARCH: OAMSeach(); break;
			case PIXEL_TRANSFER: pixelTransfer(); break;
			case HBLANK: HBlank(); break;
			case VBLANK: VBlank(); break;
		}
		
		clockCount++;
		
		if (clockCount == 456 * (144 + 10)) {
			registers.setLCDCMode(OAM_SEARCH);
			clockCount = 0;
		}
		
		if (registers.ly == registers.lyc) {
			registers.lcdcStatus |= 0x4;
		} else {
			registers.lcdcStatus &= ~0x4;
		}
	}

	private void putPixel(int pixel, int x, int y) {
		if (x >= 0 && x < 160 && y >= 0 && y < 144) framebuffer[x + y * 160] = pixel;
	}
	
	private void putTile(int address, int screenX, int screenY, boolean window) {
		int palette = mmu.read(0xFF47);
		
		for (int y = 0; y < 8; y++) {
			for (int n = 0; n < 8; n++) {
				int value = ((vram.vram[address + y * 2] >> (7 - n)) & 1) |
						   (((vram.vram[address + y * 2 + 1] >> (7 - n)) & 1) << 1);
			
				value = (palette >> (value * 2)) & 0x3;
				
				int color = 0xFFFFFF - ((value * 64) | ((value * 64) << 8) | ((value * 64) << 16));
				
				if (window)
					putPixel(color, screenX + n, screenY + y);
				else
					putPixel(color, (screenX + n) & 255, (screenY + y) & 255);
			}
		}
	}
	
	private void putSprite(int address, int screenX, int screenY, int paletteIndex, boolean flipX, boolean flipY) {
		int palette = mmu.read(0xFF48 + paletteIndex);

		for (int y = 0; y < 8; y++) {
			for (int n = 0; n < 8; n++) {
				int value = ((vram.vram[address + y * 2] >> (7 - n)) & 1) |
						   (((vram.vram[address + y * 2 + 1] >> (7 - n)) & 1) << 1);

				if (value != 0) {
			
					value = (palette >> (value * 2)) & 0x3;

					int color = 0xFFFFFF - ((value * 64) | ((value * 64) << 8) | ((value * 64) << 16));
					
					putPixel(color, screenX + (flipX ? (7 - n) : n), screenY + (flipY ? (7 - y) : y));
				}
			}
		}
		
		if ((registers.lcdControl & 0x4) != 0) { // 8x16 Mode
			address += 16;
			
			for (int y = 0; y < 8; y++) {
				for (int n = 0; n < 8; n++) {
					int value = ((vram.vram[address + y * 2] >> (7 - n)) & 1) |
							   (((vram.vram[address + y * 2 + 1] >> (7 - n)) & 1) << 1);

					if (value != 0) {
				
						value = (palette >> (value * 2)) & 0x3;

						int color = 0xFFFFFF - ((value * 64) | ((value * 64) << 8) | ((value * 64) << 16));
						
						putPixel(color, screenX + (flipX ? (7 - n) : n), screenY + (flipY ? (7 - y) : y) + 8);
					}
				}
			}
		}
	}
	
	private void renderToFramebuffer() {
		boolean backgroundTileDataSelect = (registers.lcdControl & 0x10) != 0;
		int[] backgroundMap = (registers.lcdControl & 0x8) != 0 ? vram.background1 : vram.background0;
		int[] windowMap     = (registers.lcdControl & 0x8) == 0 ? vram.background1 : vram.background0;
		
		for (int tileY = 0; tileY < 32; tileY++) {
			for (int tileX = 0; tileX < 32; tileX++) {
				int tileIndex = backgroundMap[tileX + tileY * 32];
				
				int tileAddress = tileIndex * 16;
				
				if (!backgroundTileDataSelect) {
					if (tileIndex >= 128) tileIndex -= 256;
					tileAddress = 0x1000 + tileIndex * 16;
				}
				
				if (tileY * 8 - registers.scy >= 0 && tileY * 8 - registers.scy < 144)
					putTile(tileAddress, tileX * 8 - screenXs[tileY * 8 - registers.scy], tileY * 8 - registers.scy, false);
			}
		}
		
		// Render Window
		if ((registers.lcdControl & 0x20) != 0) {
			for (int tileY = 0; tileY < 32; tileY++) {
				for (int tileX = 0; tileX < 32; tileX++) {
					int tileIndex = windowMap[tileX + tileY * 32];
					
					int tileAddress = tileIndex * 16;
					
					if (!backgroundTileDataSelect) {
						if (tileIndex >= 128) tileIndex -= 256;
						tileAddress = 0x1000 + tileIndex * 16;
					}
					
					putTile(tileAddress, tileX * 8 + registers.wx - 4, tileY * 8 + registers.wy, true);
				}
			}
		}
		
		// Render sprites (Objects)
		for (int i = 0; i < 40; i++) {
			OAMEntry entry = oam.entries[i];
			
			int address = entry.tileNumber * 16;
			
			int palette = (entry.flags & 0x10) >> 4;
			
			boolean behindBG = (entry.flags & 0x80) != 0;
			boolean flipX = (entry.flags & 0x20) != 0;
			boolean flipY = (entry.flags & 0x40) != 0;

			if (!behindBG)
			putSprite(address, entry.x - 8, entry.y - 16, palette, flipX, flipY);
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
