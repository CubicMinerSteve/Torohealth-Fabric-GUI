package net.torocraft.torohealth.bars;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class HealthBarStates {

	private static final Map<Integer, HealthBarState> STATES = new HashMap<>();
	private static int tickCount = 0;

	public static HealthBarState getState(LivingEntity entity) {
		int id = entity.getId();
		HealthBarState state = STATES.get(id);
		if (state == null) {
			state = new HealthBarState(entity);
			STATES.put(id, state);
		}
		return state;
	}

	public static void tick() {
		for (HealthBarState state : STATES.values()) {
			state.tick();
		}
		if (tickCount % 200 == 0) {
			cleanCache();
		}
		tickCount++;
	}

	private static void cleanCache() {
		STATES.entrySet().removeIf(HealthBarStates::stateExpired);
	}

	private static boolean stateExpired(Map.Entry<Integer, HealthBarState> entry) {
		if (entry.getValue() == null) {
			return true;
		}

		Minecraft minecraft = Minecraft.getInstance();
		Entity entity = minecraft.level.getEntity(entry.getKey());

		if (!(entity instanceof LivingEntity)) {
			return true;
		}
		if (!minecraft.level.hasChunk(entity.blockPosition().getX(), entity.blockPosition().getZ())) {
			return true;
		}

		return !entity.isAlive();
	}

}
