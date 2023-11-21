package net.minecraft.client.renderer.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import io.github.nanuwo.dm2dis.DM2DIS;
import io.github.nanuwo.dm2dis.capability.FlatProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Pose;
import net.minecraft.util.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = DM2DIS.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class RenderEvent {

	public static void render2disDimensions(float f, MatrixStack matrixStack, LivingEntity entity, float partialTicks) {
		entity.getCapability(FlatProvider.FLAT_CAPABILITY).ifPresent(cap -> {
			ClientPlayerEntity player = Minecraft.getInstance().player;
			double x =  entity.getX() - player.getX();
			double z = entity.getZ() - player.getZ();
			double angle1 =  MathHelper.wrapDegrees(Math.atan2(x,z) / Math.PI * 180.0D);
			double angle2 = Math.floor((f - angle1) / 45.0D) * 45.0D;
			matrixStack.mulPose(Vector3f.YP.rotationDegrees((float) angle1+90f));
			matrixStack.scale(0.1f, 1.0f, 1.0f);
			matrixStack.mulPose(Vector3f.YP.rotationDegrees((float) angle2));
		});
	}
	//Copied vanilla code because mixins weren't working and the matrixstack scaling is weird
	@SubscribeEvent
	public static void entityRenderEvent(RenderLivingEvent.Pre<LivingEntity, EntityModel<LivingEntity>> event) {
		LivingEntity entity = event.getEntity();
		entity.getCapability(FlatProvider.FLAT_CAPABILITY).ifPresent(cap -> {
			if(cap.isFlat()) {
				MatrixStack matrixStack = event.getMatrixStack();
				float partialTicks = event.getPartialRenderTick();
				IRenderTypeBuffer buffer = event.getBuffers();
				int packedLight = event.getLight();
				LivingRenderer<LivingEntity, EntityModel<LivingEntity>> renderer = event.getRenderer();
				event.setCanceled(true);
				matrixStack.pushPose();
				renderer.getModel().attackTime = renderer.getAttackAnim(entity, partialTicks);
				boolean shouldSit = entity.isPassenger()
						&& (entity.getVehicle() != null && entity.getVehicle().shouldRiderSit());
				renderer.getModel().riding = shouldSit;
				renderer.getModel().young = entity.isBaby();
				float f = MathHelper.rotLerp(partialTicks, entity.yBodyRotO, entity.yBodyRot);
				float f1 = MathHelper.rotLerp(partialTicks, entity.yHeadRotO, entity.yHeadRot);
				float f2 = f1 - f;
				if (shouldSit && entity.getVehicle() instanceof LivingEntity) {
					LivingEntity livingentity = (LivingEntity) entity.getVehicle();
					f = MathHelper.rotLerp(partialTicks, livingentity.yBodyRotO, livingentity.yBodyRot);
					f2 = f1 - f;
					float f3 = MathHelper.wrapDegrees(f2);
					if (f3 < -85.0F) {
						f3 = -85.0F;
					}
					
					if (f3 >= 85.0F) {
						f3 = 85.0F;
					}
					
					f = f1 - f3;
					if (f3 * f3 > 2500.0F) {
						f += f3 * 0.2F;
					}
					
					f2 = f1 - f;
				}
				
				render2disDimensions(f, matrixStack, entity, partialTicks);
				float f6 = MathHelper.lerp(partialTicks, entity.xRotO, entity.xRot);
				if (entity.getPose() == Pose.SLEEPING) {
					Direction direction = entity.getBedOrientation();
					if (direction != null) {
						float f4 = entity.getEyeHeight(Pose.STANDING) - 0.1F;
						matrixStack.translate((double) ((float) (-direction.getStepX()) * f4), 0.0D,
								(double) ((float) (-direction.getStepZ()) * f4));
					}
				}
				
				float f7 = renderer.getBob(entity, partialTicks);
				renderer.setupRotations(entity, matrixStack, f7, f, partialTicks);
				matrixStack.scale(-1.0F, -1.0F, 1.0F);
				renderer.scale(entity, matrixStack, partialTicks);
				matrixStack.translate(0.0D, (double) -1.501F, 0.0D);
				float f8 = 0.0F;
				float f5 = 0.0F;
				if (!shouldSit && entity.isAlive()) {
					f8 = MathHelper.lerp(partialTicks, entity.animationSpeedOld, entity.animationSpeed);
					f5 = entity.animationPosition - entity.animationSpeed * (1.0F - partialTicks);
					if (entity.isBaby()) {
						f5 *= 3.0F;
					}
					
					if (f8 > 1.0F) {
						f8 = 1.0F;
					}
				}
				
				renderer.getModel().prepareMobModel(entity, f5, f8, partialTicks);
				renderer.getModel().setupAnim(entity, f5, f8, f7, f2, f6);
				Minecraft minecraft = Minecraft.getInstance();
				boolean flag = renderer.isBodyVisible(entity);
				boolean flag1 = !flag && !entity.isInvisibleTo(minecraft.player);
				boolean flag2 = minecraft.shouldEntityAppearGlowing(entity);
				RenderType rendertype = renderer.getRenderType(entity, flag, flag1, flag2);
				if (rendertype != null) {
					IVertexBuilder ivertexbuilder = buffer.getBuffer(rendertype);
					int i = LivingRenderer.getOverlayCoords(entity, renderer.getWhiteOverlayProgress(entity, partialTicks));
					renderer.getModel().renderToBuffer(matrixStack, ivertexbuilder, packedLight, i, 1.0F, 1.0F, 1.0F,
							flag1 ? 0.15F : 1.0F);
				}
				
				if (!entity.isSpectator()) {
					for (LayerRenderer<LivingEntity, EntityModel<LivingEntity>> layerrenderer : renderer.layers) {
						layerrenderer.render(matrixStack, buffer, packedLight, entity, f5, f8, partialTicks, f7, f2, f6);
					}
				}
				
				matrixStack.popPose();
				net.minecraftforge.client.event.RenderNameplateEvent renderNameplateEvent = new net.minecraftforge.client.event.RenderNameplateEvent(
						entity, entity.getDisplayName(), renderer, matrixStack, buffer, packedLight, partialTicks);
				net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(renderNameplateEvent);
				if (renderNameplateEvent.getResult() != net.minecraftforge.eventbus.api.Event.Result.DENY
						&& (renderNameplateEvent.getResult() == net.minecraftforge.eventbus.api.Event.Result.ALLOW
						|| renderer.shouldShowName(entity))) {
					renderer.renderNameTag(entity, renderNameplateEvent.getContent(), matrixStack, buffer, packedLight);
				}
				net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(
						new net.minecraftforge.client.event.RenderLivingEvent.Post<LivingEntity, EntityModel<LivingEntity>>(
								entity, renderer, partialTicks, matrixStack, buffer, packedLight));
				
			}
		});
	}
	

}
