package com.theoparis.creepinoutils.util.world.fake

import com.google.common.collect.Maps
import com.mojang.authlib.GameProfile
import net.minecraft.entity.player.ServerPlayerEntity
import net.minecraft.world.server.ServerWorld
import net.minecraftforge.event.world.WorldEvent
import net.minecraftforge.eventbus.api.SubscribeEvent

/*
This is a direct copy of net/minecraftforge/common/util/FakePlayerFactory.java.
All credit to the forge team.
*/
//To be expanded for generic Mod fake players?
object UsefulFakePlayerFactory {
    // Map of all active fake player usernames to their entities
    val PLAYERS: MutableMap<GameProfile, FakeServerPlayer> = Maps.newHashMap()

    /**
     * Fake players must be unloaded with the world to prevent memory leaks.
     */
    @SubscribeEvent
    fun unload(e: WorldEvent.Unload) {
        PLAYERS.entries.removeIf { entry: Map.Entry<GameProfile, FakeServerPlayer> -> entry.value.world === e.world }
    }

    fun copyFrom(original: ServerPlayerEntity): FakeServerPlayer? {
        val fake = UsefulFakePlayerFactory[original.serverWorld, original.gameProfile]
        val nbt = original.serializeNBT()
        // Copy data from original player
        fake!!.deserializeNBT(nbt)
        return fake
    }

    /**
     * Get a fake player with a given username,
     * Mods should either hold weak references to the return value, or listen for a
     * WorldEvent.Unload and kill all references to prevent worlds staying in memory.
     */
    operator fun get(world: ServerWorld, username: GameProfile): FakeServerPlayer? {
        if (!PLAYERS.containsKey(username)) {
            val fakePlayer = FakeServerPlayer(world, username)
            PLAYERS[username] = fakePlayer
        }
        return PLAYERS[username]
    }

    fun unloadWorld(world: ServerWorld) {
        PLAYERS.entries.removeIf { entry: Map.Entry<GameProfile, FakeServerPlayer> -> entry.value.world === world }
    }
}