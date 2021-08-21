package nuzp.yuliy_gorichenko.openhab_helper.manager;

import nuzp.yuliy_gorichenko.openhab_helper.Settings;
import nuzp.yuliy_gorichenko.openhab_helper.exceptions.OpenHabConnectionException;
import nuzp.yuliy_gorichenko.openhab_helper.exceptions.OpenHabException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.StringJoiner;

public class ConnectionManager {
    /**
     * Request Type
     */
    public enum RequestType {
        POST,
        PUT,
        DELETE,
        GET
    }

    /**
     * Send request to URL and get response
     */
    public String sendRequest(String url, RequestType type, String data) throws OpenHabException, OpenHabConnectionException {
        try {
            HttpURLConnection connection = getConnection(url, type);

            if (data != null) {
                OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
                writer.write(data);
                writer.close();
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringJoiner response = new StringJoiner(" ");
            for (String line; (line = reader.readLine()) != null; ) {
                response.add(line);
            }
            reader.close();

            return response.toString().trim();
        } catch (IOException e) {
            throw new OpenHabException();
        }
    }

    /**
     * Create Http Connection
     */
    private HttpURLConnection getConnection(String url, RequestType type) throws OpenHabException, OpenHabConnectionException {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod(type.name());
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setConnectTimeout(Settings.CONNECTION_TIMEOUT);
            connection.setReadTimeout(Settings.CONNECTION_TIMEOUT);
            connection.setRequestProperty("Content-Type", "text/plain");

            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new OpenHabException();
            }

            return connection;
        } catch (IOException e) {
            throw new OpenHabConnectionException();
        }
    }
}
