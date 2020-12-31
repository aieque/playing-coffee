package playingcoffee.core;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import playingcoffee.log.Log;

public class Cartridge implements MemorySpace {

	private int[] rom;
	
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
	}
	
	@Override
	public int read(int address) {
		return rom[address];
	}

	@Override
	public void write(int value, int address) {
		Log.warn("Attempting to write to ROM at address: 0x%4x.", address);
	}

	@Override
	public boolean inMemorySpace(int address) {
		return address >= 0x0000 && address <= 0x7FFF;
	}

}
