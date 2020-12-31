package playingcoffee.core.opcode;

import playingcoffee.core.MMU;
import playingcoffee.core.cpu.Flags;
import playingcoffee.core.cpu.Registers;

public class JumpRelativeOpcode implements Opcode {

	private int conditionFlag;
	private Argument address;
	private boolean not;
	
	public JumpRelativeOpcode(int conditionFlag, Argument address, boolean not) {
		this.conditionFlag = conditionFlag;
		this.address = address;
		this.not = not;
	}
	
	public boolean canExecute(Registers registers) {
		return (conditionFlag == 0) || (registers.getFlags().get(conditionFlag) ^ not);
	}
	
	@Override
	public int run(Registers registers, MMU mmu) {
		int relativeValue = address.read(registers, mmu);
		
		int addressToJump = registers.getPC() + relativeValue;
		if (canExecute(registers)) {
			registers.setPC(addressToJump);
			return address.getCycles() + 4;
		}
		
		return address.getCycles();
	}

	@Override
	public String toString() {
		if (conditionFlag == 0)
			return "JR " + address.getName();
		
		char flag = ' ';
		switch (conditionFlag) {
		case Flags.ZERO:       flag = 'Z'; break;
		case Flags.NEGATIVE:   flag = 'N'; break;
		case Flags.HALF_CARRY: flag = 'H'; break;
		case Flags.CARRY:      flag = 'C'; break;
		}
		
		return "JR " + (not ? "" : "N") + flag + ", " + address.getName();
	}
}
