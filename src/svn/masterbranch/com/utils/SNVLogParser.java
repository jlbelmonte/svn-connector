package svn.masterbranch.com.utils;


import jregex.Matcher;
import jregex.Pattern;
import siena.Json;
import siena.embed.JsonSerializer;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;

public class SNVLogParser {
	
	public static Json parseData(BufferedReader stdOutput){

		Json result = Json.map();
		String s;
		Json commitList = Json.list();

		Pattern firstCommitLine = new Pattern("\\-+");
		Pattern authorCommitInfo = new Pattern("r(\\d+)\\s*\\|\\s*([\\w\\-\\_\\+\\*\\s\\(\\)@<>\\.\\(\\)]+)\\s*\\|\\s*(.*?)\\s*\\|\\s*([\\w\\s]+)");
		Pattern filesStart = new Pattern("Changed paths\\:");
		Pattern fileName = new Pattern("\\s*([AUDGCM])\\s+([\\w\\-\\./]+)(.*)");

		boolean  lastWasFile = false;
		boolean  authorMatched = false;

		TempCommit t = null;
		
		try{
			int count =0;
			while( ( s = stdOutput.readLine()) != null){
				count ++;
				s = s.trim();
				if (firstCommitLine.matches(s)){
					lastWasFile = false;
					authorMatched = false;
					if (t != null){
						Json commit = Json.map();
						commit.put("revision", t.revision.trim())
								.put("author", t.author.trim())
								.put("added", JsonSerializer.serialize(t.added))
								.put("deleted", JsonSerializer.serialize(t.deleted))
								.put("modified", JsonSerializer.serialize(t.modified))
								.put("message", t.message.trim())
								.put("date", t.dateString.trim());
						commitList.add(commit);
					}
					t = new TempCommit();
					continue;
				}

				Matcher authorMatch = authorCommitInfo.matcher(s);
				if (authorMatch.matches()){
					lastWasFile = false;
					t.author = authorMatch.group(2);
					t.dateString = authorMatch.group(3);
					t.revision = authorMatch.group(1);
					if (t.author == null || t.author.isEmpty() || t.revision == null || t.revision.isEmpty()){
						return rejectLog();
					}
					authorMatched = true;
					continue;
				}

				if (filesStart.matches(s)) continue;
				
				Matcher filePatterMatcher = fileName.matcher(s);
				if (filePatterMatcher.matches()){
					if ("A".equals(filePatterMatcher.group(1))){
						t.added.add(filePatterMatcher.group(2));
						lastWasFile = true;
						continue;
					}
					if ("D".equals(filePatterMatcher.group(1))){
						t.deleted.add(filePatterMatcher.group(2));
						lastWasFile = true;
						continue;
					}
					if ("U".equals(filePatterMatcher.group(1)) || "M".equals(filePatterMatcher.group(1))){
						t.modified.add(filePatterMatcher.group(2));
						lastWasFile = true;
						continue;
					}
					lastWasFile = true;
				}

				Pattern message = new Pattern(".*");
				if (authorMatched && lastWasFile && message.matcher(s).find()){
					t.message += " "+s;
					t.message = t.message.trim();
				}

			}

		} catch (Exception e ){
		  	e.printStackTrace();
			return rejectLog();
		}

		result.put("status", "OK")
				.put("commits", commitList);
		return result;
	}
	
	private static Json rejectLog(){
		Json result = Json.map();
		result.put("status", "NOK")
				.put("error", "Unable to parse the log");
		return result;
	}
	// nested static class instead  inner
	public static class TempCommit{
		List<String> added;
		List<String> deleted;
		List<String> modified;
		String author;
		String dateString;
		String revision;
		String message;

		public TempCommit(){
		added = new ArrayList<String>();
		deleted = new ArrayList<String>();
		modified = new ArrayList<String>();
		author = "";
		dateString = "";
		revision = "";
		message = "";
		}
	}
}
