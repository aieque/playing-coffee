package playingcoffee.core;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class MMU {

	private int ram[];
	
	public void loadBootRom() {
		byte[] data;
		try {
			data = Files.readAllBytes(Paths.get("DMG_ROM.bin"));
			for (int i = 0; i < data.length; i++) {
				ram[i] = Byte.toUnsignedInt(data[i]);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public MMU() {
		ram = new int[0x10000];
	}
	
	public void write(int value, int address) {
		ram[address] = value;
	}
	
	public int read(int address) {
		return ram[address];
	}
}