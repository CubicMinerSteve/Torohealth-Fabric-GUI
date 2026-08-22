package net.torocraft.torohealth.mixin.accessor;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.SubmitNodeStorage;

@Mixin(LevelRenderer.class)
public interface LevelRendererAccessor {
    
    @Accessor("submitNodeStorage")
    SubmitNodeStorage getSubmitNodeStorage();

}
