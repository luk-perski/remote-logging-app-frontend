package utils.sys;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.jboss.logging.Logger;

import models.db.app.config.ApplicationConfiguration;
import utils.app.config.AppConfig;
import utils.app.exception.GeneralApplicationException;
import utils.other.ContenTypeExtensions;

public class StaticContentUtils {

	private static final Logger log = Logger.getLogger(StaticContentUtils.class);

	public File saveFile(byte[] file_bytes, String path, String filename) {
		FileOutputStream fos = null;
		try {
			if (file_bytes != null) {
				String full_path = getBasePath() + path;
				generatePathIfNotExists(full_path);
				full_path += filename;

				File file = new File(full_path);
				fos = new FileOutputStream(file);
				fos.write(file_bytes);

				return file;
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Could not save file (" + path + filename + "): " + e.getMessage());
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					log.error("Error while trying to close file stream for file (" + path + filename + "): " + e.getMessage());
				}
			}
		}

		return null;
	}

	public File getFile(String path) {
		// Check if path is acceptable
		if (path != null && !path.trim().isEmpty()) {
			try {
				// Define path to be used
				String full_path = generateFullPath(getBasePath(), path);
				log.trace("Retrieving file from " + full_path);
				// Get resource from static content file path
				return new File(full_path);
			} catch (Exception ignore) {
				log.warn("Could not get file from path " + path);
			}
		}
		// If nothing was found, return NULL
		return null;
	}

	public InputStream getFileAsInputStream(String path) {
		// Check if path is acceptable
		if (path != null && !path.trim().isEmpty()) {
			try {
				// Define path to be used
				String full_path = generateFullPath(getBasePath(), path);
				log.trace("Retrieving file from " + full_path);
				// Get resource from static content file path
				return new FileInputStream(full_path);
			} catch (Exception ignore) {
				log.warn("Could not get file from path " + path);
			}
		}
		// If nothing was found, return NULL
		return null;
	}

	private String generateFullPath(String base_path, String path) {
		base_path = base_path.trim();
		path = path.trim();

		final String SEPARATOR = File.separator;

		if (base_path.endsWith(SEPARATOR) && path.startsWith(SEPARATOR)) {
			return base_path + path.substring(1);
		}

		if (!base_path.endsWith(SEPARATOR) && !path.startsWith(SEPARATOR)) {
			return base_path + SEPARATOR + path;
		}

		return base_path + path;
	}

	private String getBasePath() throws GeneralApplicationException {
		ApplicationConfiguration configuration = AppConfig.getInstance();
		if (configuration != null) {
			if (configuration.getFilesPath() != null && !configuration.getFilesPath().trim().isEmpty()) {
				generatePathIfNotExists(configuration.getFilesPath());
				return configuration.getFilesPath();
			}
		}

		return "./files";
	}

	private void generatePathIfNotExists(String path) throws GeneralApplicationException {
		File _path = new File(path);
		if (!_path.exists()) {
			if (!_path.mkdirs()) {
				throw new GeneralApplicationException("general.text.operation_error");
			}
		}
	}

	public String getExtensionFromContentType(String content_type) {
		if (content_type != null) {
			return ContenTypeExtensions.MAP.get(content_type);
		}
		return "";
	}
}
