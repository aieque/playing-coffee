package playingcoffee.application;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;

import playingcoffee.core.GameBoy;

public class Application extends Canvas implements Runnable {

	private static final long serialVersionUID = 1L;

	private boolean running;
	private Thread thread;
	
	private GameBoy gameBoy;
	
	private SwingJoypad joypad;
	
	public Application() {
		setPreferredSize(new Dimension(320, 288));
		setFocusable(true);
		requestFocus();
		
		gameBoy = new GameBoy();

		joypad = new SwingJoypad(gameBoy.getInterruptManager());
		addKeyListener(joypad);
		gameBoy.setJoypad(joypad);
		
		gameBoy.start();
	}
	
	public void renderGameBoy() {
		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(2);
			return;
		}
		
		Graphics g = bs.getDrawGraphics();
		
		/*int[] framebuffer = gameBoy.getPPU().getFrameBuffer();
		
		for (int x = 0; x < 160; x++) {
			for (int y = 0; y < 144; y++) {
				g.setColor(new Color(framebuffer[x + y * 160]));
				g.fillRect(x * 2, y * 2, 2, 2);
			}
		}*/
		
//		g.setColor(Color.WHITE);
//		g.fillRect(0, 0, getWidth(), getHeight());
//		
//		int lcdControl = (gameBoy.getMMU().read(0xFF40));
//		
//		int tileDataOffset = ((lcdControl & 0x10) != 0) ? 0x8000 : 0x8800;
//		int bgTileMap = ((lcdControl & 0x8) != 0) ? 0x9C00 : 0x9800;
//		
//		for (int tileY = 0; tileY < 32; tileY++) {
//			for (int tileX = 0; tileX < 32; tileX++) {
//				int tileIndex = gameBoy.getPPU().getVRAM().background0[tileX + tileY * 32];
//				
//				for (int y = 0; y < 8; y++) {
//					for (int n = 0; n < 8; n++) {
//						int value = (((gameBoy.getMMU().read(0x9000 + y * 2 + (tileIndex > 127 ? tileIndex - 256 : tileIndex) * 16 + 1) >> (7 - n)) & 1) << 1) | 
//								    (((gameBoy.getMMU().read(0x9000 + y * 2 + (tileIndex > 127 ? tileIndex - 256 : tileIndex) * 16)     >> (7 - n)) & 1));
//						
//						g.setColor(new Color(value * 64, value * 64, value * 64));
//						if (value != 0x0)
//							g.fillRect((n + (tileX) * 8 - gameBoy.getPPU().getRegisters().scrollX) * 2, (y + (tileY) * 8 - gameBoy.getPPU().getRegisters().scrollY) * 2, 2, 2);
//					}
//				}
//			}
//		}
		
		g.dispose();
		
		bs.show();
	}
	
	@Override
	public void run() {
		while (running) {
			renderGameBoy();
			try {
				Thread.sleep(15);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		gameBoy.stop();
	}
	
	public void start() {
		if (running) return;
		
		running = true;
		
		thread = new Thread(this);
		thread.start();
	}
	
	public static void main(String[] args) {
		Application application = new Application();
		
		JFrame frame = new JFrame("playing-coffee");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.add(application);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

		application.start();
	}
}
