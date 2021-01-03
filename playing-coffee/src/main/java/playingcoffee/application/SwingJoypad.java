package playingcoffee.application;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import playingcoffee.core.Joypad;
import playingcoffee.interrupt.InterruptManager;

public class SwingJoypad extends Joypad implements KeyListener {

	public static final int BUTTON_LEFT   = KeyEvent.VK_LEFT;
	public static final int BUTTON_RIGHT  = KeyEvent.VK_RIGHT;
	public static final int BUTTON_UP     = KeyEvent.VK_UP;
	public static final int BUTTON_DOWN   = KeyEvent.VK_DOWN;
	public static final int BUTTON_A      = KeyEvent.VK_Z;
	public static final int BUTTON_B      = KeyEvent.VK_X;
	public static final int BUTTON_START  = KeyEvent.VK_ENTER;
	public static final int BUTTON_SELECT = KeyEvent.VK_SHIFT;
	
	public boolean[] keys;
	
	private boolean buttonSelected = false;
	
	public SwingJoypad(InterruptManager interruptManager) {
		super(interruptManager);
		
		keys = new boolean[0x1000];
	}

	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void keyPressed(KeyEvent e) {
		for (int i = 0; i < keys.length; i++) {
			if (i == e.getKeyCode()) keys[i] = true;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		for (int i = 0; i < keys.length; i++) {
			if (i == e.getKeyCode()) keys[i] = false;
		}		
	}

	@Override
	public int read(int address) {
		int value = buttonSelected ? (1 << 5) : (1 << 4);
		
		if (buttonSelected) {
			value |= (keys[BUTTON_START]  ? 0 : 1) << 3;
			value |= (keys[BUTTON_SELECT] ? 0 : 1) << 2;
			value |= (keys[BUTTON_B]      ? 0 : 1) << 1;
			value |= (keys[BUTTON_A]      ? 0 : 1) << 0;
		} else {
			value |= (keys[BUTTON_DOWN]   ? 0 : 1) << 3;
			value |= (keys[BUTTON_UP]     ? 0 : 1) << 2;
			value |= (keys[BUTTON_LEFT]   ? 0 : 1) << 1;
			value |= (keys[BUTTON_RIGHT]  ? 0 : 1) << 0;
		}
		
		return value;
	}

	@Override
	public void write(int value, int address) {
		if ((value & 0x30) != 0) {
			buttonSelected = (value & 0x20) == 0;
		}
	}
}
