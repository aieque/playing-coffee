package playingcoffee.application;

import playingcoffee.core.Cartridge;
import playingcoffee.core.InterruptManager;
import playingcoffee.core.MMU;
import playingcoffee.core.cpu.CPU;
import playingcoffee.log.Log;
import playingcoffee.ppu.PPU;

public class GameBoy implements Runnable {

	private Thread thread;
	private boolean running = false;
	
	private InterruptManager interruptManager;
	private PPU ppu;
	private MMU mmu;
	private CPU cpu;
	
	public GameBoy() {
		interruptManager = new InterruptManager();
		mmu = new MMU();
		
		ppu = new PPU(mmu, interruptManager);
		cpu = new CPU(mmu, interruptManager);
	}
	
	public void init() {
		mmu.connectMemorySpace(interruptManager);
		mmu.connectMemorySpace(new Cartridge("roms/supermarioland.gb"));	
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
			}

			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
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
	
}
