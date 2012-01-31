package svn.masterbranch.com.exceptions;


public class SVNException extends Exception{
	String msg;
	String uri;
	public SVNException() {}

	public SVNException(String uri, String message) {
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
