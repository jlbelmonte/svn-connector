package svn.masterbranch.com;

import org.junit.Test;
import svn.masterbranch.com.exceptions.SVNException;

public class SVNWrapperTest {
	@Test
	public void testSimpleLogRevision(){
		String uri = "";
		SVNWrapper wrapper = new SVNWrapper(uri, "/usr/bin/svn", null);
		try{
			wrapper.callSVN();
		} catch (SVNException e){
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
	}

	@Test
	public void testCompleteLogFromRevision(){
		String uri = "";
		long revision = 5;
		SVNWrapper wrapper = new SVNWrapper(uri, "/usr/bin/svn", revision);
		try{
			wrapper.callSVN();
		} catch (SVNException e){
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
	}
	
	@Test
	public void testAconnectionError(){
		String uri = "";
		long revision = 5;
		SVNWrapper wrapper = new SVNWrapper(uri, "/usr/bin/svn", revision);
		try{
			wrapper.callSVN();
		} catch (SVNException e){
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
	}

	@Test
	public void testWrongUri(){
		String uri = "";
		long revision = 5;
		SVNWrapper wrapper = new SVNWrapper(uri, "/usr/bin/svn", revision);
		try{
			wrapper.callSVN();
		} catch (SVNException e){
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
	}

	
	
	

}

