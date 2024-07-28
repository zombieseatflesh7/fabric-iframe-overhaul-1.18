package net.zombieseatflesh7.iframeoverhaul.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ArrowEntity.class)
public abstract class MixinArrowEntity extends PersistentProjectileEntity {
    protected MixinArrowEntity(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    protected MixinArrowEntity(EntityType<? extends PersistentProjectileEntity> type, double x, double y, double z, World world) {
        super(type, x, y, z, world);
    }

    protected MixinArrowEntity(EntityType<? extends PersistentProjectileEntity> type, LivingEntity owner, World world) {
        super(type, owner, world);
    }

    @Redirect(method = "onHit", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;addStatusEffect(Lnet/minecraft/entity/effect/StatusEffectInstance;Lnet/minecraft/entity/Entity;)Z"))
    private boolean addStatusEffect(LivingEntity target, StatusEffectInstance statusEffectInstance, Entity entity) {
        StatusEffect statusEffect = statusEffectInstance.getEffectType();
        if (statusEffect.isInstant())
            statusEffect.applyInstantEffect(this, entity, target, statusEffectInstance.getAmplifier(), 1);
        else
            target.addStatusEffect(statusEffectInstance, entity);
        return false;
    }
}
