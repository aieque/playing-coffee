package playingcoffee.core;

import playingcoffee.cartridge.Cartridge;
import playingcoffee.core.cpu.CPU;
import playingcoffee.interrupt.InterruptManager;
import playingcoffee.interrupt.Timer;
import playingcoffee.log.Log;
import playingcoffee.ppu.PPU;

public class GameBoy implements Runnable {

	private Thread thread;
	private boolean running = false;
	
	private InterruptManager interruptManager;
	private PPU ppu;
	private CPU cpu;
	private MMU mmu;
	private Timer timer;

	public GameBoy() {
		interruptManager = new InterruptManager();
		mmu = new MMU();
		
		ppu = new PPU(mmu, interruptManager);
		cpu = new CPU(mmu, interruptManager);
		timer = new Timer(interruptManager);
	}
	
	public void init() {
		mmu.connectMemorySpace(interruptManager);
		mmu.connectMemorySpace(new Cartridge("roms/cpu_instrs.gb").getMBC());
		mmu.connectMemorySpace(timer);
	}
	
	public void setJoypad(Joypad joypad) {
		mmu.connectMemorySpace(joypad);
	}
	
	public void start() {
		if (running)
			return;
		
		running = true;
		thread = new Thread(this);
		thread.start();
	}
	
	public void stop() {
		if (!running)
			return;
		
		running = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void run() {
		Log.init();
		
		init();
		
		while (running) {
			for (int i = 0; i < 8192; i++) {
				cpu.clock();
				ppu.clock();
				interruptManager.clock();
				timer.clock();
			
				if (mmu.read(0xFF02) == 0x81) {
					System.out.print((char)mmu.read(0xFF01));
					mmu.write(0, 0xFF02);
				}
			}

			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		Log.close();
	}
	
	public PPU getPPU() {
		return ppu;
	}
	
	public MMU getMMU() {
		return mmu;
	}
	
	public CPU getCPU() {
		return cpu;
	}

	public InterruptManager getInterruptManager() {
		return interruptManager;
	}
	
}
