package me.creepinson.creepinoutils.util.util.text;


import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

/**
 * Simple color enum for adding colors to in-game GUI strings of text.
 *
 * @author AidanBrady
 */
public enum EnumColor {//TODO: Maybe rename some of these or their lang keys
    BLACK("\u00a70", "Black", "Black", "black", new int[]{0, 0, 0}, TextFormatting.BLACK),
    DARK_BLUE("\u00a71", "Blue", "Blue", "blue", new int[]{0, 0, 170}, TextFormatting.DARK_BLUE),
    DARK_GREEN("\u00a72", "Green", "Green", "green", new int[]{0, 170, 0}, TextFormatting.DARK_GREEN),
    DARK_AQUA("\u00a73", "Cyan", "Cyan", "cyan", new int[]{0, 255, 255}, TextFormatting.DARK_AQUA),
    //TODO: Better dye tag?
    DARK_RED("\u00a74", "Dark Red", null, "dark_red", new int[]{170, 0, 0}, TextFormatting.DARK_RED),
    PURPLE("\u00a75", "Purple", "Purple", "purple", new int[]{170, 0, 170}, TextFormatting.DARK_PURPLE),
    ORANGE("\u00a76", "Orange", "Orange", "orange", new int[]{255, 170, 0}, TextFormatting.GOLD),
    GRAY("\u00a77", "Light Gray", "LightGray", "light_gray", new int[]{170, 170, 170}, TextFormatting.GRAY),
    DARK_GRAY("\u00a78", "Gray", "Gray", "gray", new int[]{85, 85, 85}, TextFormatting.DARK_GRAY),
    INDIGO("\u00a79", "Light Blue", "LightBlue", "light_blue", new int[]{85, 85, 255}, TextFormatting.BLUE),
    BRIGHT_GREEN("\u00a7a", "Lime", "Lime", "lime", new int[]{85, 255, 85}, TextFormatting.GREEN),
    //TODO: Better dye tag?
    AQUA("\u00a7b", "Aqua", null, "aqua", new int[]{85, 255, 255}, TextFormatting.AQUA),
    RED("\u00a7c", "Red", "Red", "red", new int[]{255, 0, 0}, TextFormatting.RED),
    PINK("\u00a7d", "Magenta", "Magenta", "magenta", new int[]{255, 85, 255}, TextFormatting.LIGHT_PURPLE),
    YELLOW("\u00a7e", "Yellow", "Yellow", "yellow", new int[]{255, 255, 85}, TextFormatting.YELLOW),
    WHITE("\u00a7f", "White", "White", "white", new int[]{255, 255, 255}, TextFormatting.WHITE),
    //Extras for dye-completeness
    BROWN("\u00a76", "Brown", "Brown", "brown", new int[]{150, 75, 0}, TextFormatting.GOLD),
    BRIGHT_PINK("\u00a7d", "Pink", "Pink", "pink", new int[]{255, 192, 203}, TextFormatting.LIGHT_PURPLE);

    //TODO: Remove?
    public static EnumColor[] DYES = new EnumColor[]{BLACK, RED, DARK_GREEN, BROWN, DARK_BLUE, PURPLE, DARK_AQUA, GRAY, DARK_GRAY, BRIGHT_PINK, BRIGHT_GREEN, YELLOW,
            INDIGO, PINK, ORANGE, WHITE};

    /**
     * The color code that will be displayed
     */
    public final String code;

    public final int[] rgbCode;

    public final TextFormatting textFormatting;
    private final String englishName;
    private final String registryPrefix;
    private final String dyeName;

    EnumColor(String code, String englishName, String dyeName, String registryPrefix, int[] rgbCode, TextFormatting textFormatting) {
        this.code = code;
        this.englishName = englishName;
        this.dyeName = dyeName;
        this.registryPrefix = registryPrefix;
        this.rgbCode = rgbCode;
        this.textFormatting = textFormatting;
    }

    public static EnumColor getFromDyeName(String s) {
        for (EnumColor c : values()) {
            if (c.dyeName.equalsIgnoreCase(s)) {
                return c;
            }
        }
        return null;
    }

    public String getRegistryPrefix() {
        return registryPrefix;
    }

    public String getDyeName() {
        return dyeName;
    }

    public String getEnglishName() {
        return englishName;
    }


    /**
     * Gets the name of this color with it's color prefix code.
     *
     * @return the color's name and color prefix
     */
    public ITextComponent getColoredName() {
        return new TextComponentString(englishName).setStyle(new Style().setColor(textFormatting));
    }

    /**
     * Gets the 0-1 of this color's RGB value by dividing by 255 (used for OpenGL coloring).
     *
     * @param index - R:0, G:1, B:2
     * @return the color value
     */
    public float getColor(int index) {
        return (float) rgbCode[index] / 255F;
    }

    @Override
    public String toString() {
        return code;
    }
}