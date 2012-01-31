package exceptions;


public class SVNException extends Exception{
	String msg;
	String uri;
	public SVNException() {}

	public SVNException(String uri, String message) {
		this.msg = message;
		this.repoPath = repoId;
	}

	public String getMessage(){
		return this.msg;
	}
	public String getPath() {
		return repoPath;
	}
}
