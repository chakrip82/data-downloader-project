package com.chakri.downloader.http;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.chakri.downloader.DataDownloader;

import junit.framework.TestCase;


/**
 * The Class HTTPDownloaderIntegrationTest.
 */
@RunWith(BlockJUnit4ClassRunner.class)
public class HTTPDownloaderIntegrationTest extends TestCase {

	/** The data downloader. */
	DataDownloader dataDownloader;

	/** The folder. */
	@Rule
	public TemporaryFolder folder = new TemporaryFolder();

	/** The encoding. */
	final String encoding = null;
	// File tempFile = null;

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	@Before
	public void setUp() throws Exception {

		super.setUp();
		dataDownloader = new HTTPDownloader();

	}

	/**
	 * Test download data.
	 */
	@Test
	public void testDownloadData() {

		System.out.println("inside testDownloadData");

		Throwable th = null;

		String downloadURL = "http://tutorialspoint.com/java/java_tutorial.pdf";

		String destination = "destination";

		try {
			dataDownloader.downloadData(downloadURL, destination);

			String fileName = destination.concat(File.separator).concat(downloadURL);

			System.out.println("fileName is:" + fileName);

			File f = new File(fileName);

			assertTrue(f.exists() && !f.isDirectory());

			FileUtils.forceDelete(new File(destination));

		} catch (Exception e) {
			th = e;
			Assert.fail("exception thrown:" + e.getMessage());

		}

	}

	/**
	 * Test download data invalid file download.
	 */
	@Test
	public void testDownloadDataInvalidFileDownload() {

		System.out.println("inside testDownloadDataInvalidFileDownload");

		Throwable throwable = null;

		String downloadURL = "http://tutorialspoint.com/java/java_tutorialNotFound.pdf";

		String destination = "destination";

		try {
			dataDownloader.downloadData(downloadURL, destination);

			String fileName = destination.concat(File.separator).concat(downloadURL);

			System.out.println("fileName is:" + fileName);

			File f = new File(fileName);

			assertFalse(f.exists());

		} catch (Exception e) {

			System.out.println("inside exception:" + e.getMessage());
			throwable = e;

		}

	}

}
