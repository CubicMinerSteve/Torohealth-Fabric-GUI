package net.torocraft.torohealth.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.Hud;
import net.torocraft.torohealth.ToroHealth;

@Mixin(Hud.class)
public class HudMixin {

	@Inject(method = "extractRenderState", at = @At("RETURN"))
	private void render(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker, CallbackInfo info) {
		if (ToroHealth.CONFIG.enabled) {
			ToroHealth.HUD.draw(graphics, ToroHealth.CONFIG);
		}
	}

}
