package playingcoffee.interrupt;

import playingcoffee.core.MemorySpace;

public class Timer implements MemorySpace {

	private final InterruptManager interruptManager;
	
	private int dividerRegister;
	private int timerCounter;
	private int timerModulo;
	private int timerControl;
	
	private int dividerClockCount;
	private int timerClockCount;
	
	public Timer(final InterruptManager interruptManager) {
		this.interruptManager = interruptManager;
	}

	public void clock() {
		if (dividerClockCount % 256 == 0) {
			dividerRegister = (dividerRegister + 1) & 0xFF;
		}
		
		dividerClockCount++;
		
		if ((timerControl & 0x4) != 0) {
			int timerMod = 0;

			switch (timerControl & 0x3) {
				case 0x00: timerMod = 1024; break;
				case 0x01: timerMod = 16; break;
				case 0x02: timerMod = 64; break;
				case 0x03: timerMod = 256; break;
			}
			
			if (timerClockCount % timerMod == 0) {
				timerCounter++;
				if (timerCounter > 0xFF) {
					timerCounter = timerModulo;
					interruptManager.requestInterrupt(InterruptManager.TIMER);
				}
			}
			timerClockCount++;
		}
	}
	
	@Override
	public int read(int address) {
		switch (address) {
			case 0xFF04: return dividerRegister;
			case 0xFF05: return timerCounter;
			case 0xFF06: return timerModulo;
			case 0xFF07: return timerControl;
		}

		return 0;
	}

	@Override
	public void write(int value, int address) {
		switch (address) {
			case 0xFF04: dividerRegister = 0; return;
			case 0xFF05: timerCounter = value; return;
			case 0xFF06: timerModulo = value; return;
			case 0xFF07: timerControl = value; return;
		}
	}

	@Override
	public boolean inMemorySpace(int address) {
		return address >= 0xFF04 && address <= 0xFF07;
	}

}
