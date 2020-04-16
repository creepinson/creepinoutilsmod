package me.creepinson.creepinoutils.base;


import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;

import java.io.File;


public class ModConfig {
    public Configuration config;
    private String file;

    public ModConfig(BaseMod mod, String fileName) {
        file = mod._CONFIG_BASE + "/" + fileName;
    }

    public ModConfig(String basePath, String fileName) {
        file = basePath + "/" + fileName;
    }

    public void init() {
        try {
            config = new Configuration(new File(file));
            config.load();
        } catch (Exception e) {
            System.out.println("Cannot load configuration file!");
        } finally {
            config.save();
        }
    }

    /*
     * Removes specific category from configuration file.
     */
    public void removeConfig(String category) {
        config = new Configuration(new File(file));
        try {
            config.load();
            if (config.hasCategory(category))
                config.removeCategory(new ConfigCategory(category));
        } catch (Exception e) {
            System.out.println("Cannot load configuration file!");
        } finally {
            config.save();
        }
    }

    /*
     * Removes specific key in specific category from configuration file.
     */
    public void removeConfig(String category, String key) {
        config = new Configuration(new File(file));
        try {
            config.load();
            if (config.getCategory(category).containsKey(key))
                config.getCategory(category).remove(key);
        } catch (Exception e) {
            System.out.println("Cannot load configuration file!");
        } finally {
            config.save();
        }
    }

    public int getInt(String category, String key) {
        config = new Configuration(new File(file));
        try {
            config.load();
            if (config.getCategory(category).containsKey(key)) {
                return config.get(category, key, 0).getInt();
            }
        } catch (Exception e) {
            System.out.println("Cannot load configuration file!");
        } finally {
            config.save();
        }
        return 0;
    }

    public double getDouble(String category, String key) {
        config = new Configuration(new File(file));
        try {
            config.load();
            if (config.getCategory(category).containsKey(key)) {
                return config.get(category, key, 0D).getDouble();
            }
        } catch (Exception e) {
            System.out.println("Cannot load configuration file!");
        } finally {
            config.save();
        }
        return 0D;
    }

    public float getFloat(String category, String key) {
        config = new Configuration(new File(file));
        try {
            config.load();
            if (config.getCategory(category).containsKey(key)) {
                return (float) config.get(category, key, 0D).getDouble();
            }
        } catch (Exception e) {
            System.out.println("Cannot load configuration file!");
        } finally {
            config.save();
        }
        return 0f;
    }

    public String getString(String category, String key) {
        config = new Configuration(new File(file));
        try {
            config.load();
            if (config.getCategory(category).containsKey(key)) {
                return config.get(category, key, "").getString();
            }
        } catch (Exception e) {
            System.out.println("Cannot load configuration file!");
        } finally {
            config.save();
        }
        return "";
    }

    public long getLong(String category, String key) {
        config = new Configuration(new File(file));
        try {
            config.load();
            if (config.getCategory(category).containsKey(key)) {
                return config.get(category, key, 0L).getLong();
            }
        } catch (Exception e) {
            System.out.println("Cannot load configuration file!");
        } finally {
            config.save();
        }
        return 0L;
    }

    public short getShort(String category, String key) {
        config = new Configuration(new File(file));
        try {
            config.load();
            if (config.getCategory(category).containsKey(key)) {
                return (short) config.get(category, key, (short) 0).getInt();
            }
        } catch (Exception e) {
            System.out.println("Cannot load configuration file!");
        } finally {
            config.save();
        }
        return (short) 0;
    }

    public byte getByte(String category, String key) {
        config = new Configuration(new File(file));
        try {
            config.load();
            if (config.getCategory(category).containsKey(key)) {
                return (byte) config.get(category, key, (byte) 0).getInt();
            }
        } catch (Exception e) {
            System.out.println("Cannot load configuration file!");
        } finally {
            config.save();
        }
        return (byte) 0;
    }

    public boolean getBoolean(String category, String key) {
        config = new Configuration(new File(file));
        try {
            config.load();
            if (config.getCategory(category).containsKey(key))
                return config.get(category, key, false).getBoolean();
        } catch (Exception e) {
            System.out.println("Cannot load configuration file!");
        } finally {
            config.save();
        }
        return false;
    }

    public void writeConfig(String category, String key, String value) {
        config = new Configuration(new File(file));
        try {
            config.load();
            String set = config.get(category, key, value).getString();
            config.getCategory(category).get(key).set(value);
        } catch (Exception e) {
            System.out.println("Cannot load configuration file!");
        } finally {
            config.save();
        }
    }

    public void writeConfig(String category, String key, int value) {
        config = new Configuration(new File(file));
        try {
            config.load();
            int set = config.get(category, key, value).getInt();
            config.getCategory(category).get(key).set(value);
        } catch (Exception e) {
            System.out.println("Cannot load configuration file!");
        } finally {
            config.save();
        }
    }

    public void writeConfig(String category, String key, boolean value) {
        config = new Configuration(new File(file));
        try {
            config.load();
            boolean set = config.get(category, key, value).getBoolean();
            config.getCategory(category).get(key).set(value);
        } catch (Exception e) {
            System.out.println("Cannot load configuration file!");
        } finally {
            config.save();
        }
    }

    public void writeConfig(String category, String key, long value) {
        config = new Configuration(new File(file));
        try {
            config.load();
            long set = config.get(category, key, value).getLong();
            config.getCategory(category).get(key).set(value);
        } catch (Exception e) {
            System.out.println("Cannot load configuration file!");
        } finally {
            config.save();
        }
    }

    public void writeConfig(String category, String key, double value) {
        config = new Configuration(new File(file));
        try {
            config.load();
            double set = config.get(category, key, value).getDouble();
            config.getCategory(category).get(key).set(value);
        } catch (Exception e) {
            System.out.println("Cannot load configuration file!");
        } finally {
            config.save();
        }
    }

    public void writeConfig(String category, String key, short value) {
        config = new Configuration(new File(file));
        try {
            config.load();
            int set = config.get(category, key, value).getInt();
            config.getCategory(category).get(key).set(value);
        } catch (Exception e) {
            System.out.println("Cannot load configuration file!");
        } finally {
            config.save();
        }
    }

    public void writeConfig(String category, String key, byte value) {
        config = new Configuration(new File(file));
        try {
            config.load();
            int set = config.get(category, key, value).getInt();
            config.getCategory(category).get(key).set(value);
        } catch (Exception e) {
            System.out.println("Cannot load configuration file!");
        } finally {
            config.save();
        }
    }

    public void writeConfig(String category, String key, float value) {
        config = new Configuration(new File(file));
        try {
            config.load();
            double set = config.get(category, key, value).getDouble();
            config.getCategory(category).get(key).set(value);
        } catch (Exception e) {
            System.out.println("Cannot load configuration file!");
        } finally {
            config.save();
        }
    }

    public boolean hasCategory(String category) {
        config = new Configuration(new File(file));
        try {
            config.load();
            return config.hasCategory(category);
        } catch (Exception e) {
            System.out.println("Cannot load configuration file!");
        } finally {
            config.save();
        }
        return false;
    }

    public boolean hasKey(String category, String key) {
        config = new Configuration(new File(file));
        try {
            config.load();
            if (!config.hasCategory(category))
                return false;
            return config.getCategory(category).containsKey(key);
        } catch (Exception e) {
            System.out.println("Cannot load configuration file!");
        } finally {
            config.save();
        }
        return false;
    }

    public void setFile(String filename) {
        file = "config/" + filename;
    }

    public String getFile() {
        return file;
    }

    public boolean fileExists() {
        return new File(file).exists();
    }
}