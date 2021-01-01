package playingcoffee.core.opcode.prefixed;

import playingcoffee.core.MMU;
import playingcoffee.core.cpu.Flags;
import playingcoffee.core.cpu.Registers;
import playingcoffee.core.opcode.Argument;
import playingcoffee.core.opcode.Opcode;

public class SwapOpcode implements Opcode {

	private Argument register;

	public SwapOpcode(Argument register) {
		this.register = register;
	}

	@Override
	public int run(Registers registers, MMU mmu) {
		int value = register.read(registers, mmu);
		int result = ((value & 0xF0) >> 4) | ((value & 0xF) << 4);
		
		register.write(result, registers, mmu);
		
		registers.getFlags().set(Flags.ZERO, result == 0);
		registers.getFlags().set(Flags.NEGATIVE | Flags.HALF_CARRY | Flags.CARRY, false);
		
		return register.getCycles() * 2;
	}

}
