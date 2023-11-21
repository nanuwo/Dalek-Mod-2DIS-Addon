package io.github.nanuwo.dm2dis.event;

import io.github.nanuwo.dm2dis.DM2DIS;
import io.github.nanuwo.dm2dis.capability.IFlatCapability;
import io.github.nanuwo.dm2dis.capability.FlatCapability;
import io.github.nanuwo.dm2dis.capability.FlatStorage;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(modid = DM2DIS.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEvents {
	
	@SubscribeEvent
	public static void registerCapabilities(FMLCommonSetupEvent event) {
		CapabilityManager.INSTANCE.register(IFlatCapability.class, new FlatStorage(), () -> new FlatCapability(null));
	}
	
	
	
	
	
}
