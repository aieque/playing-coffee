package playingcoffee.core.cpu;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import playingcoffee.core.MMU;
import playingcoffee.core.opcode.ALU16Opcode;
import playingcoffee.core.opcode.ALU16Opcode.ALU16Type;
import playingcoffee.core.opcode.ALUOpcode;
import playingcoffee.core.opcode.ALUOpcode.ALUType;
import playingcoffee.core.opcode.AddSPI8Opcode;
import playingcoffee.core.opcode.Argument;
import playingcoffee.core.opcode.BCDOpcode;
import playingcoffee.core.opcode.CallOpcode;
import playingcoffee.core.opcode.ComplementOpcode;
import playingcoffee.core.opcode.FlipCarryOpcode;
import playingcoffee.core.opcode.InterruptOpcode;
import playingcoffee.core.opcode.JumpOpcode;
import playingcoffee.core.opcode.JumpRelativeOpcode;
import playingcoffee.core.opcode.LoadOpcode;
import playingcoffee.core.opcode.Opcode;
import playingcoffee.core.opcode.PopOpcode;
import playingcoffee.core.opcode.PushOpcode;
import playingcoffee.core.opcode.RestartOpcode;
import playingcoffee.core.opcode.ReturnOpcode;
import playingcoffee.core.opcode.RotateAOpcode;
import playingcoffee.core.opcode.SetCarryOpcode;
import playingcoffee.core.opcode.HaltOpcode;
import playingcoffee.core.opcode.prefixed.BitOpcode;
import playingcoffee.core.opcode.prefixed.ResetBitOpcode;
import playingcoffee.core.opcode.prefixed.RotateOpcode;
import playingcoffee.core.opcode.prefixed.SetBitOpcode;
import playingcoffee.core.opcode.prefixed.ShiftOpcode;
import playingcoffee.core.opcode.prefixed.ShiftOpcode.ShiftType;
import playingcoffee.interrupt.InterruptListener;
import playingcoffee.interrupt.InterruptManager;
import playingcoffee.core.opcode.prefixed.SwapOpcode;
import playingcoffee.log.Log;

public class CPU implements InterruptListener {

	private final MMU mmu;
	private final InterruptManager interruptManager;
	
	private Registers registers;
	
	private int cycles;
	
	private boolean lowPowerMode = false;
	
	Opcode[] opcodes;
	Opcode[] prefixedOpcodes;
	
	public CPU(final MMU mmu, final InterruptManager interruptManager) {
		this.mmu = mmu;
		this.interruptManager = interruptManager;
		
		registers = new Registers();
		
		opcodes = new Opcode[0x100];
		prefixedOpcodes = new Opcode[0x100];
		
		loadOpcodes();
		loadPrefixedOpcodes();
	
		interruptManager.addListener(this);
	}
	
	private void loadOpcodes() {
		// NOP
		opcodes[0x00] = new Opcode() {
			@Override
			public int run(Registers registers, MMU mmu) {
				return 0;
			}
			
			@Override
			public String toString() {
				return "NOP";
			}
		};
		
		// Load Opcodes
		for (Entry<Integer, Argument> val : indexedList(0x01, 0x10, Argument.BC, Argument.DE, Argument.HL, Argument.SP)) {
			opcodes[val.getKey()] = new LoadOpcode(Argument.D16, val.getValue());
		}
		
		for (Entry<Integer, Argument> val : indexedList(0x02, 0x10, Argument._BC, Argument._DE, Argument._HL_INC, Argument._HL_DEC)) {
			opcodes[val.getKey()] = new LoadOpcode(Argument.A, val.getValue());
		}
		for (Entry<Integer, Argument> val : indexedList(0x06, 0x10, Argument.B, Argument.D, Argument.H, Argument._HL)) {
			opcodes[val.getKey()] = new LoadOpcode(Argument.D8, val.getValue());
		}
		for (Entry<Integer, Argument> val : indexedList(0x0A, 0x10, Argument._BC, Argument._DE, Argument._HL_INC, Argument._HL_DEC)) {
			opcodes[val.getKey()] = new LoadOpcode(val.getValue(), Argument.A);
		}
		for (Entry<Integer, Argument> val : indexedList(0x0E, 0x10, Argument.C, Argument.E, Argument.L, Argument.A)) {
			opcodes[val.getKey()] = new LoadOpcode(Argument.D8, val.getValue());
		}
		
		for (Entry<Integer, Argument> val : indexedList(0x40, 1, Argument.B, Argument.C, Argument.D, Argument.E, Argument.H, Argument.L, Argument._HL, Argument.A)) {
			for (Entry<Integer, Argument> row : indexedList(val.getKey(), 0x10, Argument.B, Argument.D, Argument.H, Argument._HL)) {
				opcodes[row.getKey()] = new LoadOpcode(val.getValue(), row.getValue());
			}
		}
		for (Entry<Integer, Argument> val : indexedList(0x48, 1, Argument.B, Argument.C, Argument.D, Argument.E, Argument.H, Argument.L, Argument._HL, Argument.A)) {
			for (Entry<Integer, Argument> row : indexedList(val.getKey(), 0x10, Argument.C, Argument.E, Argument.L, Argument.A)) {
				opcodes[row.getKey()] = new LoadOpcode(val.getValue(), row.getValue());
			}
		}
		
		opcodes[0x08] = new LoadOpcode(Argument.SP, Argument._D16_SHORT);
		opcodes[0xF8] = new LoadOpcode(Argument.SP_I8, Argument.HL);
		
		opcodes[0xE0] = new LoadOpcode(Argument.A, Argument._D8);
		opcodes[0xF0] = new LoadOpcode(Argument._D8, Argument.A);
		
		opcodes[0xE2] = new LoadOpcode(Argument.A, Argument._C);
		opcodes[0xF2] = new LoadOpcode(Argument._C, Argument.A);
		
		opcodes[0xF9] = new LoadOpcode(Argument.HL, Argument.SP);
		
		opcodes[0xEA] = new LoadOpcode(Argument.A, Argument._D16);
		opcodes[0xFA] = new LoadOpcode(Argument._D16, Argument.A);
		
		// POP and PUSH
		for (Entry<Integer, Argument> val : indexedList(0xC1, 0x10, Argument.BC, Argument.DE, Argument.HL, Argument.AF)) {
			opcodes[val.getKey()] = new PopOpcode(val.getValue());
		}
		for (Entry<Integer, Argument> val : indexedList(0xC5, 0x10, Argument.BC, Argument.DE, Argument.HL, Argument.AF)) {
			opcodes[val.getKey()] = new PushOpcode(val.getValue());
		}
		
		opcodes[0x76] = new HaltOpcode(this);
		
		// Jumps, Calls and Returns
		opcodes[0x20] = new JumpRelativeOpcode(Flags.ZERO, Argument.I8, true);
		opcodes[0x30] = new JumpRelativeOpcode(Flags.CARRY, Argument.I8, true);
		
		opcodes[0x18] = new JumpRelativeOpcode(0, Argument.I8, false);
		opcodes[0x28] = new JumpRelativeOpcode(Flags.ZERO, Argument.I8, false);
		opcodes[0x38] = new JumpRelativeOpcode(Flags.CARRY, Argument.I8, false);
		
		opcodes[0xC3] = new JumpOpcode(0, Argument.D16, false);
		opcodes[0xC2] = new JumpOpcode(Flags.ZERO, Argument.D16, true);
		opcodes[0xD2] = new JumpOpcode(Flags.CARRY, Argument.D16, true);
		
		opcodes[0xE9] = new JumpOpcode(0, Argument.HL, false);

		opcodes[0xCA] = new JumpOpcode(Flags.ZERO, Argument.D16, false);
		opcodes[0xDA] = new JumpOpcode(Flags.CARRY, Argument.D16, false);
		
		opcodes[0xC4] = new CallOpcode(Flags.ZERO, Argument.D16, true);
		opcodes[0xD4] = new CallOpcode(Flags.CARRY, Argument.D16, true);

		opcodes[0xCC] = new CallOpcode(Flags.ZERO, Argument.D16, false);
		opcodes[0xDC] = new CallOpcode(Flags.CARRY, Argument.D16, false);
		
		opcodes[0xCD] = new CallOpcode(0, Argument.D16, false);
		
		opcodes[0xC0] = new ReturnOpcode(Flags.ZERO, true, false, interruptManager);
		opcodes[0xD0] = new ReturnOpcode(Flags.CARRY, true, false, interruptManager);

		opcodes[0xC8] = new ReturnOpcode(Flags.ZERO, false, false, interruptManager);
		opcodes[0xD8] = new ReturnOpcode(Flags.CARRY, false, false, interruptManager);
		
		opcodes[0xC9] = new ReturnOpcode(0, false, false, interruptManager);
		opcodes[0xD9] = new ReturnOpcode(0, false, true, interruptManager);
		
		// Bit Operations
		
		opcodes[0x07] = new RotateAOpcode(true, RotateAOpcode.LEFT);
		opcodes[0x17] = new RotateAOpcode(false, RotateAOpcode.LEFT);
		opcodes[0x0F] = new RotateAOpcode(true, RotateAOpcode.RIGHT);
		opcodes[0x1F] = new RotateAOpcode(false, RotateAOpcode.RIGHT);
		
		// 16-bit ALU Opcodes
		for (Entry<Integer, Argument> val : indexedList(0x03, 0x10, Argument.BC, Argument.DE,Argument.HL, Argument.SP)) {
			opcodes[val.getKey()] = new ALU16Opcode(ALU16Type.INC, val.getValue());
			opcodes[val.getKey() + 8] = new ALU16Opcode(ALU16Type.DEC, val.getValue());
		}
		for (Entry<Integer, Argument> val : indexedList(0x09, 0x10, Argument.BC, Argument.DE,Argument.HL, Argument.SP)) {
			opcodes[val.getKey()] = new ALU16Opcode(ALU16Type.ADD, val.getValue());
		}
		opcodes[0xE8] = new AddSPI8Opcode();
		
		// ALU Opcodes
		for (Entry<Integer, Argument> val : indexedList(0x04, 8, Argument.B, Argument.C, Argument.D, Argument.E, Argument.H, Argument.L, Argument._HL, Argument.A)) {
			opcodes[val.getKey()] = new ALUOpcode(ALUType.INC, val.getValue());
			opcodes[val.getKey() + 1] = new ALUOpcode(ALUType.DEC, val.getValue());
		}
		
		for (Entry<Integer, ALUType> val : indexedList(0x80, 8, ALUType.ADD, ALUType.ADC, ALUType.SUB, ALUType.SBC, ALUType.AND, ALUType.XOR, ALUType.OR, ALUType.CP)) {
			for (Entry<Integer, Argument> row : indexedList(val.getKey(), 1, Argument.B, Argument.C, Argument.D, Argument.E, Argument.H, Argument.L, Argument._HL, Argument.A)) {
				opcodes[row.getKey()] = new ALUOpcode(val.getValue(), row.getValue());
			}
		}
		for (Entry<Integer, ALUType> val : indexedList(0xC6, 8, ALUType.ADD, ALUType.ADC, ALUType.SUB, ALUType.SBC, ALUType.AND, ALUType.XOR, ALUType.OR, ALUType.CP)) {
			opcodes[val.getKey()] = new ALUOpcode(val.getValue(), Argument.D8);
		}
		
		// Restarts
		for (Entry<Integer, Integer> val : indexedList(0xC7, 8, 0x00, 0x08, 0x10, 0x18, 0x20, 0x28, 0x30, 0x38)) {
			opcodes[val.getKey()] = new RestartOpcode(val.getValue());
		}
		
		// Interrupts
		opcodes[0xF3] = new InterruptOpcode(false, interruptManager);
		opcodes[0xFB] = new InterruptOpcode(true, interruptManager);
		
		// Misc
		opcodes[0x10] = new HaltOpcode(this);
		
		opcodes[0x27] = new BCDOpcode();
		opcodes[0x2F] = new ComplementOpcode();
		opcodes[0x37] = new SetCarryOpcode();
		opcodes[0x3F] = new FlipCarryOpcode();
	}
	
	public void loadPrefixedOpcodes() {
		for (Entry<Integer, Argument> val : indexedList(0x00, 1, Argument.B, Argument.C, Argument.D, Argument.E, Argument.H, Argument.L, Argument._HL, Argument.A)) {
			prefixedOpcodes[val.getKey() + 0x00] = new RotateOpcode(true, val.getValue(), RotateOpcode.LEFT);
			prefixedOpcodes[val.getKey() + 0x08] = new RotateOpcode(true, val.getValue(), RotateOpcode.RIGHT);
			prefixedOpcodes[val.getKey() + 0x10] = new RotateOpcode(false, val.getValue(), RotateOpcode.LEFT);
			prefixedOpcodes[val.getKey() + 0x18] = new RotateOpcode(false, val.getValue(), RotateOpcode.RIGHT);
		}
		
		for (Entry<Integer, Integer> val : indexedList(0x40, 8, 0, 1, 2, 3, 4, 5, 6, 7)) {
			for (Entry<Integer, Argument> row : indexedList(val.getKey(), 1, Argument.B, Argument.C, Argument.D, Argument.E, Argument.H, Argument.L, Argument._HL, Argument.A)) {
				prefixedOpcodes[row.getKey()] = new BitOpcode(val.getValue(), row.getValue());
			}
		}
		
		for (Entry<Integer, Argument> val : indexedList(0x30, 1, Argument.B, Argument.C, Argument.D, Argument.E, Argument.H, Argument.L, Argument._HL, Argument.A)) {
			prefixedOpcodes[val.getKey()] = new SwapOpcode(val.getValue());
		}
		
		// Reset Bit
		for (Entry<Integer, Integer> val : indexedList(0x80, 8, 0, 1, 2, 3, 4, 5, 6, 7)) {
			for (Entry<Integer, Argument> row : indexedList(val.getKey(), 1, Argument.B, Argument.C, Argument.D, Argument.E, Argument.H, Argument.L, Argument._HL, Argument.A)) {
				prefixedOpcodes[row.getKey()] = new ResetBitOpcode(val.getValue(), row.getValue());
			}
		}

		// Set Bit
		for (Entry<Integer, Integer> val : indexedList(0xC0, 8, 0, 1, 2, 3, 4, 5, 6, 7)) {
			for (Entry<Integer, Argument> row : indexedList(val.getKey(), 1, Argument.B, Argument.C, Argument.D, Argument.E, Argument.H, Argument.L, Argument._HL, Argument.A)) {
				prefixedOpcodes[row.getKey()] = new SetBitOpcode(val.getValue(), row.getValue());
			}
		}
		
		for (Entry<Integer, Argument> val : indexedList(0x20, 1, Argument.B, Argument.C, Argument.D, Argument.E, Argument.H, Argument.L, Argument._HL, Argument.A)) {
			prefixedOpcodes[val.getKey()] = new ShiftOpcode(ShiftType.SLA, val.getValue());
		}
		for (Entry<Integer, Argument> val : indexedList(0x28, 1, Argument.B, Argument.C, Argument.D, Argument.E, Argument.H, Argument.L, Argument._HL, Argument.A)) {
			prefixedOpcodes[val.getKey()] = new ShiftOpcode(ShiftType.SRA, val.getValue());
		}
		for (Entry<Integer, Argument> val : indexedList(0x38, 1, Argument.B, Argument.C, Argument.D, Argument.E, Argument.H, Argument.L, Argument._HL, Argument.A)) {
			prefixedOpcodes[val.getKey()] = new ShiftOpcode(ShiftType.SRL, val.getValue());
		}
	}
	
	@SafeVarargs
	private static <T> Iterable<Entry<Integer, T>> indexedList(int start, int step, T... values) {
        Map<Integer, T> map = new LinkedHashMap<>();
        int i = start;
        for (T e : values) {
            map.put(i, e);
            i += step;
        }
        return map.entrySet();
    }
	
	public void clock() {
		if (lowPowerMode && interruptManager.getPendingInterruptsBeforeHalt() != interruptManager.getInterruptFlag()) {
			cycles += 4;
			lowPowerMode = false;
		}
		
		if (lowPowerMode) return;
		
		if (cycles == 0) {
			//Log.info("PC: 0x%4x", registers.getPC());
			
			int opcodeValue = mmu.read(registers.getPC());
			registers.incPC();
			
			if (opcodeValue == 0xCB) {
				int prefixedValue = mmu.read(registers.getPC());
				registers.incPC();
			
				runPrefixedOpcode(prefixedValue);
			
			} else {
				runOpcode(opcodeValue);
			}
		}
		
		cycles--;
	}

	private void runOpcode(int opcodeValue) {
		Opcode opcode = opcodes[opcodeValue];
		
		if (opcode == null) {
			Log.error("Unimplemented opcode 0x%2x at 0x%4x!", opcodeValue, registers.getPC());
			
			Log.close();

			throw new IllegalStateException();
		}
		
//		Log.info("Executing opcode: 0x%2x (%s) at 0x%4x", opcodeValue, opcode.toString(), registers.getPC());
		cycles += opcode.run(registers, mmu) + 4; // Adding 4 because we fetch the instruction.
	}

	private void runPrefixedOpcode(int opcodeValue) {
		Opcode opcode = prefixedOpcodes[opcodeValue];
		
		if (opcode == null) {
			Log.error("Unimplemented prefixed opcode 0x%2x!", opcodeValue);
			
			Log.close();
			
			throw new IllegalStateException();
		}
		
//		Log.info("Executing prefixed opcode: 0x%2x (%s)", opcodeValue, opcode.toString());
		cycles += opcode.run(registers, mmu) + 8; // Adding 8 because we fetch the 0xCB prefix and the instruction.
	}

	public InterruptManager getInterruptManager() {
		return interruptManager;
	}

	public void enterLowPowerMode() {
		lowPowerMode = true;
	}
	
	public void exitLowPowerMode() {
		lowPowerMode = false;
	}
	
	@Override
	public void interruptOccured(int types) {
		if ((types & InterruptManager.VBLANK) != 0) {
			interruptManager.disable();
			lowPowerMode = false;
			mmu.pushStack(registers.getPC(), registers);
			cycles += 13;
			registers.setPC(0x40);
		}

		if ((types & InterruptManager.LCDC_STAT) != 0) {
			interruptManager.disable();
			lowPowerMode = false;
			mmu.pushStack(registers.getPC(), registers);
			cycles += 13;
			registers.setPC(0x48);
		}
		
		if ((types & InterruptManager.TIMER) != 0) {
			interruptManager.disable();
			lowPowerMode = false;
			mmu.pushStack(registers.getPC(), registers);
			cycles += 13;
			registers.setPC(0x50);
		}
	}
	
	
}
