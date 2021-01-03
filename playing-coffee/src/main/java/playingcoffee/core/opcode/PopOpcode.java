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
		int value = mmu.popStack(registers);
		
		register.write(value, registers, mmu);
		
		//Log.info("Popping 0x%4x from the stack.", value);
		
		return 8;
	}

	@Override
	public String toString() {
		return "POP " + register.getName();
	}
	
}
