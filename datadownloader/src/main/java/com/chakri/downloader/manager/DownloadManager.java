package com.chakri.downloader.manager;

import com.chakri.downloader.exception.DownloadManagerException;


/**
 * The Interface DownloadManager.
 */
public interface DownloadManager {

	/**
	 * Connect.
	 *
	 * @param host the host
	 * @param port the port
	 * @param userName the user name
	 * @param password the password
	 * @return true, if successful
	 * @throws DownloadManagerException the download manager exception
	 */
	public boolean connect(String host, Integer port, String userName, String password) throws DownloadManagerException;

	/**
	 * Download.
	 *
	 * @param remoteURL the remote URL
	 * @param destinationFolder the destination folder
	 * @throws DownloadManagerException the download manager exception
	 */
	public void download(String remoteURL, String destinationFolder) throws DownloadManagerException;

	/**
	 * Disconnect.
	 *
	 * @throws DownloadManagerException the download manager exception
	 */
	public void disconnect() throws DownloadManagerException;

}
