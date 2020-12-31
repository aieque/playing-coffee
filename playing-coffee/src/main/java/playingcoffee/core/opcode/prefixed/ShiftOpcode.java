package playingcoffee.core.opcode.prefixed;

import playingcoffee.core.MMU;
import playingcoffee.core.cpu.Flags;
import playingcoffee.core.cpu.Registers;
import playingcoffee.core.opcode.Argument;
import playingcoffee.core.opcode.Opcode;

public class ShiftOpcode implements Opcode {

	private ShiftType type;
	private Argument register;
	
	public ShiftOpcode(ShiftType type, Argument register) {
		this.type = type;
		this.register = register;
	}

	public enum ShiftType {
		SLA, SRA, SRL
	}

	@Override
	public int run(Registers registers, MMU mmu) {
		int value, result;
		
		switch (type) {
		case SLA:
			value = register.read(registers, mmu);
			result = value << 1;
			
			registers.getFlags().set(Flags.ZERO, result == 0);
			registers.getFlags().set(Flags.NEGATIVE | Flags.HALF_CARRY, false);
			registers.getFlags().set(Flags.CARRY, (value & 0x80) != 0);
			break;
		case SRA:
			value = register.read(registers, mmu);
			result = (value >> 1) | (value & 0x80);
			
			registers.getFlags().set(Flags.ZERO, result == 0);
			registers.getFlags().set(Flags.NEGATIVE | Flags.HALF_CARRY, false);
			registers.getFlags().set(Flags.CARRY, (value & 0x1) != 0);
			break;
		case SRL:
			value = register.read(registers, mmu);
			result = (value >> 1);
			
			registers.getFlags().set(Flags.ZERO, result == 0);
			registers.getFlags().set(Flags.NEGATIVE | Flags.HALF_CARRY, false);
			registers.getFlags().set(Flags.CARRY, (value & 0x1) != 0);
			break;
		}
		
		return register.getCycles() * 2;
	}
	
}
