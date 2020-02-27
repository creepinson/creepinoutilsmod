package me.creepinson.creepinoutils.asm;

import java.util.Map;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin.TransformerExclusions;

@IFMLLoadingPlugin.Name("creepinoutils")
@IFMLLoadingPlugin.SortingIndex(1010)
@IFMLLoadingPlugin.MCVersion("1.12.2")
@TransformerExclusions("me.creepinson.mod.asm.")
public class Plugin implements IFMLLoadingPlugin {

    public static boolean isObf;

    @Override
    public String[] getASMTransformerClass() {
        return new String[]{"me.creepinson.mod.asm.RenderTransformer"};
    }

    @Override
    public String getModContainerClass() {
        return null;
    }

    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {

    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }

}