package playingcoffee.core;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class MMU {

	private int[] memory;
	private int[] bootRom;

	private List<MemorySpace> memorySpaces;
	
	public MMU() {
		memory = new int[0x10000];

		memorySpaces = new ArrayList<MemorySpace>();
		
		loadBootROM();
	}

	public void connectMemorySpace(MemorySpace memorySpace) {
		memorySpaces.add(memorySpace);
	}
	
	// TODO: Move the ROM into a separate file.

	public void loadROM(String rom) {
		byte[] bin = null;

		try {
			bin = Files.readAllBytes(Paths.get(rom));
		} catch (IOException e) {
			e.printStackTrace();
		}

		for (int i = 0; i < bin.length; i++) {
			memory[i] = Byte.toUnsignedInt(bin[i]);
		}
	}

	// TODO: Move the boot ROM to a separate file.
	private void loadBootROM() {
		bootRom = new int[0x100];

		byte[] bin = null;

		try {
			bin = Files.readAllBytes(Paths.get("dmg_boot.bin"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		for (int i = 0; i < 0x100; i++) {
			bootRom[i] = Byte.toUnsignedInt(bin[i]);
		}
	}

	public int read(int address) {
		if (address == 0xFF00) return 0xF; // TODO: Remove
		
		if (address >= 0x00 && address <= 0xFF && read(0xFF50) == 0)
			return bootRom[address];
	
		for (MemorySpace memorySpace : memorySpaces) {
			if (memorySpace.inMemorySpace(address)) {
				return memorySpace.read(address);
			}
		}

		return memory[address];
	}

	public void write(int value, int address) {
		if (address == 0xFF00) return; // TODO: Remove
		
		if (address >= 0x00 && address <= 0xFF && read(0xFF50) == 0)
			bootRom[address] = value & 0xFF;
		
		for (MemorySpace memorySpace : memorySpaces) {
			if (memorySpace.inMemorySpace(address)) {
				memorySpace.write(value & 0xFF, address);
			}
		}
		memory[address] = value & 0xFF;
	}

	public int[] getMemory() {
		return memory;
	}
}
