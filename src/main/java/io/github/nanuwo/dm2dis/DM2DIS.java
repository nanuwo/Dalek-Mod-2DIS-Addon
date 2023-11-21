package io.github.nanuwo.dm2dis;

import io.github.nanuwo.dm2dis.init.ItemInit;
import io.github.nanuwo.dm2dis.init.SoundEvents;
import io.github.nanuwo.dm2dis.network.PacketHandler;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("dm2dis")
public class DM2DIS {
	
	public static final String MODID = "dm2dis";
	public DM2DIS() {
		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
		ItemInit.ITEMS.register(bus);
		SoundEvents.SOUNDS.register(bus);
		PacketHandler.register();
	}
   
    
}
