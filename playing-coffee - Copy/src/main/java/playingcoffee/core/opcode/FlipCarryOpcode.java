package playingcoffee.core.opcode;

import playingcoffee.core.MMU;
import playingcoffee.core.cpu.Flags;
import playingcoffee.core.cpu.Registers;

public class FlipCarryOpcode implements Opcode {

	@Override
	public int run(Registers registers, MMU mmu) {
		registers.getFlags().set(Flags.NEGATIVE | Flags.HALF_CARRY, false);
		registers.getFlags().set(Flags.CARRY, !registers.getFlags().get(Flags.CARRY));
		
		return 0;
	}

}
