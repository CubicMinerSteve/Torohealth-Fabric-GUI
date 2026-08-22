package net.torocraft.torohealth.particles;

import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import net.torocraft.torohealth.ToroHealth;

public class DamageParticleRenderer {

	public static void renderParticle(Entity entity, int lastDamage) {
		Minecraft minecraft = Minecraft.getInstance();
		ClientLevel world = minecraft.level;

		if (world == null || world != entity.level()) {
			return;
		}

		Camera camera = minecraft.gameRenderer.getMainCamera();
		Vec3 entityLocation = entity.position().add(0, entity.getBbHeight() / 2, 0);

		double distanceSquared = camera.position().distanceToSqr(entityLocation);
		if (distanceSquared > ToroHealth.CONFIG.particle.distanceSquared) {
			return;
		}

		double offsetBy = entity.getBbWidth();
		Vec3 offset = camera.position().subtract(entityLocation).normalize().scale(offsetBy);
		Vec3 particlePos = entityLocation.add(offset);

		Vec3 particleVelocity = entity.getDeltaMovement();
		double velocityX = ToroHealth.RAND.nextGaussian() * 0.04;
		double velocityY = 0.10 + (ToroHealth.RAND.nextGaussian() * 0.05);
		double velocityZ = ToroHealth.RAND.nextGaussian() * 0.04;

		particleVelocity = particleVelocity.add(velocityX, velocityY, velocityZ);

		DamageParticle particle = new DamageParticle(world, particlePos, particleVelocity);
		particle.setDamageNumber(lastDamage);
		particle.setLifetime(50);
		minecraft.particleEngine.add(particle);
	}
}
