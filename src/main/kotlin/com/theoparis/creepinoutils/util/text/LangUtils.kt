package com.theoparis.creepinoutils.util.text

import net.minecraft.client.resources.I18n
import net.minecraft.util.text.Color
import net.minecraft.util.text.ITextComponent
import net.minecraft.util.text.TextFormatting
import net.minecraft.util.text.TranslationTextComponent
import java.util.*

object LangUtils {
    fun transOnOff(b: Boolean): String {
        return localize(transOnOffKey(b))
    }

    fun transOnOffKey(b: Boolean): String {
        return "gui." + if (b) "on" else "off"
    }

    fun transYesNo(b: Boolean): String {
        return localize("tooltip." + if (b) "yes" else "no")
    }

    fun transOutputInput(b: Boolean): String {
        return localize("gui." + if (b) "output" else "input")
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
    fun localize(s: String): String {
        return I18n.format(s)
    }

    fun canLocalize(s: String): Boolean {
        return I18n.hasKey(s)
    }

    fun localizeWithFormat(key: String, vararg format: Any?): String {
        val s = localize(key)
        return try {
            String.format(s, *format)
        } catch (e: IllegalFormatException) {
            "Format error: $s"
        }
    }

    fun translationWithColour(langKey: String, color: TextFormatting): TranslationTextComponent {
        val translation = TranslationTextComponent(langKey)
        translation.style.color = Color.fromTextFormatting(color)
        return translation
    }

    fun <T : ITextComponent?> withColor(component: T, color: TextFormatting): T {
        component!!.style.color = Color.fromTextFormatting(color)
        return component
    }

    fun onOffColoured(isOn: Boolean): TranslationTextComponent {
        val translation = TranslationTextComponent(transOnOffKey(isOn))
        translation.style.color =
            Color.fromTextFormatting(if (isOn) TextFormatting.DARK_GREEN else TextFormatting.DARK_RED)
        return translation
    }
}