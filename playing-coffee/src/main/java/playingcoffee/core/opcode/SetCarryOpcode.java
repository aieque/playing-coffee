package playingcoffee.core.opcode;

import playingcoffee.core.MMU;
import playingcoffee.core.cpu.Flags;
import playingcoffee.core.cpu.Registers;

public class SetCarryOpcode implements Opcode {

	// NOTE: Tetris does not use this opcode.
	
	@Override
	public int run(Registers registers, MMU mmu) {
		registers.getFlags().set(Flags.NEGATIVE | Flags.HALF_CARRY, false);
		registers.getFlags().set(Flags.CARRY, true);
		
		return 0;
	}

}
