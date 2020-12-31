package playingcoffee.core.opcode;

import playingcoffee.core.MMU;
import playingcoffee.core.cpu.Registers;

public interface Opcode {

	public int run(Registers registers, MMU mmu);
	
}
