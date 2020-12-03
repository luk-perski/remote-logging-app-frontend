package utils.auth.oauth.models;

public class UserPhoto {

	private byte[] image_bytes;

	private String content_type;

	public UserPhoto(byte[] image_bytes, String content_type) {
		this.image_bytes = image_bytes;
		this.content_type = content_type;
	}

	public byte[] getImageBytes() {
		return image_bytes;
	}

	public String getContentType() {
		return content_type;
	}

}
