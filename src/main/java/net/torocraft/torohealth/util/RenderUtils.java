package net.torocraft.torohealth.util;

import java.util.function.Function;

import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.ColorTargetState;
import com.mojang.blaze3d.pipeline.DepthStencilState;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.CompareOp;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.feature.phase.SimpleFeatureRenderPhase;
import net.minecraft.client.renderer.rendertype.RenderSetup;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Util;
import net.torocraft.torohealth.ToroHealth;
import net.torocraft.torohealth.mixin.accessor.LevelRendererAccessor;

public class RenderUtils {

	public static final SimpleFeatureRenderPhase textFeatureRenderPhase =
			((LevelRendererAccessor) (Minecraft.getInstance().levelRenderer)).getSubmitNodeStorage().order(0).texts;

	public static final RenderPipeline TOROHEALTH_HUD_PIPELINE = RenderPipeline
			.builder(
				new RenderPipeline.Snippet[] {
					RenderPipelines.GUI_TEXTURED_SNIPPET
				}
			)
			.withLocation(Identifier.fromNamespaceAndPath(ToroHealth.MOD_ID, "pipeline/torohealth_hud"))
			.withDepthStencilState(new DepthStencilState(CompareOp.LESS_THAN_OR_EQUAL, true))
			.withColorTargetState(
				new ColorTargetState(BlendFunction.TRANSLUCENT)
			)
			.build();

	public static final Function<Identifier, RenderType> TOROHEALTH_HUD_DEPTH_TEST = Util
			.memoize(
				resourceLocation ->
				RenderType.create(
					"torohealth_hud_depth_test",
					RenderSetup.builder(TOROHEALTH_HUD_PIPELINE).withTexture("Sampler0", resourceLocation).createRenderSetup()
				)
			);

	public static final RenderPipeline TOROHEALTH_WORLD_PIPELINE = RenderPipeline
			.builder(
				new RenderPipeline.Snippet[] {
					RenderPipelines.GUI_TEXTURED_SNIPPET
				}
			)
			.withLocation(Identifier.fromNamespaceAndPath(ToroHealth.MOD_ID, "pipeline/torohealth_world"))
			.withDepthStencilState(new DepthStencilState(CompareOp.GREATER_THAN_OR_EQUAL, true))
			.withColorTargetState(
				new ColorTargetState(BlendFunction.TRANSLUCENT)
			)
			.build();

	public static final Function<Identifier, RenderType> TOROHEALTH_WORLD_DEPTH_TEST = Util
			.memoize(
				resourceLocation ->
				RenderType.create("torohealth_world_depth_test",
					RenderSetup.builder(TOROHEALTH_WORLD_PIPELINE).withTexture("Sampler0", resourceLocation).createRenderSetup()
				)
			);
}
