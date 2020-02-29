package me.creepinson.creepinoutils.util;

/**
 * @author Creepinson http://gitlab.com/creepinson
 * Project creepinoutils
 **/
public class EnergyConversionUtils {
    public static final double JOULES_TO_RF = 2.5D;
    public static final double RF_TO_JOULES = 0.4D;

    public static final double JOULES_TO_EU = 10D;
    public static final double EU_TO_JOULES = .1D;

    public static final double JOULES_TO_TESLA = 2.5D;
    public static final double TESLA_TO_JOULES = 0.4D;

    public static final double JOULES_TO_FORGE = 2.5D;
    public static final double FORGE_TO_JOULES = 0.4D;
    public static class EnergyType {
        public static final String RF = "RF";
        public static final String EU = "EU";
        public static final String F = "FORGE";
        public static final String J = "JOULES";
        public static final String T = "TESLA";
    }

    // TODO: add ic2 integration and configuration values
}
