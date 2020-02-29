package me.creepinson.creepinoutils;

import me.creepinson.creepinoutils.base.BaseMod;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.registries.IForgeRegistryEntry;

@Mod.EventBusSubscriber
@Mod(modid = CreepinoUtilsMod.MOD_ID, name = CreepinoUtilsMod.MOD_NAME, version = CreepinoUtilsMod.MOD_VERSION/*, dependencies = "required-after:"*/)
public class  CreepinoUtilsMod extends BaseMod {
    public static final String MOD_ID = "creepinoutils", MOD_ID_SHORT = "creepinoutils", MOD_NAME = "Creepino Utilities", MOD_URL = "", MOD_VERSION = "1.0.0", MOD_DEPENDENCIES = "";

    // TODO: make creativecore not a requirement

    @Instance(CreepinoUtilsMod.MOD_ID)
    private static CreepinoUtilsMod INSTANCE;

    public static CreepinoUtilsMod getInstance() {
        return INSTANCE;
    }

    @SidedProxy(clientSide = "me.creepinson.creepinoutils.ClientEasyRegistry", serverSide = "me.creepinson.creepinoutils.EasyRegistry")
    public static EasyRegistry proxy;

    public CreepinoUtilsMod() {
        super(MOD_URL, MOD_ID, MOD_ID_SHORT, MOD_NAME, MOD_VERSION);
        this.hasCreativeTab = false;
    }


    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        event.getRegistry().registerAll(proxy.blocksToReg.toArray(new Block[proxy.blocksToReg.size()]));
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll(proxy.itemsToReg.toArray(new Item[proxy.itemsToReg.size()]));
    }

    @SubscribeEvent
    public static void registerEnchantments(RegistryEvent.Register<Enchantment> event) {
        for (IForgeRegistryEntry e : proxy.otherItems) {
            if (e instanceof Enchantment) event.getRegistry().registerAll((Enchantment) e);
        }
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event, proxy);

        OreDictionary.registerOre("stoneAny", new ItemStack(Blocks.STONE, 1, 0));
        OreDictionary.registerOre("stoneAny", new ItemStack(Blocks.STONE, 1, 1));
        OreDictionary.registerOre("stoneAny", new ItemStack(Blocks.STONE, 1, 2));
        OreDictionary.registerOre("stoneAny", new ItemStack(Blocks.STONE, 1, 3));
        OreDictionary.registerOre("stoneAny", new ItemStack(Blocks.STONE, 1, 4));
        OreDictionary.registerOre("stoneAny", new ItemStack(Blocks.STONE, 1, 5));
        OreDictionary.registerOre("stoneAny", new ItemStack(Blocks.STONE, 1, 6));
        // TODO: remove easyregistry and use registry events
//        EasyRegistry.registerBlockWithItem(BlockHandler.ANIMATION_TEST_BLOCK, new ResourceLocation(MOD_ID, "animation_test_block"));
        if (CreepinoUtilsMod.getInstance().isDebug()) {
            CreepinoUtilsMod.getInstance().getLogger().info("Mod loading...");
        }
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
