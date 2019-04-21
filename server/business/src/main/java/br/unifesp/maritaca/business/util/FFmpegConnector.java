package br.unifesp.maritaca.business.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class FFmpegConnector {
	
	private static final Log logger = LogFactory.getLog(FFmpegConnector.class);
	
	public static void execute(List<String> command) {
		try {
			ProcessBuilder builder = new ProcessBuilder(command);
			builder.redirectErrorStream(true);
			Process process = builder.start();
			
			String s = "";
			BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
			while ((s = stdInput.readLine()) != null) {
				logger.info(s);
			}
			process.waitFor();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
}
