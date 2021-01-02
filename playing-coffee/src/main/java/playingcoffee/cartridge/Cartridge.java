package playingcoffee.cartridge;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import playingcoffee.log.Log;

public class Cartridge {

	private int[] rom;
	
	private String title;
	private boolean isGameBoyColor;
	private int romSize;
	private int ramSize;
	
	private MBC mbc;
	
	public Cartridge(String rom) {
		byte[] bin = null;
		
		try {
			bin = Files.readAllBytes(Paths.get(rom));
		} catch (IOException e) {
			e.printStackTrace();
		}

		this.rom = new int[bin.length];
		
		for (int i = 0; i < bin.length; i++) {
			this.rom[i] = Byte.toUnsignedInt(bin[i]);
		}
		
		byte[] binTitle = new byte[0x10];
		for (int i = 0; i < 0x10; i++) {
			binTitle[i] = bin[0x0134 + i];
		}
		
		this.title = new String(binTitle, StandardCharsets.US_ASCII);
		this.isGameBoyColor = this.rom[0x0143] == 0xC0;
		
		this.romSize = (32 * 1024) << this.rom[0x0148];
		
		switch (this.rom[0x0149]) {
		case 0: ramSize = 0; break;
		case 1: ramSize = 2 * 1024; break;
		case 2: ramSize = 8 * 1024; break;
		case 4: ramSize = 32 * 1024; break;
		case 5: ramSize = 128 * 1024; break;
		case 6: ramSize = 64 * 1024; break;
		}
		
		this.mbc = MBC.create(this.rom[0x0147], this.rom);
		
		Log.info("ROM Name: %s", title);
		Log.info("Requires GBC: %s", isGameBoyColor);
		Log.info("ROM Size: %s", romSize);
		Log.info("RAM Size: %s", ramSize);
		Log.info("Cartridge Type: 0x%2x", this.rom[0x0147]);
	}
	
	public MBC getMBC() {
		return mbc;
	}

}
