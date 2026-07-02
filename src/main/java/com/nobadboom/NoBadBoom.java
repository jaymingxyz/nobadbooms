package com.nobadboom;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.vehicle.MinecartTNT;
import net.minecraft.world.level.GameRules;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.level.ExplosionEvent;

@Mod(NoBadBoom.MOD_ID)
public final class NoBadBoom {
    public static final String MOD_ID = "nobadboom";

    /**
     * {@code /gamerule tntExplodes false} stops TNT blocks and TNT minecarts from
     * destroying terrain. Unlike Bedrock, the TNT still ignites and detonates
     * (fuse, sound, knockback, entity damage) — only block destruction is prevented.
     *
     * Registered during class initialization, which FML triggers at mod construction,
     * safely before any level is loaded.
     */
    public static final GameRules.Key<GameRules.BooleanValue> TNT_EXPLODES =
            GameRules.register("tntExplodes", GameRules.Category.MISC, GameRules.BooleanValue.create(true));

    public NoBadBoom() {
        NeoForge.EVENT_BUS.addListener(NoBadBoom::onExplosionDetonate);
    }

    /**
     * Fires after the explosion has computed which blocks and entities it affects,
     * but before any of them are actually destroyed. Clearing the block list here
     * keeps the explosion (particles, damage, knockback) while leaving terrain intact.
     */
    private static void onExplosionDetonate(ExplosionEvent.Detonate event) {
        if (event.getLevel().isClientSide()) {
            return;
        }
        if (event.getLevel().getGameRules().getBoolean(TNT_EXPLODES)) {
            return;
        }
        Entity source = event.getExplosion().getDirectSourceEntity();
        if (source instanceof PrimedTnt || source instanceof MinecartTNT) {
            event.getAffectedBlocks().clear();
        }
    }
}
