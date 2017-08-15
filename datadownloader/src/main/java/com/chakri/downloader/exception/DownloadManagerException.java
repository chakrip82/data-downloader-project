package com.chakri.downloader.exception;


/**
 * The Class DownloadManagerException.
 */
public class DownloadManagerException extends Exception {
	
	/** The message. */
	private String message;

	/**
	 * Instantiates a new download manager exception.
	 */
	public DownloadManagerException() {

	}

	/**
	 * Instantiates a new download manager exception.
	 *
	 * @param msg the msg
	 */
	public DownloadManagerException(String msg) {
		this.message = msg;
	}

	/* (non-Javadoc)
	 * @see java.lang.Throwable#toString()
	 */
	@Override
	public String toString() {
		return this.message;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Throwable#getMessage()
	 */
	@Override
	public String getMessage() {
        return this.message;
    }

}
