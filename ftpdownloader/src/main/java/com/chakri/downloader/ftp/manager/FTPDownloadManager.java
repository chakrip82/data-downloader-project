package com.chakri.downloader.ftp.manager;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketException;
import java.util.logging.Logger;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import com.chakri.downloader.exception.DownloadManagerException;
import com.chakri.downloader.manager.DownloadManager;


/**
 * The Class FTPDownloadManager.
 */
public class FTPDownloadManager implements DownloadManager {

	/** The Constant logger. */
	private static final Logger logger = Logger.getLogger(FTPDownloadManager.class.getName());

	/** The ftp client. */
	FTPClient ftpClient;

	// final String FTP_PREFIX = "ftp://";

	/**
	 * Instantiates a new FTP download manager.
	 */
	public FTPDownloadManager() {
		ftpClient = new FTPClient();
		ftpClient.setConnectTimeout(2000);

	}

	/* (non-Javadoc)
	 * @see DownloadManager#connect(java.lang.String, java.lang.Integer, java.lang.String, java.lang.String)
	 */
	public boolean connect(String host, Integer port, String userName, String password)
			throws DownloadManagerException {

		boolean isConnected = false;

		logger.info("Connecting to the server ...");

		try {
			int reply;
			ftpClient.connect(host, port);

			reply = ftpClient.getReplyCode();

			logger.info("reply code returned is:" + reply);

			if (!FTPReply.isPositiveCompletion(reply)) {
				ftpClient.disconnect();
				throw new DownloadManagerException("Exception in connecting to FTP Server");
			}

			// login to server
			isConnected = ftpClient.login(userName, password);

			if (isConnected) {

				logger.info("The client was connected successfully!");

			}

			else {
				ftpClient.logout();
				throw new DownloadManagerException("Exception in authenticating to FTP Server");
			}

			int replyCode = ftpClient.getReplyCode();

			// FTPReply stores a set of constants for FTP reply codes.
			if (!FTPReply.isPositiveCompletion(replyCode)) {
				ftpClient.disconnect();
				throw new DownloadManagerException("Invalid reply from  FTP Server");

			}

			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

			ftpClient.enterLocalPassiveMode();
		} catch (SocketException e) {
			logger.severe("Socket Exception thrown:" + e.getMessage());
			// e.printStackTrace();
			throw new DownloadManagerException(e.getMessage());
		} catch (IOException e) {
			logger.severe("IO Exception thrown:" + e.getMessage());
			// e.printStackTrace();
			throw new DownloadManagerException(e.getMessage());
		} catch (Exception e) {
			logger.severe(" Exception thrown:" + e.getMessage());
			// e.printStackTrace();
			throw new DownloadManagerException(e.getMessage());
		}

		return isConnected;

	}

	/* (non-Javadoc)
	 * @see DownloadManager#download(java.lang.String, java.lang.String)
	 */
	public void download(String remotepath, String destination) throws DownloadManagerException {

		OutputStream outputStream2 = null;
		InputStream inputStream = null;
		File downloadFile2 = null;
		try {

			// String destination = destinationFolder.concat(remotepath);

			inputStream = ftpClient.retrieveFileStream(remotepath);

			// int replyCode = ftpClient.getReplyCode();

			int returnCode = ftpClient.getReplyCode();
			if (returnCode == 550) {
				// file/directory is unavailable
				logger.severe("Error: " + "Resource not Available:" + remotepath + " " + ftpClient.getReplyString());
				// return;
				throw new DownloadManagerException("Resource Not Available");

			}

			downloadFile2 = new File(destination);

			downloadFile2.getParentFile().mkdirs();

			downloadFile2.createNewFile();

			outputStream2 = new BufferedOutputStream(new FileOutputStream(downloadFile2));

			logger.info("File Downloading...");

			// int maxsize =
			// Integer.parseInt(props.getProperty("MAX_READ_SIZE"));

			byte[] bytesArray = new byte[4096];
			int bytesRead = -1;
			while ((bytesRead = inputStream.read(bytesArray)) != -1) {
				
				outputStream2.write(bytesArray, 0, bytesRead);
			}

			boolean success = ftpClient.completePendingCommand();
			if (success) {
				logger.info("File has been downloaded successfully.");
			}
			// outputStream2.close();
			// inputStream.close();
		} catch (FileNotFoundException fileNotFoundException) {

			logger.severe("Error File Download Failed: " + fileNotFoundException.getMessage());
			// e.printStackTrace();
			throw new DownloadManagerException(fileNotFoundException.getMessage());
		} catch (IOException ioException) {

			logger.severe("Error File Download Failed: " + ioException.getMessage());
			throw new DownloadManagerException(ioException.getMessage());
			// e.printStackTrace();
		} catch (Exception exception) {

			logger.severe("Error File Download Failed: " + exception.getMessage());
			throw new DownloadManagerException(exception.getMessage());
			// e.printStackTrace();
		}

		finally {
			try {
				if (inputStream != null) {
					inputStream.close();
				}
				if (outputStream2 != null) {
					outputStream2.close();
				}
			} catch (IOException ioe) {
			}
		}

	}

	/* (non-Javadoc)
	 * @see DownloadManager#disconnect()
	 */
	// @Override
	public void disconnect() {

		if (this.ftpClient.isConnected()) {
			try {
				logger.info("Logout ...");
				this.ftpClient.logout();

				logger.info("disconnecting ...");
				this.ftpClient.disconnect();

				logger.info("The client was disconnected successfully!");

			} catch (IOException ioException) {
				// do nothing as file is already downloaded from FTP server
				logger.severe("ioException while disconnecting " + ioException.getMessage());

			}
		}

	}

}
