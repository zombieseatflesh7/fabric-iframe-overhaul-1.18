package net.zombieseatflesh7.iframeoverhaul;

import net.minecraft.entity.damage.DamageSource;
import org.apache.commons.compress.utils.Lists;

import java.util.ArrayList;
import java.util.List;

/*
Damage groups (world):
FIRE: onFire, inFire, lava, hotGround
THORNS: sweetBerryBush, cactus
 */
public record DamageGroup(String name, DamageType[] damageTypes) {

    //private static final List<DamageGroup> groups = Lists.newArrayList();
    private static final ArrayList<DamageGroup> groups = new ArrayList<DamageGroup>();

    public DamageType getDamageType(DamageSource source) {
        for (int i = 0; i < damageTypes.length; i++) {
            if (damageTypes[i].name.equals(source.getName()))
                return damageTypes[i];
        }
        return new DamageType(source.getName());
    }

    public static void initialize() {
        groups.add(new DamageGroup("fire",
                new DamageType[]{new DamageType("inFire"),
                        new DamageType("onFire"),
                        new DamageType("lava"),
                        new DamageType("hotFloor"),
                        new DamageType("lightningBolt")}));
        groups.add(new DamageGroup("thorny",
                new DamageType[]{new DamageType("sweetBerryBush"),
                        new DamageType("cactus")}));
        groups.add(new DamageGroup("velocity",
                new DamageType[]{new DamageType("flyIntoWall", 2)}));
    }

    public static DamageGroup getGroupFromSource(DamageSource source) {
        for (int groupIndex = 0; groupIndex < groups.size(); groupIndex++) {
            DamageGroup group = groups.get(groupIndex);
            for (int i = 0; i < group.damageTypes.length; i++)
                if (group.damageTypes[i].name.equals(source.getName()))
                    return group;
        }
        return null;
    }
}

