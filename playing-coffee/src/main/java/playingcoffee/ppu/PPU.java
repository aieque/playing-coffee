package playingcoffee.ppu;

import playingcoffee.core.MMU;
import playingcoffee.interrupt.InterruptManager;
import playingcoffee.ppu.mode.HBlankMode;
import playingcoffee.ppu.mode.OAMSearchMode;
import playingcoffee.ppu.mode.PPUMode;
import playingcoffee.ppu.mode.PixelTransferMode;
import playingcoffee.ppu.mode.VBlankMode;

public class PPU {

	public static final int NONE = -1;
	public static final int HBLANK = 0;
	public static final int VBLANK = 1;
	public static final int OAM_SEARCH = 2;
	public static final int PIXEL_TRANSFER = 3;
	
	private PPUMode[] modes;
	private int currentMode = OAM_SEARCH;
	
	private final MMU mmu;
	private final InterruptManager interruptManager;
	
	private PPURegisters registers;
	private OAM oam;
	private VRAM vram;
	
	private int[] framebuffer;

	public PPU(final MMU mmu, final InterruptManager interruptManager) {
		modes = new PPUMode[4];
		
		modes[HBLANK] = new HBlankMode();
		modes[VBLANK] = new VBlankMode();
		modes[OAM_SEARCH] = new OAMSearchMode();
		modes[PIXEL_TRANSFER] = new PixelTransferMode();
	
		this.mmu = mmu;
		this.interruptManager = interruptManager;
	
		registers = new PPURegisters(this);
		oam = new OAM();
		vram = new VRAM();
		
		connectMemorySpaces();
	}
	
	private void connectMemorySpaces() {
		mmu.connectMemorySpace(oam);
		mmu.connectMemorySpace(registers);
		mmu.connectMemorySpace(vram);
	}
	
	private void setMode(int modeValue) {
		currentMode = modeValue;
		registers.setLCDCMode(modeValue);
		
		PPUMode mode = modes[currentMode];
		
		mode.start(this);
		checkLCDCStatusInterrupt();
		
		checkVBlankInterrupt();
	}
	
	private void checkLCDCStatusInterrupt() {
		PPUMode mode = modes[currentMode];
		
		if (mode.firesLCDCStatusInterrupt()) {
			int lcdcStatus = registers.lcdcStatus;
			
			/*  From Pan Docs
			 	
				Bit 5 - Mode 2 OAM Interrupt         (1=Enable) (Read/Write)
				Bit 4 - Mode 1 V-Blank Interrupt     (1=Enable) (Read/Write)
				Bit 3 - Mode 0 H-Blank Interrupt     (1=Enable) (Read/Write)
			
				Therefore we can get the correct bit by simply taking the current mode and adding three to it.
			*/
			int bitToCheck = 1 << (3 + currentMode);
			
			if ((lcdcStatus & bitToCheck) != 0) {
				interruptManager.requestInterrupt(InterruptManager.LCDC_STAT);
			}
		}
	}
	
	private void checkVBlankInterrupt() {
		PPUMode mode = modes[currentMode];
		
		if (mode instanceof VBlankMode) {
			interruptManager.requestInterrupt(InterruptManager.VBLANK);
		}
	}

	public void clock() {
		PPUMode mode = modes[currentMode];
		
		mode.clock(this);
		
		int nextMode = mode.nextMode();
		
		if (nextMode != NONE) {
			setMode(nextMode);
		}
	}
	
	public MMU getMMU() {
		return mmu;
	}
	
	public PPURegisters getRegisters() {
		return registers;
	}
	
	public OAM getOAM() {
		return oam;
	}
	
	public VRAM getVRAM() {
		return vram;
	}
	
	public int[] getFramebuffer() {
		return framebuffer;
	}
}
