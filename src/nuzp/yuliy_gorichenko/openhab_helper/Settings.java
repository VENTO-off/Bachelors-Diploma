package nuzp.yuliy_gorichenko.openhab_helper;

public class Settings {
    /**
     * Chart history in ms
     */
    public static final long CHART_HISTORY = 5 * 60 * 1000;

    /**
     * Connection timeout in ms
     */
    public static final int CONNECTION_TIMEOUT = 1000;

    /**
     * Device recognition precision
     */
    public static final double RECOGNITION_PRECISION = 0.1;

    /**
     * Normal temperature for circuit breaker
     */
    public static final double NORMAL_TEMPERATURE = 25;

    /**
     * Minimum temperature for circuit breaker
     */
    public static final double MIN_TEMPERATURE = 15;

    /**
     * Maximum temperature for circuit breaker
     */
    public static final double MAX_TEMPERATURE = 35;
}
