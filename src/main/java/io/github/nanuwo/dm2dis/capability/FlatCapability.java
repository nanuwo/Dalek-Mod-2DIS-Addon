package io.github.nanuwo.dm2dis.capability;

import io.github.nanuwo.dm2dis.network.PacketHandler;
import io.github.nanuwo.dm2dis.network.PacketSyncEntityFlat;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;

public class FlatCapability implements IFlatCapability,INBTSerializable<CompoundNBT>{
	
	private boolean isFlat = false;
	private int lastFlattened = 0;
	private LivingEntity entity;
	
	public static final String IS_FLAT = "IsFlat";
	public static final String LAST_FLATTENED = "LastFlattened";

	
	public FlatCapability(LivingEntity entity) {
		this.entity = entity;
	}
	@Override
	public boolean isFlat() {
		return this.isFlat;
	}
	
	
	@Override
	public void setFlat(boolean isFlat) {
		this.isFlat = isFlat;
		if(!entity.level.isClientSide()) {
			sync();
		}
	}
	
	@Override
	public void setLastFlattened(int lastFlattened) {
		this.lastFlattened = lastFlattened;
	}
	
	@Override
	public int getLastFlattened() {
		return this.lastFlattened;
	}
	
	@Override
	public void sync() {
		PacketHandler.sendToWatchingPlayers(new PacketSyncEntityFlat(entity, isFlat()), entity);
	}
	
	
	@Override
	public CompoundNBT serializeNBT() {
		CompoundNBT nbt = new CompoundNBT();
		nbt.putBoolean(IS_FLAT, this.isFlat());
		nbt.putInt(LAST_FLATTENED, this.getLastFlattened());
		return nbt;
	}
	
	@Override
	public void deserializeNBT(CompoundNBT nbt) {
		this.setFlat(nbt.getBoolean(IS_FLAT));
		this.setLastFlattened(nbt.getInt(LAST_FLATTENED));
	}
	
	@Override
	public void tick() {
		if(isFlat()) {
			if(lastFlattened < 3600) {
				lastFlattened++;
			} else if(lastFlattened == 3600) {
				lastFlattened = 0;
				setFlat(false);
			}
			
		}
		
		
	}



	

	


}
