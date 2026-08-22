package net.torocraft.torohealth.bars;

import java.util.ArrayList;
import java.util.List;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.phys.Vec3;
import net.torocraft.torohealth.ToroHealth;
import net.torocraft.torohealth.config.Config;
import net.torocraft.torohealth.config.Config.InWorld;
import net.torocraft.torohealth.config.Config.Mode;
import net.torocraft.torohealth.util.EntityUtils;
import net.torocraft.torohealth.util.EntityUtils.Relation;
import net.torocraft.torohealth.util.RenderUtils;

public class HealthBarRenderer {

	public static List<LivingEntity> entities = new ArrayList<LivingEntity>();

	private static final Identifier HEALTH_BAR_IDENTIFIER = Identifier.parse(ToroHealth.MOD_ID + ":textures/gui/bars.png");

	private static final int DARK_GRAY = 0x00808080;
	private static final float FULL_SIZE = 40;

	private static InWorld getConfig() {
		return ToroHealth.CONFIG.inWorld;
	}

	public static void batchRenderInWorld(PoseStack poseStack, SubmitNodeCollector output) {
		Vec3 cameraPos = Minecraft.getInstance().gameRenderer.mainCamera().position();
		for (LivingEntity entity : entities) {
			if (HealthBarRenderer.checkEntity(entity)) {
				HealthBarRenderer.renderInWorld(poseStack, output, entity, cameraPos.x, cameraPos.y, cameraPos.z);
			}
		}
		entities.clear();
	}

	private static boolean checkEntity(LivingEntity entity) {
		Minecraft minecraft = Minecraft.getInstance();
		if (!EntityUtils.shouldShowHealthBarInWorld(entity, minecraft)) {
			return false;
		}
		if (entity.distanceTo(minecraft.getCameraEntity()) > ToroHealth.CONFIG.inWorld.distance) {
			return false;
		}
		HealthBarStates.getState(entity);
		if (Mode.WHEN_HOLDING_WEAPON.equals(getConfig().mode) && !ToroHealth.IS_HOLDING_WEAPON) {
			return false;
		}
		if (Mode.NONE.equals(getConfig().mode)) {
			return false;
		}
		if (ToroHealth.CONFIG.inWorld.onlyWhenLookingAt && ToroHealth.HUD.getEntity() != entity) {
			return false;
		}
		if (ToroHealth.CONFIG.inWorld.onlyWhenHurt && entity.getHealth() >= EntityUtils.limitlessAttribute(entity, Attributes.MAX_HEALTH)) {
			return false;
		}
		return true;
	}

	public static void renderInHud(GuiGraphicsExtractor graphics, LivingEntity entity, double xOffset, double yOffset, float width) {
		// Get entity relation to players.
		Relation relation = EntityUtils.determineRelation(entity);

		// Set First Color and Second Color according to the relation.
		int color = relation.equals(Relation.FRIEND) ? ToroHealth.CONFIG.bar.friendColor : ToroHealth.CONFIG.bar.foeColor;
		int color2 = relation.equals(Relation.FRIEND) ? ToroHealth.CONFIG.bar.friendColorSecondary : ToroHealth.CONFIG.bar.foeColorSecondary;

		HealthBarState state = HealthBarStates.getState(entity);

		double maxHealth = EntityUtils.limitlessAttribute(entity, Attributes.MAX_HEALTH);
		float percent = (float) Math.min(1, Math.min(state.health, maxHealth) / maxHealth);
		float percent2 = (float) (Math.min(state.previousHealthDisplay, maxHealth) / maxHealth);

		drawBarInHud(graphics, xOffset, yOffset, width, 1, DARK_GRAY);
		drawBarInHud(graphics, xOffset, yOffset, width, percent2, color2);
		drawBarInHud(graphics, xOffset, yOffset, width, percent, color);

		if (ToroHealth.CONFIG.bar.damageNumberType.equals(Config.NumberType.CUMULATIVE)) {
			drawDamageNumber(graphics, state.lastDmgCumulative, xOffset, yOffset, width);
		} else if (ToroHealth.CONFIG.bar.damageNumberType.equals(Config.NumberType.LAST)) {
			drawDamageNumber(graphics, state.lastDmg, xOffset, yOffset, width);
		}
	}

	public static void renderInWorld(PoseStack poseStack, SubmitNodeCollector output, LivingEntity entity, double cameraX, double cameraY, double cameraZ) {
		// Get entity relation to players.
		Relation relation = EntityUtils.determineRelation(entity);

		// Set First Color and Second Color according to the relation.
		int color = relation.equals(Relation.FRIEND) ? ToroHealth.CONFIG.bar.friendColor : ToroHealth.CONFIG.bar.foeColor;
		int color2 = relation.equals(Relation.FRIEND) ? ToroHealth.CONFIG.bar.friendColorSecondary : ToroHealth.CONFIG.bar.foeColorSecondary;

		HealthBarState state = HealthBarStates.getState(entity);

		double maxHealth = EntityUtils.limitlessAttribute(entity, Attributes.MAX_HEALTH);
		float percent = (float) Math.min(1, Math.min(state.health, maxHealth) / maxHealth);
		float percent2 = (float) (Math.min(state.previousHealthDisplay, maxHealth) / maxHealth);

		// Z-axis offset to prevent z-fighting.
		int zOffset = 0;

		// Scale of the health bar.
		float scaleToGui = 0.025F;

		// Determine how high the health bar will display above the entity.
		boolean isSneaking = entity.isCrouching();
		float height = entity.getBbHeight() + 0.6F - (isSneaking ? 0.25F : 0.0F);

		// Use 'false' boolean for safety. May change later.
		float partialTickTime = Minecraft.getInstance().getDeltaTracker().getGameTimeDeltaPartialTick(false);

		double x = Mth.lerp((double) partialTickTime, entity.xo, entity.getX());
		double y = Mth.lerp((double) partialTickTime, entity.yo, entity.getY());
		double z = Mth.lerp((double) partialTickTime, entity.zo, entity.getZ());

		poseStack.pushPose();
		poseStack.translate((float) (x - cameraX), (float) (y + height - cameraY), (float) (z - cameraZ));
		poseStack.mulPose(Axis.YP.rotationDegrees(-Minecraft.getInstance().getEntityRenderDispatcher().camera.yRot()));
		poseStack.mulPose(Axis.XP.rotationDegrees(Minecraft.getInstance().getEntityRenderDispatcher().camera.xRot()));
		poseStack.scale(-scaleToGui, -scaleToGui, scaleToGui);

		drawBarInWorld(poseStack, output, 0, 0, FULL_SIZE, 1, DARK_GRAY, zOffset++);
		drawBarInWorld(poseStack, output, 0, 0, FULL_SIZE, percent2, color2, zOffset++);
		drawBarInWorld(poseStack, output, 0, 0, FULL_SIZE, percent, color, zOffset);

		poseStack.popPose();
	}

	private static void drawBarInHud(GuiGraphicsExtractor graphics, double xOffset, double yOffset, float width, float percent, int color) {
		// Horizontal starting point in texture files, which is u in uv format.
		int u = 0;
		// Vertical starting point in texture files, which is v in uv format.
		int v = 6 * 5 * 2 + 5;
		// Horizontal pixel amount. 92 is the actual width of the bar texture.
		int uw = Mth.ceil(92 * percent);
		// Vertical pixel amount. Bar texture's height is 5 in default.
		int vh = 5;
		// Region Width to render. Background is 100% width and so on. Width is 130 in HudBarDisplay.java
		int regionW = Mth.ceil(width * percent);
		// Region Height to render. Default is 6 in hud.
		int regionH = 6;
		// Need to assign texture width and texture height. bar.png is 256 x 256.
		graphics.blit(RenderUtils.TOROHEALTH_HUD_PIPELINE, HEALTH_BAR_IDENTIFIER, (int) xOffset, (int) yOffset, u, v, regionW, regionH, uw, vh, 256, 256, color | 0xFF000000);
	}

	private static void drawBarInWorld(PoseStack poseStack, SubmitNodeCollector output, double x, double y, float width, float percent, int color, int zOffset) {
		// Constant 1 / 256. Represents 1 pixel for both of the width and height in the texture file.
		float c = 0.00390625F;
		int u = 0;
		int v = 6 * 5 * 2 + 5;

		int uw = Mth.ceil(92 * percent);
		int vh = 5;

		int regionW = Mth.ceil(width * percent);
		int regionH = 4;

		int hex = color | 0xFF000000;
		// Half width of the bar.
		float half = width / 2;
		// Get rid of z-fighting.
		float zOffsetAmount = -0.1F;

		RenderType layer = RenderUtils.TOROHEALTH_WORLD_DEPTH_TEST.apply(HEALTH_BAR_IDENTIFIER);
		// .texture() method requires the percentage of texture file's width and height.
		output.submitCustomGeometry(poseStack, layer,
			(pose, buffer) -> {
				buffer.addVertex(pose, (float) (-half + x), (float) y, zOffset * zOffsetAmount).setUv(u * c, v * c).setColor(hex);
				buffer.addVertex(pose, (float) (-half + x), (float) (regionH + y), zOffset * zOffsetAmount).setUv(u * c, (v + vh) * c).setColor(hex);
				buffer.addVertex(pose, (float) (-half + regionW + x), (float) (regionH + y), zOffset * zOffsetAmount).setUv((u + uw) * c, (v + vh) * c).setColor(hex);
				buffer.addVertex(pose, (float) (-half + regionW + x), (float) y, zOffset * zOffsetAmount).setUv((u + uw) * c, v * c).setColor(hex);
			}
		);
	}

	public static void drawDamageNumber(GuiGraphicsExtractor graphics, int dmg, double xOffset, double yOffset, float width) {
		int i = Math.abs(Math.round(dmg));
		if (i == 0) {
			return;
		}
		String s = Integer.toString(i);

		Minecraft minecraft = Minecraft.getInstance();
		int sw = minecraft.font.width(s);
		int color = (dmg < 0 ? ToroHealth.CONFIG.particle.healColor : ToroHealth.CONFIG.particle.damageColor) | 0xFF000000;

		graphics.text(minecraft.font, s, (int) (width + xOffset) - 2 - sw, (int) yOffset + 10, color);
	}

}
