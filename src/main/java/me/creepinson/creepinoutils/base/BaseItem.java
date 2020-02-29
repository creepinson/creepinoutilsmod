package me.creepinson.creepinoutils.base;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.OreDictionary;

public class BaseItem extends Item {

	protected ResourceLocation name;

	public BaseItem(ResourceLocation name) {
		this.name = name;
		setTranslationKey(name.getPath());
	}
	
	public BaseItem(ResourceLocation name, CreativeTabs tab) {
		this(name);
		this.setCreativeTab(tab);
	}

	@Override
	public BaseItem setCreativeTab(CreativeTabs tab) {
		super.setCreativeTab(tab);
		return this;
	}
	
	public void registerToOreDictionary(String oreDictName, int meta){
		OreDictionary.registerOre(oreDictName, new ItemStack(this, 1, meta));
	}
	@Override
	public BaseItem setMaxStackSize(int maxStackSize) {
		this.maxStackSize = maxStackSize;
		return this;
	}
}