package me.creepinson.creepinoutils;


import me.creepinson.creepinoutils.api.util.data.JsonUtils;
import me.creepinson.creepinoutils.api.util.math.shape.Cuboid;
import me.creepinson.creepinoutils.base.BaseMod;
import me.creepinson.creepinoutils.serializer.BlockInfoHolder;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.oredict.OreDictionary;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


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
        Map<String, BlockInfoHolder> blocks = new HashMap<>();
        Map<String, Set<Cuboid>> boxes = new HashMap<>();
        for (Block b : ForgeRegistries.BLOCKS.getValuesCollection()) {
//            blocks.put(b.getRegistryName().toString(), new BlockInfoHolder(b));
//            boxes.put(b.getRegistryName().toString(), BlockInfoHolder.CuboidUtils.getBoxes(b));
        }

        try {
            FileWriter f1 = new FileWriter(new File(_CONFIG_BASE, "exported_blocks.json"));
            f1.write(JsonUtils.get().toJson(blocks));
            f1.close();

            FileWriter f2 = new FileWriter(new File(_CONFIG_BASE, "exported_blocks.json"));
            f2.write(JsonUtils.get().toJson(boxes));
            f2.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        GameRegistry.registerTileEntity(TileEntityAnimationTest.class, new ResourceLocation(MOD_ID, "tile_animation_test"));
    }

    @EventHandler
    @Override
    public void postInit(FMLPostInitializationEvent event) {
        super.postInit(event);
    }
}
