package me.creepinson.creepinoutils.util.util.item;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Creepinson http://gitlab.com/creepinson
 * Project creepinoutils
 **/
public class OreDictUtils {
    public static String[] getOreNamesAsArray(ItemStack stack) {
        int[] ids = OreDictionary.getOreIDs(stack);
        String[] list = new String[ids.length];
        for (int i = 0; i < ids.length; i++) {
            list[i] = OreDictionary.getOreName(ids[i]);
        }
        return list;
    }

    public static List<String> getOreNames(ItemStack stack) {
        int[] ids = OreDictionary.getOreIDs(stack);
        List<String> list = new ArrayList<>();
        for (int i = 0; i < ids.length; i++) {
            list.add(OreDictionary.getOreName(ids[i]));
        }
        return list;
    }
}
