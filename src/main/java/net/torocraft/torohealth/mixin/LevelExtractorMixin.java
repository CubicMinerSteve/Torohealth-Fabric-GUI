package net.torocraft.torohealth.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.extract.LevelExtractor;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.torocraft.torohealth.ToroHealth;
import net.torocraft.torohealth.bars.HealthBarRenderer;

@Mixin(LevelExtractor.class)
public class LevelExtractorMixin {
    
	@Inject(method = "extractEntity", at = @At(value = "RETURN"))
	private void getAndUpdateRenderState(Entity entity, float partialTickTime, CallbackInfoReturnable<EntityRenderState> info) {
		if (ToroHealth.CONFIG.enabled) {
			if (entity instanceof LivingEntity) {
				HealthBarRenderer.entities.add((LivingEntity) entity);
			}
		}
	}
}
