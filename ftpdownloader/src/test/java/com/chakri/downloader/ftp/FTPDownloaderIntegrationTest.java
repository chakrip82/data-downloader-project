package com.chakri.downloader.ftp;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.chakri.downloader.DataDownloader;

import junit.framework.TestCase;


/**
 * The Class FTPDownloaderIntegrationTest.
 */
@RunWith(BlockJUnit4ClassRunner.class)
public class FTPDownloaderIntegrationTest extends TestCase {

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
		dataDownloader = new FTPDownloader();

	}

	/**
	 * Test download data.
	 */
	@Test
	public void testDownloadData() {

		String downloadURL = "ftp://localhost:21/hello.txt";

		String destination = "destination";

		try {
			dataDownloader.downloadData(downloadURL, destination);

			String fileName = destination.concat(File.separator).concat("hello.txt");

			System.out.println("fileName is:" + fileName);

			System.out.println(FileUtils.readFileToString(new File(fileName), encoding).trim().length());

			assertEquals("hello world", FileUtils.readFileToString(new File(fileName), encoding).trim());

			FileUtils.forceDelete(new File(destination));

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * Test download data invalid file download.
	 */
	@Test
	public void testDownloadDataInvalidFileDownload() {

		System.out.println("inside testDownloadDataInvalidFileDownload");

		Throwable throwable = null;

		String downloadURL = "ftp://localhost:21/hellooo.txt";

		String destination = "destination";

		try {
			dataDownloader.downloadData(downloadURL, destination);

			String fileName = destination.concat(File.separator).concat("hellooo.txt");

			System.out.println("fileName is:" + fileName);

			System.out.println(FileUtils.readFileToString(new File(fileName), encoding).trim().length());

			// assertEquals("hello world", FileUtils.readFileToString(new
			// File(fileName), encoding).trim());
		} catch (IOException e) {
			throwable = e;

		} catch (Exception e) {
			throwable = e;

		}

		assertTrue(throwable instanceof IOException);

	}

}
