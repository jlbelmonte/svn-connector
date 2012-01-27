package utils;

import org.apache.log4j.Logger;
import siena.Json;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utilities {
	public static Logger logger = Logger.getLogger(Utilities.class);

	public static String getString(BufferedReader bR) {
		String s = null;
		String out = "";
		try {
			while ((s = bR.readLine()) != null) {
				out += s;
			}
			return out;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String piped2String(PipedInputStream pis){
		final PipedOutputStream pos = new PipedOutputStream();
		final BufferedInputStream bis = new BufferedInputStream(pis);
		final BufferedOutputStream bos = new BufferedOutputStream(pos);
		final StringBuffer sb = new StringBuffer();
		int p = 0;
		byte[] b = new byte[1024];
		try {
			while (!(bis.available()<1)){
				p = bis.read(b, 0, 1024);
				bos.write(b, 0 , p);
				sb.append(new String(b, 0 , p));
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
			ioe.getMessage();
		}
		String content = sb.toString();
		return content;
	}


	private static Json processRevisionLog(Matcher rev, List<String> files) {
		String revision = rev.group(1);
		String tree = rev.group(2);
		String author = rev.group(3);
		String email = rev.group(4);
		String message = rev.group(5).replaceAll("-", " ");
		String date = rev.group(6);
		Map<String, List<String>> mapFiles = new HashMap<String, List<String>>();
		Pattern pattern = Pattern.compile("([ADM])\\s+(.*?)$");
		for (String file : files) {
			if (file.isEmpty())
				continue;
			Matcher matcher = pattern.matcher(file);
			if (matcher.find()) {
				String fileType = matcher.group(1);
				String fileName = matcher.group(2);
				List<String> filesNames = new ArrayList<String>();
				if (mapFiles.containsKey(fileType)) {
					filesNames = mapFiles.get(fileType);
				}
				filesNames.add(fileName);
				mapFiles.put(fileType, filesNames);
			}
		}
		
		Json revLog = Json.map();
	
		revLog.put("revision", revision)
			.put("tree", tree)
			.put("author", author)
			.put("email", email)
			.put("date", date)
			.put("message", message);
		if (mapFiles.containsKey("A")) {
			revLog.put("added", mapFiles.get("A"));
		} else {
			revLog.put("added", new ArrayList<String>());
		}
		if (mapFiles.containsKey("D")) {
			revLog.put("removed", mapFiles.get("D"));
		} else {
			revLog.put("removed", new ArrayList<String>());
		}
		if (mapFiles.containsKey("M")) {
			revLog.put("modified", mapFiles.get("M"));
		} else {
			revLog.put("modified", new ArrayList<String>());
		}
	
		return revLog;
	}
	
		static public boolean deleteDirectory(File path) {
			if( path.exists() ) {
				File[] files = path.listFiles();
				for(int i=0; i<files.length; i++) {
					if(files[i].isDirectory()) {
						deleteDirectory(files[i]);
					}
					else {
						files[i].delete();
					}
				}
			}
			return( path.delete() );
		}
}
