package com.theoparis.creepinoutils.util.client.gui

import com.mojang.blaze3d.matrix.MatrixStack
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.widget.list.AbstractList
import net.minecraft.client.gui.widget.list.ExtendedList

class CustomList<E : AbstractList.AbstractListEntry<E>>(
    mcIn: Minecraft,
    widthIn: Int,
    heightIn: Int,
    topIn: Int,
    bottomIn: Int,
    slotHeightIn: Int
) : ExtendedList<E>(mcIn, widthIn, heightIn, topIn, bottomIn, slotHeightIn) {
    override fun renderBackground(matrixStack: MatrixStack) {
        // Do not render dirt background
    }
}