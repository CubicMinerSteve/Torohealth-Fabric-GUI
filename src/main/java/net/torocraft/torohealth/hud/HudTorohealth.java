package net.torocraft.torohealth.hud;

import org.joml.Matrix3x2fStack;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.torocraft.torohealth.ToroHealth;
import net.torocraft.torohealth.config.Config;
import net.torocraft.torohealth.config.Config.AnchorPoint;
import net.torocraft.torohealth.util.EntityUtils;
import net.torocraft.torohealth.util.RenderUtils;

public class HudTorohealth extends Screen {

	private static final Identifier BACKGROUND_TEXTURE = Identifier.parse(ToroHealth.MOD_ID + ":textures/gui/default_skin_basic.png");
	private Config config = new Config();
	private HudEntityDisplay entityDisplay = new HudEntityDisplay();
	private LivingEntity entity;

	private HudBarDisplay barDisplay;
	private int age;

	private int hudWidth;
	private int hudTranslation;

	private final int HUD_HEIGHT = 60;
	private final int BAR_OFFSET = 44;

	public HudTorohealth() {
		super(Component.literal("ToroHealth HUD"));
		barDisplay = new HudBarDisplay(this.minecraft);
	}

	public void draw(GuiGraphicsExtractor graphics, Config config) {
		if (this.minecraft.getDebugOverlay().showDebugScreen() || this.minecraft.options.hideGui) {
			return;
		}
		this.config = config;
		if (this.config == null) {
			this.config = new Config();
		}

		updateHudWidth();
		float xPos = determineHudXPos();
		float yPos = determineHudYPos();
		drawHud(graphics, xPos, yPos, config.hud.scale);
	}

	public void updateHudWidth() {
		if (config.hud.showEntity) {
			// hudWidth = 182 = 130 (Bar) + 44 (Entity Display) + 8 (Display Translation)
			hudWidth = 182;
			hudTranslation = 8;
		} else {
			// Only need 130 (Bar) here since no entity is rendered.
			hudWidth = 130;
			hudTranslation = 0;
		}
	}

	public float determineHudXPos() {
		float xPos = config.hud.x;
		AnchorPoint anchor = config.hud.anchorPoint;
		float wScreen = minecraft.getWindow().getGuiScaledWidth();

		switch (anchor) {
			case BOTTOM_CENTER:
			case TOP_CENTER:
				return wScreen / 2 + (hudTranslation - hudWidth / 2) * config.hud.scale + xPos - 4;
			case BOTTOM_RIGHT:
			case TOP_RIGHT:
				return wScreen + (hudTranslation - hudWidth) * config.hud.scale + xPos - 8;
			default:
				return xPos;
		}
	}

	public float determineHudYPos() {
		float yPos = config.hud.y;
		AnchorPoint anchor = config.hud.anchorPoint;
		float hScreen = minecraft.getWindow().getGuiScaledHeight();

		switch (anchor) {
			case BOTTOM_CENTER:
			case BOTTOM_LEFT:
			case BOTTOM_RIGHT:
				return hScreen - HUD_HEIGHT * config.hud.scale + yPos;
			default:
				return yPos;
		}
	}

	private void drawHud(GuiGraphicsExtractor graphics, float xPos, float yPos, float hudScale) {
		if (entity == null) {
			return;
		}
		if (config.hud.onlyWhenHurt && entity.getHealth() >= EntityUtils.limitlessAttribute(entity, Attributes.MAX_HEALTH)) {
			return;
		}

		Matrix3x2fStack matrix = graphics.pose();
		matrix.pushMatrix();
		matrix.scaleAround(hudScale, hudScale, xPos, yPos);
		matrix.translate(xPos, yPos);
		if (config.hud.showEntity) {
			if (config.hud.showSkin) {
				drawSkin(graphics);
			}
			drawEntity(graphics, xPos, yPos, hudScale);
		}
		if (config.hud.showBar) {
			matrix.translate(BAR_OFFSET, 0);
			barDisplay.draw(graphics, entity);
		}
		matrix.popMatrix();
	}

	private void drawSkin(GuiGraphicsExtractor graphics) {
		// Remain full size for further implementation.
		int w = 182, h = 60;

		Matrix3x2fStack matrix = graphics.pose();
		matrix.translate(-8, -8);
		graphics.blit(RenderUtils.TOROHEALTH_HUD_PIPELINE, BACKGROUND_TEXTURE, 0, 0, 0.0F, 0.0F, w, h, w, h);
		matrix.translate(8, 8);
	}

	private void drawEntity(GuiGraphicsExtractor graphics, float xPos, float yPos, float hudScale) {
		// HudEntityDisplay.java uses ScreenPosition, which takes in absolute Screen coords.
		// However, the matrix here only changes the GuiGraphicsExtractor's local origin and scaling.
		// We need to convert the relative 4-pixel offset in .png files to an absolute one.

		Matrix3x2fStack matrix = graphics.pose();
		matrix.translate(4, 4);
		entityDisplay.draw(graphics, xPos + 4 * hudScale, yPos + 4 * hudScale, hudScale);
		matrix.translate(-4, -4);
	}

	public void tick() {
		age++;
	}

	public void setEntity(LivingEntity entity) {
		if (entity != null) {
			age = 0;
		}
		if (entity == null && age > config.hud.hideDelay) {
			setEntityWork(null);
		}
		if (entity != null && entity != this.entity) {
			setEntityWork(entity);
		}
	}

	private void setEntityWork(LivingEntity entity) {
		this.entity = entity;
		entityDisplay.setEntity(entity);
	}

	public LivingEntity getEntity() {
		return entity;
	}

}
