package net.torocraft.torohealth.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.torocraft.torohealth.ToroHealth;
import net.torocraft.torohealth.bars.HealthBarStates;
import net.torocraft.torohealth.util.WeaponChecker;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity {

	protected PlayerMixin(EntityType<? extends LivingEntity> type, Level level) {
		super(type, level);
	}

	@Inject(method = "tick", at = @At("HEAD"))
	private void tick(CallbackInfo info) {
		if (ToroHealth.CONFIG.enabled) {
			Level level = this.level();
			if (!level.isClientSide()) {
				return;
			}
			ToroHealth.HUD.setEntity(ToroHealth.GETTER.getLivingEntityInCrosshair(0, ToroHealth.CONFIG.hud.distance));
			HealthBarStates.tick();
			WeaponChecker.update();
			ToroHealth.HUD.tick();
		}
	}

}
