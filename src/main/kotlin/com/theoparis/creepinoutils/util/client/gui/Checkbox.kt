/*
 * Copyright (c) Creepinson
 */
package com.theoparis.creepinoutils.util.client.gui

import com.mojang.blaze3d.matrix.MatrixStack
import com.mojang.blaze3d.platform.GlStateManager
import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.widget.button.Button
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.MathHelper
import net.minecraft.util.text.ITextComponent
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn

/**
 * @author Creepinson
 */
@OnlyIn(Dist.CLIENT)
class Checkbox(
    xIn: Int,
    yIn: Int,
    widthIn: Int,
    heightIn: Int,
    msg: ITextComponent?,
    var isChecked: Boolean,
    onPress: IPressable?
) : Button(xIn, yIn, widthIn, heightIn, msg, onPress) {
    override fun onPress() {
        isChecked = !isChecked
        super.onPress()
    }

    override fun renderButton(matrixStack: MatrixStack, mouseX: Int, mouseY: Int, partialTicks: Float) {
        val minecraft = Minecraft.getInstance()
        minecraft.getTextureManager().bindTexture(TEXTURE)
        RenderSystem.enableDepthTest()
        val fontrenderer = minecraft.fontRenderer
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, alpha)
        RenderSystem.enableBlend()
        RenderSystem.defaultBlendFunc()
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA)
        blit(matrixStack, x, y, 0.0f, if (isChecked) 20.0f else 0.0f, 20, height, 32, 64)
        renderBg(matrixStack, minecraft, mouseX, mouseY)
        val i = 14737632
        Minecraft.getInstance().fontRenderer.drawString(
            matrixStack, message.string, x + 24.toFloat(), y + (height - 8) / 2f, 14737632 or MathHelper.ceil(
                alpha * 255.0f
            ) shl 24
        )
    }

    companion object {
        private val TEXTURE = ResourceLocation("textures/gui/checkbox.png")
    }
}