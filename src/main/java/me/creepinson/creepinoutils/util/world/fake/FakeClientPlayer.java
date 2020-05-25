package me.creepinson.creepinoutils.util.world.fake;

import com.mojang.authlib.GameProfile;
import me.creepinson.creepinoutils.util.Gamemode;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.stats.StatBase;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.IInteractionObject;
import net.minecraft.world.World;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.UUID;

/**
 * This is a multiplayer fake player class for use in server-side mods.
 * Some credit goes to the forge team.
 */
public class FakeClientPlayer extends EntityPlayer implements IFakePlayer {
    public boolean spectating;

    public FakeClientPlayer(World world) {
        this(world, new GameProfile(UUID.randomUUID(), RandomStringUtils.random(6)));
    }

    public FakeClientPlayer(World world, GameProfile name) {
        super(world, name);
    }

    @Override
    public void sendMessage(ITextComponent component) {

    }


    // Allows for the position of the player to be the exact source when raytracing.
    @Override
    public float getEyeHeight() {
        return 0;
    }

    @Override
    public float getCooledAttackStrength(float adjustTicks) {
        return 1; // Prevent the attack strength from always being 0.03 due to not ticking.
    }

    @Override
    public void addStat(StatBase par1StatBase, int par2) {
    }

    @Override
    public boolean isSpectator() {
        return this.spectating;
    }

    @Override
    public boolean isCreative() {
        return this.capabilities.isCreativeMode;
    }

    @Override
    public void setGamemode(Gamemode g) {
        this.capabilities.isCreativeMode = g == Gamemode.CREATIVE;
    }

    @Override
    public Gamemode getGamemode() {
        return Gamemode.fromBool(this.capabilities.isCreativeMode);
    }

    @Override
    public void onDeath(DamageSource source) {
        // Does not do anything atm, but this can be used in other fake player implementations
    }

    @Override
    public void onUpdate() {
    }

    // We don't need to respond to any gui invocations

    @Override
    public void openGui(Object mod, int modGuiId, World world, int x, int y, int z) {
    }

    @Override
    public void displayGUIChest(IInventory par1IInventory) {
    }

    @Override
    public void openGuiHorseInventory(AbstractHorse horse, IInventory inventoryIn) {
    }

    @Override
    public void displayGui(IInteractionObject guiOwner) {
    }
}
