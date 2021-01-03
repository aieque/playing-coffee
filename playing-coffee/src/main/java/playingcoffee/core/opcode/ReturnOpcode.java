package playingcoffee.core.opcode;

import playingcoffee.core.MMU;
import playingcoffee.core.cpu.Flags;
import playingcoffee.core.cpu.Registers;
import playingcoffee.interrupt.InterruptManager;

public class ReturnOpcode implements Opcode {

	private int conditionFlag;
	private boolean not;
	private boolean fromInterupt;
	
	private final InterruptManager interruptManager;
	
	public ReturnOpcode(int conditionFlag, boolean not, boolean fromInterupt, final InterruptManager interruptManager) {
		this.conditionFlag = conditionFlag;
		this.not = not;
		this.fromInterupt = fromInterupt;
		this.interruptManager = interruptManager;
	}

	public boolean canExecute(Registers registers) {
		return (conditionFlag == 0) || (registers.getFlags().get(conditionFlag) ^ not);
	}
	
	@Override
	public int run(Registers registers, MMU mmu) {
		if (conditionFlag == 0) {
			int addressToJump = mmu.popStack(registers);
			registers.setPC(addressToJump);
			
			if (fromInterupt) {
				interruptManager.enable();
			}
			
			return 12;
		}
		
		if (canExecute(registers)) {
			int addressToJump = mmu.popStack(registers);
			//Log.info("Returning from 0x%4x to 0x%4x", registers.getPC(), addressToJump);
			
			registers.setPC(addressToJump);
			
			if (fromInterupt) {
				interruptManager.enable();
			}
			
			return 16;
		}
		
		return 4;
	}

	@Override
	public String toString() {
		if (conditionFlag == 0)
			return "RET" + (fromInterupt ? "I" : "");
		
		char flag = ' ';
		switch (conditionFlag) {
		case Flags.ZERO:       flag = 'Z'; break;
		case Flags.NEGATIVE:   flag = 'N'; break;
		case Flags.HALF_CARRY: flag = 'H'; break;
		case Flags.CARRY:      flag = 'C'; break;
		}
		
		return "RET" + (fromInterupt ? "I" : "") + " " + (not ? "" : "N") + flag;
	}

}
