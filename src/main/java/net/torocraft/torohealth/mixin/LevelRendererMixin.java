package net.torocraft.torohealth.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.state.level.LevelRenderState;
import net.torocraft.torohealth.ToroHealth;
import net.torocraft.torohealth.bars.HealthBarRenderer;

@Mixin(LevelRenderer.class)
public class LevelRendererMixin {

	@Inject(method = "submitEntities", at = @At(value = "RETURN"))
	private void pushEntityRenders(PoseStack poseStack, LevelRenderState levelRenderState, SubmitNodeCollector output, CallbackInfo info) {
		if (ToroHealth.CONFIG.enabled) {
			HealthBarRenderer.batchRenderInWorld(poseStack, output);
		}
	}
}
