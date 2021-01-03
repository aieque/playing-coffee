package playingcoffee.core.opcode;

import playingcoffee.core.MMU;
import playingcoffee.core.cpu.Flags;
import playingcoffee.core.cpu.Registers;
import playingcoffee.log.Log;

public enum Argument {

	// Note: The '_' prefix, specifies the value located at the memory address the register is pointing to.
	// Example: _HL <=> memory[HL]
	
	A {
		@Override
		public int read(Registers registers, MMU mmu) {
			return registers.getA();
		}

		@Override
		public void write(int value, Registers registers, MMU mmu) {
			registers.setA(value);
		}
	}, B {
		@Override
		public int read(Registers registers, MMU mmu) {
			return registers.getB();
		}

		@Override
		public void write(int value, Registers registers, MMU mmu) {
			registers.setB(value);
		}
	}, C {
		@Override
		public int read(Registers registers, MMU mmu) {
			return registers.getC();
		}

		@Override
		public void write(int value, Registers registers, MMU mmu) {
			registers.setC(value);
		}
	}, D {
		@Override
		public int read(Registers registers, MMU mmu) {
			return registers.getD();
		}

		@Override
		public void write(int value, Registers registers, MMU mmu) {
			registers.setD(value);
		}
	}, E {
		@Override
		public int read(Registers registers, MMU mmu) {
			return registers.getE();
		}

		@Override
		public void write(int value, Registers registers, MMU mmu) {
			registers.setE(value);
		}
	}, H {
		@Override
		public int read(Registers registers, MMU mmu) {
			return registers.getH();
		}

		@Override
		public void write(int value, Registers registers, MMU mmu) {
			registers.setH(value);
		}
	}, L {
		@Override
		public int read(Registers registers, MMU mmu) {
			return registers.getL();
		}

		@Override
		public void write(int value, Registers registers, MMU mmu) {
			registers.setL(value);
		}
	}, AF {
		@Override
		public int read(Registers registers, MMU mmu) {
			return registers.getAF();
		}

		@Override
		public void write(int value, Registers registers, MMU mmu) {
			registers.setAF(value);
		}
	}, BC {
		@Override
		public int read(Registers registers, MMU mmu) {
			return registers.getBC();
		}

		@Override
		public void write(int value, Registers registers, MMU mmu) {
			registers.setBC(value);
		}
	}, DE {
		@Override
		public int read(Registers registers, MMU mmu) {
			return registers.getDE();
		}

		@Override
		public void write(int value, Registers registers, MMU mmu) {
			registers.setDE(value);
		}
	}, HL {
		@Override
		public int read(Registers registers, MMU mmu) {
			return registers.getHL();
		}

		@Override
		public void write(int value, Registers registers, MMU mmu) {
			registers.setHL(value);
		}
	}, SP {
		@Override
		public int read(Registers registers, MMU mmu) {
			return registers.getSP();
		}

		@Override
		public void write(int value, Registers registers, MMU mmu) {
			registers.setSP(value);
		}
	}, _BC("(BC)", 4) {
		@Override
		public int read(Registers registers, MMU mmu) {
			return mmu.read(registers.getBC());
		}

		@Override
		public void write(int value, Registers registers, MMU mmu) {
			mmu.write(value, registers.getBC());
		}
	}, _DE("(DE)", 4) {
		@Override
		public int read(Registers registers, MMU mmu) {
			return mmu.read(registers.getDE());
		}

		@Override
		public void write(int value, Registers registers, MMU mmu) {
			mmu.write(value, registers.getDE());
		}
	}, _HL("(HL)", 4) {
		@Override
		public int read(Registers registers, MMU mmu) {
			return mmu.read(registers.getHL());
		}

		@Override
		public void write(int value, Registers registers, MMU mmu) {
			mmu.write(value, registers.getHL());
		}
	}, _HL_INC("(HL+)", 4) {
		@Override
		public int read(Registers registers, MMU mmu) {
			int value = mmu.read(registers.getHL());
			registers.setHL(registers.getHL() + 1);
			return value;
		}

		@Override
		public void write(int value, Registers registers, MMU mmu) {
			int address = registers.getHL();
			registers.setHL(registers.getHL() + 1);
			mmu.write(value, address);
		}
	}, _HL_DEC("(HL-)", 4) {
		@Override
		public int read(Registers registers, MMU mmu) {
			int value = mmu.read(registers.getHL());
			registers.setHL(registers.getHL() - 1);
			return value;
		}

		@Override
		public void write(int value, Registers registers, MMU mmu) {
			int address = registers.getHL();
			registers.setHL(registers.getHL() - 1);
			mmu.write(value, address);
		}
	}, D8(4) {
		@Override
		public int read(Registers registers, MMU mmu) {
			int value = mmu.read(registers.getPC());
			registers.incPC();
			
			return value;
		}

		@Override
		public void write(int value, Registers registers, MMU mmu) {
			Log.warn("Why are you writing to this argument?");
		}
	}, D16(8) {
		@Override
		public int read(Registers registers, MMU mmu) {
			int value = mmu.read(registers.getPC());
			
			registers.incPC();
			
			value |= mmu.read(registers.getPC()) << 8;
			
			registers.incPC();
			
			return value;
		}

		@Override
		public void write(int value, Registers registers, MMU mmu) {
			Log.warn("Why are you writing to this argument?");			
		}
	}, I8(4) {

		@Override
		public int read(Registers registers, MMU mmu) {
			byte relativeAddress = (byte) mmu.read(registers.getPC());
			
			registers.incPC();
			
			return relativeAddress;
		}

		@Override
		public void write(int value, Registers registers, MMU mmu) {
			Log.warn("Why are you writing to this argument?");
		}
		
	}, _C("(C)", 4) { // memory[0xFF00 + C]
		@Override
		public int read(Registers registers, MMU mmu) {
			int value = mmu.read(0xFF00 + registers.getC());
			
			return value;
		}

		@Override
		public void write(int value, Registers registers, MMU mmu) {
			mmu.write(value, 0xFF00 + registers.getC());
		}
	}, _D8("(D8)", 8) { // memory[0xFF00 + memory[PC++]]
		@Override
		public int read(Registers registers, MMU mmu) {
			int value = mmu.read(0xFF00 + mmu.read(registers.getPC()));
			
			registers.incPC();
			
			return value;
		}

		@Override
		public void write(int value, Registers registers, MMU mmu) {
			mmu.write(value, 0xFF00 + mmu.read(registers.getPC()));
			
			registers.incPC();
		}
	}, _D16("(D16)", 12) {
		@Override
		public int read(Registers registers, MMU mmu) {
			int address = mmu.read(registers.getPC());
			
			registers.incPC();
			
			address |= mmu.read(registers.getPC()) << 8;
			
			registers.incPC();
			
			return mmu.read(address);
		}

		@Override
		public void write(int value, Registers registers, MMU mmu) {
			int address = mmu.read(registers.getPC());
			
			registers.incPC();
			
			address |= mmu.read(registers.getPC()) << 8;
			
			registers.incPC();
			
			mmu.write(value, address);
		}
	}, _D16_SHORT("(D16 (16 bits))", 16) {
		@Override
		public int read(Registers registers, MMU mmu) {
			int address = mmu.read(registers.getPC());
			
			registers.incPC();
			
			address |= mmu.read(registers.getPC()) << 8;
			
			registers.incPC();
			
			return (mmu.read(address + 1) << 8) | mmu.read(address);
		}

		@Override
		public void write(int value, Registers registers, MMU mmu) {
			int address = mmu.read(registers.getPC());
			
			registers.incPC();
			
			address |= mmu.read(registers.getPC()) << 8;
			
			registers.incPC();
			
			mmu.write(value >> 8, address + 1);
			mmu.write(value, address);
		}
	}, SP_I8(8) {

		@Override
		public int read(Registers registers, MMU mmu) {
			int relativeAddress = (byte) mmu.read(registers.getPC());
			
			registers.incPC();
			
			int returnValue = registers.getSP() + relativeAddress;
			
			registers.getFlags().set(Flags.ZERO | Flags.NEGATIVE, false);
			registers.getFlags().set(Flags.HALF_CARRY, ((registers.getSP() ^ relativeAddress ^ returnValue) & 0x10) == 0x10);
			registers.getFlags().set(Flags.CARRY, ((registers.getSP() ^ relativeAddress ^ returnValue) & 0x100) == 0x100);
			
			return returnValue;
		}

		@Override
		public void write(int value, Registers registers, MMU mmu) {
			Log.error("Why are you writing to this argument?");
		}
		
	};

	private String name;
	private int cycles;
	
	Argument(String name, int cycles) {
		this.name = name;
		this.cycles = cycles;
	}
	
	Argument(int cycles) {
		this.name = name();
		this.cycles = cycles;
	}
	
	Argument() {
		this.name = name();
		this.cycles = 0;
	}
	
	public abstract int read(Registers registers, MMU mmu);
	public abstract void write(int value, Registers registers, MMU mmu);
	
	public String getName() {
		return name;
	}
	
	public int getCycles() {
		return cycles;
	}
}
