package playingcoffee.core.opcode;

import playingcoffee.core.MMU;
import playingcoffee.core.cpu.Registers;

public class PopOpcode implements Opcode {

	private final Argument register;
	
	public PopOpcode(Argument register) {
		this.register = register;
	}
	
	@Override
	public int run(Registers registers, MMU mmu) {
		int value = mmu.read(registers.getSP());
		registers.incSP();
		
		value |= mmu.read(registers.getSP()) << 8;
		registers.incSP();
		
		register.write(value, registers, mmu);
		
		return 8;
	}

	@Override
	public String toString() {
		return "POP " + register.getName();
	}
	
}
