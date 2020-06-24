package me.creepinson.creepinoutils.util.text;

import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fluids.FluidStack;

import java.util.IllegalFormatException;

public final class LangUtils {

    public static String transOnOff(boolean b) {
        return LangUtils.localize(transOnOffKey(b));
    }

    public static String transOnOffKey(boolean b) {
        return "gui." + (b ? "on" : "off");
    }

    public static String transYesNo(boolean b) {
        return LangUtils.localize("tooltip." + (b ? "yes" : "no"));
    }

    public static String transOutputInput(boolean b) {
        return LangUtils.localize("gui." + (b ? "output" : "input"));
    }

/*     public static String localizeFluidStack(FluidStack fluidStack) {
        return fluidStack == null ? null : fluidStack.getFluid().getloc(fluidStack);
    } */

    /**
     * Localizes the defined string.
     *
     * @param s - string to localized
     * @return localized string
     */
    public static String localize(String s) {
        return I18n.format(s);
    }

    public static boolean canLocalize(String s) {
        return I18n.hasKey(s);
    }

    public static String localizeWithFormat(String key, Object... format) {
        String s = localize(key);
        try {
            return String.format(s, format);
        } catch (IllegalFormatException e) {
            return "Format error: " + s;
        }
    }

    public static TranslationTextComponent translationWithColour(String langKey, TextFormatting color) {
        TranslationTextComponent translation = new TranslationTextComponent(langKey);
        translation.getStyle().setColor(color);
        return translation;
    }

    public static <T extends ITextComponent> T withColor(T component, TextFormatting color) {
        component.getStyle().setColor(color);
        return component;
    }

    public static TranslationTextComponent onOffColoured(boolean isOn) {
        TranslationTextComponent translation = new TranslationTextComponent(transOnOffKey(isOn));
        translation.getStyle().setColor(isOn ? TextFormatting.DARK_GREEN : TextFormatting.DARK_RED);
        return translation;
    }
}