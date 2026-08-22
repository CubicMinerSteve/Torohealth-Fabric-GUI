package net.torocraft.torohealth.util;

import java.util.function.Function;

import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.DepthTestFunction;

import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.rendertype.RenderSetup;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Util;
import net.torocraft.torohealth.ToroHealth;
import net.torocraft.torohealth.mixin.accessor.RenderTypeAccessor;

public class RenderUtils {

	public static final RenderPipeline TOROHEALTH_HUD_PIPELINE = RenderPipeline
			.builder(
				new RenderPipeline.Snippet[] {
					RenderPipelines.GUI_TEXTURED_SNIPPET
				}
			)
			.withLocation(Identifier.fromNamespaceAndPath(ToroHealth.MOD_ID, "pipeline/torohealth_hud"))
			.withDepthTestFunction(DepthTestFunction.LEQUAL_DEPTH_TEST)
			.withDepthWrite(true)
			.withBlend(BlendFunction.TRANSLUCENT)
			.build();

	public static final Function<Identifier, RenderType> TOROHEALTH_HUD_DEPTH_TEST = Util
			.memoize(
				resourceLocation -> RenderTypeAccessor.create(
					"torohealth_hud_depth_test",
					RenderSetup.builder(TOROHEALTH_HUD_PIPELINE).bufferSize(1536).withTexture("Sampler0", resourceLocation).createRenderSetup()
				)
			);

	public static final RenderPipeline TOROHEALTH_WORLD_PIPELINE = RenderPipeline
			.builder(
				new RenderPipeline.Snippet[] {
					RenderPipelines.GUI_TEXTURED_SNIPPET
				}
			)
			.withLocation(Identifier.fromNamespaceAndPath(ToroHealth.MOD_ID, "pipeline/torohealth_world"))
			.withDepthTestFunction(DepthTestFunction.LEQUAL_DEPTH_TEST)
			.withDepthWrite(true)
			.withBlend(BlendFunction.TRANSLUCENT)
			.build();

	public static final Function<Identifier, RenderType> TOROHEALTH_WORLD_DEPTH_TEST = Util
			.memoize(
				resourceLocation -> RenderTypeAccessor.create(
					"torohealth_world_depth_test",
					RenderSetup.builder(TOROHEALTH_WORLD_PIPELINE).bufferSize(1536).withTexture("Sampler0", resourceLocation).createRenderSetup()
				)
			);

}
