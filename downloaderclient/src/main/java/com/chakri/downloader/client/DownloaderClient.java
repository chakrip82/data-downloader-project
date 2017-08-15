package com.chakri.downloader.client;

import java.util.Iterator;
import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

import com.chakri.downloader.DataDownloader;

/**
 * The Class DownloaderClient which will download data from multiple
 * sources/protocols.
 */
public class DownloaderClient {

	/** The Constant logger. */
	private static final Logger logger = Logger.getLogger(DownloaderClient.class.getName());

	/** The loader. */
	private ServiceLoader<DataDownloader> loader;

	/** The executor. */
	ExecutorService executor;

	/**
	 * Creates a new instance of DownloaderService.
	 */
	public DownloaderClient() {

		executor = Executors.newFixedThreadPool(5);

	}

	/**
	 * Download data from sources.
	 *
	 * @param urisToGet
	 *            the uris to get
	 * @param destinationFolder
	 *            the destination folder
	 */
	public void downloadDataFromSources(String[] urisToGet, String destinationFolder) {

		// create a thread for each URI
		DownloadWorker[] threads = new DownloadWorker[urisToGet.length];

		for (int i = 0; i < threads.length; i++) {

			threads[i] = new DownloadWorker(urisToGet[i], destinationFolder, i + 1,
					loader = ServiceLoader.load(DataDownloader.class));
			executor.execute(threads[i]);
		}

		executor.shutdown();
		while (!executor.isTerminated()) {
		}
		logger.info("Finished all threads");

	}

	/**
	 * The main method.
	 *
	 * @param args
	 *            the arguments
	 */
	public static void main(String[] args) {
		String[] downloadURLS = {
				"ftp://localhost/Downloads/Colonia (2015) [YTS.AG]/Colonia.2015.720p.BluRay.x264-[YTS.AG].mp4",
				"http://tutorialspoint.com/java/java_tutorial.pdf",
				"http://tutorialspoint123.com/java/java_tutorial.pdf",
				"ftp://localhost123/Downloads/Colonia (2015) [YTS.AG]/Colonia.2015.720p.BluRay.x264-[YTS.AG].mp4", };

		String destinationFolder = "/Users/m01457/Downloads/destination/";

		DownloaderClient client = new DownloaderClient();
		client.downloadDataFromSources(downloadURLS, destinationFolder);

	}

}

/**
 * The Class DownloadWorker is a thread will be created/invoked for each input
 * url.
 */
class DownloadWorker implements Runnable {

	private static final Logger logger = Logger.getLogger(DownloadWorker.class.getName());

	private int id;
	private String uri;
	private String destinationFolder;
	private ServiceLoader<DataDownloader> loader;

	public DownloadWorker(String downloadURI, String destinationFolder, int id, ServiceLoader<DataDownloader> loader) {
		this.id = id;
		this.destinationFolder = destinationFolder;
		this.uri = downloadURI;
		this.loader = loader;
	}

	public void run() {

		int position = uri.indexOf("://");

		String protocol = uri.substring(0, position);

		logger.info("protocol is :" + protocol);

		try {
			Iterator<DataDownloader> downloaders = loader.iterator();

			while (downloaders.hasNext()) {
				DataDownloader d = downloaders.next();
				if (d.getProtocol().equalsIgnoreCase(protocol)) {
					d.downloadData(uri, destinationFolder);
					break;
				}

			}
		} catch (ServiceConfigurationError serviceError) {

			serviceError.printStackTrace();

		}

	}
}
