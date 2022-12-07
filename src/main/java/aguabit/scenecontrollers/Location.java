package aguabit.scenecontrollers;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;


public class Location {
    public static void main(String[] args) throws IOException, GeoIp2Exception {

        date();

    }
    public static void date() throws IOException, GeoIp2Exception {



        InetAddress myIP = InetAddress.getLocalHost();
        InetAddress ip = InetAddress.getLocalHost();



        String IP = ip.getHostAddress();
        String dbLocation = "src/main/resources/database/GeoLite2-City.mmdb";
        File database = new File(dbLocation);
        DatabaseReader dbr = new DatabaseReader.Builder(database).build();

        InetAddress IpA = InetAddress.getByName(IP);
        CityResponse response = dbr.city(IpA);

        String country = response.getCountry().getName();
        String postal = response.getPostal().getCode();
        String state = response.getLeastSpecificSubdivision().getName();
        String city = response.getCity().getName();

        System.out.println("Users country: " + country);
        System.out.println("Users postal: " + postal);
        System.out.println("User city: " + city);
        System.out.println("Users state: " + state);

    }
}
