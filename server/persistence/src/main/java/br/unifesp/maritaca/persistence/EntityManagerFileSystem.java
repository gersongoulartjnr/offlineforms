package br.unifesp.maritaca.persistence;

public interface EntityManagerFileSystem {

	void createDirectory(String formUrlPath);
	
	byte[] readFile(String pathFile);
	
	void saveFile(byte[] data, String pathfile);
	
	void deleteFile(String pathFile);
	
}
