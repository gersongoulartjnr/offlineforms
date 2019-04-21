package br.unifesp.maritaca.business.init;

public interface MaritacaInit {

	/**
	 * This method creates (if they don't exist) column families in database.
	 */
	void createAllEntities();

	/**
	 * This method creates (if they don't exist) the directories in file system.
	 */
	void createDirectories();
	
	/**
	 * This method closes connection with database.
	 */
	void close();
}