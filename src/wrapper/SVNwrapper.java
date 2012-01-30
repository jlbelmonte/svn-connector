package wrapper;

import exceptions.SVNException;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.Executor;
import org.apache.commons.exec.PumpStreamHandler;
import org.apache.log4j.Logger;
import siena.Json;
import utils.SNVLogParser;
import utils.Utilities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;

public class SVNWrapper {
	private static Logger logger = Logger.getLogger(SVNWrapper.class);
	private String command = "/usr/bin/svn";
	private String uri;
	private Long revFrom = null;
	private String action = "log";
	private String verboseParam = "-v";
	
	public SVNWrapper (String repoUri, String path, Long revision){
		this.uri = repoUri;
		this.command = path;
		this.revFrom = revision;
	}

	private Json callSVN() throws SVNException{
		logger.debug("building the command to execute");
		CommandLine cl = new CommandLine(command);
		cl.addArgument(action);
		cl.addArgument(verboseParam);
		cl.addArgument(uri);
		if (revFrom != null && revFrom > 0){
			cl.addArgument("-r");
			cl.addArgument(""+revFrom);
		}
		logger.debug("SVN connector msg: Attempting to log : "+this.uri);

		Json result = Json.map();

		File file = null;
		FileOutputStream fOS = null;
		FileReader fr = null;
		BufferedReader br = null;
		PipedOutputStream pipeOut = null;
		PipedInputStream pipeIn = null;

		try{
			file = File.createTempFile("SVN-", ".log");
			fOS = new FileOutputStream(file);
			PumpStreamHandler streamHandler = new PumpStreamHandler();
			streamHandler = new PumpStreamHandler(fOS);
			Executor executor = new DefaultExecutor();
			executor.setStreamHandler(streamHandler);
			logger.debug(cl.toString());

			// redirecting System.err and prepare a pipeIn to read it
			pipeOut = new PipedOutputStream();
			pipeIn = new PipedInputStream(pipeOut);
			System.setErr (new PrintStream(pipeOut));

			/* 	DefaultExecutor only accepts one value as success value
				by default, we need to accept more and discriminate the
				result afterwards. So Don't Panic
			*/
			int [] dontPanicValues = new int[256];
			for (int i = 0; i <= 255; i++) {
				dontPanicValues[i]=i;
			}
			executor.setExitValues(dontPanicValues);
			//execute command
			int statusCode = executor.execute(cl);
			logger.debug("SVN Connector msg: Executed over "+this.uri+ " exitStatus "+statusCode);

			fr = new FileReader(file);

			br = new BufferedReader(fr);

			String stdErr = Utilities.piped2String(pipeIn);

			if (statusCode != 0){
				if (stdErr == null || stdErr.isEmpty()){
					stdErr = "";
					String s;
					while( ( s = br.readLine()) != null){
						stdErr += s;
					}
				}
				result.put("status", "NOK")
						.put("error", "SVNError " + stdErr);


			} else {

				result = SNVLogParser.parseData(br);
			}
			logger.debug("SVNWrapper msg: result "+ result);

		}
		catch (IOException e) {
			logger.error("Git Exception: "+ uri, e);
		} finally {
			try {fOS.close();} catch (Exception e) {}
			try {fr.close();} catch (Exception e) {}
			try {br.close();} catch (Exception e) {}
			try {pipeOut.close();} catch (Exception e) {}
			try {pipeIn.close();} catch (Exception e) {}
			logger.debug("File size " + file.length());
			file.delete();

		}
		return result;
	}

	public static void main(String[] args) {
		SVNWrapper wrapper = new SVNWrapper("http://plugins.svn.wordpress.org/wp-stripe", "/usr/bin/svn", null);
		try {
			System.out.println(wrapper.callSVN());
		} catch (Exception e){
			e.printStackTrace();
		}
	}
}
