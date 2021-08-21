package nuzp.yuliy_gorichenko.openhab_helper.manager;

import nuzp.yuliy_gorichenko.openhab_helper.exceptions.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static nuzp.yuliy_gorichenko.openhab_helper.manager.DeviceManager.Device;

public class DataBaseManager {
    private final SettingsManager settingsManager;

    /**
     * Constructor
     */
    public DataBaseManager(SettingsManager settingsManager) {
        this.settingsManager = settingsManager;
    }

    /**
     * Connect to MySQL DataBase
     */
    private Connection getConnection() throws DataBaseConnectionException {
        try {
            return DriverManager.getConnection("jdbc:mysql://" + settingsManager.getMysqlIp() + ":" + settingsManager.getMysqlPort() + "/" + settingsManager.getMysqlDatabase(), settingsManager.getMysqlUser(), settingsManager.getMysqlPassword());
        } catch (SQLException e) {
            throw new DataBaseConnectionException();
        }
    }

    /**
     * Execute SQL query to create table if not exists
     */
    private void createTableIfNotExists(Connection connection) throws SQLException {
        Statement query = connection.createStatement();
        query.executeUpdate("CREATE TABLE IF NOT EXISTS `openhab_helper` (" +
                "`id` INT NOT NULL AUTO_INCREMENT," +
                "`name` VARCHAR(30) NOT NULL," +
                "`amperage` DOUBLE NOT NULL," +
                "`item` VARCHAR(30) NOT NULL," +
                "PRIMARY KEY (`id`));");
        query.close();
    }

    /**
     * Execute SQL query to truncate table
     */
    private void truncate(Connection connection) throws SQLException {
        Statement query = connection.createStatement();
        query.executeUpdate("TRUNCATE TABLE `openhab_helper`;");
        query.close();
    }

    /**
     * Read devices from DataBase
     */
    public List<Device> readData() throws DataBaseException, DataBaseConnectionException {
        if (!settingsManager.isSettingsDone()) {
            return new ArrayList<>();
        }

        try (Connection connection = getConnection()) {
            List<Device> devices = new ArrayList<>();
            createTableIfNotExists(connection);

            Statement query = connection.createStatement();
            ResultSet result = query.executeQuery("SELECT * FROM `openhab_helper`;");

            while (result.next()) {
                devices.add(new Device(result.getString("name"), result.getDouble("amperage"), result.getString("item")));
            }

            result.close();
            query.close();

            return devices;
        } catch (SQLException e) {
            throw new DataBaseException();
        }
    }

    /**
     * Write devices to DataBase
     */
    public void writeData(List<Device> devices) throws DataBaseException, DataBaseConnectionException {
        if (!settingsManager.isSettingsDone()) {
            return;
        }

        try (Connection connection = getConnection()) {
            createTableIfNotExists(connection);
            truncate(connection);

            for (Device device : devices) {
                PreparedStatement query = connection.prepareStatement("INSERT INTO `openhab_helper` (`name`, `amperage`, `item`) VALUES (?, ?, ?);");
                query.setString(1, device.getName());
                query.setDouble(2, device.getAmperage());
                query.setString(3, device.getItem());
                query.executeUpdate();
                query.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataBaseException();
        }
    }
}
