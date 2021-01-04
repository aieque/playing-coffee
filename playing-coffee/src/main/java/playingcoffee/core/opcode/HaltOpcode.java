package playingcoffee.core.opcode;

import playingcoffee.core.MMU;
import playingcoffee.core.cpu.CPU;
import playingcoffee.core.cpu.Registers;

public class HaltOpcode implements Opcode {

	// NOTE: Tetris does not use this opcode.
	
	private CPU cpu;
	
	public HaltOpcode(CPU cpu) {
		this.cpu = cpu;
	}

	@Override
	public int run(Registers registers, MMU mmu) {
		cpu.enterLowPowerMode();
		cpu.getInterruptManager().storePendingInterruptsBeforeHalt();
		
		return 0;
	}

}
