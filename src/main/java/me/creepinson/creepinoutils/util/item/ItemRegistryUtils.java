package me.creepinson.creepinoutils.util.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.moddiscovery.ModInfo;
import org.apache.commons.lang3.text.WordUtils;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * @author Theo Paris https://theoparis.com Project creepinoutils
 **/
public final class ItemRegistryUtils {

    private static final Map<String, String> modIDMap = new HashMap<>();

    private static void populateMap() {
        for (ModInfo entry : ModList.get().getMods()) {
            modIDMap.put(entry.getModId().toLowerCase(Locale.ROOT), entry.getDisplayName());
        }
    }

    public static int getID(ItemStack itemStack) {
        if (itemStack.isEmpty()) {
            return -1;
        }
        return Item.getIdFromItem(itemStack.getItem());
    }

    /* Mod ID lookup thanks to JEI */
    public static String getMod(ItemStack stack) {
        if (stack.isEmpty()) {
            return "null";
        }

        if (modIDMap.isEmpty()) {
            populateMap();
        }

        ResourceLocation itemResourceLocation = Registry.ITEM.getKey(stack.getItem());

        if (itemResourceLocation == null) {
            return "null";
        }

        String modId = itemResourceLocation.getNamespace();
        String lowercaseModId = modId.toLowerCase(Locale.ENGLISH);
        return modIDMap.computeIfAbsent(lowercaseModId, k -> WordUtils.capitalize(modId));
    }
}