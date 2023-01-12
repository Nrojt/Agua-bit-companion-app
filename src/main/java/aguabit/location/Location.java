package aguabit.location;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.URL;
import java.util.Objects;



public class Location {
    public static String getUserLocation() throws IOException, GeoIp2Exception, RuntimeException {
        String IP = "";
        String latitude = "unknown";
        String longitude = "unknown";

        //This gives the correct ipv4 address, localhost doesn't work correctly when multiple network devices are present.
        URL url = new URL("https://checkip.amazonaws.com/");
        try{
            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
            IP = br.readLine();
        } catch (IOException ignored) {
        }

        //if the ip is blank, it means the user is offline or the server is unreachable. In that case, the lookup of the ip in the database should not happen.
        if(!Objects.requireNonNull(IP).isBlank()) {
            //the geolite2 database is not entirely accurate, but good enough. Getting gps coordinates from windows itself seems impossible
            String dbLocation = "src/main/resources/aguabit/scenecontrollers/database/GeoLite2-City.mmdb";
            File database = new File(dbLocation);
            DatabaseReader dbr = new DatabaseReader.Builder(database).build();
            InetAddress IpA = InetAddress.getByName(IP);
            CityResponse response = dbr.city(IpA);

            latitude = response.getLocation().getLatitude().toString();
            longitude = response.getLocation().getLongitude().toString();
        }

        return (latitude + "," + longitude);
    }
}

