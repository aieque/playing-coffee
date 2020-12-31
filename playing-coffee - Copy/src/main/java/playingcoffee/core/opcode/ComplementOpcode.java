package playingcoffee.core.opcode;

import playingcoffee.core.MMU;
import playingcoffee.core.cpu.Flags;
import playingcoffee.core.cpu.Registers;

public class ComplementOpcode implements Opcode {

	@Override
	public int run(Registers registers, MMU mmu) {
		registers.setA(~registers.getA());
		
		registers.getFlags().set(Flags.NEGATIVE | Flags.HALF_CARRY, false);
		
		return 0;
	}

}
