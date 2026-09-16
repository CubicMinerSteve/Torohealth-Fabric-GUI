package net.torocraft.torohealth.mixin.accessor;

import java.util.Collection;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;

@Mixin(AttributeInstance.class)
public interface AttributeInstanceAccessor {

	@Invoker("getModifiersOrEmpty")
	public Collection<AttributeModifier> invokeGetModifiersByOperation(Operation operation);

}
