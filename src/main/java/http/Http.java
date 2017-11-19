package http;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Logger;

import static controllers.Controller.Status;

public class Http {
    private static final Logger logger = Logger.getLogger(Http.class.getName());
    public static String url;

    public static Status httpGetQueue(Status status) throws NullPointerException {
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("GET");
            connection.setUseCaches(false);
            connection.connect();

            if (HttpURLConnection.HTTP_OK == connection.getResponseCode()) {
                String line;
                String driveroperators = null;
                String driverclients = null;
                String nkoperators = null;
                String nkclients = null;
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "cp1251"));
                while ((line = br.readLine()) != null) {
                    try {
                        JSONObject object = (JSONObject) new JSONParser().parse(line);
                        JSONArray jsondrivers = (JSONArray) object.get("DRIVERS");
                        if (jsondrivers.size() == 2) {
                            driveroperators = ((JSONObject) jsondrivers.get(0)).get("LoggedIn").toString();
                            driverclients = ((JSONObject) jsondrivers.get(1)).get("Callers").toString();

                        }
                        JSONArray jsonnkekb = (JSONArray) object.get("NK_EKB");
                        if (jsonnkekb.size() == 2) {
                            nkoperators = ((JSONObject) jsonnkekb.get(0)).get("LoggedIn").toString();
                            nkclients = ((JSONObject) jsonnkekb.get(1)).get("Callers").toString();
                        }
                        status = new Status(nkclients, nkoperators, driverclients, driveroperators);
                    } catch (ParseException e) {
                        logger.severe("ERROR PARSE JSON " + e);
                    }
                }
                br.close();
            } else {
                logger.warning(("FAILED HTTP REQUEST " + connection.getResponseCode() + " : " + connection.getResponseMessage()));
            }
        } catch (Throwable cause) {
            logger.warning("ERROR CONNECTION " + cause.getMessage());
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        connection.disconnect();
        return status;
    }
}
