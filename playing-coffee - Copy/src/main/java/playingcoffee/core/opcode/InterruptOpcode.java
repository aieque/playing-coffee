package playingcoffee.core.opcode;

import playingcoffee.core.InterruptManager;
import playingcoffee.core.MMU;
import playingcoffee.core.cpu.Registers;

public class InterruptOpcode implements Opcode {

	private boolean enable;
	private InterruptManager interruptManager;
	
	public InterruptOpcode(boolean enable, InterruptManager interruptManager) {
		this.enable = enable;
		this.interruptManager = interruptManager;
	}

	@Override
	public int run(Registers registers, MMU mmu) {
		if (enable)
			interruptManager.enable();
		else
			interruptManager.disable();
		
		return 0;
	}

	
	
}
