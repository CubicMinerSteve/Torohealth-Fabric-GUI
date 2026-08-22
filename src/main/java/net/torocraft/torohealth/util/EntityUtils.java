package net.torocraft.torohealth.util;

import java.util.Collection;

import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.monster.Creeper;
import net.torocraft.torohealth.mixin.accessor.AttributeInstanceAccessor;

public class EntityUtils {

	public enum Relation {
		FRIEND, FOE, UNKNOWN
	}

	public static Relation determineRelation(Entity entity) {
		return entity.getType().getCategory().isFriendly() ? Relation.FRIEND : Relation.FOE;
	}

	public static boolean shouldShowHealthBarInWorld(Entity entity, Minecraft minecraft) {
		if (entity == minecraft.player || entity.isSpectator()) {
			return false;
		}
		if (entity instanceof LivingEntity) {
			if (entity instanceof ArmorStand) {
				return false;
			}
			if (entity.isInvisibleTo(minecraft.player)) {
				if (entity.isCurrentlyGlowing() || entity.isOnFire()) {
					return true;
				}
				if (entity instanceof Creeper && ((Creeper) entity).isPowered()) {
					return true;
				}
				if (EntityUtils.hasEquipments((LivingEntity) entity)) {
					return true;
				}
				return false;
			}
			return true;
		}
		return false;
	}

	private static boolean hasEquipments(LivingEntity livingEntity) {
		for (EquipmentSlot equipmentSlot : EquipmentSlot.values()) {
			if (livingEntity.hasItemInSlot(equipmentSlot)) {
				return true;
			}
		}
		return false;
	}

	public static double limitlessAttribute(LivingEntity livingEntity, Holder<Attribute> attribute) {
		// Get the instance containing all types of attributes.
		AttributeMap entityAttributeMap = livingEntity.getAttributes();
		// Get a specific type of attribute.
		AttributeInstance entityAttributeInstance = entityAttributeMap.getInstance(attribute);
		// If not null then calculate the final value bypassing the upper limit.
		if (entityAttributeInstance != null) {
			// Base value of the attribute.
			double d = entityAttributeInstance.getBaseValue();
			// Get a collection of the modifiers, iterating over all according to the
			// operation type.
			Collection<AttributeModifier> allModifiers = ((AttributeInstanceAccessor) entityAttributeInstance)
					.invokeGetModifiersByOperation(Operation.ADD_VALUE);
			for (AttributeModifier current : allModifiers) {
				d += current.amount();
			}
			// Store the value for backup. Safer! :)
			double e = d;
			// Same above but a different type.
			allModifiers = ((AttributeInstanceAccessor) entityAttributeInstance)
					.invokeGetModifiersByOperation(Operation.ADD_MULTIPLIED_BASE);
			for (AttributeModifier current : allModifiers) {
				e += d * current.amount();
			}
			allModifiers = ((AttributeInstanceAccessor) entityAttributeInstance)
					.invokeGetModifiersByOperation(Operation.ADD_MULTIPLIED_TOTAL);
			for (AttributeModifier current : allModifiers) {
				e *= 1.0 + current.amount();
			}
			return e;
		} else {
			return entityAttributeMap.getBaseValue(attribute);
		}
	}
}
