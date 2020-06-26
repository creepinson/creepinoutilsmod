package me.creepinson.creepinoutils.util;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nonnull;

/**
 * Originally created by Thiakil.
 * This is a utility class to make text components fluent.
 * @author Theo Paris
 */
public class GroupTextComponent extends StringTextComponent {

    public GroupTextComponent() {
        super("");
    }

    public GroupTextComponent(TextFormatting color) {
        super("");
        getStyle().setColor(color);
    }

    @Nonnull
    @Override
    public String getUnformattedComponentText() {
        return "";
    }

    @Nonnull
    @Override
    public GroupTextComponent shallowCopy() {
        GroupTextComponent StringTextComponent = new GroupTextComponent();
        StringTextComponent.setStyle(this.getStyle().createShallowCopy());
        for (ITextComponent itextcomponent : this.getSiblings()) {
            StringTextComponent.appendSibling(itextcomponent.shallowCopy());
        }
        return StringTextComponent;
    }

    public GroupTextComponent string(String s) {
        this.appendSibling(new StringTextComponent(s));
        return this;
    }

    public GroupTextComponent string(String s, TextFormatting color) {
        ITextComponent t = new StringTextComponent(s);
        t.getStyle().setColor(color);
        this.appendSibling(t);
        return this;
    }

    public GroupTextComponent translation(String key) {
        this.appendSibling(new TranslationTextComponent(key));
        return this;
    }

    public GroupTextComponent translation(String key, Object... args) {
        this.appendSibling(new TranslationTextComponent(key, args));
        return this;
    }

    public GroupTextComponent translation(String key, TextFormatting color) {
        ITextComponent t = new TranslationTextComponent(key);
        t.getStyle().setColor(color);
        this.appendSibling(t);
        return this;
    }

    public GroupTextComponent translation(String key, TextFormatting color, Object... args) {
        ITextComponent t = new TranslationTextComponent(key, args);
        t.getStyle().setColor(color);
        this.appendSibling(t);
        return this;
    }

    public GroupTextComponent component(ITextComponent component) {
        this.appendSibling(component);
        return this;
    }

    public GroupTextComponent component(ITextComponent component, TextFormatting color) {
        component.getStyle().setColor(color);
        this.appendSibling(component);
        return this;
    }
}