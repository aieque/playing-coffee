package playingcoffee.core.opcode;

import playingcoffee.core.MMU;
import playingcoffee.core.cpu.Flags;
import playingcoffee.core.cpu.Registers;

public class BCDOpcode implements Opcode {

	@Override
	public int run(Registers registers, MMU mmu) {
		// TODO: Learn what this opcode actually does.
		
		int s = registers.getA();
		
		if (registers.getFlags().get(Flags.NEGATIVE)) {
			if (registers.getFlags().get(Flags.HALF_CARRY)) s = (s - 0x06) & 0xFF;
			if (registers.getFlags().get(Flags.CARRY)) s = (s - 0x60) & 0xFFFF;
		} else {
			if (registers.getFlags().get(Flags.HALF_CARRY) || (s & 0xF) > 9) s = (s + 0x06) & 0xFFFF;
			if (registers.getFlags().get(Flags.CARRY) || s > 0x9F) s = (s + 0x60) & 0xFFFF;	
		}
		
		registers.setA(s);
		
		registers.getFlags().set(Flags.ZERO, registers.getA() == 0);
		registers.getFlags().set(Flags.HALF_CARRY, false);
		registers.getFlags().set(Flags.CARRY, s >= 0x100);
		
		return 0;
	}

}
