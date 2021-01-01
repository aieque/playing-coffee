package playingcoffee.core.opcode;

import playingcoffee.core.MMU;
import playingcoffee.core.cpu.Flags;
import playingcoffee.core.cpu.Registers;
import playingcoffee.log.Log;

public class ALU16Opcode implements Opcode {

	private final ALU16Type type;
	private final Argument register;
	
	public ALU16Opcode(ALU16Type type, Argument register) {
		this.type = type;
		this.register = register;
	}

	@Override
	public int run(Registers registers, MMU mmu) {
		int result = 0;
		
		switch (type) {
		case INC:
			register.write(register.read(registers, mmu) + 1, registers, mmu);
			break;
		case DEC:
			register.write(register.read(registers, mmu) - 1, registers, mmu);
			break;
		case ADD:
			int value = register.read(registers, mmu);
			result = registers.getHL() + value;
			
			registers.getFlags().set(Flags.NEGATIVE, false);
			registers.getFlags().set(Flags.HALF_CARRY, ((value & 0xF0) + (registers.getHL() & 0xF0) > 0xF0));
			registers.getFlags().set(Flags.CARRY, (result & 0xFFFF0000) != 0);
			
			registers.setHL(result);
			
			break;
		default:
			Log.error("wtf!? how did we get here?!?!?");
			break;
		}
		
		return register.getCycles() * (type == ALU16Type.ADD ? 1 : 2);
	}

	public enum ALU16Type {
		INC, DEC, ADD
	}
	
	@Override
	public String toString() {
		return type.name() + " " + register.getName();
	}
	
}
