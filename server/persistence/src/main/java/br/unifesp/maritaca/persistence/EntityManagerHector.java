package br.unifesp.maritaca.persistence;

import java.util.List;
import java.util.UUID;

public interface EntityManagerHector {
	
	/**
	 * This method persist an Entity object using HOM
	 * 
	 * @param obj Entity to persist
	 * @return 
	 */
	<T> boolean persist(T obj);

	/**
	 * This method find an Entity (row) in Database using HOM
	 * 
	 * @param cl kind of Entity
	 * @param uuid Key
	 * @return Entity
	 */
	<T> T find(Class<T> cl, UUID uuid);

	/**
	 * This method find all columns from entities that will be match 
	 * with the field and value
	 *  
	 * @param cl kind of Entity (Columnfamily)
	 * @param field column name
	 * @param value column value 
	 * @return List of rows matched
	 */
	<T> List<T> cQuery(Class<T> cl, String field, String value);
	
	/**
	 * This method find just the minimal (or not) columns from entities that will 
	 * be match with the field and value
	 * 
	 * @param cl kind of Entity (Columnfamily)
	 * @param field column name
	 * @param value column value 
	 * @param justMinimal false means all columns and true otherwise
	 * @return List of rows matched
	 * @throws IllegalArgumentException
	 */
	@Deprecated
	<T> List<T> cQuery(Class<T> cl, String field, String value, boolean justMinimal)
			throws IllegalArgumentException;
	
	<T> List<T> cQuery(Class<T> cl, String[] field, String[] value, Integer rowCount, Integer colCount, boolean justMinimal)
			throws IllegalArgumentException;
	
	/**
	 * This method lists all columns of the rows from Entity cl
	 * Cassandra is going to return 100 rows by default
	 * @param cl kind of entity
	 * @return list
	 */
	<T> List<T> listAll(Class<T> cl);
	
	/**
	 * This method lists all columns of the rows from Entity cl
	 * 
	 * @param cl kind of entity
	 * @param rowCount number of rows, Ex. 5, 105
	 * @param colCount number of columns, Ex. 5, 105
	 * @return list
	 */
	<T> List<T> listAll(Class<T> cl, Integer rowCount, Integer colCount);
	
	/**
	 * This method lists all columns (or not) of the rows from Entity cl
	 * 
	 * @param cl kind of entity
	 * @param justMinimal false means all columns and true otherwise
	 * @return list
	 */
	<T> List<T> listAll(Class<T> cl, boolean justMinimal);
	
	/**
	 * This method lists all columns (or not) of the rows from Entity cl
	 * 
	 * @param cl kind of entity
	 * @param rowCount number of rows, Ex. 5, 105
	 * @param colCount number of columns, Ex. 5, 105
	 * @param justMinimal false means all columns and true otherwise
	 * @return list
	 */	
	<T> List<T> listAll(Class<T> cl, Integer rowCount, Integer colCount, boolean justMinimal);
	
	<T> List<T> cRangeQuery(Class<T> cl, String field, String value);

	/**
	 * This method delete a row
	 * 
	 * @param cl Entity
	 * @param uuid key
	 * @return
	 */
	public <T> boolean delete(Class<T> cl, UUID uuid);
	
	/**
	 * This method creates an column family
	 * 
	 * @param cl
	 * @return
	 */
	<T> boolean createColumnFamily(Class<T> cl);

	/**
	 * This method verifies if an specific column family exist
	 * 
	 * @param cl
	 * @return
	 */
	<T> boolean existColumnFamily(Class<T> cl);

	/**
	 * This method deletes an specific column family
	 * 
	 * @param cl
	 * @return
	 */
	<T> boolean dropColumnFamily(Class<T> cl);

	<T> boolean rowDataExists(Class<T> cl, UUID uuid);
	
	/**
	 * This method closes the connection from database
	 */
	void close();
	
	<T> List<T> objectsStartingWith(Class<T> cl, String startingStr, String methodName);
}