package playingcoffee.core;

import playingcoffee.interrupt.InterruptManager;

public abstract class Joypad implements MemorySpace {

	protected final InterruptManager interruptManager;
	
	protected Joypad(final InterruptManager interruptManager) {
		this.interruptManager = interruptManager;
	}
	
	@Override
	public boolean inMemorySpace(int address) {
		return address == 0xFF00;
	}
}
