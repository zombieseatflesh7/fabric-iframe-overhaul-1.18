package net.zombieseatflesh7.iframeoverhaul;

public class DamageType {
    public final String name;
    public final int iFrameDuration;
    public final boolean ignoreAttacker;
    public final boolean ignoreIFrames;

    public DamageType(String name)
    {
        this.name = name;
        this.iFrameDuration = 10;
        this.ignoreAttacker = false;
        this.ignoreIFrames = false;
    }
    public DamageType(String name, int iFrameDuration)
    {
        this.name = name;
        this.iFrameDuration = iFrameDuration;
        this.ignoreAttacker = false;
        this.ignoreIFrames = false;
    }

    public DamageType(String name, int iFrameDuration, boolean ignoreAttacker, boolean ignoreIFrames)
    {
        this.name = name;
        this.iFrameDuration = iFrameDuration;
        this.ignoreAttacker = ignoreAttacker;
        this.ignoreIFrames = ignoreIFrames;
    }
}
