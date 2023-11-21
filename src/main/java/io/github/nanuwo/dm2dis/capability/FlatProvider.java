package io.github.nanuwo.dm2dis.capability;

import io.github.nanuwo.dm2dis.DM2DIS;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

public class FlatProvider implements ICapabilitySerializable<CompoundNBT>{
	
	@CapabilityInject(IFlatCapability.class)
	public static Capability<IFlatCapability> FLAT_CAPABILITY = null;
	
	private FlatCapability instance;
	private LazyOptional<IFlatCapability> optional;	
	public static ResourceLocation RESOURCE_LOCATION = new ResourceLocation(DM2DIS.MODID, "flat");
	
	public FlatProvider(LivingEntity entity) {
		instance = new FlatCapability(entity);
		optional = LazyOptional.of(() -> instance);
	}
	
	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
		if(cap == FLAT_CAPABILITY) {
			return optional.cast();
		}
		return LazyOptional.empty();
	}

	@Override
	public CompoundNBT serializeNBT() {
		return instance.serializeNBT();		
	}

	@Override
	public void deserializeNBT(CompoundNBT nbt) {
		instance.deserializeNBT(nbt);
	}
	
}

