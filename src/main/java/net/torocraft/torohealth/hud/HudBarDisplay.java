package net.torocraft.torohealth.hud;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.torocraft.torohealth.bars.HealthBarRenderer;
import net.torocraft.torohealth.util.EntityUtils;
import net.torocraft.torohealth.util.RenderUtils;

public class HudBarDisplay {

	private static final Identifier HEART_CONTAINER = Identifier.parse("textures/gui/sprites/hud/heart/container.png");
	private static final Identifier HEART_FULL = Identifier.parse("textures/gui/sprites/hud/heart/full.png");
	private static final Identifier ARMOR_FULL = Identifier.parse("textures/gui/sprites/hud/armor_full.png");

	private final Minecraft mc;

	public HudBarDisplay(Minecraft mc) {
		this.mc = mc;
	}

	public void draw(GuiGraphics graphics, LivingEntity entity) {

		// The starting offset of horizontal position.
		int xOffset = 2;
		Font font = mc.font;

		HealthBarRenderer.renderInHud(graphics, entity, 0, 14, 130);

		String name = getEntityName(entity);
		int healthMax = Mth.ceil(EntityUtils.limitlessAttribute(entity, Attributes.MAX_HEALTH));
		int healthCur = Mth.ceil(entity.getHealth());
		String healthText = healthCur + "/" + healthMax;

		graphics.drawString(font, name, xOffset, 4, 0xFFE0E0E0);
		xOffset += font.width(name) + 5;

		renderHeartIcon(graphics, xOffset, 4);
		xOffset += 10;

		graphics.drawString(font, healthText, xOffset, 4, 0xFFE0E0E0);
		xOffset += font.width(healthText) + 5;

		int armor = entity.getArmorValue();
		if (armor > 0) {
			renderArmorIcon(graphics, xOffset, 4);
			xOffset += 10;
			graphics.drawString(font, entity.getArmorValue() + "", xOffset, 4, 0xFFE0E0E0);
		}
	}

	private String getEntityName(LivingEntity entity) {
		return entity.getDisplayName().getString();
	}

	private void renderArmorIcon(GuiGraphics graphics, int xOffset, int yOffset) {
		graphics.blit(RenderUtils.TOROHEALTH_HUD_PIPELINE, ARMOR_FULL, xOffset, yOffset, 0, 0, 9, 9, 9, 9);
	}

	private void renderHeartIcon(GuiGraphics graphics, int xOffset, int yOffset) {
		graphics.blit(RenderUtils.TOROHEALTH_HUD_PIPELINE, HEART_CONTAINER, xOffset, yOffset, 0, 0, 9, 9, 9, 9);
		graphics.blit(RenderUtils.TOROHEALTH_HUD_PIPELINE, HEART_FULL, xOffset, yOffset, 0, 0, 9, 9, 9, 9);
	}

}
