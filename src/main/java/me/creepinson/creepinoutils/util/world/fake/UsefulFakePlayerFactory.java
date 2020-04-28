
package me.creepinson.creepinoutils.util.world.fake;

import com.google.common.collect.Maps;
import com.mojang.authlib.GameProfile;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Map;
import java.util.UUID;

/*
This is a direct copy of net/minecraftforge/common/util/FakePlayerFactory.java.
All credit to the forge team.
*/

//To be expanded for generic Mod fake players?
@Mod.EventBusSubscriber
public class UsefulFakePlayerFactory {
    private static GameProfile MINECRAFT = new GameProfile(UUID.fromString("41C82C87-7AfB-4024-BA57-13D2C99CAE77"), "[Minecraft]");
    // Map of all active fake player usernames to their entities
    public static final Map<GameProfile, UsefulFakePlayer> PLAYERS = Maps.newHashMap();
    private static UsefulFakePlayer MINECRAFT_PLAYER = null;

    /**
     * Fake players must be unloaded with the world to prevent memory leaks.
     */
    @SubscribeEvent
    public static void unload(WorldEvent.Unload e) {
        PLAYERS.entrySet().removeIf(entry -> entry.getValue().world == e.getWorld());
    }

    public static UsefulFakePlayer getMinecraft(WorldServer world) {
        if (MINECRAFT_PLAYER == null) {
            MINECRAFT_PLAYER = UsefulFakePlayerFactory.get(world, MINECRAFT);
        }
        return MINECRAFT_PLAYER;
    }

    /**
     * Get a fake player with a given username,
     * Mods should either hold weak references to the return value, or listen for a
     * WorldEvent.Unload and kill all references to prevent worlds staying in memory.
     */
    public static UsefulFakePlayer get(WorldServer world, GameProfile username) {
        if (!PLAYERS.containsKey(username)) {
            UsefulFakePlayer fakePlayer = new UsefulFakePlayer(world, username);
            PLAYERS.put(username, fakePlayer);
        }

        return PLAYERS.get(username);
    }

    public static void unloadWorld(WorldServer world) {
        PLAYERS.entrySet().removeIf(entry -> entry.getValue().world == world);
    }
}

