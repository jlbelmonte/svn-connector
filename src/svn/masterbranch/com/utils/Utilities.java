package svn.masterbranch.com.utils;

import org.apache.log4j.Logger;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

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
				bos.write(b, 0, p);
				sb.append(new String(b, 0 , p));
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
			ioe.getMessage();
		}
		String content = sb.toString();
		return content;
	}
}
