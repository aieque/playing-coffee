package playingcoffee.test;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;

import playingcoffee.core.cpu.Flags;
import playingcoffee.core.cpu.Registers;

public class GameBoyTest {

	@Test
	public void testCPU() throws IOException {
		/*Log.init();
		
		
		byte[] bootRom = Files.readAllBytes(Paths.get("dmg_boot.bin"));
		
		MMU mmu = new MMU();
		CPU cpu = new CPU(mmu);
		
		for (int i = 0; i < 0x100; i++) {
			mmu.write(Byte.toUnsignedInt(bootRom[i]), i);
		}
		
		while (true) cpu.clock();*/
	}
	
	@Test
	public void testFlags() {
		Flags flags = new Flags();
		assertEquals(flags.get(), 0);
		
		flags.set(Flags.ZERO | Flags.CARRY, true);
		
		assertEquals(flags.get(), Flags.ZERO | Flags.CARRY);
		
		flags.set(Flags.ZERO | Flags.HALF_CARRY, false);
		
		assertEquals(flags.get(), Flags.CARRY);
		
		flags.set(Flags.CARRY, true);
		
		assertEquals(flags.get(), Flags.CARRY);
	}
	
	public void testRegisters() {
		Registers registers = new Registers();
		
		registers.setAF(0x1284);
		assertEquals(0x1284, registers.getAF());
		
		registers.setHL(0x4295);
		assertEquals(0x42, registers.getH());
		assertEquals(0x95, registers.getL());
		
		assertEquals(0x4295, registers.getHL());

		registers.setSP(0xFF89);
		registers.setHL(registers.getSP());
		
		assertEquals(registers.getSP(), registers.getHL());
		
	}
}
