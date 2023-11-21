package io.github.nanuwo.dm2dis.item;

import io.github.nanuwo.dm2dis.capability.FlatProvider;
import io.github.nanuwo.dm2dis.init.SoundEvents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

public class TwoDis extends Item {
	
	private long previousUseTime;

	public TwoDis(Item.Properties properties) {
		super(properties);
	}

	@Override
	public ActionResultType interactLivingEntity(ItemStack itemStack, PlayerEntity player, LivingEntity entity, Hand hand) {
		World level = player.level;
		if (entity.getCapability(FlatProvider.FLAT_CAPABILITY).isPresent()&&!player.getCooldowns().isOnCooldown(itemStack.getItem())) {
			entity.getCapability(FlatProvider.FLAT_CAPABILITY).ifPresent(cap -> {
				if (cap.getLastFlattened() == 0 && !cap.isFlat()) {
					cap.setFlat(true);
					useSuccess(level, player,hand);					
				} else {
					useError(level, player);
				}
			});
		} else {
			useError(level, player);	
		}
		return ActionResultType.sidedSuccess(player.level.isClientSide);

	}
	@Override
	public ActionResult<ItemStack> use(World level, PlayerEntity player, Hand hand) {
		if (player.isShiftKeyDown()) {
			player.getCapability(FlatProvider.FLAT_CAPABILITY).ifPresent(cap -> {
				if (cap.isFlat()) {
					cap.setFlat(false);
					useSuccess(level,player,hand);
				} 					
			});
		}
		return super.use(level, player, hand);
	}

	public void useSuccess(World level, PlayerEntity player, Hand hand) {
		if(!player.isCreative()) {
			player.getCooldowns().addCooldown(player.getItemInHand(hand).getItem(), 2400);
		}
		level.playSound(player, player.blockPosition(), SoundEvents.TWODIS_USE.get(), SoundCategory.PLAYERS, 1.0F, 1.0F);
		player.getItemInHand(hand).hurtAndBreak(1, player, livingEntity -> livingEntity.broadcastBreakEvent(hand));

	}

	public void useError(World level, PlayerEntity player) {
		if (previousUseTime + 10 <= level.getGameTime()) {
			level.playSound(player, player.blockPosition(), SoundEvents.TWODIS_ERROR.get(), SoundCategory.PLAYERS, 1.0F, 1.0F);
			previousUseTime = level.getGameTime();
		}
	}

}
