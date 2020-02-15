package enhanced.base.mod;

import net.minecraftforge.common.config.Configuration;

import java.io.File;

public abstract class BaseProxy {
    protected Configuration config;

    public void init() {

    }

    public void preInit(File file) {
        config = new Configuration(file);
        registerConfiguration();
        config.save();
    }

    public void postInit() {

    }

    public abstract void registerBlocks();

    public abstract void registerItems();

    public abstract void registerTileEntities();

    public abstract void registerRecipes();

    protected abstract void registerConfiguration();

    public abstract void registerPackets();

    public void registerPotions() {

    }
}
