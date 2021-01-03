package playingcoffee.core.opcode.prefixed;

import playingcoffee.core.MMU;
import playingcoffee.core.cpu.Flags;
import playingcoffee.core.cpu.Registers;
import playingcoffee.core.opcode.Argument;
import playingcoffee.core.opcode.Opcode;

public class RotateOpcode implements Opcode {

	private boolean withCarry;
	private Argument register;
	private int direction;
	
	public static final int LEFT = -1;
	public static final int RIGHT = 1;
	
	public RotateOpcode(boolean withCarry, Argument register, int direction) {
		this.withCarry = withCarry;
		this.register = register;
		this.direction = direction;
	}

	@Override
	public int run(Registers registers, MMU mmu) {
		if (direction == LEFT) {
			/*int value = register.read(registers, mmu);
			int result = (value << 1) | (!withCarry ? (registers.getFlags().get(Flags.CARRY) ? 1 : 0) : (value >> 7) & 1);
			
			registers.getFlags().set(Flags.ZERO, result == 0);
			registers.getFlags().set(Flags.NEGATIVE | Flags.HALF_CARRY, false);
			registers.getFlags().set(Flags.CARRY, (value & 0x80) != 0);
			
			register.write(result, registers, mmu);*/
			
			if (withCarry) { // NOTE: Tetris does not use this opcode.
				int value = register.read(registers, mmu);
				int result = (value << 1) | ((value >> 7) & 1);

				registers.getFlags().set(Flags.ZERO, result == 0);
				registers.getFlags().set(Flags.NEGATIVE | Flags.HALF_CARRY, false);
				registers.getFlags().set(Flags.CARRY, (value & 0x80) != 0);

				register.write(result, registers, mmu);
			} else {
				int value = register.read(registers, mmu);
				int result = (value << 1) | (registers.getFlags().get(Flags.CARRY) ? 1 : 0);
				
				registers.getFlags().set(Flags.ZERO, result == 0);
				registers.getFlags().set(Flags.NEGATIVE | Flags.HALF_CARRY, false);
				registers.getFlags().set(Flags.CARRY, (value & 0x80) != 0);
				
				register.write(result, registers, mmu);
			}
			
		} else { // NOTE: Tetris does not use this opcode.
			/*int value = register.read(registers, mmu);
			int result = (value >> 1) | ((!withCarry ? (registers.getFlags().get(Flags.CARRY) ? 1 : 0) : (value & 0x1)) << 7);
			
			registers.getFlags().set(Flags.ZERO, result == 0);
			registers.getFlags().set(Flags.NEGATIVE | Flags.HALF_CARRY, false);
			registers.getFlags().set(Flags.CARRY, (value & 1) != 0);
			
			register.write(result, registers, mmu);*/
			
			if (withCarry) { // NOTE: Tetris does not use this opcode.
				int value = register.read(registers, mmu);
				int result = (value >> 1) | ((value & 0x1) << 7);
				
				registers.getFlags().set(Flags.ZERO, result == 0);
				registers.getFlags().set(Flags.NEGATIVE | Flags.HALF_CARRY, false);
				registers.getFlags().set(Flags.CARRY, (value & 0x1) != 0); // NOTE: Cinoop has result & 0x1 instead.
				
				register.write(result, registers, mmu);
			} else { // NOTE: Tetris does not use this opcode.
				int value = register.read(registers, mmu);
				int result = (value >> 1) | (registers.getFlags().get(Flags.CARRY) ? 0x80 : 0);
				
				registers.getFlags().set(Flags.ZERO, result == 0);
				registers.getFlags().set(Flags.NEGATIVE | Flags.HALF_CARRY, false);
				registers.getFlags().set(Flags.CARRY, (value & 0x1) != 0); // NOTE: Cinoop has result & 0x1 instead.
				
				register.write(result, registers, mmu);
			}
		}
		
		return register.getCycles();
	}
	
	@Override
	public String toString() {
		return "R" + (direction == LEFT ? "L" : "R") + (withCarry ? "C " : " " + register.getName());
	}
}
