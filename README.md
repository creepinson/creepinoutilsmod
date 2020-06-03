# Creepino Utils Mod

### Dependants:
- essenceplus

## For Developers:
To add my mod as a dependency in gradle, add the following to your dependencies section in build.gradle:

```gradle
deobfCompile 'com.github.creepinson:creepino-utils-mod:1.12.2-SNAPSHOT'
```

If you are using a different minecraft version of this mod, just replace 1.12.2 with the version you want, and make sure it is compatible with creepinoutils (check the branches)
Also, make sure to add the following to build.gradle as well:

```gradle
plugins {
  id "com.wynprice.cursemaven" version "2.1.1"
}
```
