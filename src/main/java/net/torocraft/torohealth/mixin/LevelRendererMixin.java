package net.torocraft.torohealth.mixin;

import java.util.ArrayList;
import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.state.level.LevelRenderState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import net.torocraft.torohealth.ToroHealth;
import net.torocraft.torohealth.bars.HealthBarRenderer;

@Mixin(LevelRenderer.class)
public class LevelRendererMixin {

	private Camera camera = Minecraft.getInstance().gameRenderer.getMainCamera();

	private List<LivingEntity> entities = new ArrayList<LivingEntity>();

	@Inject(method = "extractEntity", at = @At(value = "RETURN"))
	private void getAndUpdateRenderState(Entity entity, float partialTickTime, CallbackInfoReturnable<EntityRenderState> info) {
		if (ToroHealth.CONFIG.enabled) {
			if (entity instanceof LivingEntity) {
				this.entities.add((LivingEntity) entity);
			}
		}
	}

	@Inject(method = "submitEntities", at = @At(value = "RETURN"))
	private void pushEntityRenders(PoseStack poseStack, LevelRenderState levelRenderState, SubmitNodeCollector output, CallbackInfo info) {
		if (ToroHealth.CONFIG.enabled) {
			Vec3 cameraPos = camera.position();
			for (LivingEntity entity : entities) {
				if (HealthBarRenderer.checkEntity(entity)) {
					HealthBarRenderer.renderInWorld(poseStack, entity, cameraPos.x, cameraPos.y, cameraPos.z);
				}
			}
			entities.clear();
		}
	}

}
