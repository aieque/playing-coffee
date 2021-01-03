package playingcoffee.core.opcode;

import playingcoffee.core.MMU;
import playingcoffee.core.cpu.Flags;
import playingcoffee.core.cpu.Registers;

public class AddSPI8Opcode implements Opcode {

	@Override
	public int run(Registers registers, MMU mmu) {
		int value = Argument.I8.read(registers, mmu);
		int result = value + registers.getSP();
		
		registers.getFlags().set(Flags.ZERO | Flags.NEGATIVE, false);
		registers.getFlags().set(Flags.HALF_CARRY, ((registers.getSP() ^ value ^ result) & 0x10) == 0x10);
		registers.getFlags().set(Flags.CARRY, ((registers.getSP() ^ value ^ result) & 0x100) == 0x100);
		
		registers.setSP(result);
		
		return 12;
	}

	
	
}
