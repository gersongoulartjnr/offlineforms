package br.unifesp.maritaca.business.test.mock;

import java.util.HashMap;
import java.util.Map;

import br.unifesp.maritaca.persistence.EntityManagerFileSystem;

public class EntityManagerFileSystemMock implements EntityManagerFileSystem {

	private Map<String, byte[]> fileStorage = new HashMap<String, byte[]>();

	@Override
	public void saveFile(byte[] data, String pathfile) {
		getFileStorage().put(pathfile, data);
	}

	@Override
	public byte[] readFile(String pathFile) {
		return getFileStorage().get(pathFile);
	}
	
	@Override
	public void createDirectory(String formUrlPath) {
	}
	
	@Override
	public void deleteFile(String pathFile) {
	}
	
	public Map<String, byte[]> getFileStorage() {
		return fileStorage;
	}

	public void setFileStorage(Map<String, byte[]> fileStorage) {
		this.fileStorage = fileStorage;
	}

}
