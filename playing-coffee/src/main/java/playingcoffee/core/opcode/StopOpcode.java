package playingcoffee.core.opcode;

import playingcoffee.core.MMU;
import playingcoffee.core.cpu.CPU;
import playingcoffee.core.cpu.Registers;

public class StopOpcode implements Opcode {

	// NOTE: Tetris does not use this opcode.
	
	private CPU cpu;
	
	public StopOpcode(CPU cpu) {
		this.cpu = cpu;
	}

	@Override
	public int run(Registers registers, MMU mmu) {
		cpu.enterLowPowerMode();
		
		registers.incPC();
		
		return 0;
	}

}
