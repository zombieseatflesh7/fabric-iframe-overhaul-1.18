package net.zombieseatflesh7.iframeoverhaul;

import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;

public class IFrame {
    private final DamageSource source;
    private final DamageGroup group;
    private float damage;
    private final int entityAge;
    private final int duration;

    public IFrame(DamageSource source, float damage, int entityAge) {
        this.source = source;
        this.damage = damage;
        this.entityAge = entityAge;
        this.group = DamageGroup.getGroupFromSource(source);
        this.duration = (group != null) ? group.getDamageType(source).iFrameDuration : 10;
    }

    public IFrame(DamageSource source, float damage, int entityAge, DamageGroup group) {
        this.source = source;
        this.damage = damage;
        this.entityAge = entityAge;
        this.duration = group.getDamageType(source).iFrameDuration;
        this.group = group;
    }

    public IFrame(DamageSource source, float damage, int entityAge, int duration) {
        this.source = source;
        this.damage = damage;
        this.entityAge = entityAge;
        this.duration = duration;
        this.group = null;
    }

    public DamageSource getDamageSource() {
        return source;
    }

    public Entity getAttacker() {
        return source.getAttacker();
    }

    public String getDamageType() {
        return source.getName();
    }

    public DamageGroup getDamageGroup() { return group;}

    public float getDamage() {
        return damage;
    }

    public void setDamage(float d) {
        damage = d;
    }

    public boolean isExpired(int age) {
        return (entityAge + duration) <= age;
    }
}
