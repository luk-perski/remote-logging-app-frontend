package utils.other;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

public class OtherUtils {

	public static final String REGEX_URL = "https?:\\/\\/(www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b([-a-zA-Z0-9()@:%_\\+.~#?&//=]*)";

	public String printStackTrace(Exception exception) {
		StringWriter errors = new StringWriter();
		exception.printStackTrace(new PrintWriter(errors));
		return errors.toString();
	}

	public Result renderResourceByContentType(String content_type, FileInputStream file_input_stream, String filename, String link_to_resource, boolean force_download, boolean is_content_transfer_encoding_binary) {

		Result result = Controller.ok(file_input_stream);
		result = result.withHeader(Http.HeaderNames.CACHE_CONTROL, "public");
		if (force_download) {
			result = result.withHeader(Http.HeaderNames.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"");
		} else {
			result = result.withHeader(Http.HeaderNames.CONTENT_DISPOSITION, "filename=\"" + filename + "\"");
		}
		if (is_content_transfer_encoding_binary) {
			result = result.withHeader(Http.HeaderNames.CONTENT_TRANSFER_ENCODING, "binary");
		}
		if (link_to_resource != null && !link_to_resource.trim().isEmpty()) {
			result = result.withHeader(Http.HeaderNames.LINK, link_to_resource);
		}

		return result.as(content_type);

	}

	public String getFileMD5Hash(File file) throws IOException, NoSuchAlgorithmException {
		return getFileDigest(MessageDigest.getInstance("MD5"), file);
	}

	public String getFileSHA1Hash(File file) throws IOException, NoSuchAlgorithmException {
		return getFileDigest(MessageDigest.getInstance("SHA-1"), file);
	}

	public String getFileDigest(MessageDigest digest, File file) throws IOException {

		// Get file input stream for reading the file content
		FileInputStream fis = new FileInputStream(file);

		// Create byte array to read data in chunks
		byte[] byte_array = new byte[1024];
		int bytes_count = 0;

		// Read file data and update in message digest
		while ((bytes_count = fis.read(byte_array)) != -1) {
			digest.update(byte_array, 0, bytes_count);
		}

		// close the stream; We don't need it now.
		fis.close();

		// Get the hash's bytes
		byte[] bytes = digest.digest();

		// This bytes[] has bytes in decimal format;
		// Convert it to hexadecimal format
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < bytes.length; i++) {
			sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
		}

		// return complete hash
		return sb.toString();
	}
}
