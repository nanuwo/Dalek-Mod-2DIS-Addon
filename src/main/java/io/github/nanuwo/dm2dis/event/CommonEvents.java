package io.github.nanuwo.dm2dis.event;

import java.util.List;

import com.swdteam.common.init.DMFlightMode;

import io.github.nanuwo.dm2dis.DM2DIS;
import io.github.nanuwo.dm2dis.capability.FlatProvider;
import io.github.nanuwo.dm2dis.init.ItemInit;
import io.github.nanuwo.dm2dis.init.TardisDamageSource;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.GameType;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerFlyableFallEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = DM2DIS.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CommonEvents {

	@SubscribeEvent
	public static void attachCapabilities(AttachCapabilitiesEvent<Entity> event) {
		if (!event.getObject().getCapability(FlatProvider.FLAT_CAPABILITY).isPresent()) {
			if (event.getObject() instanceof LivingEntity) {
				event.addCapability(FlatProvider.RESOURCE_LOCATION,
						new FlatProvider((LivingEntity) event.getObject()));
			}
		}
	}

	@SubscribeEvent
	public static void onEntityJoinWorld(EntityJoinWorldEvent event) {
		if (!event.getEntity().level.isClientSide() && event.getEntity() instanceof LivingEntity) {
			LivingEntity entity = (LivingEntity) event.getEntity();
			entity.getCapability(FlatProvider.FLAT_CAPABILITY).ifPresent(cap -> cap.sync());
		}
	}

	@SubscribeEvent
	public static void playerStartTracking(PlayerEvent.StartTracking event) {
		if (event.getPlayer() instanceof ServerPlayerEntity && event.getTarget() instanceof LivingEntity) {
			LivingEntity entity = (LivingEntity) event.getTarget();
			entity.getCapability(FlatProvider.FLAT_CAPABILITY).ifPresent(cap -> cap.sync());
		}
	}

	@SubscribeEvent
	public static void playerRespawnEvent(PlayerEvent.PlayerRespawnEvent event) {
		event.getPlayer().getCapability(FlatProvider.FLAT_CAPABILITY).ifPresent(cap -> cap.sync());
	}

	@SubscribeEvent
	public static void playerConnect(PlayerEvent.PlayerLoggedInEvent event) {
		event.getPlayer().getCapability(FlatProvider.FLAT_CAPABILITY).ifPresent(cap -> cap.sync());
	}

	@SubscribeEvent
	public static void playerChangeDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
		event.getPlayer().getCapability(FlatProvider.FLAT_CAPABILITY).ifPresent(cap -> cap.sync());
	}

	@SubscribeEvent
	public static void livingUpdateEvent(LivingUpdateEvent event) {
		event.getEntityLiving().getCapability(FlatProvider.FLAT_CAPABILITY).ifPresent(cap -> cap.tick());

	}

	@SubscribeEvent
	public static void onPlayerFall(PlayerFlyableFallEvent event) {
		PlayerEntity player = event.getPlayer();
		if (DMFlightMode.isInFlight(player)) {
			List<Entity> entities = player.level.getEntities(player, new AxisAlignedBB(player.blockPosition()));
			for (Entity entity : entities) {
				if (entity instanceof LivingEntity) {
					LivingEntity livingEntity = (LivingEntity) entity;
					livingEntity.getCapability(FlatProvider.FLAT_CAPABILITY).ifPresent(cap -> {
						if(!cap.isFlat()) {
							livingEntity.hurt(TardisDamageSource.tardisSquish(player), event.getDistance()/2.0f);							
						}
					});

				}

			}
		}

	}
	
	
	@SubscribeEvent
	public static void onPlayerCreative(PlayerEvent.PlayerChangeGameModeEvent event) {
		if(event.getNewGameMode() == GameType.CREATIVE) {
			event.getPlayer().getCooldowns().removeCooldown(ItemInit.TWODIS.get());
		}
	}

}
