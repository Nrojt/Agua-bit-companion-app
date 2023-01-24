package aguabit.location;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;
import com.google.gson.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
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
            String dbLocation = "src/main/resources/GeoLite2-City.mmdb";
            File database = new File(dbLocation);
            if (database.exists()) {
                DatabaseReader dbr = new DatabaseReader.Builder(database).build();
                InetAddress IpA = InetAddress.getByName(IP);
                CityResponse response = dbr.city(IpA);

                latitude = response.getLocation().getLatitude().toString();
                longitude = response.getLocation().getLongitude().toString();
            } else {
                //backup method because I can't get the geolite2 database to work with an exported jar. This website is good for like 2000 calls per month per ip
                try {
                    URL urlLocationBackup = new URL("https://funkemunky.cc/vpn?ip=" + IP);
                    HttpURLConnection http = (HttpURLConnection) urlLocationBackup.openConnection();
                    http.setRequestProperty("Accept", "application/json");

                    //checking to see if the website is available
                    if (http.getResponseCode() >= 100 && http.getResponseCode() <= 399) {
                        BufferedReader br = new BufferedReader(new InputStreamReader(http.getInputStream()));
                        String resultFromWebpage = br.readLine();
                        JsonObject jsonObject = new JsonParser().parse(resultFromWebpage).getAsJsonObject();

                        latitude = jsonObject.get("latitude").getAsString();
                        longitude = jsonObject.get("longitude").getAsString();
                    }
                } catch (IOException ignored) {
                }
            }
        }
        return (latitude + "," + longitude);
    }
}

