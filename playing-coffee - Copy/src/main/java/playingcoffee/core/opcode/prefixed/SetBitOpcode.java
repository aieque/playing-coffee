package playingcoffee.core.opcode.prefixed;

import playingcoffee.core.MMU;
import playingcoffee.core.cpu.Registers;
import playingcoffee.core.opcode.Argument;
import playingcoffee.core.opcode.Opcode;

public class SetBitOpcode implements Opcode {

	private int bit;
	private Argument argument;
	
	public SetBitOpcode(int bit, Argument argument) {
		this.bit = bit;
		this.argument = argument;
	}

	@Override
	public int run(Registers registers, MMU mmu) {
		argument.write(argument.read(registers, mmu) | (1 << bit), registers, mmu);
		
		return argument.getCycles() * 2;
	}
	
}
