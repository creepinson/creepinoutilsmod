package com.draco18s.hardlib;

import com.draco18s.hardlib.api.interfaces.IBlockWithMapper;
import com.draco18s.hardlib.api.interfaces.IItemWithMeshDefinition;
import com.draco18s.hardlib.api.internal.IMetaLookup;
import me.creepinson.mod.base.BaseProxy;
import me.creepinson.mod.CreepinoUtilsMod;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.ICriterionInstance;
import net.minecraft.advancements.ICriterionTrigger;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * This code is lisenced under a "feel free to take this class and use it" license. The only
 * provisions are that the package declaration is different as to not cause conflicts in the JVM
 * and that I retain authorship.<br>
 * EasyRegistry and ClientEasyRegistry are both proxy and event handlers. HardLib may be replaced as
 * the reference to any main mod class that treats these two classes as required in order to work:
 * you do not need to replicate the entirety of the HardLib package.<br>
 * Alternatively, you can take HardLib as a dependency. https://minecraft.curseforge.com/projects/hardlib/
 *
 * @author Draco18s
 */
public class EasyRegistry extends BaseProxy {
    private List<Block> blocksToReg = new ArrayList<Block>();
    private List<Item> itemsToReg = new ArrayList<Item>();
    private List<IForgeRegistryEntry> otherItems = new ArrayList<IForgeRegistryEntry>();
    protected HashMap<Block, Item> blockItems = new HashMap<Block, Item>();
    private static Method CriterionRegister;

    public static void registerBlock(Block block, ResourceLocation registryname) {
        CreepinoUtilsMod.proxy._registerBlock(block, registryname);
    }

    public static void registerBlockWithItem(Block block, ResourceLocation registryname) {
        CreepinoUtilsMod.proxy._registerBlockWithItem(block, registryname);
    }

    public static void registerBlockWithCustomItem(Block block, ItemBlock iBlock, ResourceLocation registryname) {
        CreepinoUtilsMod.proxy._registerBlockWithCustomItem(block, iBlock, registryname);
    }

    public static <T extends Block & IBlockWithMapper> void registerBlockWithCustomItemAndMapper(T block, ItemBlock iBlock, ResourceLocation registryname) {
        CreepinoUtilsMod.proxy._registerBlockWithCustomItemAndMapper(block, iBlock, registryname);
    }

    public static void registerItem(Item item, ResourceLocation registryname) {
        CreepinoUtilsMod.proxy._registerItem(item, registryname);
    }

    public static <T extends IMetaLookup> void registerItemWithVariants(Item item, ResourceLocation registryname, T variant) {
        CreepinoUtilsMod.proxy._registerItemWithVariants(item, registryname, variant);
    }

    public static void registerItem(Item item, ResourceLocation registryname, String unlocalized) {
        CreepinoUtilsMod.proxy._registerItem(item, registryname, unlocalized);
    }

    /**
     * Registers other types of IForgeRegistryEntry items. Currently supports:<br>
     * <ul><li>Enchantment</li>
     * </ul>
     *
     * @param object
     * @return
     */
    public static <K extends IForgeRegistryEntry<K>> K registerOther(K object) {
        return CreepinoUtilsMod.proxy._registerOther(object);
    }

    /**
     * Registers an item and loads/registers models based on the item's SubItems.
     *
     * @param item         - Must implement IItemWithMeshDefinition
     * @param registryname
     */
    public static <T extends Item & IItemWithMeshDefinition> void registerItemWithCustomMeshDefinition(T item, ResourceLocation registryname) {
        CreepinoUtilsMod.proxy._registerItemWithCustomMeshDefinition(item, registryname);
    }

    /**
     * Registers a specific model for variant item stack.
     *
     * @param item         - Must implement IItemWithMeshDefinition
     * @param variantStack
     */
    public static <T extends Item & IItemWithMeshDefinition> void registerSpecificItemVariantsWithBakery(T item, ItemStack variantStack) {
        CreepinoUtilsMod.proxy._registerSpecificItemVariantsWithBakery(item, variantStack);
    }

    public void _registerBlock(Block block, ResourceLocation registryname) {
        block.setRegistryName(registryname);
        //GameRegistry.register(block);
        blocksToReg.add(block);
    }

    public void _registerBlockWithItem(Block block, ResourceLocation registryname) {
        block.setRegistryName(registryname);
        ItemBlock ib = new ItemBlock(block);
        ib.setRegistryName(registryname);
        //GameRegistry.register(block);
        blocksToReg.add(block);
        //GameRegistry.register(ib);
        itemsToReg.add(ib);
        blockItems.put(block, ib);
    }

    public void _registerBlockWithCustomItem(Block block, ItemBlock iBlock, ResourceLocation registryname) {
        block.setRegistryName(registryname);
        iBlock.setRegistryName(registryname);
        //GameRegistry.register(block);
        blocksToReg.add(block);
        //GameRegistry.register(iBlock);
        itemsToReg.add(iBlock);
        blockItems.put(block, iBlock);
    }

    public <T extends Block & IBlockWithMapper> void _registerBlockWithCustomItemAndMapper(T block, ItemBlock iBlock, ResourceLocation registryname) {
        block.setRegistryName(registryname);
        iBlock.setRegistryName(registryname);
        //GameRegistry.register(block);
        blocksToReg.add(block);
        //GameRegistry.register(iBlock);
        itemsToReg.add(iBlock);
        blockItems.put(block, iBlock);
    }

    public void _registerItem(Item item, ResourceLocation registryname) {
        item.setRegistryName(registryname);
        //GameRegistry.register(item);
        itemsToReg.add(item);
    }

    public void _registerItem(Item item, ResourceLocation registryname, String unlocalized) {
        item.setRegistryName(registryname);
        itemsToReg.add(item);
    }

    public <T extends Item & IItemWithMeshDefinition> void _registerItemWithCustomMeshDefinition(T item, ResourceLocation registryname) {
        item.setRegistryName(registryname);
        //GameRegistry.register(item);
        itemsToReg.add(item);
    }

    public <T extends Item & IItemWithMeshDefinition> void _registerSpecificItemVariantsWithBakery(T item, ItemStack variantStack) {
        //client only
    }

    public <T extends IMetaLookup> void _registerItemWithVariants(Item item, ResourceLocation registryname, T variant) {
        item.setRegistryName(registryname);
        //GameRegistry.register(item);
        itemsToReg.add(item);
    }

    public <K extends IForgeRegistryEntry<K>> K _registerOther(K object) {
        otherItems.add(object);
        return object;
    }

    @SubscribeEvent
    public void registerBlocks(RegistryEvent.Register<Block> event) {
        event.getRegistry().registerAll(blocksToReg.toArray(new Block[blocksToReg.size()]));
    }

    @SubscribeEvent
    public void registerItems(RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll(itemsToReg.toArray(new Item[itemsToReg.size()]));
    }

    @SubscribeEvent
    public void registerEnchantments(RegistryEvent.Register<Enchantment> event) {
        for (IForgeRegistryEntry e : otherItems) {
            if (e instanceof Enchantment) call(event, e);
        }
    }

    private <K extends IForgeRegistryEntry<K>, T> void call(RegistryEvent.Register<K> event, T value) {
        event.getRegistry().register((K) value);
    }

    public static <T extends ICriterionInstance> ICriterionTrigger<T> registerAdvancementTrigger(ICriterionTrigger<T> trigger) {
        if (CriterionRegister == null) {
            CriterionRegister = ReflectionHelper.findMethod(CriteriaTriggers.class, "register", "func_192118_a", ICriterionTrigger.class);
            CriterionRegister.setAccessible(true);
        }
        try {
            trigger = (ICriterionTrigger<T>) CriterionRegister.invoke(null, trigger);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            System.out.println("Failed to register trigger " + trigger.getId() + "!");
            e.printStackTrace();
        }
        return trigger;
    }

    public EasyRegistry() {

    }

    @Override
    public void registerBlocks() {

    }

    @Override
    public void registerItems() {

    }

    @Override
    public void registerTileEntities() {

    }

    @Override
    public void registerRecipes() {

    }

    @Override
    public void registerPackets() {

    }
}