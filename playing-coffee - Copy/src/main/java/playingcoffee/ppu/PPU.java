package playingcoffee.ppu;

import playingcoffee.core.InterruptManager;
import playingcoffee.core.MMU;

public class PPU {

	private final MMU mmu;
	private final InterruptManager interruptManager;
	
	private PPURegisters registers;
	private VRAM vram;	
	
	private int clockCount = 0;
	
	public PPU(final MMU mmu, final InterruptManager interruptManager) {
		this.mmu = mmu;
		this.interruptManager = interruptManager;
		
		registers = new PPURegisters();
		vram = new VRAM();
		
		this.mmu.connectMemorySpace(registers);
		this.mmu.connectMemorySpace(vram);
	}
	
	public void OAMSeach() {
		registers.setLCDCMode(2);
	}
	
	public void pixelTransfer() {
		registers.setLCDCMode(3);		
	}
	
	public void HBlank() {
		registers.setLCDCMode(0);		
	}	
	
	public void VBlank() {
		registers.setLCDCMode(1);
	}
	
	public void clock() {
		registers.lcdcYCoord = clockCount / 114;
		
		if (clockCount == 114 * 144)
			interruptManager.requestInterrupt(InterruptManager.VBLANK);
		
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

	public PPURegisters getRegisters() {
		return registers;
	}

	public VRAM getVram() {
		return vram;
	}
	
}
