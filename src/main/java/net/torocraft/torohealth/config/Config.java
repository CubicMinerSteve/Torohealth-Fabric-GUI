package net.torocraft.torohealth.config;

import com.google.gson.annotations.JsonAdapter;

import net.torocraft.torohealth.config.loader.ColorJsonAdapter;
import net.torocraft.torohealth.config.loader.IConfig;

public class Config implements IConfig {

	public boolean enabled = true;

	public enum Mode {
		NONE, WHEN_HOLDING_WEAPON, ALWAYS
	}

	public enum NumberType {
		NONE, LAST, CUMULATIVE
	}

	public enum AnchorPoint {
		TOP_LEFT, TOP_CENTER, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_CENTER, BOTTOM_RIGHT
	}

	public boolean watchForChanges = true;
	public Hud hud = new Hud();
	public Bar bar = new Bar();
	public InWorld inWorld = new InWorld();
	public Particle particle = new Particle();

	public static class Hud {
		public int distance = 60;
		public float x = 4F;
		public float y = 4F;
		public float scale = 1;
		public int hideDelay = 20;
		public AnchorPoint anchorPoint = AnchorPoint.TOP_LEFT;
		public boolean showEntity = true;
		public boolean showBar = true;
		public boolean showSkin = true;
		public boolean onlyWhenHurt = false;
	}

	public static class Particle {
		public boolean show = true;
		@JsonAdapter(ColorJsonAdapter.class)
		public int damageColor = 0xFF0000;
		@JsonAdapter(ColorJsonAdapter.class)
		public int healColor = 0x00FF00;
		public int distance = 60;
		public transient int distanceSquared = 0;
	}

	public static class Bar {
		public NumberType damageNumberType = NumberType.LAST;
		@JsonAdapter(ColorJsonAdapter.class)
		public int friendColor = 0x00FF00;
		@JsonAdapter(ColorJsonAdapter.class)
		public int friendColorSecondary = 0x008000;
		@JsonAdapter(ColorJsonAdapter.class)
		public int foeColor = 0xFF0000;
		@JsonAdapter(ColorJsonAdapter.class)
		public int foeColorSecondary = 0x800000;
	}

	public static class InWorld {
		public Mode mode = Mode.NONE;
		public float distance = 60.0F;
		public boolean onlyWhenLookingAt = false;
		public boolean onlyWhenHurt = false;
	}

	@Override
	public void update() {
		particle.distanceSquared = particle.distance * particle.distance;
	}

	@Override
	public boolean shouldWatch() {
		return watchForChanges;
	}

}
