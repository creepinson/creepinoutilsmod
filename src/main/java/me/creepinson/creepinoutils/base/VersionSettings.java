package me.creepinson.creepinoutils.base;

/**
 * @author Creepinson http://gitlab.com/creepinson
 * Project CreepinoUtilsMod
 **/
public class VersionSettings {
    public final String versionUrl;
    public final boolean raw;

    /**
     * @param versionUrl The version url to read from.
     * @param raw        If true, the version checker will use a single line of text containing the version.
     *                   If false, the version checker will try to convert the retrieved text to a JsonObject (See {@link com.google.gson.JsonObject})
     *                   with a property containing a key of "latestVersion".
     */
    public VersionSettings(String versionUrl, boolean raw) {
        this.versionUrl = versionUrl;
        this.raw = raw;
    }
}
