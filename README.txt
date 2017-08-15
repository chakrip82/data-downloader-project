chakri Data Download From Multiple Sources/Protocols

The following are the steps needed for executing the project.

The following are the dependencies for executing this project
1) Java7/Java8, Maven3.


Building the DownloadEngine System
----------------------------------

 1) Current DownloadEngine System supports ftp download as well as http download.
 For supporting the ftp download, please goto the file ftpdownloader/src/main/resources/ftp.properties and update the ftp server credentials.
 For suporting the http download no need of any further changes.
 
 2) Also goto downloaderclient/src/main/java/com/chakri/downloader/client/DownloaderClient.java and in the main function specify the url's to download and destination folder.
 	Here you can give valid ur's to download and also invalid url's to download.
 
 3) Please run mvn clean install. This will clean, compile, test, generate artifacts and install the artifacts to repository as well.
 
 	3.1) All the unit-tests and integration tests are executed as part of the above "mvn clean install" command.
 	3.2) If you want only unit-tests and integration-tests to be run please invoke the command "mvn clean test"

Above step 3 will build the all the jars like datadownloader.jar,ftpdownloader.jar,httpdownloader.jar,downloaderclient.jar and also uploads them to local repo.
Also downloaderclient.jar is also a executable jar which is almost like our main class.


Execution of the DownloadEngine System
--------------------------------------

Invoke the command "java -jar downloaderclient/target/downloaderclient-0.0.1-SNAPSHOT.jar "

This will invoke the main class which is DownloaderClient and downloads all the valid url's specified in the main DownloaderClient class to the given destination folder.

Or you can import the whole maven project into IDE(eclipse) and that way also you can run the DownloaderClient
which downloads the files from multiple sources/protocols to local destination folder.

The following is the sample ouput on my local system upon invoking 4 url's with a given destination folder

Out of them

one valid ftp url(to download a 1gb movie) - to make sure huge file download from ftp is verified.
one invalid ftp url
one valid http url(to download a pdf file from http server)
one invalid http url


java -jar downloaderclient/target/downloaderclient-0.0.1-SNAPSHOT.jar 
Jan 15, 2017 4:01:44 PM DownloadWorker run
INFO: protocol is :ftp
Jan 15, 2017 4:01:44 PM DownloadWorker run
INFO: protocol is :http
Jan 15, 2017 4:01:44 PM DownloadWorker run
INFO: protocol is :http
Jan 15, 2017 4:01:44 PM DownloadWorker run
INFO: protocol is :ftp
Jan 15, 2017 4:01:44 PM HTTPDownloadManager connect
INFO: Connecting to the server ...
Jan 15, 2017 4:01:44 PM HTTPDownloadManager connect
INFO: Connecting to the server ...
Jan 15, 2017 4:01:44 PM HTTPDownloadManager connect
INFO: opening connection to http
Jan 15, 2017 4:01:44 PM HTTPDownloadManager connect
INFO: opening connection to http
Jan 15, 2017 4:01:44 PM FTPDownloader parseURL
INFO: inside parseURL:ftp://localhost123/Downloads/Colonia (2015) [YTS.AG]/Colonia.2015.720p.BluRay.x264-[YTS.AG].mp4
Jan 15, 2017 4:01:44 PM FTPDownloader parseURL
INFO: inside parseURL:ftp://localhost/Downloads/Colonia (2015) [YTS.AG]/Colonia.2015.720p.BluRay.x264-[YTS.AG].mp4
Jan 15, 2017 4:01:44 PM FTPDownloader parseURL
INFO: host details are :localhost123
Jan 15, 2017 4:01:44 PM FTPDownloader parseURL
INFO: host details are :localhost
Jan 15, 2017 4:01:44 PM FTPDownloader parseURL
INFO: printing ftpEntity remoteURL:Downloads/Colonia (2015) [YTS.AG]/Colonia.2015.720p.BluRay.x264-[YTS.AG].mp4
Jan 15, 2017 4:01:44 PM FTPDownloader parseURL
INFO: printing ftpEntity remoteURL:Downloads/Colonia (2015) [YTS.AG]/Colonia.2015.720p.BluRay.x264-[YTS.AG].mp4
Jan 15, 2017 4:01:44 PM FTPDownloadManager connect
INFO: Connecting to the server ...
Jan 15, 2017 4:01:44 PM FTPDownloadManager connect
INFO: Connecting to the server ...
isConnected:true
Jan 15, 2017 4:01:44 PM HTTPDownloadManager connect
INFO: The client was connected successfully!
Jan 15, 2017 4:01:44 PM HTTPDownloadManager download
INFO: in download api remoteURL is:http://tutorialspoint.com/java/java_tutorial.pdf
Jan 15, 2017 4:01:44 PM FTPDownloadManager connect
INFO: reply code returned is:220
Jan 15, 2017 4:01:44 PM HTTPDownloadManager download
INFO: uri in download is :http://tutorialspoint.com/java/java_tutorial.pdf
Jan 15, 2017 4:01:44 PM HTTPDownloadManager download
INFO: invoking execute method
Jan 15, 2017 4:01:44 PM FTPDownloadManager connect
SEVERE: IO Exception thrown:localhost123: unknown error
Jan 15, 2017 4:01:44 PM FTPDownloader downloadData
SEVERE: Download data Failed:localhost123: unknown error
Jan 15, 2017 4:01:44 PM HTTPDownloadManager connect
SEVERE: exception while opening connection:tutorialspoint123.com
Jan 15, 2017 4:01:44 PM HTTPDownloader downloadData
SEVERE: Download data Failed:tutorialspoint123.com
Jan 15, 2017 4:01:44 PM FTPDownloadManager connect
INFO: The client was connected successfully!
Jan 15, 2017 4:01:44 PM FTPDownloader downloadData
INFO: isConnected is:true
Jan 15, 2017 4:01:44 PM FTPDownloader downloadData
INFO: Downloads/Colonia (2015) [YTS.AG]/Colonia.2015.720p.BluRay.x264-[YTS.AG].mp4
Jan 15, 2017 4:01:44 PM FTPDownloader downloadData
INFO: /Users/m01457/Downloads/destination//Downloads/Colonia (2015) [YTS.AG]/Colonia.2015.720p.BluRay.x264-[YTS.AG].mp4
Jan 15, 2017 4:01:44 PM FTPDownloadManager download
INFO: File Downloading...
Jan 15, 2017 4:01:45 PM HTTPDownloadManager download
INFO: status code is:200
Jan 15, 2017 4:01:45 PM HTTPDownloadManager download
INFO: File Downloading...
Jan 15, 2017 4:01:45 PM HTTPDownloadManager download
INFO: File has been downloaded successfully.
Jan 15, 2017 4:01:45 PM HTTPDownloadManager disconnect
INFO: disconnecting ...
Jan 15, 2017 4:01:45 PM HTTPDownloadManager disconnect
INFO: The client was disconnected successfully!
Jan 15, 2017 4:01:46 PM FTPDownloadManager download
INFO: File has been downloaded successfully.
Jan 15, 2017 4:01:46 PM FTPDownloadManager disconnect
INFO: Logout ...
Jan 15, 2017 4:01:46 PM FTPDownloadManager disconnect
INFO: disconnecting ...
Jan 15, 2017 4:01:46 PM FTPDownloadManager disconnect
INFO: The client was disconnected successfully!
Jan 15, 2017 4:01:46 PM DownloaderClient downloadDataFromSources
INFO: Finished all threads


Post this you can verify the succesfully downloaded files in destination folder.




