package me.creepinson.creepinoutils.util.world.fake;

import java.util.UUID;

import org.apache.commons.lang3.RandomStringUtils;

import com.mojang.authlib.GameProfile;

import me.creepinson.creepinoutils.util.Gamemode;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.server.management.PlayerInteractionManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

/**
 * This is a multiplayer fake player class for use in server-side mods.
 * Some credit goes to the forge team.
 */
public class FakeServerPlayer extends ServerPlayerEntity implements IFakePlayer {
    public FakeServerPlayer(ServerWorld world) {
        this(world, new GameProfile(UUID.randomUUID(), RandomStringUtils.random(6)));
    }

    public FakeServerPlayer(ServerWorld world, GameProfile name) {
        super(world.getServer(), world, name, new PlayerInteractionManager(world));
    }

    @Override
    public void sendMessage(ITextComponent component) {
    }


    // Allows for the position of the player to be the exact source when raytracing.
    @Override
    public double getPosYEye() {
    	return this.getPosY();
    }

    @Override
    public void sendAllContents(Container containerToSend, NonNullList<ItemStack> itemsList) {
        // Prevent crashing when objects with containers are clicked on.
    }

    @Override
    public float getCooledAttackStrength(float adjustTicks) {
        return 1; // Prevent the attack strength from always being 0.03 due to not ticking.
    }

    @Override
    public void addStat(ResourceLocation p_195067_1_, int p_195067_2_) {
    	super.addStat(p_195067_1_, p_195067_2_);
    }

    @Override
    public void onDeath(DamageSource source) {
        // Does not do anything atm, but this can be used in other fake player implementations
    }

    // We don't need to respond to any gui invocations
}
