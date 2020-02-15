package me.creepinson.mod;

import com.draco18s.hardlib.EasyRegistry;
import enhanced.base.mod.BaseMod;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.oredict.OreDictionary;

@Mod(modid = "creepinoutils", name = "Creepino Utils", version = "1.0.0"/*, dependencies = "required-after:"*/)
public class CreepinoUtilsMod extends BaseMod {
    public static final String MOD_ID = "creepinoutils", MOD_ID_SHORT = "creepinoutils", MOD_NAME = "Creepino Utilities", MOD_URL = "", MOD_VERSION = "1.0.0", MOD_DEPENDENCIES = "";

    @Instance("creepinoutils")
    public static CreepinoUtilsMod instance;

    @SidedProxy(clientSide = "com.draco18s.hardlib.client.ClientEasyRegistry", serverSide = "com.draco18s.hardlib.EasyRegistry")
    public static EasyRegistry proxy;

    public CreepinoUtilsMod() {
        super(MOD_URL, MOD_ID, MOD_ID_SHORT, MOD_NAME, MOD_VERSION);
        this.hasCreativeTab = false;
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

    }

    @EventHandler
    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);
    }

    @EventHandler
    @Override
    public void postInit(FMLPostInitializationEvent event) {
        super.postInit(event);
    }
}
