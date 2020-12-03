package utils.location;

import java.io.InputStream;
import java.net.InetAddress;

import javax.inject.Singleton;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.record.City;
import com.maxmind.geoip2.record.Country;
import com.maxmind.geoip2.record.Location;
import com.maxmind.geoip2.record.Postal;

import utils.location.exception.GeoIPHelperException;

@Singleton
public class GeoIPHelper {

	private static DatabaseReader reader = null;

	public GeoIPRecord generateRecordFromIPAddress(String ip_address) throws GeoIPHelperException {
		try {
			if (reader == null) {
				// Load GeoIP database
				InputStream database = GeoIPHelper.class.getClassLoader().getResourceAsStream("public/geoip/GeoLite2-City.mmdb");
				if (database == null) {
					throw new GeoIp2Exception("Could not load geolite2 database file");
				}
				reader = new DatabaseReader.Builder(database).build();
			}

			// Process IP address
			InetAddress ip_addr = InetAddress.getByName(ip_address);
			if (ip_addr != null) {
				// Get values
				CityResponse response = reader.city(ip_addr);

				// Read values
				Country country = response.getCountry();
				City city = response.getCity();
				Postal postal = response.getPostal();
				Location location = response.getLocation();

				// Generate Geo IP record
				GeoIPRecord record = new GeoIPRecord();

				if (country != null) {
					record.setCountryISOCode(country.getIsoCode());
					record.setCountryNameEN(country.getName());
					record.setCountryNamePT(country.getNames().get("pt-BR"));
				}

				if (city != null) {
					record.setCityNameEN(city.getName());
					record.setCityNamePT(city.getNames().get("pt-BR"));
				}

				if (postal != null) {
					record.setPostalCode(postal.getCode());
				}

				if (location != null) {
					if (location.getLatitude() != null) {
						record.setLatitude(location.getLatitude().toString());
					}
					if (location.getLongitude() != null)
						record.setLongitude(location.getLongitude().toString());
				}

				return record;
			}
		} catch (Exception e) {
			throw new GeoIPHelperException(e);
		}

		return null;
	}
}
