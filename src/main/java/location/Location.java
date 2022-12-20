package location;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.URL;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


public class Location {
    public static String getUserLocation() throws IOException, GeoIp2Exception {
            String IP;

            //This gives the correct ipv4 address, localhost doesn't work correctly when multiple network devices are present.
            URL url = new URL("https://checkip.amazonaws.com/");
            try (BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()))) {
                IP = br.readLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            String dbLocation = "src/main/resources/database/GeoLite2-City.mmdb";
            File database = new File(dbLocation);
            DatabaseReader dbr = new DatabaseReader.Builder(database).build();

            InetAddress IpA = InetAddress.getByName(IP);
            CityResponse response = dbr.city(IpA);

            String latitude = response.getLocation().getLatitude().toString();
            String longitude = response.getLocation().getLongitude().toString();

            return (latitude + "," + longitude);
        }
}

