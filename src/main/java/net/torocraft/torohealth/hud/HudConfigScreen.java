package net.torocraft.torohealth.hud;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.impl.builders.BooleanToggleBuilder;
import me.shedaniel.clothconfig2.impl.builders.ColorFieldBuilder;
import me.shedaniel.clothconfig2.impl.builders.EnumSelectorBuilder;
import me.shedaniel.clothconfig2.impl.builders.IntSliderBuilder;
import me.shedaniel.clothconfig2.impl.builders.SubCategoryBuilder;
import me.shedaniel.math.Color;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.torocraft.torohealth.ToroHealth;
import net.torocraft.torohealth.config.Config;
import net.torocraft.torohealth.config.Config.AnchorPoint;
import net.torocraft.torohealth.config.Config.Mode;
import net.torocraft.torohealth.config.Config.NumberType;

public class HudConfigScreen {

	public static Screen buildConfigScreen(Screen parentScreen) {

		ConfigBuilder builder = ConfigBuilder.create().setParentScreen(parentScreen)
				.setTitle(Component.translatable("torohealth.main.title")).setSavingRunnable(() -> {
					ToroHealth.CONFIG_LOADER.save(ToroHealth.CONFIG);
				});
		ConfigEntryBuilder entryBuilder = builder.entryBuilder();
		ConfigCategory catGeneric = builder.getOrCreateCategory(Component.translatable("torohealth.generic.title"));

		BooleanToggleBuilder toggleEnabled = entryBuilder
				.startBooleanToggle(Component.translatable("torohealth.generic.enabled"), ToroHealth.CONFIG.enabled)
				.setDefaultValue(true).setSaveConsumer(enabled -> ToroHealth.CONFIG.enabled = enabled);
		BooleanToggleBuilder toggleWatchForChanges = entryBuilder
				.startBooleanToggle(Component.translatable("torohealth.generic.watchForChanges"), ToroHealth.CONFIG.watchForChanges)
				.setDefaultValue(true).setSaveConsumer(watchForChanges -> ToroHealth.CONFIG.watchForChanges = watchForChanges);

		SubCategoryBuilder subCatHudBuilder = entryBuilder.startSubCategory(Component.translatable("torohealth.hud.title"));
		IntSliderBuilder slideHudDistance = entryBuilder
				.startIntSlider(Component.translatable("torohealth.hud.distance"), (int) ToroHealth.CONFIG.hud.distance, 0, 96)
				.setDefaultValue(60).setSaveConsumer(hudDistance -> ToroHealth.CONFIG.hud.distance = hudDistance);
		IntSliderBuilder sliderHudX = entryBuilder
				.startIntSlider(Component.translatable("torohealth.hud.x"), (int) ToroHealth.CONFIG.hud.x, -80, 80)
				.setDefaultValue(4).setSaveConsumer(x -> ToroHealth.CONFIG.hud.x = x);
		IntSliderBuilder sliderHudY = entryBuilder
				.startIntSlider(Component.translatable("torohealth.hud.y"), (int) ToroHealth.CONFIG.hud.y, -80, 80)
				.setDefaultValue(4).setSaveConsumer(y -> ToroHealth.CONFIG.hud.y = y);
		IntSliderBuilder sliderHudScale = entryBuilder
				.startIntSlider(Component.translatable("torohealth.hud.scale"), (int) (ToroHealth.CONFIG.hud.scale * 100), 0, 100)
				.setDefaultValue(100).setSaveConsumer(scale -> ToroHealth.CONFIG.hud.scale = scale * 0.01F);
		IntSliderBuilder sliderHideDelay = entryBuilder
				.startIntSlider(Component.translatable("torohealth.hud.hideDelay"), (int) ToroHealth.CONFIG.hud.hideDelay, 0, 40)
				.setDefaultValue(20).setSaveConsumer(hideDelay -> ToroHealth.CONFIG.hud.hideDelay = hideDelay);
		EnumSelectorBuilder<AnchorPoint> enumAnchorPoint = entryBuilder
				.startEnumSelector(Component.translatable("torohealth.hud.anchorPoint"), Config.AnchorPoint.class, ToroHealth.CONFIG.hud.anchorPoint)
				.setEnumNameProvider(point -> {
					if (point.equals(AnchorPoint.TOP_LEFT)) {
						return Component.translatable("torohealth.hud.anchorPoint.topLeft");
					} else if (point.equals(AnchorPoint.TOP_CENTER)) {
						return Component.translatable("torohealth.hud.anchorPoint.topCenter");
					} else if (point.equals(AnchorPoint.TOP_RIGHT)) {
						return Component.translatable("torohealth.hud.anchorPoint.topRight");
					} else if (point.equals(AnchorPoint.BOTTOM_LEFT)) {
						return Component.translatable("torohealth.hud.anchorPoint.bottomLeft");
					} else if (point.equals(AnchorPoint.BOTTOM_CENTER)) {
						return Component.translatable("torohealth.hud.anchorPoint.bottomCenter");
					} else {
						return Component.translatable("torohealth.hud.anchorPoint.bottomRight");
					}
				}).setDefaultValue(AnchorPoint.TOP_LEFT)
				.setSaveConsumer(anchorPoint -> ToroHealth.CONFIG.hud.anchorPoint = anchorPoint);
		BooleanToggleBuilder toggleShowEntity = entryBuilder
				.startBooleanToggle(Component.translatable("torohealth.hud.showEntity"), ToroHealth.CONFIG.hud.showEntity)
				.setDefaultValue(true).setSaveConsumer(showEntity -> ToroHealth.CONFIG.hud.showEntity = showEntity);
		BooleanToggleBuilder toggleShowBar = entryBuilder
				.startBooleanToggle(Component.translatable("torohealth.hud.showBar"), ToroHealth.CONFIG.hud.showBar)
				.setDefaultValue(true).setSaveConsumer(showBar -> ToroHealth.CONFIG.hud.showBar = showBar);
		BooleanToggleBuilder toggleShowSkin = entryBuilder
				.startBooleanToggle(Component.translatable("torohealth.hud.showSkin"), ToroHealth.CONFIG.hud.showSkin)
				.setDefaultValue(true).setSaveConsumer(showSkin -> ToroHealth.CONFIG.hud.showSkin = showSkin);
		BooleanToggleBuilder toggleHudOnlyWhenHurt = entryBuilder
				.startBooleanToggle(Component.translatable("torohealth.hud.onlyWhenHurt"), ToroHealth.CONFIG.hud.onlyWhenHurt)
				.setDefaultValue(false).setSaveConsumer(onlyWhenHurt -> ToroHealth.CONFIG.hud.onlyWhenHurt = onlyWhenHurt);

		subCatHudBuilder.add(slideHudDistance.build());
		subCatHudBuilder.add(sliderHudX.build());
		subCatHudBuilder.add(sliderHudY.build());
		subCatHudBuilder.add(sliderHudScale.build());
		subCatHudBuilder.add(sliderHideDelay.build());
		subCatHudBuilder.add(enumAnchorPoint.build());
		subCatHudBuilder.add(toggleShowEntity.build());
		subCatHudBuilder.add(toggleShowBar.build());
		subCatHudBuilder.add(toggleShowSkin.build());
		subCatHudBuilder.add(toggleHudOnlyWhenHurt.build());

		SubCategoryBuilder subCatBarBuilder = entryBuilder.startSubCategory(Component.translatable("torohealth.bar.title"));
		EnumSelectorBuilder<NumberType> enumDamageNumberType = entryBuilder
				.startEnumSelector(Component.translatable("torohealth.bar.damageNumberType"), Config.NumberType.class, ToroHealth.CONFIG.bar.damageNumberType)
				.setEnumNameProvider(type -> {
					if (type.equals(NumberType.NONE)) {
						return Component.translatable("torohealth.bar.damageNumberType.none");
					} else if (type.equals(NumberType.CUMULATIVE)) {
						return Component.translatable("torohealth.bar.damageNumberType.cumulative");
					} else {
						return Component.translatable("torohealth.bar.damageNumberType.last");
					}
				}).setDefaultValue(NumberType.LAST).setSaveConsumer(damageNumberType -> ToroHealth.CONFIG.bar.damageNumberType = damageNumberType);
		ColorFieldBuilder friendColorBuilder = entryBuilder
				.startColorField(Component.translatable("torohealth.bar.friendColor"), Color.ofTransparent(ToroHealth.CONFIG.bar.friendColor))
				.setDefaultValue(0x00FF00).setSaveConsumer(friendColor -> ToroHealth.CONFIG.bar.friendColor = friendColor);
		ColorFieldBuilder friendSecondaryColorBuilder = entryBuilder
				.startColorField(Component.translatable("torohealth.bar.friendColorSecondary"), Color.ofTransparent(ToroHealth.CONFIG.bar.friendColorSecondary))
				.setDefaultValue(0x008000).setSaveConsumer(friendColorSecondary -> ToroHealth.CONFIG.bar.friendColorSecondary = friendColorSecondary);
		ColorFieldBuilder foeColorBuilder = entryBuilder
				.startColorField(Component.translatable("torohealth.bar.foeColor"), Color.ofTransparent(ToroHealth.CONFIG.bar.foeColor))
				.setDefaultValue(0xFF0000).setSaveConsumer(foeColor -> ToroHealth.CONFIG.bar.foeColor = foeColor);
		ColorFieldBuilder foeSecondaryColorBuilder = entryBuilder
				.startColorField(Component.translatable("torohealth.bar.foeColorSecondary"), Color.ofTransparent(ToroHealth.CONFIG.bar.foeColorSecondary))
				.setDefaultValue(0x800000).setSaveConsumer(foeColorSecondary -> ToroHealth.CONFIG.bar.foeColorSecondary = foeColorSecondary);

		subCatBarBuilder.add(enumDamageNumberType.build());
		subCatBarBuilder.add(friendColorBuilder.build());
		subCatBarBuilder.add(friendSecondaryColorBuilder.build());
		subCatBarBuilder.add(foeColorBuilder.build());
		subCatBarBuilder.add(foeSecondaryColorBuilder.build());

		SubCategoryBuilder subInWorldBuilder = entryBuilder.startSubCategory(Component.translatable("torohealth.inWorld.title"));
		EnumSelectorBuilder<Mode> enumInWorldMode = entryBuilder
				.startEnumSelector(Component.translatable("torohealth.inWorld.mode"), Config.Mode.class, ToroHealth.CONFIG.inWorld.mode)
				.setEnumNameProvider(mode -> {
					if (mode.equals(Mode.NONE)) {
						return Component.translatable("torohealth.inWorld.mode.none");
					} else if (mode.equals(Mode.WHEN_HOLDING_WEAPON)) {
						return Component.translatable("torohealth.inWorld.mode.whenHoldingWeapon");
					} else {
						return Component.translatable("torohealth.inWorld.mode.always");
					}
				}).setDefaultValue(Mode.NONE).setSaveConsumer(mode -> ToroHealth.CONFIG.inWorld.mode = mode);
		IntSliderBuilder sliderInWorldDistance = entryBuilder
				.startIntSlider(Component.translatable("torohealth.inWorld.distance"), (int) ToroHealth.CONFIG.inWorld.distance, 0, 96)
				.setDefaultValue(60).setSaveConsumer(inWorldDistance -> ToroHealth.CONFIG.inWorld.distance = inWorldDistance);
		BooleanToggleBuilder toggleOnlyWhenLookingAt = entryBuilder
				.startBooleanToggle(Component.translatable("torohealth.inWorld.onlyWhenLookingAt"), ToroHealth.CONFIG.inWorld.onlyWhenLookingAt)
				.setDefaultValue(false).setSaveConsumer(onlyWhenLookingAt -> ToroHealth.CONFIG.inWorld.onlyWhenLookingAt = onlyWhenLookingAt);
		BooleanToggleBuilder toggleInWorldOnlyWhenHurt = entryBuilder
				.startBooleanToggle(Component.translatable("torohealth.inWorld.onlyWhenHurt"), ToroHealth.CONFIG.inWorld.onlyWhenHurt)
				.setDefaultValue(false).setSaveConsumer(inWorldOnlyWhenHurt -> ToroHealth.CONFIG.inWorld.onlyWhenHurt = inWorldOnlyWhenHurt);

		subInWorldBuilder.add(enumInWorldMode.build());
		subInWorldBuilder.add(sliderInWorldDistance.build());
		subInWorldBuilder.add(toggleOnlyWhenLookingAt.build());
		subInWorldBuilder.add(toggleInWorldOnlyWhenHurt.build());

		SubCategoryBuilder subParticleBuilder = entryBuilder.startSubCategory(Component.translatable("torohealth.particle.title"));
		BooleanToggleBuilder toggleParticleShow = entryBuilder
				.startBooleanToggle(Component.translatable("torohealth.particle.show"), ToroHealth.CONFIG.particle.show)
				.setDefaultValue(true).setSaveConsumer(particleShow -> ToroHealth.CONFIG.particle.show = particleShow);
		IntSliderBuilder sliderParticleDistance = entryBuilder
				.startIntSlider(Component.translatable("torohealth.particle.distance"), (int) ToroHealth.CONFIG.particle.distance, 0, 96)
				.setDefaultValue(60).setSaveConsumer(particleDistance -> ToroHealth.CONFIG.particle.distance = particleDistance);
		ColorFieldBuilder damageColorBuilder = entryBuilder
				.startColorField(Component.translatable("torohealth.particle.damageColor"), Color.ofTransparent(ToroHealth.CONFIG.particle.damageColor))
				.setDefaultValue(0xFF0000).setSaveConsumer(damageColor -> ToroHealth.CONFIG.particle.damageColor = damageColor);
		ColorFieldBuilder healColorBuilder = entryBuilder
				.startColorField(Component.translatable("torohealth.particle.healColor"), Color.ofTransparent(ToroHealth.CONFIG.particle.healColor))
				.setDefaultValue(0x00FF00).setSaveConsumer(healColor -> ToroHealth.CONFIG.particle.healColor = healColor);

		subParticleBuilder.add(toggleParticleShow.build());
		subParticleBuilder.add(sliderParticleDistance.build());
		subParticleBuilder.add(damageColorBuilder.build());
		subParticleBuilder.add(healColorBuilder.build());

		catGeneric.addEntry(toggleEnabled.build());
		catGeneric.addEntry(toggleWatchForChanges.build());
		catGeneric.addEntry(subCatHudBuilder.build());
		catGeneric.addEntry(subCatBarBuilder.build());
		catGeneric.addEntry(subInWorldBuilder.build());
		catGeneric.addEntry(subParticleBuilder.build());

		return builder.build();
	}
}
