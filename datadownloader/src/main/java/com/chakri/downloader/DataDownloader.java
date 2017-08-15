package com.chakri.downloader;


/**
 * The Interface DataDownloader.
 */
public interface DataDownloader {
	
	/**
	 * Download data.
	 *
	 * @param URL the url
	 * @param destinationFolder the destination folder
	 */
	public void downloadData(String URL, String destinationFolder);
	
	/**
	 * Gets the protocol.
	 *
	 * @return the protocol
	 */
	public String getProtocol();
	

}
