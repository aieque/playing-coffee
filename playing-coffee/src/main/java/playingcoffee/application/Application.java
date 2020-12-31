package playingcoffee.application;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;

public class Application extends Canvas implements Runnable {

	private static final long serialVersionUID = 1L;

	private boolean running;
	private Thread thread;
	
	private GameBoy gameBoy;
	
	public Application() {
		setPreferredSize(new Dimension(320, 288));
	
		gameBoy = new GameBoy();
		gameBoy.start();
	}
	
	public void renderGameBoy() {
		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(2);
			return;
		}
		
		Graphics g = bs.getDrawGraphics();
		
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, getWidth(), getHeight());
		
		int lcdControl = (gameBoy.getMMU().read(0xFF40));
		
		int tileDataOffset = ((lcdControl & 0x10) != 0) ? 0x8000 : 0x8800;
		
		for (int tile = 0; tile < 512; tile++) {
			int tileIndex = tile;
			for (int y = 0; y < 8; y++) {
				for (int n = 0; n < 8; n++) {
					int value = (((gameBoy.getMMU().read(0x8000 + y * 2 + tileIndex * 16 + 1) >> (7 - n)) & 1) << 1) | 
							    (((gameBoy.getMMU().read(0x8000 + y * 2 + tileIndex * 16)     >> (7 - n)) & 1));
					
					g.setColor(new Color(value * 64, value * 64, value * 64));
					if (value != 0x0)
						g.fillRect((n + (tile % 16) * 8), (y + (tile / 16) * 8), 1, 1);
				}
			}
		}
		
		if ((lcdControl & 0x80) != 0) {
			for (int tileY = 0; tileY < 32; tileY++) {
				for (int tileX = 0; tileX < 32; tileX++) {
					int tileIndex = gameBoy.getMMU().read(0x9800 + (tileX + tileY * 32));
					for (int y = 0; y < 8; y++) {
						for (int n = 0; n < 8; n++) {
							int value = (((gameBoy.getMMU().read(tileDataOffset + y * 2 + tileIndex * 16 + 1) >> (7 - n)) & 1) << 1) | 
									    (((gameBoy.getMMU().read(tileDataOffset + y * 2 + tileIndex * 16)     >> (7 - n)) & 1));
							
							g.setColor(new Color(value * 64, value * 64, value * 64));
							if (value != 0x0)
								g.fillRect((n + (tileX) * 8 - gameBoy.getPPU().getRegisters().scrollX) * 2, (y + (tileY) * 8 - gameBoy.getPPU().getRegisters().scrollY) * 2, 2, 2);
						}
					}
				}
			}
		}
		
		g.dispose();
		
		bs.show();
	}
	
	@Override
	public void run() {
		while (running) {
			renderGameBoy();
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
