package gamecubeloader.common;

import ghidra.app.util.MemoryBlockUtils;
import ghidra.app.util.bin.ByteProvider;
import ghidra.app.util.importer.MessageLog;
import ghidra.program.model.listing.Program;
import ghidra.util.task.TaskMonitor;

public final class SystemMemorySections {
	public static void Create(ByteProvider provider, Program program, TaskMonitor monitor, MessageLog log) {
		var addressSpace = program.getAddressFactory().getDefaultAddressSpace();
		
		/* Create OS globals section. Check if we have the data, otherwise create uninitialized block. */
		if (program.getMinAddress().compareTo(addressSpace.getAddress(0x80000000L)) <= 0) {
		    /* We have all the OS globals data, likely from a RAM dump. */
		    try {
                MemoryBlockUtils.createInitializedBlock(program, true, "OSGlobals", addressSpace.getAddress(0x80000000), provider.getInputStream(0),
                        0x3100, "", null, true, true, true, null, monitor);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
		}
		else {
		    /* Only mark as overlay if any part of the data already exists. */
		    final boolean overlay = program.getMinAddress().compareTo(addressSpace.getAddress(0x80003100)) < 0;
		    MemoryBlockUtils.createUninitializedBlock(program, overlay, "OSGlobals", addressSpace.getAddress(0x80000000), 0x3100, "Operating System Globals", null, true, true, false, log);
		}
		
		// Now create hardware registers
		var cp = MemoryBlockUtils.createUninitializedBlock(program, false, "CP", addressSpace.getAddress(0xCC000000), 0x80, "Command Processor Register", null, true, true, false, log);
		cp.setVolatile(true);

		var pe = MemoryBlockUtils.createUninitializedBlock(program, false, "PE", addressSpace.getAddress(0xCC001000), 0x100, "Pixel Engine Register", null, true, true, false, log);
		pe.setVolatile(true);
		
		var vi = MemoryBlockUtils.createUninitializedBlock(program, false, "VI", addressSpace.getAddress(0xCC002000), 0x100, "Video Interface Register", null, true, true, false, log);
		vi.setVolatile(true);
		
		var pi = MemoryBlockUtils.createUninitializedBlock(program, false, "PI", addressSpace.getAddress(0xCC003000), 0x100, "Processor Interface Register", null, true, true, false, log);
		pi.setVolatile(true);
		
		var mi = MemoryBlockUtils.createUninitializedBlock(program, false, "MI", addressSpace.getAddress(0xCC004000), 0x80, "Memory Interface Register", null, true, true, false, log);
		mi.setVolatile(true);
		
		var dsp = MemoryBlockUtils.createUninitializedBlock(program, false, "DSP", addressSpace.getAddress(0xCC005000), 0x200, "Digital Signal Processor Register", null, true, true, false, log);
		dsp.setVolatile(true);
		
		var di = MemoryBlockUtils.createUninitializedBlock(program, false, "DI", addressSpace.getAddress(0xCC006000), 0x40, "DVD Interface Reigster", null, true, true, false, log);
		di.setVolatile(true);
		
		var si = MemoryBlockUtils.createUninitializedBlock(program, false, "SI", addressSpace.getAddress(0xCC006400), 0x100, "Serial Interface Reigster", null, true, true, false, log);
		si.setVolatile(true);
		
		var exi = MemoryBlockUtils.createUninitializedBlock(program, false, "EXI", addressSpace.getAddress(0xCC006800), 0x40, "External Interface Reigster", null, true, true, false, log);
		exi.setVolatile(true);
		
		var ai = MemoryBlockUtils.createUninitializedBlock(program, false, "AI", addressSpace.getAddress(0xCC006C00), 0x40, "Audio Interface Reigster", null, true, true, false, log);
		ai.setVolatile(true);
		
		var gxfifo = MemoryBlockUtils.createUninitializedBlock(program, false, "GXFIFO", addressSpace.getAddress(0xCC008000), 0x8, "Graphics FIFO Reigster", null, true, true, false, log);
		gxfifo.setVolatile(true);
	}
}
