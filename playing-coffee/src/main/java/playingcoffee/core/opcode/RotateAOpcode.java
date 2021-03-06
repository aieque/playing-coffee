package playingcoffee.core.opcode;

import playingcoffee.core.MMU;
import playingcoffee.core.cpu.Flags;
import playingcoffee.core.cpu.Registers;

public class RotateAOpcode implements Opcode {

	private boolean withCarry;
	private int direction;
	
	public static final int LEFT = -1;
	public static final int RIGHT = 1;
	
	public RotateAOpcode(boolean withCarry, int direction) {
		this.withCarry = withCarry;
		this.direction = direction;
	}

	@Override
	public int run(Registers registers, MMU mmu) {
		if (direction == LEFT) {
			if (withCarry) { // NOTE: Tetris does not use this opcode.
				int value = registers.getA();
				int result = (value << 1) | ((value & 0x80) >> 7);

				registers.getFlags().set(Flags.ZERO | Flags.NEGATIVE | Flags.HALF_CARRY, false);
				registers.getFlags().set(Flags.CARRY, (value & 0x80) != 0);
				
				registers.setA(result);
			} else { // NOTE: Tetris only uses this opcode.
				int value = registers.getA();
				int result = (value << 1) | (registers.getFlags().get(Flags.CARRY) ? 1 : 0);
				
				registers.getFlags().set(Flags.ZERO | Flags.NEGATIVE | Flags.HALF_CARRY, false);
				registers.getFlags().set(Flags.CARRY, (value & 0x80) != 0);
				
				registers.setA(result);				
			}
		} else { // NOTE: Tetris does not use this opcode.
			if (withCarry) {
				int value = registers.getA();
				int result = (value >> 1) | ((value & 1) << 7);

				registers.getFlags().set(Flags.ZERO | Flags.NEGATIVE | Flags.HALF_CARRY, false);
				registers.getFlags().set(Flags.CARRY, (value & 1) != 0);
				
				registers.setA(result);
			} else { // NOTE: Tetris does not use this opcode.
				int value = registers.getA();
				int result = (value >> 1) | ((registers.getFlags().get(Flags.CARRY) ? 0x80 : 0));
				
				registers.getFlags().set(Flags.ZERO | Flags.NEGATIVE | Flags.HALF_CARRY, false);
				registers.getFlags().set(Flags.CARRY, (value & 1) != 0);
				
				registers.setA(result);				
			}
		}
		
		return 0;
	}
	
	@Override
	public String toString() {
		return "R" + (direction == LEFT ? "L" : "R") + (withCarry ? "C" : "" + "A");
	}
	
}
