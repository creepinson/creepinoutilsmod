package me.creepinson.creepinoutils.util.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.list.AbstractList;
import net.minecraft.client.gui.widget.list.ExtendedList;

public class CustomList<E extends AbstractList.AbstractListEntry<E>> extends ExtendedList<E> {

    public CustomList(Minecraft mcIn, int widthIn, int heightIn, int topIn, int bottomIn, int slotHeightIn) {
        super(mcIn, widthIn, heightIn, topIn, bottomIn, slotHeightIn);
    }

    @Override
    protected void renderHoleBackground(int p_renderHoleBackground_1_, int p_renderHoleBackground_2_,
                                        int p_renderHoleBackground_3_, int p_renderHoleBackground_4_) {
        // Do not render dirt background
    }

}