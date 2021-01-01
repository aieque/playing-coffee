package playingcoffee.core.opcode;

import playingcoffee.core.MMU;
import playingcoffee.core.cpu.Registers;

public class RestartOpcode implements Opcode {

	private int address;
	
	public RestartOpcode(int address) {
		this.address = address;
	}

	@Override
	public int run(Registers registers, MMU mmu) {
		mmu.pushStack(registers.getPC(), registers);
		registers.setPC(address);
		
		return 12;
	}

}
