package me.creepinson.creepinoutils;

import me.creepinson.creepinoutils.base.BaseBlock;
import me.creepinson.creepinoutils.block.BlockAnimationTest;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.ResourceLocation;

/**
 * @author Creepinson http://gitlab.com/creepinson
 * Project creepinoutils
 **/
public class BlockHandler {

    public static final BaseBlock ANIMATION_TEST_BLOCK = new BlockAnimationTest(Material.CLOTH, new ResourceLocation(CreepinoUtilsMod.MOD_ID, "animation_test_block"), CreativeTabs.MISC);

}
