package svn.masterbranch.com;

import org.junit.Test;
import siena.Json;
import svn.masterbranch.com.exceptions.SVNException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

public class SVNWrapperTest {
	
	@Test
	public void testCodeplexRepository() throws SVNException {
		String uri = "https://exepack.svn.codeplex.com/svn";
		SVNWrapper wrapper = new SVNWrapper(uri, "/usr/bin/svn", null);
		Json svnResult = wrapper.callSVN(); 
		assertEquals("OK", svnResult.get("status").str());
		Json commits = svnResult.get("commits");
		assertEquals(71, commits.size());
		for (Json commit : commits) {
			assertNotSame("", commit.get("author").str());
		}
	}
	
	@Test
	public void testSimpleLogRevision(){
		String uri = "http://dmo-compact.googlecode.com/svn/trunk/";
		SVNWrapper wrapper = new SVNWrapper(uri, "/usr/bin/svn", null);
		Json svnResult = Json.map(); 
		try{
			svnResult = wrapper.callSVN();
		} catch (SVNException e){
			e.printStackTrace();
		}

		assertEquals("OK", svnResult.get("status").str());
		Json commits = svnResult.get("commits");
		assertEquals(6, commits.size());
		
		Json commit1 = commits.at(5);
		Json commit2 = commits.at(4);
		Json commit3 = commits.at(3);
		Json commit4 = commits.at(2);
		Json commit5 = commits.at(1);
		Json commit6 = commits.at(0);

		assertEquals("1", commit1.get("revision").str());
		assertEquals("2", commit2.get("revision").str());
		assertEquals("3", commit3.get("revision").str());
		assertEquals("4", commit4.get("revision").str());
		assertEquals("5", commit5.get("revision").str());
		assertEquals("6", commit6.get("revision").str());

		assertEquals("only uses yui compresor, no obfuscation for js code", commit6.get("message").str());

		assertEquals("jlbelmonte", commit2.get("author").str());
		assertEquals("jlbelmonte", commit3.get("author").str());
		assertEquals("jlbelmonte", commit4.get("author").str());
		assertEquals("jlbelmonte", commit5.get("author").str());
		assertEquals("jlbelmonte", commit6.get("author").str());
		
		Json deleted = Json.list("/trunk/Pack.pm", "/trunk/ParseMaster.pm","/trunk/Redistribute.txt", "/trunk/jsPacker.pl","/trunk/perllibs");
		assertEquals(deleted, commit6.get("deleted"));
	}
	@Test
	public void testSimpleLogRevisionZeroRev(){
		String uri = "http://dmo-compact.googlecode.com/svn/trunk/";
		SVNWrapper wrapper = new SVNWrapper(uri, "/usr/bin/svn", 0L);
		Json svnResult = Json.map();
		try{
			svnResult = wrapper.callSVN();
		} catch (SVNException e){
			e.printStackTrace();
		}

		assertEquals("OK", svnResult.get("status").str());
		Json commits = svnResult.get("commits");
		assertEquals(6, commits.size());

		Json commit1 = commits.at(5);
		Json commit2 = commits.at(4);
		Json commit3 = commits.at(3);
		Json commit4 = commits.at(2);
		Json commit5 = commits.at(1);
		Json commit6 = commits.at(0);

		assertEquals("1", commit1.get("revision").str());
		assertEquals("2", commit2.get("revision").str());
		assertEquals("3", commit3.get("revision").str());
		assertEquals("4", commit4.get("revision").str());
		assertEquals("5", commit5.get("revision").str());
		assertEquals("6", commit6.get("revision").str());

		assertEquals("only uses yui compresor, no obfuscation for js code", commit6.get("message").str());

		assertEquals("jlbelmonte", commit2.get("author").str());
		assertEquals("jlbelmonte", commit3.get("author").str());
		assertEquals("jlbelmonte", commit4.get("author").str());
		assertEquals("jlbelmonte", commit5.get("author").str());
		assertEquals("jlbelmonte", commit6.get("author").str());

		Json deleted = Json.list("/trunk/Pack.pm", "/trunk/ParseMaster.pm","/trunk/Redistribute.txt", "/trunk/jsPacker.pl","/trunk/perllibs");
		assertEquals(deleted, commit6.get("deleted"));
	}


	@Test
	public void testCompleteLogFromRevision(){
		String uri = "http://dmo-compact.googlecode.com/svn/trunk/";
		long revision = 5;

		SVNWrapper wrapper = new SVNWrapper(uri, "/usr/bin/svn", revision);
		Json svnResult = Json.map();
		try{
			svnResult = wrapper.callSVN();
		} catch (SVNException e){
			e.printStackTrace();
		}
		assertEquals("OK", svnResult.get("status").str());
		Json commits = svnResult.get("commits");
		assertEquals(2, commits.size());
		System.out.println(commits);

		Json commit6 = commits.at(1);
		Json commit5 = commits.at(0);

		assertEquals("6", commit6.get("revision").str());

		assertEquals("only uses yui compresor, no obfuscation for js code", commit6.get("message").str());

		assertEquals("jlbelmonte", commit6.get("author").str());

		Json deleted = Json.list("/trunk/Pack.pm", "/trunk/ParseMaster.pm","/trunk/Redistribute.txt", "/trunk/jsPacker.pl","/trunk/perllibs");
		assertEquals(deleted, commit6.get("deleted"));
	}

	@Test(expected = SVNException.class)
	public void testNoNewRevisions ()throws SVNException{
		String uri = "http://dmo-compact.googlecode.com/svn/trunk/";
		long revision = 7;

		SVNWrapper wrapper = new SVNWrapper(uri, "/usr/bin/svn", revision);
		Json svnResult = Json.map();

		svnResult = wrapper.callSVN();

		assertTrue(svnResult.isEmpty());
	}
	
	@Test
	public void testNotResolvHost(){
		String uri = "http://dmo-compact.googlecode.com/svn/trunk/";
		long revision = 6;

		SVNWrapper wrapper = new SVNWrapper(uri, "/usr/bin/svn", revision);
		Json svnResult = Json.map();
		try{
			svnResult = wrapper.callSVN();
		} catch (SVNException e){
			assertEquals("Unable to resolv host" , e.getMessage());
			e.printStackTrace();
		}
		//assertTrue(svnResult.isEmpty());
	}




	
	
	

}

