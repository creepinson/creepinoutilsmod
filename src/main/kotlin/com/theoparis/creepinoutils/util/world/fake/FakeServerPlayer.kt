package com.theoparis.creepinoutils.util.world.fake

import com.mojang.authlib.GameProfile
import com.theoparis.creepinoutils.util.api.IFakePlayer
import net.minecraft.entity.player.ServerPlayerEntity
import net.minecraft.inventory.container.Container
import net.minecraft.item.ItemStack
import net.minecraft.server.management.PlayerInteractionManager
import net.minecraft.util.DamageSource
import net.minecraft.util.NonNullList
import net.minecraft.util.ResourceLocation
import net.minecraft.util.text.ITextComponent
import net.minecraft.world.server.ServerWorld
import org.apache.commons.lang3.RandomStringUtils
import java.util.*

/**
 * This is a multiplayer fake player class for use in server-side mods.
 * Some credit goes to the forge team.
 */
class FakeServerPlayer @JvmOverloads constructor(
    world: ServerWorld,
    name: GameProfile? = GameProfile(
        UUID.randomUUID(),
        RandomStringUtils.random(6)
    )
) : ServerPlayerEntity(world.server, world, name, PlayerInteractionManager(world)), IFakePlayer {
    override fun sendMessage(component: ITextComponent, sender: UUID) {}

    // Allows for the position of the player to be the exact source when raytracing.
    override fun getPosYEye(): Double {
        return this.posY
    }

    override fun sendAllContents(containerToSend: Container, itemsList: NonNullList<ItemStack>) {
        // Prevent crashing when objects with containers are clicked on.
    }

    override fun getCooledAttackStrength(adjustTicks: Float): Float {
        return 1f // Prevent the attack strength from always being 0.03 due to not ticking.
    }

    override fun addStat(p_195067_1_: ResourceLocation, p_195067_2_: Int) {
        super.addStat(p_195067_1_, p_195067_2_)
    }

    override fun onDeath(source: DamageSource) {
        // Does not do anything atm, but this can be used in other fake player implementations
    } // We don't need to respond to any gui invocations
}