package io.github.nanuwo.dm2dis.init;

import io.github.nanuwo.dm2dis.DM2DIS;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class SoundEvents {
	
	public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, DM2DIS.MODID);
	
	public static final RegistryObject<SoundEvent> TWODIS_USE = register("item.2dis_use");
	
	public static final RegistryObject<SoundEvent> TWODIS_ERROR = register("item.2dis_error");
	
	public static RegistryObject<SoundEvent> register(String location) {
		return SOUNDS.register(location, () -> new SoundEvent(new ResourceLocation(DM2DIS.MODID, location)));
	}
}
