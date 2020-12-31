package playingcoffee.core.opcode;

import playingcoffee.core.MMU;
import playingcoffee.core.cpu.Registers;

public class LoadOpcode implements Opcode {

	private Argument from, to;
	
	public LoadOpcode(Argument from, Argument to) {
		this.from = from;
		this.to = to;
	}
	
	@Override
	public int run(Registers registers, MMU mmu) {
		to.write(from.read(registers, mmu), registers, mmu);
		
		return to.getCycles() + from.getCycles();
	}
	
	@Override
	public String toString() {
		return "LD " + to.getName() + ", " + from.getName();
	}
}
