package me.creepinson.creepinoutils.util.world.fake;

import com.google.common.collect.Maps;
import com.mojang.authlib.GameProfile;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Map;

/*
This is a direct copy of net/minecraftforge/common/util/FakePlayerFactory.java.
All credit to the forge team.
*/

//To be expanded for generic Mod fake players?
@Mod.EventBusSubscriber
public class UsefulFakePlayerFactory {
    // Map of all active fake player usernames to their entities
    public static final Map<GameProfile, FakeServerPlayer> PLAYERS = Maps.newHashMap();

    /**
     * Fake players must be unloaded with the world to prevent memory leaks.
     */
    @SubscribeEvent
    public static void unload(WorldEvent.Unload e) {
        PLAYERS.entrySet().removeIf(entry -> entry.getValue().world == e.getWorld());
    }

    public static FakeServerPlayer copyFrom(ServerPlayerEntity original) {
        FakeServerPlayer fake = UsefulFakePlayerFactory.get(original.getServerWorld(), original.getGameProfile());
        CompoundNBT nbt = original.serializeNBT();
        // Copy data from original player
        fake.deserializeNBT(nbt);
        return fake;
    }


    /**
     * Get a fake player with a given username,
     * Mods should either hold weak references to the return value, or listen for a
     * WorldEvent.Unload and kill all references to prevent worlds staying in memory.
     */
    public static FakeServerPlayer get(ServerWorld world, GameProfile username) {
        if (!PLAYERS.containsKey(username)) {
            FakeServerPlayer fakePlayer = new FakeServerPlayer(world, username);
            PLAYERS.put(username, fakePlayer);
        }

        return PLAYERS.get(username);
    }

    public static void unloadWorld(ServerWorld world) {
        PLAYERS.entrySet().removeIf(entry -> entry.getValue().world == world);
    }
}
