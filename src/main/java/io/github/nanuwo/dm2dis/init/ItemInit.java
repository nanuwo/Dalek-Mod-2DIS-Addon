package io.github.nanuwo.dm2dis.init;

import io.github.nanuwo.dm2dis.DM2DIS;
import io.github.nanuwo.dm2dis.item.TwoDis;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemInit {
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, DM2DIS.MODID);
	
	public static final RegistryObject<Item> TWODIS = ITEMS.register("2dis", () -> 
	new TwoDis(new Item.Properties().stacksTo(1).durability(64).tab(ItemGroup.TAB_TOOLS)));

	
}
