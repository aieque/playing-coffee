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
		registers.decSP();
		mmu.write(registers.getPC() >> 8, registers.getSP());
		registers.decSP();
		mmu.write(registers.getPC(), registers.getSP());
		
		registers.setPC(address);
		
		return 12;
	}

}
