package io.github.nanuwo.dm2dis.network;

import io.github.nanuwo.dm2dis.DM2DIS;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class PacketHandler {
	
	private static int id = 0;
	private static int id() {
		return id++;
	}
	
	public static SimpleChannel INSTANCE;
	
	public static void register() {
		SimpleChannel net = NetworkRegistry.ChannelBuilder
				.named(new ResourceLocation(DM2DIS.MODID,"messages"))
				.networkProtocolVersion(() -> "1.0")
	            .clientAcceptedVersions(s -> true)
	            .serverAcceptedVersions(s -> true)
	            .simpleChannel();
		INSTANCE = net; 
		net.messageBuilder(PacketSyncEntityFlat.class, id())		
		.decoder(PacketSyncEntityFlat::new)
		.encoder(PacketSyncEntityFlat::toBytes)
		.consumer(PacketSyncEntityFlat::handle)
		.add();
		
	}
	
	public static <MSG> void sendToServer(MSG message) {
		INSTANCE.sendToServer(message);
	}
	
	public static <MSG> void sendToClient(MSG message, ServerPlayerEntity player) {
		INSTANCE.send(PacketDistributor.PLAYER.with(() -> player),message);
	}
	
	public static <MSG> void sendToWatchingPlayers(MSG message, LivingEntity entity) {
		INSTANCE.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> entity), message);
	}

}
