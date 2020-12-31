package playingcoffee.core.cpu;

import playingcoffee.core.MMU;

public enum Argument {
	
	A {
		@Override
		public int read(Registers registers, MMU mmu) {
			return registers.getA();
		}

		@Override
		public void write(Registers registers, MMU mmu, int value) {
			registers.setA(value);
		}
	}, B {
		@Override
		public int read(Registers registers, MMU mmu) {
			return registers.getB();
		}

		@Override
		public void write(Registers registers, MMU mmu, int value) {
			registers.setB(value);
		}
	}, C {
		@Override
		public int read(Registers registers, MMU mmu) {
			return registers.getC();
		}

		@Override
		public void write(Registers registers, MMU mmu, int value) {
			registers.setC(value);
		}
	}, D {
		@Override
		public int read(Registers registers, MMU mmu) {
			return registers.getD();
		}

		@Override
		public void write(Registers registers, MMU mmu, int value) {
			registers.setD(value);
		}
	}, E {
		@Override
		public int read(Registers registers, MMU mmu) {
			return registers.getE();
		}

		@Override
		public void write(Registers registers, MMU mmu, int value) {
			registers.setE(value);
		}
	};
	
	private String label;
	
	public abstract int read(Registers registers, MMU mmu);
	public abstract void write(Registers registers, MMU mmu, int value);
	
	public Argument parse(String argument) {
		for (Argument arg : values())
			if (arg.label.equals(argument))
				return arg;
		
		throw new IllegalArgumentException(String.format("Invalid argument: %s", argument));
	}
	
}
