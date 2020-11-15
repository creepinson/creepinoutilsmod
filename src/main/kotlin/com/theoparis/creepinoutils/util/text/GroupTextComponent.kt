/*
 * Copyright (c) Creepinson
 */
package com.theoparis.creepinoutils.util.text

import net.minecraft.util.text.*
import javax.annotation.Nonnull

/**
 * Originally created by Thiakil.
 * This is a utility class to make text components fluent.
 *
 * @author Theo Paris
 */
class GroupTextComponent : StringTextComponent {
    constructor() : super("")
    constructor(color: TextFormatting) : super("") {
        style.color = Color.fromTextFormatting(color)
    }

    @Nonnull
    override fun getUnformattedComponentText(): String {
        return ""
    }

    override fun copyRaw(): GroupTextComponent {
        val stc = GroupTextComponent()
        stc.style = style
        for (itextcomponent in getSiblings()) {
            stc.append(itextcomponent.deepCopy())
        }
        return stc
    }

    fun string(s: String): GroupTextComponent {
        this.append(StringTextComponent(s))
        return this
    }

    fun string(s: String, color: TextFormatting): GroupTextComponent {
        val t: ITextComponent = StringTextComponent(s)
        t.style.color = Color.fromTextFormatting(color)
        this.append(t)
        return this
    }

    fun translation(key: String): GroupTextComponent {
        this.append(TranslationTextComponent(key))
        return this
    }

    fun translation(key: String, vararg args: Any): GroupTextComponent {
        this.append(TranslationTextComponent(key, *args))
        return this
    }

    fun translation(key: String, color: TextFormatting): GroupTextComponent {
        val t: ITextComponent = TranslationTextComponent(key)
        t.style.color = Color.fromTextFormatting(color)
        this.append(t)
        return this
    }

    fun translation(key: String, color: TextFormatting, vararg args: Any): GroupTextComponent {
        val t: ITextComponent = TranslationTextComponent(key, *args)
        t.style.color = Color.fromTextFormatting(color)
        this.append(t)
        return this
    }

    fun component(component: ITextComponent): GroupTextComponent {
        this.append(component)
        return this
    }

    fun component(component: ITextComponent, color: TextFormatting): GroupTextComponent {
        component.style.color = Color.fromTextFormatting(color)
        this.append(component)
        return this
    }
}