package nuzp.yuliy_gorichenko.openhab_helper.manager;

import nuzp.yuliy_gorichenko.openhab_helper.utils.DialogUtils;

import java.util.ArrayList;
import java.util.List;

public class DeviceManager {
    private final DataBaseManager dataBaseManager;
    private final LocalizationManager localizationManager;
    private final List<Device> allDevicesList;

    /**
     * Constructor
     */
    public DeviceManager(DataBaseManager dataBaseManager, LocalizationManager localizationManager) {
        this.dataBaseManager = dataBaseManager;
        this.localizationManager = localizationManager;
        this.allDevicesList = loadAllDevices();
    }

    /**
     * Load all devices
     */
    private List<Device> loadAllDevices() {
        try {
            return dataBaseManager.readData();
        } catch (Exception e) {
            DialogUtils.showErrorMessage(e, localizationManager);
            return new ArrayList<>();
        }
    }

    /**
     * Save all devices
     */
    public void saveAllDevices() {
        try {
            dataBaseManager.writeData(allDevicesList);
        } catch (Exception e) {
            DialogUtils.showErrorMessage(e, localizationManager);
        }
    }

    /**
     * Get all devices
     */
    public List<Device> getAllDevices() {
        return allDevicesList;
    }

    /**
     * Add new device
     */
    public void addDevice(Device device) {
        allDevicesList.add(device);
    }

    /**
     * Remove device
     */
    public void removeDevice(Device device) {
        allDevicesList.remove(device);
    }

    /**
     * Device class
     */
    public static class Device {
        private final String name;
        private final double amperage;
        private final String item;

        public Device(String name, double amperage, String item) {
            this.name = name;
            this.amperage = amperage;
            this.item = item;
        }

        public String getName() {
            return name;
        }

        public double getAmperage() {
            return amperage;
        }

        public String getItem() {
            return item != null ? item : "";
        }

        @Override
        public boolean equals(Object object) {
            if (this == object) return true;
            if (object == null || getClass() != object.getClass()) return false;
            Device device = (Device) object;
            return Double.compare(device.amperage, amperage) == 0 && name.equals(device.name) && item.equals(device.item);
        }
    }
}
