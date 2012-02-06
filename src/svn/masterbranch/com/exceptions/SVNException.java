package svn.masterbranch.com.exceptions;


public class SVNException extends Exception{
	String msg;
	String uri;
	public SVNException() {}

	public SVNException(String message, String uri) {
		this.msg = message;
		this.uri = uri;
	}

	public String getMessage(){
		return this.msg;
	}
	public String getUri() {
		return this.uri;
	}
}
