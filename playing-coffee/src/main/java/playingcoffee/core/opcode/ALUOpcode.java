package playingcoffee.core.opcode;

import playingcoffee.core.MMU;
import playingcoffee.core.cpu.Flags;
import playingcoffee.core.cpu.Registers;
import playingcoffee.log.Log;

public class ALUOpcode implements Opcode {

	private final ALUType type;
	private final Argument register;
	
	public ALUOpcode(ALUType type, Argument register) {
		this.type = type;
		this.register = register;
	}

	@Override
	public int run(Registers registers, MMU mmu) {
		int result = 0;
		int registerValue = register.read(registers, mmu);
		
		switch (type) {
		case ADD:
			result = registers.getA() + registerValue;
			
			registers.getFlags().set(Flags.ZERO, (result & 0xFF) == 0);
			registers.getFlags().set(Flags.NEGATIVE, false);
			registers.getFlags().set(Flags.HALF_CARRY, (registers.getA() & 0xF) + (registerValue & 0xF) > 0xF);
			registers.getFlags().set(Flags.CARRY, (result & 0xFF00) > 0);
			
			break;
		case ADC: // NOTE: Tetris does not use this opcode.
			int carry = (registers.getFlags().get(Flags.CARRY) ? 1 : 0);
			int value = registerValue;
			
			result = registers.getA() + value + carry;
			
			registers.getFlags().set(Flags.ZERO, (result & 0xFF) == 0);
			registers.getFlags().set(Flags.NEGATIVE, false); // Cinoop sets this to true?
			registers.getFlags().set(Flags.HALF_CARRY, (registers.getA() & 0xF) + (registerValue & 0xF) + carry > 0xF);
			registers.getFlags().set(Flags.CARRY, (result & 0xFF00) > 0);
			
			break;
		case AND:
			result = registers.getA() & registerValue;
			
			registers.getFlags().set(Flags.ZERO, (result & 0xFF) == 0);
			registers.getFlags().set(Flags.NEGATIVE, false);
			registers.getFlags().set(Flags.HALF_CARRY, true);
			registers.getFlags().set(Flags.CARRY, false);
			
			break;
		case CP:
			registers.getFlags().set(Flags.ZERO, registers.getA() == registerValue);
			registers.getFlags().set(Flags.NEGATIVE, true);
			registers.getFlags().set(Flags.HALF_CARRY, (registerValue & 0xF) > (registers.getA() & 0xF));
			registers.getFlags().set(Flags.CARRY, registerValue > registers.getA());
			
			break;
		case OR:
			result = registers.getA() | registerValue;
			
			registers.getFlags().set(Flags.ZERO, (result & 0xFF) == 0);
			registers.getFlags().set(Flags.NEGATIVE | Flags.HALF_CARRY | Flags.CARRY, false);
			
			break;
		case SBC: // NOTE: Tetris does not use this opcode.
			carry = (registers.getFlags().get(Flags.CARRY) ? 1 : 0);
			value = registerValue;
			
			result = registers.getA() - value - carry;
			
			registers.getFlags().set(Flags.ZERO, (result & 0xFF) == 0);
			registers.getFlags().set(Flags.NEGATIVE, true);
			registers.getFlags().set(Flags.HALF_CARRY, (registers.getA() & 0xF) - (value & 0xF) - carry < 0);
			registers.getFlags().set(Flags.CARRY, result < 0);

			break;
		case SUB:
			registers.getFlags().set(Flags.ZERO, registerValue == registers.getA());
			registers.getFlags().set(Flags.NEGATIVE, true);
			registers.getFlags().set(Flags.HALF_CARRY, (registerValue & 0xF) > (registers.getA() & 0xF));
			registers.getFlags().set(Flags.CARRY, registerValue > registers.getA());
			
			result = registers.getA() - registerValue;
			
			break;
		case XOR:
			result = registers.getA() ^ registerValue;
			
			registers.getFlags().set(Flags.ZERO, (result & 0xFF) == 0);
			registers.getFlags().set(Flags.NEGATIVE | Flags.HALF_CARRY | Flags.CARRY, false);
			
			break;
		case INC:
			result = registerValue + 1;
			
			registers.getFlags().set(Flags.ZERO, (result & 0xFF) == 0);
			registers.getFlags().set(Flags.NEGATIVE, false);
			registers.getFlags().set(Flags.HALF_CARRY, (registerValue & 0xF) == 0xF);
			
			register.write(result, registers, mmu);
			return register.getCycles() * 2;
		case DEC:
			result = registerValue - 1;
			
			registers.getFlags().set(Flags.ZERO, (result & 0xFF) == 0);
			registers.getFlags().set(Flags.NEGATIVE, true);
			registers.getFlags().set(Flags.HALF_CARRY, (result & 0x0F) == 0x0F);
			
			register.write(result, registers, mmu);
			return register.getCycles() * 2;
		default:
			Log.error("wtf!? how did we get here?!?!?");
			break;
		}
		
		if (type != ALUType.INC && type != ALUType.DEC && type != ALUType.CP)
			registers.setA(result);
		
		return register.getCycles();
	}

	public enum ALUType {
		ADD, ADC, SUB, SBC, AND, XOR, OR, CP, INC, DEC
	}
	
	@Override
	public String toString() {
		return type.name() + " A, " + register.getName();
	}
	
}
