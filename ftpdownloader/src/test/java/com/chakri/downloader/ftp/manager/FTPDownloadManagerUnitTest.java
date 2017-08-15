package com.chakri.downloader.ftp.manager;

import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.net.ftp.FTPClient;
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
 * The Class FTPDownloadManagerUnitTest.
 */
@RunWith(MockitoJUnitRunner.class)
public class FTPDownloadManagerUnitTest extends TestCase {

	/** The ftp client. */
	@Mock
	FTPClient ftpClient;

	/** The ftp download manager. */
	@InjectMocks
	FTPDownloadManager ftpDownloadManager;

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
		try {
			// ftpClient.connect("localhost", 21);

			doNothing().when(ftpClient).connect("localhost", 21);

			when(ftpClient.getReplyCode()).thenReturn(200);

			when(ftpClient.login("m01457", "myntra@123")).thenReturn(true);

			assertTrue(ftpDownloadManager.connect("localhost", 21, "m01457", "myntra@123"));

		} catch (SocketException e) {

			throw new DownloadManagerException(e.getMessage());
		} catch (IOException e) {

			throw new DownloadManagerException(e.getMessage());
		} catch (Exception e) {
			throw new DownloadManagerException(e.getMessage());
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

		try {

			doThrow(IOException.class).when(ftpClient).connect("localhost123", 21);

			when(ftpClient.login("m01457", "myntra@123")).thenReturn(true);

			ftpDownloadManager.connect("localhost123", 21, "m01457", "myntra@123");

			Assert.fail("DownloadManagerException is not thrown !");

		} catch (SocketException e) {
			throw new DownloadManagerException(e.getMessage());
		} catch (IOException e) {
			throw new DownloadManagerException(e.getMessage());
		} catch (Exception e) {
			throw new DownloadManagerException(e.getMessage());
		}

	}

	/**
	 * Test connect invalid login.
	 *
	 * @throws DownloadManagerException the download manager exception
	 */
	@Test(expected = DownloadManagerException.class)
	public void testConnectInvalidLogin() throws DownloadManagerException {

		System.out.println("Inside testConnectInvalidLogin");

		try {

			ftpClient.connect("localhost", 21);

			when(ftpClient.getReplyCode()).thenReturn(201);

			when(ftpClient.login("m01457", "myntra@1234")).thenReturn(false);

			ftpDownloadManager.connect("localhost", 21, "m01457", "myntra@1234");
			Assert.fail("DownloadManagerException is not thrown !");

		} catch (SocketException e) {

			throw new DownloadManagerException(e.getMessage());
		} catch (IOException e) {

			throw new DownloadManagerException(e.getMessage());
		} catch (Exception e) {

			throw new DownloadManagerException(e.getMessage());
		}

	}

	/**
	 * Test disconnect.
	 */
	@Test
	public void testDisconnect() {
		System.out.println("Inside testDisconnect()");
		try {

			when(ftpClient.isConnected()).thenReturn(true);

			when(ftpClient.logout()).thenReturn(true);

			// doNothing().when(ftpClient).disconnect();

			doAnswer(new Answer<Void>() {
				public Void answer(InvocationOnMock invocation) throws Throwable {
					when(ftpClient.isConnected()).thenReturn(false);
					return null;
				}
			}).when(ftpClient).disconnect();

			ftpDownloadManager.disconnect();

			System.out.println("printing isconnected:" + ftpClient.isConnected());

			assertFalse(ftpClient.isConnected());

			// System.out.println("Done with testDisconnect() with no exceptions
			// thrown");

		} catch (SocketException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
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

			String remoteURL = "myfile.txt";

			final File tempFile = folder.newFile(remoteURL);

			final File destination_fodler = folder.newFolder("destination");

			String destination = destination_fodler.getName().concat(File.separator).concat(remoteURL);

			System.out.println("destination is:" + destination);

			String encoding = null;

			FileUtils.writeStringToFile(tempFile, "hello world", encoding);

			System.out.println(FileUtils.readFileToString(tempFile, encoding));

			InputStream inputStream = null;

			inputStream = new FileInputStream(tempFile);

			when(ftpClient.retrieveFileStream(remoteURL)).thenReturn(inputStream);

			when(ftpClient.getReplyCode()).thenReturn(200);

			when(ftpClient.completePendingCommand()).thenReturn(true);

			ftpDownloadManager.download(remoteURL, destination);

			// assertTrue()

			assertEquals("hello world", FileUtils.readFileToString(new File(destination), encoding));

			FileUtils.deleteQuietly(new File(destination));

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

			String remoteURL = "myfile.txt";

			String remoteURL_Invliad = "myfile1.txt";

			final File tempFile = folder.newFile(remoteURL);

			final File destination_fodler = folder.newFolder("destination");

			String destination = destination_fodler.getName().concat(File.separator).concat(remoteURL);

			System.out.println("destination is:" + destination);

			String encoding = null;

			FileUtils.writeStringToFile(tempFile, "hello world", encoding);

			System.out.println(FileUtils.readFileToString(tempFile, encoding));

			InputStream inputStream = null;

			inputStream = new FileInputStream(tempFile);

			when(ftpClient.retrieveFileStream(remoteURL)).thenReturn(inputStream);

			when(ftpClient.getReplyCode()).thenReturn(550);

			when(ftpClient.completePendingCommand()).thenReturn(true);

			ftpDownloadManager.download(remoteURL_Invliad, destination);

			Assert.fail("DownloadManager Exception not thrown");

			// assertTrue()

			// assertEquals("hello world", FileUtils.readFileToString(new
			// File(destination), encoding));

			// FileUtils.deleteQuietly(new File(destination));

		} catch (FileNotFoundException e) {
			throw new DownloadManagerException(e.getMessage());
		} catch (IOException e) {
			throw new DownloadManagerException(e.getMessage());
		} catch (Exception e) {
			throw new DownloadManagerException(e.getMessage());
		}

	}

	/**
	 * Test download incomplete file download.
	 *
	 * @throws DownloadManagerException the download manager exception
	 */
	@Test(expected = DownloadManagerException.class)
	public void testDownloadIncompleteFileDownload() throws DownloadManagerException {

		System.out.println("Inside testDownloadIncompleteFileDownload()");

		try {

			String remoteURL = "myfile.txt";

			final File tempFile = folder.newFile(remoteURL);

			final File destination_fodler = folder.newFolder("destination");

			String destination = destination_fodler.getName().concat(File.separator).concat(remoteURL);

			System.out.println("destination is:" + destination);

			String encoding = null;

			FileUtils.writeStringToFile(tempFile, "hello world", encoding);

			System.out.println(FileUtils.readFileToString(tempFile, encoding));

			InputStream inputStream = null;

			inputStream = new FileInputStream(tempFile);

			when(ftpClient.retrieveFileStream(remoteURL)).thenReturn(inputStream);

			when(ftpClient.getReplyCode()).thenReturn(200);

			doThrow(IOException.class).when(ftpClient).completePendingCommand();

			ftpDownloadManager.download(remoteURL, destination);

			Assert.fail("DownloadManager Exception not thrown");

		} catch (FileNotFoundException e) {
			throw new DownloadManagerException(e.getMessage());
		} catch (IOException e) {
			throw new DownloadManagerException(e.getMessage());
		} catch (Exception e) {
			throw new DownloadManagerException(e.getMessage());
		}

	}
	
	
	
	
	/**
	 * Test download Failed file download.
	 *
	 * @throws DownloadManagerException the download manager exception
	 */
	@Test(expected = DownloadManagerException.class)
	public void testDownloadFailedFileDownload() throws DownloadManagerException {

		System.out.println("Inside testDownloadFailedFileDownload()");
		InputStream inputStream = null;

		try {

			String remoteURL = "myfile.txt";

			final File tempFile = folder.newFile(remoteURL);

			final File destination_fodler = folder.newFolder("destination");

			String destination = destination_fodler.getName().concat(File.separator).concat(remoteURL);

			System.out.println("destination is:" + destination);

			String encoding = null;

			FileUtils.writeStringToFile(tempFile, "hello world", encoding);

			System.out.println(FileUtils.readFileToString(tempFile, encoding));

			

			inputStream = new FileInputStream(tempFile);

			//when(ftpClient.retrieveFileStream(remoteURL)).thenReturn(inputStream);
			
			doThrow(FileNotFoundException.class).when(ftpClient.retrieveFileStream(remoteURL));

			when(ftpClient.getReplyCode()).thenReturn(200);

			when(ftpClient.completePendingCommand()).thenReturn(true);

			ftpDownloadManager.download(remoteURL, destination);

			Assert.fail("DownloadManager Exception not thrown");

		} catch (FileNotFoundException e) {
			throw new DownloadManagerException(e.getMessage());
		} catch (IOException e) {
			throw new DownloadManagerException(e.getMessage());
		} catch (Exception e) {
			throw new DownloadManagerException(e.getMessage());
		} finally {
			try {
				inputStream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
	
	
	/**
	 * Test download Partial file download.
	 *
	 * @throws DownloadManagerException the download manager exception
	 */
	@Test(expected = DownloadManagerException.class)
	public void testDownloadPartialFileDownload() throws DownloadManagerException {

		System.out.println("Inside testDownloadPartialFileDownload()");
		InputStream inputStream = null;

		try {

			String remoteURL = "myfile.txt";

			final File tempFile = folder.newFile(remoteURL);

			final File destination_fodler = folder.newFolder("destination");

			String destination = destination_fodler.getName().concat(File.separator).concat(remoteURL);

			System.out.println("destination is:" + destination);

			String encoding = null;

			FileUtils.writeStringToFile(tempFile, "hello world", encoding);

			System.out.println(FileUtils.readFileToString(tempFile, encoding));

			

			inputStream = new FileInputStream(tempFile);
			
			//close the inputstream
			inputStream.close();
			
			System.out.println("inputStream closed");

			when(ftpClient.retrieveFileStream(remoteURL)).thenReturn(inputStream);
			
			//doThrow(FileNotFoundException.class).when(ftpClient.retrieveFileStream(remoteURL));

			when(ftpClient.getReplyCode()).thenReturn(200);

			when(ftpClient.completePendingCommand()).thenReturn(true);
			
			

			ftpDownloadManager.download(remoteURL, destination);

			Assert.fail("DownloadManager Exception not thrown");

		} catch (FileNotFoundException e) {
			throw new DownloadManagerException(e.getMessage());
		} catch (IOException e) {
			throw new DownloadManagerException(e.getMessage());
		} catch (Exception e) {
			throw new DownloadManagerException(e.getMessage());
		} finally {
			try {
				inputStream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

}
