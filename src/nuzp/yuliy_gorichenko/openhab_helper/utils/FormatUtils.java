package nuzp.yuliy_gorichenko.openhab_helper.utils;

import nuzp.yuliy_gorichenko.openhab_helper.manager.LocalizationManager;

import java.text.DecimalFormat;

public class FormatUtils {
    private static final DecimalFormat formatter = new DecimalFormat("0.##");

    /**
     * Format Volts value
     */
    public static String formatVolts(double voltage, LocalizationManager localizationManager) {
        return formatter.format(voltage) + " " + localizationManager.translate("devices.volts");
    }

    /**
     * Format Amperes value
     */
    public static String formatAmperes(double amperage, LocalizationManager localizationManager) {
        return formatter.format(amperage) + " " + localizationManager.translate("devices.amperes");
    }

    /**
     * Format Watts value
     */
    public static String formatWatts(double watts, LocalizationManager localizationManager) {
        if (watts >= 1000) {
            return formatter.format(watts / 1000) + " " + localizationManager.translate("devices.kilowatts");
        }

        return (int) watts + " " + localizationManager.translate("devices.watts");
    }
}
