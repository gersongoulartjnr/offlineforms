package br.unifesp.maritaca.persistence;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class EntityManagerFileSystemImpl implements EntityManagerFileSystem {

	private static Logger logger = Logger.getLogger(EntityManagerFileSystemImpl.class);

	@Override
	public void saveFile(byte[] data, String pathFile) {
		try {
			logger.info("FileSystem saving " + pathFile);
			File file = new File(pathFile);
			BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
			bos.write(data);
			bos.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public byte[] readFile(String pathFile) {
		try {
			logger.info("FileSystem reading " + pathFile);
			File file = new File(pathFile);
			byte buffer[] = new byte[(int) file.length()];
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
			bis.read(buffer, 0, buffer.length);
			bis.close();
			return buffer;
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	@Override
	public void deleteFile(String pathFile) {
		boolean success = (new File(pathFile).delete());
		if (!success) {
			throw new RuntimeException("Failed deleteFile() in " + pathFile);
		}
	}
	
	@Override
	public void createDirectory(String path) {
		File file = new File(path);
		if (!file.exists()) {
			file.mkdir();
		}
	}
}