package com.chakri.downloader.ftp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;

import org.apache.commons.io.FileUtils;

import com.chakri.downloader.DataDownloader;
import com.chakri.downloader.entities.DownloadEntity;
import com.chakri.downloader.exception.DownloadManagerException;
import com.chakri.downloader.ftp.manager.FTPDownloadManager;
import com.chakri.downloader.manager.DownloadManager;

/**
 * The Class FTPDownloader.
 */
public class FTPDownloader implements DataDownloader {

	/** The Constant logger. */
	private static final Logger logger = Logger.getLogger(FTPDownloader.class.getName());

	/** The download manager. */
	DownloadManager downloadManager;

	/** The ftp prefix. */
	private final String FTP_PREFIX = "ftp://";

	/** The protocol ftp. */
	private final String PROTOCOL_FTP = "ftp";

	/** The default ftp port. */
	final Integer DEFAULT_FTP_PORT = 21;

	/** The props. */
	Properties props = null;

	/**
	 * Instantiates a new FTP downloader.
	 *
	 * @throws DownloadManagerException
	 *             the download manager exception
	 */
	// default constructor
	public FTPDownloader() throws DownloadManagerException {
		downloadManager = new FTPDownloadManager();
		props = new Properties();

		try {
			props.load(getClass().getClassLoader().getResourceAsStream("ftp.properties"));
		} catch (FileNotFoundException fileNotFoundException) {
			logger.severe("exception while reading from properties:" + fileNotFoundException.getMessage());
			// e.printStackTrace();
			throw new DownloadManagerException(fileNotFoundException.getMessage());
		}

		catch (IOException ioException) {
			logger.severe("exception while reading from properties:" + ioException.getMessage());
			// e.printStackTrace();
			throw new DownloadManagerException(ioException.getMessage());
		}

		catch (Exception exception) {
			logger.severe("exception while reading from properties:" + exception.getMessage());
			// e.printStackTrace();
			throw new DownloadManagerException(exception.getMessage());
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see DataDownloader#downloadData(java.lang.String,
	 * java.lang.String)
	 */
	public void downloadData(String downloadURL, String destinationFolder) {

		String destination = null;

		try {
			// parse URL and construct FTPEntity
			DownloadEntity ftpEntity = parseURL(downloadURL);

			// connect to FTP Server

			boolean isConnected = downloadManager.connect(ftpEntity.getHost(), ftpEntity.getPort(),
					ftpEntity.getUserId(), ftpEntity.getPassword());

			logger.info("isConnected is:" + isConnected);

			// download functionality
			if (isConnected) {

				destination = destinationFolder.concat(File.separator).concat(ftpEntity.getRemoteURL());

				logger.info(ftpEntity.getRemoteURL());
				logger.info(destination);

				downloadManager.download(ftpEntity.getRemoteURL(), destination);
			}

			// diconnect from FTP Server
			downloadManager.disconnect();
		} catch (DownloadManagerException e) {

			// to make sure we dont have incomplete download data, delete the
			// destination File
			try {
				if (destination != null && !destination.isEmpty()) {
					FileUtils.forceDelete(new File(destination));
				}

			} catch (IOException ioException) {
				// no need to handle it
			}
			logger.severe("Download data Failed:" + e.getMessage());

		}

	}

	/**
	 * Parses the URL.
	 *
	 * @param downloadURL
	 *            the download URL
	 * @return the download entity
	 * @throws DownloadManagerException
	 *             the download manager exception
	 */
	private DownloadEntity parseURL(String downloadURL) throws DownloadManagerException {

		logger.info("inside parseURL:" + downloadURL);

		DownloadEntity ftpEntity = new DownloadEntity();

		try {
			int ftp_prefix_length = FTP_PREFIX.length();

			int host_details_end_prefix = downloadURL.indexOf("/", ftp_prefix_length);

			String host_details = downloadURL.substring(ftp_prefix_length, host_details_end_prefix);

			logger.info("host details are :" + host_details);

			String host = new String();

			int port = DEFAULT_FTP_PORT;

			String userId = new String();
			String password = new String();

			if (host_details.indexOf(":") == -1) {
				host = host_details;

			} else {
				String[] details = host_details.split(":");
				if (details != null && details.length == 2) {
					host = details[0];
					port = Integer.parseInt(details[1]);
				}
			}

			ftpEntity.setHost(host);
			ftpEntity.setPort(port);

			String remoteURL = downloadURL.substring(host_details_end_prefix + 1);
			ftpEntity.setRemoteURL(remoteURL);

			userId = props.getProperty("userId").trim();
			password = props.getProperty("password").trim();

			ftpEntity.setUserId(userId);
			ftpEntity.setPassword(password);

		} catch (Exception e) {
			logger.severe("exception thrown:" + e.getMessage());
			throw new DownloadManagerException(e.getMessage());
		}

		logger.info("printing ftpEntity remoteURL:" + ftpEntity.getRemoteURL());

		return ftpEntity;

	}

	/**
	 * The main method.
	 *
	 * @param args
	 *            the arguments
	 * @throws DownloadManagerException
	 *             the download manager exception
	 */
	public static void main(String[] args) throws DownloadManagerException {

		String downloadURL = "ftp://192.168.1.2/Downloads/Colonia (2015) [YTS.AG]/Colonia.2015.720p.BluRay.x264-[YTS.AG].mp4";

		String destinationFolder = "/Users/m01457/Downloads/destination/";

		FTPDownloader ftpDownloader = new FTPDownloader();

		ftpDownloader.downloadData(downloadURL, destinationFolder);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see DataDownloader#getProtocol()
	 */
	public String getProtocol() {
		return PROTOCOL_FTP;
	}

}
