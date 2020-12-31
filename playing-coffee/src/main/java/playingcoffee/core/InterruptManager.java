package playingcoffee.core;

import java.util.ArrayList;
import java.util.List;

public class InterruptManager implements MemorySpace {
	
	private boolean enabled;
	
	private int interruptEnable;
	private int interruptFlag;
	
	private List<InterruptListener> listeners;
	
	public static final int VBLANK = 1 << 0;
	public static final int LCD_STAT = 1 << 1;
	public static final int TIMER = 1 << 2;
	public static final int SERIAL = 1 << 3;
	public static final int JOYPAD = 1 << 4;
	
	public InterruptManager() {
		listeners = new ArrayList<InterruptListener>();
	}
	
	public void addListener(InterruptListener listener) {
		listeners.add(listener);
	}
	
	public void enable() { enabled = true; }
	public void disable() { enabled = false; }
	
	public boolean isEnabled() { return enabled; }
	
	public void requestInterrupt(int type) {
		interruptFlag |= type;
	}
	
	public void clock() {
		if (enabled) {
			int toOccur = interruptFlag & interruptEnable;
			
			//System.out.println(toOccur);
			
			if (toOccur != 0) {
				for (int i = 0; i < 8; i++) {
					if ((toOccur & (1 << i)) != 0) {
						for (InterruptListener listener : listeners) {
							listener.interruptOccured(1 << i);
						}
						interruptFlag &= ~(1 << i);
					}
				}
			}
		}
	}
	
	@Override
	public int read(int address) {
		if (address == 0xFFFF) return interruptEnable;
		if (address == 0xFF0F) return interruptFlag;
		
		return 0;
	}

	@Override
	public void write(int value, int address) {
		if (address == 0xFFFF) interruptEnable = value;
		if (address == 0xFF0F) interruptFlag = value;
	}

	@Override
	public boolean inMemorySpace(int address) {
		return address == 0xFFFF || address == 0xFF0F;
	}
	
}
