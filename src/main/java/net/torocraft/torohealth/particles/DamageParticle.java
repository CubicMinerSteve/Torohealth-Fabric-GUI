package net.torocraft.torohealth.particles;

import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;

import net.minecraft.client.Camera;
import net.minecraft.client.gui.Font.DisplayMode;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.renderer.feature.TextFeatureRenderer;
import net.minecraft.client.renderer.state.level.QuadParticleRenderState;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.torocraft.torohealth.ToroHealth;
import net.torocraft.torohealth.util.RenderUtils;

public class DamageParticle extends SingleQuadParticle {

	public int damage;

	public DamageParticle(ClientLevel clientLevel, Vec3 pos, Vec3 velocity) {
		super(clientLevel, pos.x, pos.y, pos.z, velocity.x, velocity.y, velocity.z, null);
		this.gravity = 0.25F;
	}

	public void setDamageNumber(@NotNull int dmg) {
		this.damage = dmg;
	}

	@Override
	public void extract(QuadParticleRenderState particleTypeRenderState, Camera camera, float partialTickTime) {
		int i = Math.abs(Math.round(damage));
		if (i == 0) {
			return;
		}

		String s = Integer.toString(i);

		float scaleToGui = 0.025F;
		Vec3 cameraLocation = camera.position();

		float particleX = (float) (Mth.lerp(partialTickTime, xo, x) - cameraLocation.x);
		float particleY = (float) (Mth.lerp(partialTickTime, yo, y) - cameraLocation.y);
		float particleZ = (float) (Mth.lerp(partialTickTime, zo, z) - cameraLocation.z);

		Matrix4f matrix = new Matrix4f();
		matrix = matrix.translation(particleX, particleY, particleZ);
		matrix = matrix.rotate(camera.rotation());
		matrix = matrix.rotate((float) Math.PI, 0.0F, 1.0F, 0.0F);
		matrix = matrix.scale(-scaleToGui, -scaleToGui, scaleToGui);

		int textColor = (damage < 0 ? ToroHealth.CONFIG.particle.healColor : ToroHealth.CONFIG.particle.damageColor) | 0xFF000000;
		RenderUtils.textFeatureRenderPhase.submit(new TextFeatureRenderer.Submit(matrix, 0, 0, Component.literal(s).getVisualOrderText(), false, DisplayMode.NORMAL, 15728880, textColor, 0, 0));
	}

	@Override
	protected Layer getLayer() {
		return Layer.OPAQUE;
	}
}
