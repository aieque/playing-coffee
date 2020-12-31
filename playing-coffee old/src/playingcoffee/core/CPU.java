// Basic CPU implementation.
// This will be changed in the future.

package playingcoffee.core;

public class CPU {

	private final MMU mmu;
	
	private Registers registers;
	
	private int cycles = 0;
	
	public static final int[] INSTRUCTION_CYCLE_COUNT = {
		4, 12, 8, 8, 4, 4, 8, 4, 20, 8, 8, 8, 4, 4, 8, 4,
		4, 12, 8, 8, 4, 4, 8, 4, 12, 8, 8, 8, 4, 4, 8, 4,
		8, 12, 8, 8, 12, 12, 12, 4, 8, 8, 8, 8, 4, 4, 8, 4,
		8, 12, 8, 8, 12, 12, 12, 4, 8, 8, 8, 8, 4, 4, 8, 4,
		4, 4, 4, 4, 4, 4, 8, 4, 4, 4, 4, 4, 4, 4, 8, 4,
		4, 4, 4, 4, 4, 4, 8, 4, 4, 4, 4, 4, 4, 4, 8, 4,
		4, 4, 4, 4, 4, 4, 8, 4, 4, 4, 4, 4, 4, 4, 8, 4,
		4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 8, 4,
		4, 4, 4, 4, 4, 4, 8, 4, 4, 4, 4, 4, 4, 4, 8, 4,
		4, 4, 4, 4, 4, 4, 8, 4, 4, 4, 4, 4, 4, 4, 8, 4,
		4, 4, 4, 4, 4, 4, 8, 4, 4, 4, 4, 4, 4, 4, 8, 4,
		4, 4, 4, 4, 4, 4, 8, 4, 4, 4, 4, 4, 4, 4, 8, 4,
		8, 12, 12, 16, 12, 16, 8, 16, 8, 16, 12, 4, 12, 24, 8, 16,
		8, 12, 12, -1, 12, 16, 8, 16, 8, 16, 12, -1, 12, -1, 8, 16,
		12, 12, 8, -1, -1, 16, 8, 16, 16, 4, 16, -1, -1, -1, 8, 16,
		12, 12, 8, 4, -1, 16, 8, 16, 12, 8, 16, 4, -1, -1, 8, 16
	};
	
	public static final int[] PREFIXED_CYCLE_COUNT = {
		8, 8, 8, 8, 8, 8, 16, 8, 8, 8, 8, 8, 8, 8, 16, 8,
	};
	
	public CPU(final MMU mmu) {
		this.mmu = mmu;
		
		registers = new Registers();
	}
	
	public Registers getRegisters() {
		return registers;
	}
	
	public int getCycles() {
		return cycles;
	}
	
	public void cycle() {
		if (cycles == 0) {
		
			int opcode = mmu.read(registers.pc++);
			System.out.printf("Fetched opcode: 0x%02x\n", opcode);
			
			decodeOpcode(opcode);
		}
		cycles--;
	}
	
	public void decodeOpcode(int opcode) {
		// Apply cycle count
		cycles += INSTRUCTION_CYCLE_COUNT[opcode];
		
		switch (opcode) {
		case 0x00: // NOP
			break;
		
		case 0x21: // LD HL, d16
			registers.writeL(mmu.read(registers.pc++));
			registers.writeH(mmu.read(registers.pc++));
			break;
			
		case 0x31: // LD SP, d16
			registers.sp = mmu.read(registers.pc++) | (mmu.read(registers.pc++) << 8);
			break;
		
		case 0x32: // LD (HL-), A
			mmu.write(registers.readA(), registers.readHL());
			registers.writeHL(registers.readHL() - 1);
			break;
			
		case 0xAF: // XOR A
			registers.writeA(registers.readA() ^ registers.readA());
			registers.setFlag(Registers.FLAG_Z, registers.readA() == 0);
			break;
		
		case 0xCB: // CB - prefix
			decodePrefixedOpcode(mmu.read(registers.pc++));
			break;
			
		default:
			System.err.printf("Unknown opcode: 0x%2x\n", opcode);
		}
	}
	
	public void decodePrefixedOpcode(int opcode) {
		cycles += INSTRUCTION_CYCLE_COUNT[opcode & 0xF];

		switch (opcode) {
		case 0x7C:
			registers.setFlag(Registers.FLAG_Z, (registers.readH() & (1 << 7)) == 0);
			registers.setFlag(Registers.FLAG_N, false);
			registers.setFlag(Registers.FLAG_H, true);
			break;
			
		default:
			System.err.printf("Unknown opcode: 0xcb%2x\n", opcode);
		}
	}
}