package playingcoffee.core.opcode.prefixed;

import playingcoffee.core.MMU;
import playingcoffee.core.cpu.Flags;
import playingcoffee.core.cpu.Registers;
import playingcoffee.core.opcode.Argument;
import playingcoffee.core.opcode.Opcode;

public class BitOpcode implements Opcode {

	private int bit;
	private Argument argument;
	
	public BitOpcode(int bit, Argument argument) {
		this.bit = bit;
		this.argument = argument;
	}
	
	@Override
	public int run(Registers registers, MMU mmu) {
		registers.getFlags().set(Flags.ZERO, (argument.read(registers, mmu) & (1 << bit)) == 0);
		registers.getFlags().set(Flags.NEGATIVE, false);
		registers.getFlags().set(Flags.HALF_CARRY, true);
		
		return argument.getCycles();
	}
	
	@Override
	public String toString() {
		return "BIT " + bit + ", " + argument.getName();
	}

}
