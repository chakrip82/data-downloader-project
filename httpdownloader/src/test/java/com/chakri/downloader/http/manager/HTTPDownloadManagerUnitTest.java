package com.chakri.downloader.http.manager;

import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpConnection;
import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.io.FileUtils;
import org.junit.Assert;
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

import junit.framework.TestCase;


/**
 * The Class HTTPDownloadManagerUnitTest.
 */
@RunWith(MockitoJUnitRunner.class)
public class HTTPDownloadManagerUnitTest extends TestCase {

	/** The http client. */
	@Mock
	HttpClient httpClient;

	/** The method. */
	@Mock
	GetMethod method;

	/** The connection. */
	@Mock
	HttpConnection connection;

	/** The http download manager. */
	@InjectMocks
	HTTPDownloadManager httpDownloadManager;

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
	 * Test connect.
	 *
	 * @throws DownloadManagerException the download manager exception
	 */
	@Test
	public void testConnect() throws DownloadManagerException {
		System.out.println("Inside testConnect()");

		String host = "tutorialspoint.com";
		int port = 80;
		String userName = "";
		String password = "";

		try {

			// doNothing().when(connection).open();

			doAnswer(new Answer<Void>() {
				public Void answer(InvocationOnMock invocation) throws Throwable {
					when(connection.isOpen()).thenReturn(true);
					return null;
				}
			}).when(connection).open();

			// when(connection.isOpen()).thenReturn(true);

			assertTrue(httpDownloadManager.connect(host, port, userName, password));

		} catch (Exception ex) {
			throw new DownloadManagerException(ex.getMessage());
		}

	}

	/**
	 * Test connect invalid host.
	 *
	 * @throws DownloadManagerException the download manager exception
	 */
	@Test(expected = DownloadManagerException.class)
	public void testConnectInvalidHost() throws DownloadManagerException {
		System.out.println("Inside testConnectInvalidHost()");

		String host = "tutorialspointnotexists.com";
		int port = 80;
		String userName = "";
		String password = "";

		try {

			doNothing().when(connection).open();

			when(connection.isOpen()).thenReturn(true);

			httpDownloadManager.connect(host, port, userName, password);

			Assert.fail("DownloadManagerException is not thrown !");

		} catch (Exception ex) {
			throw new DownloadManagerException(ex.getMessage());
		}

	}

	/**
	 * Test disconnect.
	 */
	@Test
	public void testDisconnect() {
		System.out.println("Inside testDisconnect()");
		try {

			doAnswer(new Answer<Void>() {
				public Void answer(InvocationOnMock invocation) throws Throwable {
					when(connection.isOpen()).thenReturn(false);
					return null;
				}
			}).when(method).releaseConnection();

			httpDownloadManager.disconnect();

			System.out.println("printing isconnected:" + connection.isOpen());

			assertFalse(connection.isOpen());

		} catch (Exception e) {

			e.printStackTrace();
		}

	}

	/**
	 * Test download.
	 *
	 * @throws DownloadManagerException the download manager exception
	 */
	@Test
	public void testDownload() throws DownloadManagerException {

		System.out.println("Inside testDownload()");

		try {

			final String remoteURL = "myfile.txt";

			final File tempFile = folder.newFile(remoteURL);

			final File destination_fodler = folder.newFolder("destination");

			String destination = destination_fodler.getName().concat(File.separator).concat(remoteURL);

			System.out.println("destination is:" + destination);

			String encoding = null;

			FileUtils.writeStringToFile(tempFile, "hello world", encoding);

			System.out.println(FileUtils.readFileToString(tempFile, encoding));

			InputStream inputStream = null;

			inputStream = new FileInputStream(tempFile);

			String charset = "UTF-8";

			final URI uri = new URI(remoteURL, true, charset);

			doAnswer(new Answer<URI>() {
				public URI answer(InvocationOnMock invocation) throws Throwable {
					when(method.getURI()).thenReturn(uri);
					return uri;
				}
			}).when(method).setURI(uri);

			method.setURI(uri);

			System.out.println("uri is :" + method.getURI());

			when(httpClient.executeMethod(method)).thenReturn(200);

			when(method.getResponseBodyAsStream()).thenReturn(inputStream);

			httpDownloadManager.download(remoteURL, destination);

			assertEquals("hello world", FileUtils.readFileToString(new File(destination), encoding));

		} catch (FileNotFoundException e) {
			throw new DownloadManagerException(e.getMessage());
		} catch (IOException e) {
			throw new DownloadManagerException(e.getMessage());
		} catch (Exception e) {
			throw new DownloadManagerException(e.getMessage());
		}

	}

	/**
	 * Test download invalid file download.
	 *
	 * @throws DownloadManagerException the download manager exception
	 */
	@Test(expected = DownloadManagerException.class)
	public void testDownloadInvalidFileDownload() throws DownloadManagerException {

		System.out.println("Inside testDownloadInvalidFileDownload()");

		try {

			final String remoteURL = "myfile.txt";

			final String remoteURL_INVALID = "myfile1.txt";

			final File tempFile = folder.newFile(remoteURL);

			final File destination_fodler = folder.newFolder("destination");

			String destination = destination_fodler.getName().concat(File.separator).concat(remoteURL);

			System.out.println("destination is:" + destination);

			String encoding = null;

			FileUtils.writeStringToFile(tempFile, "hello world", encoding);

			System.out.println(FileUtils.readFileToString(tempFile, encoding));

			InputStream inputStream = null;

			inputStream = new FileInputStream(tempFile);

			String charset = "UTF-8";

			final URI uri = new URI(remoteURL, true, charset);

			doAnswer(new Answer<URI>() {
				public URI answer(InvocationOnMock invocation) throws Throwable {
					when(method.getURI()).thenReturn(uri);
					return uri;
				}
			}).when(method).setURI(uri);

			method.setURI(uri);

			System.out.println("uri is :" + method.getURI());

			when(httpClient.executeMethod(method)).thenReturn(550);

			when(method.getResponseBodyAsStream()).thenReturn(inputStream);

			httpDownloadManager.download(remoteURL_INVALID, destination);

			assertEquals("hello world", FileUtils.readFileToString(new File(destination), encoding));

		} catch (FileNotFoundException e) {
			throw new DownloadManagerException(e.getMessage());
		} catch (IOException e) {
			throw new DownloadManagerException(e.getMessage());
		} catch (Exception e) {
			throw new DownloadManagerException(e.getMessage());
		}

	}

}
