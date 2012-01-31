package svn.masterbranch.com;

import org.junit.Test;
import siena.Json;
import svn.masterbranch.com.utils.SNVLogParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import static org.junit.Assert.*;

public class SVNLogParserTest {

	@Test
	public void testOne(){
		try {
			System.out.println();
			BufferedReader br = new BufferedReader(new FileReader(new File(new File("").getAbsolutePath()+"/svnconnector/test/svn/masterbranch/com/svn1.log")));
			Json parserResult = SNVLogParser.parseData(br);
			assertEquals("OK", parserResult.get("status").str());
			assertNotNull(parserResult.get("commits"));
			assertTrue(parserResult.get("commits").isList());
			Json list = parserResult.get("commits");
			assertEquals(3, list.size());

			// first commit
			Json commit = list.at(0);
			assertEquals("Mock_author", commit.get("author").str());
			assertEquals("1", commit.get("revision").str());
			assertEquals("Message rev1", commit.get("message").str());

			Json addedFiles = commit.get("added");
			assertEquals(1, addedFiles.size());
			Json deletedFiles = commit.get("deleted");
			assertTrue(deletedFiles.isEmpty());
			Json modifiedFiles = commit.get("modified");
			assertTrue(modifiedFiles.isEmpty());

			assertEquals("2012-01-23 10:13:16 +0100 (Mon, 23 Jan 2012)", commit.get("date").str());

			//second commit
			commit = list.at(1);
			assertEquals("Mock_author", commit.get("author").str());
			assertEquals("2", commit.get("revision").str());
			assertEquals("Message rev2", commit.get("message").str());

			addedFiles = commit.get("added");
			assertEquals(1, addedFiles.size());
			deletedFiles = commit.get("deleted");
			assertEquals(1, deletedFiles.size());
			modifiedFiles = commit.get("modified");
			assertEquals(2,modifiedFiles.size());

			assertEquals("2012-01-23 10:10:51 +0100 (Mon, 23 Jan 2012)", commit.get("date").str());

			//third commit
			commit = list.at(2);
			assertEquals("Mock_author2", commit.get("author").str());
			assertEquals("3", commit.get("revision").str());
			assertEquals("Message rev3 Message rev3 line2 Message rev3 line3 with dots... ;", commit.get("message").str());

			addedFiles = commit.get("added");
			assertTrue(addedFiles.isEmpty());
			deletedFiles = commit.get("deleted");
			assertTrue(deletedFiles.isEmpty());
			modifiedFiles = commit.get("modified");
			assertEquals(2,modifiedFiles.size());

			assertEquals("2012-01-21 10:52:48 +0100 (Sat, 21 Jan 2012)", commit.get("date").str());

		} catch (IOException e){
			e.printStackTrace();
		}
	}
}
