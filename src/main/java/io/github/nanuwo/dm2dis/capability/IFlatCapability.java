package io.github.nanuwo.dm2dis.capability;

import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;

public interface IFlatCapability extends INBTSerializable<CompoundNBT>{

	boolean isFlat();
	int getLastFlattened();

	void setFlat(boolean isFlat);
	void setLastFlattened(int lastFlattened);

	void tick();
	
	void sync();


}
