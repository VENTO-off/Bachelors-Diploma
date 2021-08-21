package nuzp.yuliy_gorichenko.openhab_helper.utils;

import nuzp.yuliy_gorichenko.openhab_helper.Settings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static nuzp.yuliy_gorichenko.openhab_helper.manager.DeviceManager.Device;

public class DeviceRecognizer {
    private final List<Device> allDevices;
    private final double resultAmperage;
    private final HashMap<Device, Boolean> enabledDevices;
    private final double minAmperage;
    private final List<Device> startDevices;
    private final double startAmperage;
    private List<Device> finalDevices;
    private double finalPrecision;

    /**
     * Constructor
     */
    public DeviceRecognizer(List<Device> allDevices, double resultAmperage, HashMap<Device, Boolean> enabledDevices, double minAmperage) {
        this.allDevices = new ArrayList<>(allDevices);
        this.resultAmperage = resultAmperage;
        this.enabledDevices = enabledDevices;
        this.minAmperage = minAmperage;
        this.startDevices = new ArrayList<>();
        this.startAmperage = calcStartAmperage();
        this.finalDevices = new ArrayList<>();
        this.finalPrecision = Double.MAX_VALUE;
    }

    /**
     * Calculate start amperage value
     */
    private double calcStartAmperage() {
        double amperage = minAmperage;

        for (Device device : enabledDevices.keySet()) {
            allDevices.removeIf(current -> current.equals(device));

            if (enabledDevices.get(device)) {
                startDevices.add(device);
                amperage += device.getAmperage();
            }
        }

        return amperage;
    }

    /**
     * Solve subset sum
     */
    private void subsetSum(List<Device> devices, List<Device> partial) {
        if (partial == null) partial = new ArrayList<>();

        double amperage = startAmperage;
        for (Device device : partial) amperage += device.getAmperage();

        if (amperage > resultAmperage - Settings.RECOGNITION_PRECISION && amperage < resultAmperage + Settings.RECOGNITION_PRECISION) {
            double precision = Math.abs(resultAmperage - amperage);

            if (precision < finalPrecision) {
                finalDevices = new ArrayList<>(partial);
                finalPrecision = precision;
            }
        }

        if (amperage >= resultAmperage + Settings.RECOGNITION_PRECISION) return;

        for (int i = 0; i < devices.size(); i++) {
            Device device = devices.get(i);

            List<Device> remaining = devices.subList(i + 1, devices.size());
            List<Device> left = new ArrayList<>(partial);
            left.add(device);

            subsetSum(remaining, left);
        }
    }

    /**
     * Recognize enabled devices
     */
    public void recognize() {
        subsetSum(allDevices, null);
        finalDevices.addAll(startDevices);
    }

    /**
     * Get enables devices
     */
    public List<Device> getFinalDevices() {
        return finalDevices;
    }
}

