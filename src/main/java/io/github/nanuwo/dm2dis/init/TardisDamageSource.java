package io.github.nanuwo.dm2dis.init;

import io.github.nanuwo.dm2dis.DM2DIS;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.EntityDamageSource;

public class TardisDamageSource {
	
	public static EntityDamageSource tardisSquish(PlayerEntity player) {
		return new EntityDamageSource(DM2DIS.MODID+ ".tardis_squished", player);
	}

}
