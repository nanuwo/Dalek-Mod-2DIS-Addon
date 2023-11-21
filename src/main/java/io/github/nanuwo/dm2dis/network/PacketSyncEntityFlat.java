package io.github.nanuwo.dm2dis.network;

import java.util.function.Supplier;

import io.github.nanuwo.dm2dis.capability.FlatProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class PacketSyncEntityFlat {
	
	private final int entityID;
	private final boolean isFlat;
	
	public PacketSyncEntityFlat(LivingEntity entity, boolean isFlat) {
		this.entityID = entity.getId();
		this.isFlat = isFlat;
	}
	
	public PacketSyncEntityFlat(PacketBuffer buf) {
		this.entityID = buf.readInt();
		this.isFlat = buf.readBoolean();
	}
	
	public void toBytes(PacketBuffer buf) {
		buf.writeInt(entityID);
		buf.writeBoolean(isFlat);
	}
	
	public void handle(Supplier<NetworkEvent.Context> supplier) {
		NetworkEvent.Context ctx = supplier.get();
		ctx.enqueueWork(() -> {
			LivingEntity entity = (LivingEntity) Minecraft.getInstance().level.getEntity(entityID);
			entity.getCapability(FlatProvider.FLAT_CAPABILITY).ifPresent(cap -> cap.setFlat(this.isFlat));		
		});
		ctx.setPacketHandled(true);
	}
}
