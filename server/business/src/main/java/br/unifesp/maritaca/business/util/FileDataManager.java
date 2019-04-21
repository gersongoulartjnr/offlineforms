package br.unifesp.maritaca.business.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

public class FileDataManager {
	private static Logger logger = Logger.getLogger(FileDataManager.class);
	
	/**
	 * This method saves a file.
	 * 
	 * @param data encoded in Base64 
	 * @param pathFilename path and filename. Ex: /tmp/filename.xxx
	 */
	public static void saveInLocalFromBase64(String data, String pathFilename) {
		try {
			File file = new File(pathFilename);
			if (file.exists()) {
				file.delete();
			}
			logger.info("saving file: path="+pathFilename);
			byte[] buffer = Base64.decodeBase64(data.getBytes());			
			BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
			bos.write(buffer);
			bos.close();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public static void saveInLocalFromByteArray(byte[] byteArray, String pathFilename){
		try{
			File file = new File(pathFilename);
			if (file.exists()) {
				file.delete();
			}
			logger.info("saving file: path="+pathFilename);
			FileOutputStream fop = new FileOutputStream(file);
	        fop.write(byteArray);
	        fop.flush();
	        fop.close();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * This method reads a file.
	 * 
	 * @param pathFilename path and filename. Ex: /tmp/filename.xxx
	 * @return an string data encoded in Base64.
	 */
	public static String read(String pathFilename) {
		try {
			logger.info("reading file: path="+pathFilename);
			File file = new File(pathFilename);
			if (!file.exists()) {
				logger.info("READ file FAILED: path="+pathFilename + "does not exist.");
			}
			byte buffer[] = new byte[(int) file.length()];
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
			bis.read(buffer, 0, buffer.length);
			
			return new String(Base64.encodeBase64(buffer), "UTF-8");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * This method deletes a file.
	 * 
	 * @param pathFilename path and filename. Ex: /tmp/filename.xxx
	 */
	public static void delete(String pathFilename) {
		try {
			logger.info("deleting file: path="+pathFilename);
			File file = new File(pathFilename);
			if (file.isDirectory()) {
				FileUtils.deleteDirectory(file);
			} else {
				file.delete();		
			}		
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}	
}
