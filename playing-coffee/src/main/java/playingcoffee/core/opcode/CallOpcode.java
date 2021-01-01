package playingcoffee.core.opcode;

import playingcoffee.core.MMU;
import playingcoffee.core.cpu.Flags;
import playingcoffee.core.cpu.Registers;
import playingcoffee.log.Log;

public class CallOpcode implements Opcode {

	private int conditionFlag;
	private Argument address;
	private boolean not;
	
	public CallOpcode(int conditionFlag, Argument address, boolean not) {
		this.conditionFlag = conditionFlag;
		this.address = address;
		this.not = not;
	}
	
	public boolean canExecute(Registers registers) {
		return (conditionFlag == 0) || (registers.getFlags().get(conditionFlag) ^ not);
	}
	
	@Override
	public int run(Registers registers, MMU mmu) {
		int addressToJump = address.read(registers, mmu);
		
		if (canExecute(registers)) {
			mmu.pushStack(registers.getPC(), registers);
			
			Log.info("Pushing 0x%4x to the stack.", registers.getPC());
			
			registers.setPC(addressToJump);
			
			return 12 + address.getCycles(); // 2 memory writes and additional cycle
		}
		
		return address.getCycles();
	}

	@Override
	public String toString() {
		if (conditionFlag == 0)
			return "CALL " + address.getName();
		
		char flag = ' ';
		switch (conditionFlag) {
		case Flags.ZERO:       flag = 'Z'; break;
		case Flags.NEGATIVE:   flag = 'N'; break;
		case Flags.HALF_CARRY: flag = 'H'; break;
		case Flags.CARRY:      flag = 'C'; break;
		}
		
		return "CALL " + (not ? "" : "N") + flag + ", " + address.getName();
	}

}
