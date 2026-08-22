package net.torocraft.torohealth;

import java.util.Random;

import org.lwjgl.glfw.GLFW;

import com.mojang.blaze3d.platform.InputConstants;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.KeyMapping.Category;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;
import net.torocraft.torohealth.config.Config;
import net.torocraft.torohealth.config.loader.ConfigLoader;
import net.torocraft.torohealth.hud.HudConfigScreen;
import net.torocraft.torohealth.hud.HudTorohealth;
import net.torocraft.torohealth.util.EntityGetter;

public class ToroHealth implements ClientModInitializer {

	public static final String MOD_ID = "torohealth";

	public static Config CONFIG = new Config();
	public static HudTorohealth HUD = new HudTorohealth();
	public static EntityGetter GETTER = new EntityGetter();
	public static Random RAND = new Random();

	public static boolean IS_HOLDING_WEAPON = false;
	
	public static ConfigLoader<Config> CONFIG_LOADER = new ConfigLoader<>(
			new Config(),
			ToroHealth.MOD_ID + ".json",
			config -> ToroHealth.CONFIG = config
	);

	public static Category TOROHEALTH = Category.register(Identifier.parse(MOD_ID));

	private KeyMapping keySettings;

	@Override
	public void onInitializeClient() {
		CONFIG_LOADER.load();

		keySettings = KeyBindingHelper.registerKeyBinding(new KeyMapping(
				"key.torohealth.settings",
				InputConstants.Type.KEYSYM,
				GLFW.GLFW_KEY_UNKNOWN,
				TOROHEALTH
		));
		ClientTickEvents.END_CLIENT_TICK.register(this::tick);
	}

	public void tick(Minecraft minecraft) {
		if (keySettings.consumeClick()) {
			minecraft.setScreenAndShow(HudConfigScreen.buildConfigScreen(minecraft.screen));
		}
	}
}
