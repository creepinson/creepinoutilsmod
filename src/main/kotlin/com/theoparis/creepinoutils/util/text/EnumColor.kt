package com.theoparis.creepinoutils.util.text

import net.minecraft.util.text.*

/**
 * Simple color enum for adding colors to in-game GUI strings of text.
 *
 * @author AidanBrady
 */
enum class EnumColor(
    /**
     * The color code that will be displayed
     */
    val code: String, val englishName: String, val dyeName: String?, val registryPrefix: String, val rgbCode: IntArray,
    val textFormatting: TextFormatting
) {
    // TODO: Maybe rename some of these or their lang keys
    BLACK("\u00a70", "Black", "Black", "black", intArrayOf(0, 0, 0), TextFormatting.BLACK), DARK_BLUE(
        "\u00a71",
        "Blue",
        "Blue",
        "blue",
        intArrayOf(0, 0, 170),
        TextFormatting.DARK_BLUE
    ),
    DARK_GREEN(
        "\u00a72",
        "Green",
        "Green",
        "green",
        intArrayOf(0, 170, 0),
        TextFormatting.DARK_GREEN
    ),
    DARK_AQUA(
        "\u00a73",
        "Cyan",
        "Cyan",
        "cyan",
        intArrayOf(0, 255, 255),
        TextFormatting.DARK_AQUA
    ),  // TODO: Better dye tag?
    DARK_RED("\u00a74", "Dark Red", null, "dark_red", intArrayOf(170, 0, 0), TextFormatting.DARK_RED), PURPLE(
        "\u00a75",
        "Purple",
        "Purple",
        "purple",
        intArrayOf(170, 0, 170),
        TextFormatting.DARK_PURPLE
    ),
    ORANGE("\u00a76", "Orange", "Orange", "orange", intArrayOf(255, 170, 0), TextFormatting.GOLD), GRAY(
        "\u00a77",
        "Light Gray",
        "LightGray",
        "light_gray",
        intArrayOf(170, 170, 170),
        TextFormatting.GRAY
    ),
    DARK_GRAY("\u00a78", "Gray", "Gray", "gray", intArrayOf(85, 85, 85), TextFormatting.DARK_GRAY), INDIGO(
        "\u00a79",
        "Light Blue",
        "LightBlue",
        "light_blue",
        intArrayOf(85, 85, 255),
        TextFormatting.BLUE
    ),
    BRIGHT_GREEN(
        "\u00a7a",
        "Lime",
        "Lime",
        "lime",
        intArrayOf(85, 255, 85),
        TextFormatting.GREEN
    ),  // TODO: Better dye tag?
    AQUA("\u00a7b", "Aqua", null, "aqua", intArrayOf(85, 255, 255), TextFormatting.AQUA), RED(
        "\u00a7c",
        "Red",
        "Red",
        "red",
        intArrayOf(255, 0, 0),
        TextFormatting.RED
    ),
    PINK(
        "\u00a7d",
        "Magenta",
        "Magenta",
        "magenta",
        intArrayOf(255, 85, 255),
        TextFormatting.LIGHT_PURPLE
    ),
    YELLOW("\u00a7e", "Yellow", "Yellow", "yellow", intArrayOf(255, 255, 85), TextFormatting.YELLOW), WHITE(
        "\u00a7f",
        "White",
        "White",
        "white",
        intArrayOf(255, 255, 255),
        TextFormatting.WHITE
    ),  // Extras for dye-completeness
    BROWN("\u00a76", "Brown", "Brown", "brown", intArrayOf(150, 75, 0), TextFormatting.GOLD), BRIGHT_PINK(
        "\u00a7d",
        "Pink",
        "Pink",
        "pink",
        intArrayOf(255, 192, 203),
        TextFormatting.LIGHT_PURPLE
    );

    /**
     * Gets the name of this color with it's color prefix code.
     *
     * @return the color's name and color prefix
     */
    val coloredName: ITextComponent
        get() = StringTextComponent(englishName).setStyle(
            Style.EMPTY.setColor(
                Color.fromTextFormatting(
                    textFormatting
                )
            )
        )

    /**
     * Gets the 0-1 of this color's RGB value by dividing by 255 (used for OpenGL
     * coloring).
     *
     * @param index - R:0, G:1, B:2
     * @return the color value
     */
    fun getColor(index: Int): Float {
        return rgbCode[index].toFloat() / 255f
    }

    override fun toString(): String {
        return code
    }

    companion object {
        fun getFromDyeName(s: String?): EnumColor? {
            for (c in values()) {
                if (c.dyeName.equals(s, ignoreCase = true)) {
                    return c
                }
            }
            return null
        }
    }
}