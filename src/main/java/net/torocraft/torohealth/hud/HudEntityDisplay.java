package net.torocraft.torohealth.hud;

import org.joml.Quaternionf;
import org.joml.Vector3f;

import com.mojang.math.Axis;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.navigation.ScreenPosition;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ambient.Bat;
import net.minecraft.world.entity.animal.allay.Allay;
import net.minecraft.world.entity.animal.chicken.Chicken;
import net.minecraft.world.entity.animal.dolphin.Dolphin;
import net.minecraft.world.entity.animal.equine.AbstractHorse;
import net.minecraft.world.entity.animal.golem.CopperGolem;
import net.minecraft.world.entity.monster.Guardian;
import net.minecraft.world.entity.monster.Silverfish;
import net.minecraft.world.entity.monster.Vex;
import net.minecraft.world.entity.npc.villager.Villager;

public class HudEntityDisplay {

	private static final float TRANSFORM_HEIGHT = 30;
	private static final float TRANSFORM_WIDTH = 18;

	private LivingEntity entity;
	private Vector3f entityBlockOffset;
	private float entitySize = 1;

	public void setEntity(LivingEntity entity) {
		this.entity = entity;
		updateEntityScale();
	}

	private void updateEntityScale() {
		if (entity == null) {
			return;
		}

		int scaleY = Mth.ceil(TRANSFORM_HEIGHT / entity.getBbHeight());
		int scaleX = Mth.ceil(TRANSFORM_WIDTH / entity.getBbWidth());
		entitySize = Math.min(scaleX, scaleY);

		entityBlockOffset = new Vector3f(0.0F, entity.getBbHeight() / 2, 0.0F);
		if (entity instanceof Chicken || entity instanceof CopperGolem || entity instanceof Silverfish) {
			entitySize *= 0.7;
			entityBlockOffset.add(0.0F, entity.getBbHeight() / 4.0F, 0.0F);
		} else if (entity instanceof AbstractHorse) {
			entityBlockOffset.add(-entity.getBbWidth() / 8.0F, entity.getBbHeight() / 16.0F, 0.0F);
		} else if (entity instanceof Villager && entity.isSleeping()) {
			entitySize = entity.isBaby() ? 31 : 16;
		} else if (entity instanceof Guardian || entity instanceof Dolphin) {
			entityBlockOffset.add(entity.getBbWidth() / 8.0F, 0.0F, 0.0F);
		} else if (entity instanceof Allay || entity instanceof Vex || entity instanceof Bat) {
			entitySize *= 0.8;
		}
	}

	public void draw(GuiGraphicsExtractor graphics, float xPos, float yPos, float hudScale) {
		if (entity != null) {
			try {
				drawEntity(graphics, entity, xPos, yPos, hudScale, entitySize, entityBlockOffset);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void drawEntity(GuiGraphicsExtractor graphics, LivingEntity entity, float xPos, float yPos, float hudScale, float entitySize, Vector3f entityBlockOffset) {
		float f = (float) Math.atan(-2.0D);
		float g = (float) Math.atan(-0.5D);

		// I hate quaternions.
		Quaternionf zRotation = Axis.ZN.rotationDegrees(180.0F);
		Quaternionf xRotation = Axis.XP.rotationDegrees(g * 20.0F);
		zRotation.mul(xRotation);

		// Store entity's facing position for default.
		float h = entity.yBodyRot;
		float i = entity.getYRot();
		float j = entity.getXRot();
		float k = entity.yHeadRotO;
		float l = entity.yHeadRot;

		// Uniform different entity's facing for display.
		entity.yBodyRot = 180.0F + f * 20.0F;
		entity.setYRot(180.0F + f * 40.0F);
		entity.setXRot(-g * 20.0F);
		entity.yHeadRot = entity.getYRot();
		entity.yHeadRotO = entity.getYRot();

		// Get current entity's RenderState and client's EntityRenderDispatcher.
		EntityRenderDispatcher renderDispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
		EntityRenderState renderState = renderDispatcher.getRenderer(entity).createRenderState(entity, 1.0F);
		renderState.shadowRadius = 0.0F;
		renderState.lightCoords = 15728880;

		// Substract 5 from x to conform rotataion. Area width is 49 without scaling.
		int left = Math.round(xPos);
		int right = Math.round(xPos + 36 * hudScale);

		// Keep intact for y. Area and height is 70 without scaling.
		int top = Math.round(yPos);
		int bottom = Math.round(yPos + 36 * hudScale);

		// Push the scissorStack to crop the rendered screen area.
		graphics.scissorStack.push(new ScreenRectangle(new ScreenPosition(left, top), Math.round(36 * hudScale), Math.round(36 * hudScale)));
		// Add entity to GuiGraphicsExtractor and let it handle.
		graphics.entity(renderState, hudScale * entitySize, entityBlockOffset, zRotation, xRotation, left, top, right, bottom);

		// Pop the scissorStack to disable the crop.
		graphics.scissorStack.pop();

		// Set entity's facing status as default.
		entity.yBodyRot = h;
		entity.setYRot(i);
		entity.setXRot(j);
		entity.yHeadRotO = k;
		entity.yHeadRot = l;
	}

}
