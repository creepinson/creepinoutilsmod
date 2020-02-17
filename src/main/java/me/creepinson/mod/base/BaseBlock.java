package me.creepinson.mod.base;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;

public class BaseBlock extends Block {
	protected ResourceLocation name;

	public BaseBlock(Material mat, ResourceLocation name, CreativeTabs tab, float hardness, float resistance, int harvest,
					 String tool) {
		this(mat, name, tab);
		setHardness(hardness);
		setResistance(resistance);
		setHarvestLevel(tool, harvest);
	}

	public BaseBlock(Material mat, ResourceLocation name, CreativeTabs tab, float hardness, float resistance) {
		this(mat, name, tab);
		setHardness(hardness);
		setResistance(resistance);
	}

	public BaseBlock(Material mat, ResourceLocation name, CreativeTabs tab) {
		this(mat, name);
		setCreativeTab(tab);
	}

	public BaseBlock(Material mat, ResourceLocation name) {
		super(mat);
		setTranslationKey(name.getPath());
	}

	public Item createItemBlock() {
		Item item = new ItemBlock(this);
		item.setRegistryName(this.name);
		return item;
	}
	
	public BaseBlock setCreativeTab(CreativeTabs tab) {
		super.setCreativeTab(tab);
		return this;
	}

	public BaseBlock setSound(SoundType sound) {
		this.setSoundType(sound);
		return this;
	}

	public static class Ore extends BaseBlock {
		public Ore(ResourceLocation name, CreativeTabs tab, float hardness, float resistance, int harvest, String tool) {
			super(Material.ROCK, name, tab, hardness, resistance, harvest, tool);
		}

		public Ore(ResourceLocation name, CreativeTabs tab, float hardness, float resistance) {
			super(Material.ROCK, name, tab, hardness, resistance, 1, "pickaxe");
		}

	}
}
