package net.zombieseatflesh7.iframeoverhaul.mixin;

import com.google.common.collect.Lists;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.ProjectileDamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.world.World;
import net.zombieseatflesh7.iframeoverhaul.DamageGroup;
import net.zombieseatflesh7.iframeoverhaul.IFrame;
import net.zombieseatflesh7.iframeoverhaul.IFrameMod;
import net.zombieseatflesh7.iframeoverhaul.IFrameTracker;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity
		extends Entity
		implements IFrameTracker{
	@Shadow
	protected float lastDamageTaken;

	protected final List<IFrame> iFrames = Lists.newArrayList();

	public MixinLivingEntity(EntityType<?> type, World world) {
		super(type, world);
	}

	@Redirect(method = "damage", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/LivingEntity;timeUntilRegen:I", ordinal = 0))
	private int onDamage(LivingEntity entity, DamageSource source, float amount) {
		updateIFrames();
		IFrame iFrame = findMatchingIFrame(source);
		if(iFrame == null) {
			//IFrameMod.LOGGER.info("Age " + this.age + " " + entity.getEntityName() + " received " + amount + " damage of type " + source.getName() + " from " + ((source.getAttacker() != null) ? source.getAttacker().getEntityName() : "the world") + ".");
			iFrames.add(new IFrame(source, amount, this.age));
			return 0;
		}
		else {
			this.lastDamageTaken = iFrame.getDamage();
			if(amount > iFrame.getDamage()) {
				//IFrameMod.LOGGER.info("Age " + this.age + " " + entity.getEntityName() + " partially received " + amount + " - " + iFrame.getDamage() + " damage of type " + source.getName() + " from " + ((source.getAttacker() != null) ? source.getAttacker().getEntityName() : "the world") + ".");
				iFrame.setDamage(amount);
			}
			//else
			//IFrameMod.LOGGER.info("Age " + entity.age + " " + entity.getEntityName() + " blocked " + amount + " - " + iFrame.getDamage() + " damage of type " + source.getName() + " from " + ((source.getAttacker() != null) ? source.getAttacker().getEntityName() : "the world") + ".");
			return 20;
		}
	}

	@Override
	public IFrame findMatchingIFrame(DamageSource source) {
		DamageGroup group = DamageGroup.getGroupFromSource(source);
		Entity attacker = source.getAttacker();
		IFrame iFrame = null;
		if(attacker == null) { //if world damage
			if(group != null) { //if source is part of a group
				for (int i = 0; i < iFrames.size(); i++) { //all current iframes
					if (iFrames.get(i).getDamageGroup() == group && iFrames.get(i).getAttacker() == null) { //if matching damage groups
						if (group.getDamageType(source).ignoreIFrames) { //if it should ignore iframes, remove all matching iframes
							iFrames.remove(i);
							i--;
						} else { //get matching iframe
							iFrame = iFrames.get(i);
						}
					}
				}
			}
			else //not part of a group
				for (int i = 0; i < iFrames.size(); i++)
					if (iFrames.get(i).getDamageType().equals(source.getName()) && iFrames.get(i).getAttacker() == null) //compare damage types
						iFrame =  iFrames.get(i);
		}
		else { //source came from an entity
			if(source instanceof ProjectileDamageSource && source.getSource() instanceof ProjectileEntity) {
				for (int i = 0; i < iFrames.size(); i++)
					if (iFrames.get(i).getAttacker() == attacker && iFrames.get(i).getDamageSource().getSource() == source.getSource())
						iFrame =  iFrames.get(i);
				if(iFrame != null && source.getName().equals("indirectMagic"))
					iFrame = null;
			}
			else
				for (int i = 0; i < iFrames.size(); i++)
					if (iFrames.get(i).getAttacker() == attacker)
						iFrame =  iFrames.get(i);
		}
		return iFrame;
	}

	public void updateIFrames() {
		for (int i = 0; i < iFrames.size(); i++)
		{
			if (iFrames.get(i).isExpired(this.age)) {
				iFrames.remove(i);
				i--;
			}
		}
	}
}

