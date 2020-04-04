package me.creepinson.creepinoutils;


import me.creepinson.creepinoutils.base.BaseMod;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.oredict.OreDictionary;


@Mod(modid = CreepinoUtilsMod.MOD_ID, name = CreepinoUtilsMod.MOD_NAME, acceptableRemoteVersions = "*", version = CreepinoUtilsMod.MOD_VERSION)
public class CreepinoUtilsMod extends BaseMod {
    protected static final String MOD_ID = "creepinoutils", MOD_NAME = "Creepino Utilities", MOD_URL = "", MOD_VERSION = "1.0.0";

    @Instance(CreepinoUtilsMod.MOD_ID)
    private static CreepinoUtilsMod INSTANCE;

    public static CreepinoUtilsMod getInstance() {
        return INSTANCE;
    }

    public CreepinoUtilsMod() {
        super(MOD_URL, null, MOD_ID, MOD_VERSION);
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event, null);

        OreDictionary.registerOre("stoneAny", new ItemStack(Blocks.STONE, 1, 0));
        OreDictionary.registerOre("stoneAny", new ItemStack(Blocks.STONE, 1, 1));
        OreDictionary.registerOre("stoneAny", new ItemStack(Blocks.STONE, 1, 2));
        OreDictionary.registerOre("stoneAny", new ItemStack(Blocks.STONE, 1, 3));
        OreDictionary.registerOre("stoneAny", new ItemStack(Blocks.STONE, 1, 4));
        OreDictionary.registerOre("stoneAny", new ItemStack(Blocks.STONE, 1, 5));
        OreDictionary.registerOre("stoneAny", new ItemStack(Blocks.STONE, 1, 6));
        // TODO: use registry events
//        EasyRegistry.registerBlockWithItem(BlockHandler.ANIMATION_TEST_BLOCK, new ResourceLocation(MOD_ID, "animation_test_block"));
        CreepinoUtilsMod.getInstance().debug("Mod loading...");
    }

    @EventHandler
    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);
//        GameRegistry.registerTileEntity(TileEntityAnimationTest.class, new ResourceLocation(MOD_ID, "tile_animation_test"));
    }

    @EventHandler
    @Override
    public void postInit(FMLPostInitializationEvent event) {
        super.postInit(event);
    }
}
