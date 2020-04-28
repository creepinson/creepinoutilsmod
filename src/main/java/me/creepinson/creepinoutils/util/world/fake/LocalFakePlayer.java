package me.creepinson.creepinoutils.util.world.fake;

import com.mojang.authlib.GameProfile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.server.management.PlayerInteractionManager;
import net.minecraft.stats.StatBase;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.IInteractionObject;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.FMLCommonHandler;

/*
This is a slight modification of net/minecraftforge/common/util/FakePlayerFactory.java.
All credit to the forge team.
*/

//Preliminary, simple Fake Player class 
public class LocalFakePlayer extends EntityPlayerMP {

    public LocalFakePlayer(WorldServer world, GameProfile name) {
        super(FMLCommonHandler.instance().getMinecraftServerInstance(), world, name, new PlayerInteractionManager(world));
    }

    @Override
    public boolean canUseCommand(int permLevel, String commandName) {
        return false;
    }

    @Override
    public void sendMessage(ITextComponent component) {

    }

    @Override
    public void addStat(StatBase par1StatBase, int par2) {
    }

    @Override
    public void openGui(Object mod, int modGuiId, World world, int x, int y, int z) {
    }

    @Override
    public boolean isEntityInvulnerable(DamageSource source) {
        return super.isEntityInvulnerable(source);
    }

    @Override
    public boolean canAttackPlayer(EntityPlayer player) {
        return false;
    }

    @Override
    public void onDeath(DamageSource source) {
    }

    @Override
    public void onUpdate() {
    }

    @Override
    public void travel(float strafe, float vertical, float forward) {
        // Does not do anything, but could be used in other fake player implementations
    }

    // We don't respond to any gui invocations
    @Override
    public void displayGUIChest(IInventory par1IInventory) {
    }

    @Override
    public void displayGui(IInteractionObject guiOwner) {

    }
}
