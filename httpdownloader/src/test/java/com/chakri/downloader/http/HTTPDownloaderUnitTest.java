package com.chakri.downloader.http;

import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import com.chakri.downloader.exception.DownloadManagerException;
import com.chakri.downloader.manager.DownloadManager;

import junit.framework.TestCase;


/**
 * The Class HTTPDownloaderUnitTest.
 */
@RunWith(MockitoJUnitRunner.class)
public class HTTPDownloaderUnitTest extends TestCase {

	/** The download manager. */
	@Mock
	DownloadManager downloadManager;

	/** The http downloader. */
	@InjectMocks
	HTTPDownloader httpDownloader;

	/** The folder. */
	@Rule
	public TemporaryFolder folder = new TemporaryFolder();

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	@Before
	public void setUp() throws Exception {
		super.setUp();
		MockitoAnnotations.initMocks(this);
	}

	/**
	 * Test get protocol.
	 */
	@Test
	public void testGetProtocol() {
		System.out.println("inside testGetProtocol()");

		String protocol = "http";

		assertEquals(protocol, httpDownloader.getProtocol());
	}

	/**
	 * Test download data.
	 *
	 * @throws DownloadManagerException the download manager exception
	 */
	@Test
	public void testDownloadData() throws DownloadManagerException {

		System.out.println("Inside testDownloadData()");

		String host = "localhost";
		int port = 21;

		String userName = "";
		String password = "";

		final String downloadURL = "myfile.txt";

		final String destination = "destination/";

		try {

			when(downloadManager.connect(host, port, userName, password)).thenReturn(true);

			doAnswer(new Answer<Void>() {

				public Void answer(InvocationOnMock invocation) throws Throwable {

					System.out.println("inside answer function");

					// when(downloadManager.isConnected()).thenReturn(false);

					final File tempFile = folder.newFile(downloadURL);

					String encoding = null;

					FileUtils.writeStringToFile(tempFile, "hello world", encoding);

					System.out.println(FileUtils.readFileToString(tempFile, encoding));

					// final String destination =
					// "/Users/m01457/Downloads/myfile.txt";

					final File destination_fodler = folder.newFolder(destination);

					String destination_file = destination_fodler.getName().concat(File.separator).concat(downloadURL);

					System.out.println("destination is:" + destination_file);

					FileUtils.writeStringToFile(new File(destination_file), "hello world", encoding);

					System.out.println(FileUtils.readFileToString(new File(destination_file), encoding));

					return null;
				}
			}).when(downloadManager).download(downloadURL, destination.concat(downloadURL));

			doNothing().when(downloadManager).disconnect();

			System.out.println("invoking downloadData");

			httpDownloader.downloadData("ftp://localhost:21/myfile.txt", destination);

			// when(downloadManager.download(downloadURL, destination))

		} catch (DownloadManagerException e) {
			// throw new DownloadManagerException(e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
