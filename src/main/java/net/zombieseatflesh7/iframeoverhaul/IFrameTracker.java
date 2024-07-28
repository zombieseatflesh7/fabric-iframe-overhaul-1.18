package net.zombieseatflesh7.iframeoverhaul;

import net.minecraft.entity.damage.DamageSource;

public interface IFrameTracker {
    void updateIFrames();
    IFrame findMatchingIFrame(DamageSource source);
}
