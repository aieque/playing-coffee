package playingcoffee.core.opcode;

import playingcoffee.core.MMU;
import playingcoffee.core.cpu.Registers;

public class PushOpcode implements Opcode {

	private final Argument register;
	
	public PushOpcode(Argument register) {
		this.register = register;
	}
	
	@Override
	public int run(Registers registers, MMU mmu) {
		int value = register.read(registers, mmu);
		
		mmu.pushStack(value, registers);
		
		//Log.info("Pushing 0x%4x to the stack.", value);
		
		return 8;
	}

	@Override
	public String toString() {
		return "PUSH " + register.getName();
	}
	
}
