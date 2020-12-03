package utils.location;

public class GeoIPRecord {

	private String country_iso_code;

	private String country_name_en;

	private String country_name_pt;

	private String city_name_en;

	private String city_name_pt;

	private String postal_code;

	private String latitude;

	private String longitude;

	public void setCountryISOCode(String iso_code) {
		this.country_iso_code = iso_code;
	}

	public void setCountryNameEN(String name) {
		this.country_name_en = name;
	}

	public void setCountryNamePT(String name) {
		this.country_name_pt = name;
	}

	public void setCityNameEN(String name) {
		this.city_name_en = name;
	}

	public void setCityNamePT(String name) {
		this.city_name_pt = name;
	}

	public void setPostalCode(String code) {
		this.postal_code = code;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getCountryISOCode() {
		return this.country_iso_code;
	}

	public String getCountryNameEN() {
		return this.country_name_en;
	}

	public String getCountryNamePT() {
		return this.country_name_pt;
	}

	public String getCityNameEN() {
		return this.city_name_en;
	}

	public String getCityNamePT() {
		return this.city_name_pt;
	}

	public String getPostalCode() {
		return this.postal_code;
	}

	public String getLatitude() {
		return this.latitude;
	}

	public String getLongitude() {
		return this.longitude;
	}
}
