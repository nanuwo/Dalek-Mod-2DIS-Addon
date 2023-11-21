package io.github.nanuwo.dm2dis.capability;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

public class FlatStorage implements Capability.IStorage<IFlatCapability>{

	@Override
	public INBT writeNBT(Capability<IFlatCapability> capability, IFlatCapability instance, Direction side) {
		System.out.println("sussy farts" + instance.isFlat());
		return instance.serializeNBT();
	}

	@Override
	public void readNBT(Capability<IFlatCapability> capability, IFlatCapability instance, Direction side, INBT inbt) {
		System.out.println(" sussy fungus man"+instance.isFlat());
		CompoundNBT nbt = (CompoundNBT) inbt;
		instance.deserializeNBT(nbt);
	}

}