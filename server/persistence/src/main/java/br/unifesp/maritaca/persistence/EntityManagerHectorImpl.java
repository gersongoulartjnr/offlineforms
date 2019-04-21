package br.unifesp.maritaca.persistence;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.persistence.Entity;
import javax.persistence.Id;

import me.prettyprint.cassandra.model.IndexedSlicesQuery;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.serializers.UUIDSerializer;
import me.prettyprint.cassandra.service.ThriftColumnDef;
import me.prettyprint.cassandra.utils.TimeUUIDUtils;
import me.prettyprint.hector.api.Cluster;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.Serializer;
import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.beans.OrderedRows;
import me.prettyprint.hector.api.beans.Row;
import me.prettyprint.hector.api.ddl.ColumnDefinition;
import me.prettyprint.hector.api.ddl.ColumnFamilyDefinition;
import me.prettyprint.hector.api.ddl.ComparatorType;
import me.prettyprint.hector.api.ddl.KeyspaceDefinition;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.mutation.Mutator;
import me.prettyprint.hector.api.query.QueryResult;
import me.prettyprint.hector.api.query.RangeSlicesQuery;
import me.prettyprint.hom.EntityManagerImpl;
import me.prettyprint.hom.annotations.Column;

import org.apache.cassandra.db.marshal.UUIDType;
import org.apache.cassandra.thrift.ColumnDef;
import org.apache.cassandra.thrift.IndexType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import br.unifesp.maritaca.persistence.annotation.Minimal;
import br.unifesp.maritaca.persistence.converter.JSONAnswerTimestampConverter;
import br.unifesp.maritaca.persistence.converter.JSONUUIDConverter;
import br.unifesp.maritaca.persistence.converter.ListStringConverter;
import br.unifesp.maritaca.persistence.converter.PolicyConverter;
import br.unifesp.maritaca.persistence.converter.RequestStatusConverter;
import br.unifesp.maritaca.persistence.entity.AnswerTimestamp;
import br.unifesp.maritaca.persistence.entity.OAuthCode;
import br.unifesp.maritaca.persistence.entity.OAuthToken;
import br.unifesp.maritaca.persistence.permission.Policy;
import br.unifesp.maritaca.persistence.permission.RequestStatusType;
import br.unifesp.maritaca.persistence.util.UtilsPersistence;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class EntityManagerHectorImpl implements EntityManagerHector, Serializable {

	private static final long serialVersionUID = 1L;
	private static final Log log = LogFactory.getLog(EntityManagerHectorImpl.class);
	
	private Cluster cluster;
	private Keyspace keyspace;
	private EntityManagerImpl hom;

	private final StringSerializer stringSerializer = StringSerializer.get();
	private final UUIDSerializer uuidSerializer = UUIDSerializer.get();
	
	@PostConstruct
	public void init(){
		String maritacaCluster;
		maritacaCluster = UtilsPersistence.retrieveConfigFile().getClusterAddr();
		if(maritacaCluster!=null){
		   cluster  = HFactory.getOrCreateCluster("cassandra", maritacaCluster);
		   keyspace = HFactory.createKeyspace("Maritaca", cluster);
		   hom      = new EntityManagerImpl(keyspace,"br.unifesp.maritaca.persistence.entity");
		}
	}
	
	/**
	 * Go to interface
	 * {@link br.unifesp.maritaca.persistence.EntityManagerHector}
	 */
	@Override
	public <T> boolean persist(T obj) {
		if (getObjectKey(obj) == null) {
			setObjectKey(obj, TimeUUIDUtils.getUniqueTimeUUIDinMillis());
		}
		//TODO
		if (existColumnFamily(obj.getClass())) {
			if(!isColumnWithTTL(obj.getClass())) {
				hom.persist(obj);
			} else {
				maritacaPersist(obj);
			}
			return true;
		}
		return false;
	}

	/**
	 * Go to interface
	 * {@link br.unifesp.maritaca.persistence.EntityManagerHector}
	 */
	@Override
	public <T> T find(Class<T> cl, UUID uuid) {
		return hom.find(cl, uuid);
	}
	
	/**
	 * Go to interface
	 * {@link br.unifesp.maritaca.persistence.EntityManagerHector}
	 */
	@Override
	public <T> List<T> cQuery(Class<T> cl, String field, String value) {
		return cQuery(cl, field, value, false);
	}

	/**
	 * Go to interface
	 * {@link br.unifesp.maritaca.persistence.EntityManagerHector}
	 */
	@Override
	public <T> List<T> cQuery(Class<T> cl, String[] field, String[] value, Integer rowCount, Integer colCount, boolean justMinimal) {
		if (cl == null || isEntity(cl.getClass()) || (field == null || value == null) || field.length != value.length) {
			throw new IllegalArgumentException("object null or not an entity or ");
		}

		List<T> result = new ArrayList<T>();

		IndexedSlicesQuery<UUID, String, String> indexedSlicesQuery = 
				HFactory.createIndexedSlicesQuery(keyspace, uuidSerializer, stringSerializer, stringSerializer);
		for (int i = 0, length = field.length; i < length; i++) {
			indexedSlicesQuery.addEqualsExpression(field[i], value[i]);
		}
		indexedSlicesQuery.setColumnNames(getNameFields(cl, justMinimal));
		indexedSlicesQuery.setColumnFamily(cl.getSimpleName());
		
		if(colCount != null) {
			indexedSlicesQuery.setRange("", "", false, colCount);
		}
		if(rowCount != null){
			indexedSlicesQuery.setRowCount(rowCount);
		}

		QueryResult<OrderedRows<UUID, String, String>> resultq = indexedSlicesQuery.execute();

		for (Row<UUID, String, String> line : resultq.get().getList()) {
			addObjectFromRowLine(cl, justMinimal, line, result);
		}
		return result;
	}
	
	/**
	 * Go to interface
	 * {@link br.unifesp.maritaca.persistence.EntityManagerHector}
	 */
	@Override
	public <T> List<T> cQuery(Class<T> cl, String field, String value, boolean justMinimal) {
		return this.cQuery(cl, new String[]{field}, new String[]{value}, null, null, justMinimal);
	}
	
	/**
	 * Go to interface
	 * {@link br.unifesp.maritaca.persistence.EntityManagerHector}
	 */	
	@Override
	public <T> List<T> listAll(Class<T> cl) {
		return listAll(cl, false);
	}
	
	@Override
	public <T> List<T> listAll(Class<T> cl, Integer numRows, Integer colCount) {
		return listAll(cl, numRows, colCount, false);
	}
	
	@Override
	public <T> List<T> listAll(Class<T> cl, Integer rowCount, Integer colCount, boolean justMinimal) {
		if (cl == null || isEntity(cl.getClass())) {
			throw new IllegalArgumentException("object null or not an entity");
		}

		boolean keysOnly = false;

		List<T> result = new ArrayList<T>();

		Collection<String> fields = getNameFields(cl, justMinimal);
		RangeSlicesQuery<UUID, String, String> q = HFactory.createRangeSlicesQuery(
				keyspace, uuidSerializer, stringSerializer, stringSerializer);
		q.setColumnFamily(cl.getSimpleName());
		
		if(colCount != null){
			q.setRange("", "", false, colCount);
		}		
		if(rowCount != null){
			q.setRowCount(rowCount);
		}
		
		if (fields.size() > 0) {
			q.setColumnNames(fields.toArray(new String[fields.size()]));
		} else {
			q.setReturnKeysOnly();
			keysOnly = true;
		}

		QueryResult<OrderedRows<UUID, String, String>> resultq = q.execute();

		for (Row<UUID, String, String> line : resultq.get().getList()) {
			addObjectFromRowLine(cl, keysOnly, line, result);
		}
		return result;
	}

	/**
	 * Go to interface
	 * {@link br.unifesp.maritaca.persistence.EntityManagerHector}
	 */
	@Override
	public <T> List<T> listAll(Class<T> cl, boolean justMinimal) {
		if (cl == null || isEntity(cl.getClass())) {
			throw new IllegalArgumentException("object null or not an entity");
		}

		boolean keysOnly = false;

		List<T> result = new ArrayList<T>();

		Collection<String> fields = getNameFields(cl, justMinimal);
		RangeSlicesQuery<UUID, String, String> q = HFactory.createRangeSlicesQuery(
				keyspace, uuidSerializer, stringSerializer, stringSerializer);
		q.setColumnFamily(cl.getSimpleName());
		if (fields.size() > 0) {
			q.setColumnNames(fields.toArray(new String[fields.size()]));
		} else {
			q.setReturnKeysOnly();
			keysOnly = true;
		}

		QueryResult<OrderedRows<UUID, String, String>> resultq = q.execute();

		for (Row<UUID, String, String> line : resultq.get().getList()) {
			addObjectFromRowLine(cl, keysOnly, line, result);
		}
		return result;
	}
	
	@Override
	public <T> List<T> cRangeQuery(Class<T> cl, String field, String value) {
		RangeSlicesQuery<String, String, String> rangeSlicesQuery =
				HFactory.createRangeSlicesQuery(keyspace, stringSerializer, stringSerializer, stringSerializer);
		rangeSlicesQuery.setColumnFamily(cl.getSimpleName());
		rangeSlicesQuery.setRange(value, "", false, 3);//3?

		QueryResult<OrderedRows<String, String, String>> resultq;
		resultq = rangeSlicesQuery.execute();

		List<T> result = new ArrayList<T>();

		for (Row<String, String, String> line : resultq.get().getList()) {
			T obj = instantiateObject(cl);
			for (HColumn<String, String> column : line.getColumnSlice()
					.getColumns()) {
				try {
					setValue(obj, cl.getDeclaredField(column.getName()),
							column.getValue());
				} catch (SecurityException e) {
					throw new RuntimeException(e);
				} catch (NoSuchFieldException e) {
					throw new IllegalArgumentException(e);
				}
			}

			log.debug(line);
		}
		return result;
	}

	/**
	 * Go to interface
	 * {@link br.unifesp.maritaca.persistence.EntityManagerHector}
	 */
	@Override
	public <T> boolean delete(Class<T> cl, UUID uuid) {
		if (cl == null || !isEntity(cl)) {
			throw new IllegalArgumentException("object null or not an entity");
		}
		Mutator<UUID> mutator = HFactory.createMutator(keyspace, uuidSerializer);
		mutator.addDeletion(uuid, cl.getSimpleName());
		mutator.execute();
		return true;
	}
	
	/**
	 * Go to interface
	 * {@link br.unifesp.maritaca.persistence.EntityManagerHector}
	 */
	@Override
	public <T> boolean createColumnFamily(Class<T> cl) {
		if (cl == null || isEntity(cl.getClass())) {
			throw new IllegalArgumentException("object null or not an entity");
		}
		if (!existColumnFamily(cl)) {
			List<ColumnDef> columns = new ArrayList<ColumnDef>();
			
			for (Field f : getColumnFields(cl, false)) {
				String cName = f.getName();
				if (f.getAnnotation(Minimal.class) != null && f.getAnnotation(Minimal.class).indexed()){
					columns.add(newIndexedColumnDef(cName, 
													cl.getSimpleName() + cName, 
													ComparatorType.UTF8TYPE.getTypeName()));
				}
			}

			List<ColumnDefinition> columnMetadata = ThriftColumnDef.fromThriftList(columns);

			ColumnFamilyDefinition cfdef = HFactory.createColumnFamilyDefinition(keyspace.getKeyspaceName(), 
																		cl.getSimpleName(),
																		ComparatorType.UTF8TYPE, 
																		columnMetadata);
			
			cfdef.setKeyValidationClass(UUIDType.class.getSimpleName());

			cluster.addColumnFamily(cfdef);
			return true;
		}
		return false;
	}

	/**
	 * Go to interface
	 * {@link br.unifesp.maritaca.persistence.EntityManagerHector}
	 */
	@Override
	public <T> boolean existColumnFamily(Class<T> cl) {
		if (cl == null || isEntity(cl.getClass())) {
			throw new IllegalArgumentException("object null or not an entity");
		}
		KeyspaceDefinition ksdef = cluster.describeKeyspace(keyspace.getKeyspaceName());
		if (ksdef == null) {
			return false;
		}
		for (ColumnFamilyDefinition cfDef : ksdef.getCfDefs()) {
			if (cfDef.getName().equals(cl.getSimpleName())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Go to interface
	 * {@link br.unifesp.maritaca.persistence.EntityManagerHector}
	 */
	@Override
	public <T> boolean dropColumnFamily(Class<T> cl) {
		if (cl == null || isEntity(cl.getClass())) {
			throw new IllegalArgumentException("object null or not an entity");
		}

		if (existColumnFamily(cl)) {
			cluster.dropColumnFamily(keyspace.getKeyspaceName(),
					cl.getSimpleName());
			return true;
		}
		return false;
	}

	@Deprecated
	@Override
	public <T> boolean rowDataExists(Class<T> cl, UUID uuid) {
		T t = find(cl, uuid);
		return t != null;
	}

	/**
	 * Go to interface
	 * {@link br.unifesp.maritaca.persistence.EntityManagerHector}
	 */
	@Override
	public void close() {
		try {
			cluster.getConnectionManager().shutdown();
		} catch (Exception e) {
			log.error("not possible to close the connection with cassandra", e);
		}

	}
	
	// TODO This function will be deleted when HOM has support to TimeToLive
	@SuppressWarnings("rawtypes")
	private <T> boolean isColumnWithTTL(Class<T> clazz) {
		List<Class> clazzes = new ArrayList<Class>();
		clazzes.add(OAuthCode.class);
		clazzes.add(OAuthToken.class);
		if (clazzes.contains(clazz)) {
			return true;
		}
		return false;
	}
	
	// TODO This function will be deleted when HOM has support to TimeToLive
	@SuppressWarnings("rawtypes")
	private <T> boolean maritacaPersist(T obj){
		Mutator<UUID> mutator = HFactory.createMutator(keyspace, uuidSerializer);
		UUID key = getObjectKey(obj);
		
		for (Field f : getColumnFields(obj.getClass(), false)) {
			Method method = getMethod(obj, "get" + UtilsPersistence.toUpperFirst(f.getName()));
			Object result;
			try {
				result = method.invoke(obj);
			} catch (IllegalAccessException e1) {
				throw new RuntimeException("Exception while executing method "
						+ method.getName(), e1);
			} catch (InvocationTargetException e1) {
				throw new RuntimeException("Exception while executing method "
						+ method.getName(), e1);
			}

			if (result != null) {
				if(f.isAnnotationPresent(Column.class)){
					HColumn column = getHColumn(f.getName(), result);
					saveColumn(obj, mutator, key, f, column);					
				}
			}
		}

		try {
			mutator.execute();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		return true;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private HColumn getHColumn(String columnname, Object obj) {
		Serializer serializer = stringSerializer;
		obj = obj.toString();
		HColumn column = HFactory.createColumn(columnname, obj, stringSerializer,
				serializer);
		return column;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void saveColumn(Object obj, Mutator<UUID> mutator, UUID key, Field f,
			HColumn column) {
		if(f.getAnnotation(Minimal.class).ttl()){
			String getMethodName = "get" + UtilsPersistence.toUpperFirst(f.getName()) + "TTL";
			Method method = getMethod(obj, getMethodName);
			Object result;
			try {
				result = method.invoke(obj);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			String value = result.toString();
			column.setTtl(Integer.parseInt(value));
		}
		mutator.addInsertion(key, obj.getClass().getSimpleName(), column);
	}
	
	/**
	 * This method indexes a specific indexName in columnName
	 * 
	 * @param columnName
	 * @param indexName
	 * @param comparator
	 * @return
	 */
	private ColumnDef newIndexedColumnDef(String columnName, String indexName, String comparator) {
		ColumnDef columnDef = new ColumnDef(stringSerializer.toByteBuffer(columnName), comparator);
		columnDef.setIndex_name(indexName);
		columnDef.setIndex_type(IndexType.KEYS);
		return columnDef;
	}

	private <T> void addObjectFromRowLine(Class<T> cl, boolean keysOnly,
			Row<UUID, String, String> line, List<T> result) {
		T obj = instantiateObject(cl);
		setObjectKey(obj, line.getKey());
		boolean ghost = true;
		for (HColumn<String, String> column : line.getColumnSlice()
				.getColumns()) {
			try {
				if( !keysOnly ||
					(keysOnly && cl.getDeclaredField(column.getName()).isAnnotationPresent(Minimal.class))
				){
					setValue(obj, cl.getDeclaredField(column.getName()),
							column.getValue());					
				}
				ghost = false;
			} catch (SecurityException e) {
				throw new RuntimeException(e);
			} catch (NoSuchFieldException e) {
				throw new IllegalArgumentException(e);
			}
		}
		if (!ghost || keysOnly) {
			result.add(obj);
		}
	}

	private <T> T instantiateObject(Class<T> cl) {
		T obj = null;
		try {
			obj = (T) cl.newInstance();
		} catch (InstantiationException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException("Exception while instantiating "
					+ cl.getSimpleName(), e);
		}
		return obj;
	}

	private <T> void setObjectKey(T obj, UUID uuid) {
		Method method = getMethod(obj, "setKey", UUID.class);
		try {
			method.invoke(obj, uuid);
		} catch (IllegalArgumentException e) {
			throw new RuntimeException("Exception while setting key", e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException("Exception while setting key", e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException("Exception while setting key", e);
		}

	}
	
	private Collection<String> getNameFields(Class<?> cl, boolean justMinimal) {
		ArrayList<String> colFields = new ArrayList<String>();
		for (Field f : getColumnFields(cl, justMinimal) ) {
					colFields.add(f.getName());
		}
		return colFields;
	}

	@SuppressWarnings("rawtypes")
	private <T> Method getMethod(T obj, String methodName, Class... parameter) {
		try {
			return obj.getClass().getDeclaredMethod(methodName, parameter);
		} catch (SecurityException e) {
			throw new RuntimeException(e);
		} catch (NoSuchMethodException e) {
			throw new IllegalArgumentException("no method : " + methodName
					+ " in Class: " + obj.getClass().getSimpleName(), e);
		}
	}

	private <T> void setValue(T result, Field f, String value) {
		Method method = getMethod(result, "set" + UtilsPersistence.toUpperFirst(f.getName()),
				f.getType());
		try {
			String columnConverter		= f.getAnnotation(Column.class).converter().toString();
			String jsonUUIDConverter	= JSONUUIDConverter.class.toString();
			String listStringConverter	= ListStringConverter.class.toString();
			String jsonAnswerTimestampConverter = JSONAnswerTimestampConverter.class.toString();
			String policyConverter		= PolicyConverter.class.toString();
			String requestStatusConverter  = RequestStatusConverter.class.toString();
			if(jsonAnswerTimestampConverter.equals(columnConverter)){
				Gson gson = new Gson();
				Type type = new TypeToken<List<AnswerTimestamp>>(){}.getType();
				List<AnswerTimestamp> listJson = gson.fromJson(value, type);
				method.invoke(result, listJson);
			} else if(jsonUUIDConverter.equals(columnConverter)){
				Gson gson = new Gson();
				Type type = new TypeToken<List<UUID>>(){}.getType();
				List<UUID> listJson = gson.fromJson(value, type);
				method.invoke(result, listJson);
			} else if(listStringConverter.equals(columnConverter)){
				Gson gson = new Gson();
				Type type = new TypeToken<List<String>>(){}.getType();
				List<String> listJson = gson.fromJson(value, type);
				method.invoke(result, listJson);
			} else if(policyConverter.equals(columnConverter)) {
				Policy policy = Policy.getInstance(new String(value));
				method.invoke(result, policy);
			} else if(requestStatusConverter.equals(columnConverter)) {
				RequestStatusType requestStatus = RequestStatusType.getInstance(new String(value));
				method.invoke(result, requestStatus);
			} else if (f.getType() == String.class)
				method.invoke(result, value);

			else if (f.getType() == Long.class)
				method.invoke(result, Long.parseLong(value));

			else if (f.getType() == Integer.class)
				method.invoke(result, Integer.parseInt(value));

			else if (f.getType() == Double.class)
				method.invoke(result, Double.parseDouble(value));

			else if (f.getType() == Float.class)
				method.invoke(result, Float.parseFloat(value));

			else if (f.getType() == BigDecimal.class)
				method.invoke(result, new BigDecimal(value));

			else if (f.getType() == Boolean.class)
				method.invoke(result, new Boolean(value));
			else if (f.getType() == UUID.class)
				method.invoke(result, UUID.fromString(value));
			else if (f.getType() == Date.class) {
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy hh:mm a");
				Date date;
				try {
					date = sdf.parse(value);
					method.invoke(result, date);
				} catch (ParseException e) {
					log.error("date not set", e);
				}
			} else {
				try {
					// alternative method with String
					method = getMethod(result, "set"
							+ UtilsPersistence.toUpperFirst(f.getName()), String.class);
					method.invoke(result, value);
				} catch (Exception e) {
					log.error("Losing data, method set" + f.getName()
							+ "not found in class "
							+ result.getClass().getSimpleName(), e);
				}
			}

		} catch (IllegalArgumentException e1) {
			throw new RuntimeException(e1);
		} catch (IllegalAccessException e1) {
			throw new RuntimeException(e1);
		} catch (InvocationTargetException e1) {
			throw new RuntimeException(e1);
		}

	}
	
	/**
	 * This method returns the <i>key</i> of Entity obj
	 * 
	 * @param obj Entity
	 * @return key
	 */
	private <T> UUID getObjectKey(T obj) {
		UUID key = null;
		Method method = getMethod(obj, "getKey");
		try {
			key = (UUID) method.invoke(obj);
		} catch (IllegalArgumentException e) {
			throw new RuntimeException("Exception while getting key", e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException("Exception while getting key", e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException("Exception while getting key", e);
		}
		return key;
	}

	/**
	 * This method obtains the columns of an specific column family (Entity)
	 * 
	 * @param cl represents the entity
	 * @param justMinimal
	 * @return
	 */
	private <T> ArrayList<Field> getColumnFields(Class<T> cl, boolean justMinimal) {
		ArrayList<Field> colFields = new ArrayList<Field>();
		for (Field f : cl.getDeclaredFields()) {
			if (!f.isAnnotationPresent(Id.class) &&  f.isAnnotationPresent(Column.class)) {
				if (!justMinimal || f.isAnnotationPresent(Minimal.class)) {
					colFields.add(f);
				}
			}
		}
		return colFields;
	}

	/**
	 * This method verifies if <i>cl</i> is an Entity
	 * 
	 * @param cl Entity
	 * @return
	 */
	private <T> boolean isEntity(Class<T> cl) {
		return cl.isAnnotationPresent(Entity.class);
	}
	
	@Override
	public <T> List<T> objectsStartingWith(Class<T> clazz, String startingStr, String methodName) {
		ArrayList<T> result = new ArrayList<T>(0);
		try {
			// TODO: improve this. Retrieving all elements in a column family 
			// is expensive with big collections.
			Method method = clazz.getMethod(methodName);
			List<T> resultEM = listAll(clazz, false);
			for (T obj : resultEM) {

				String value = (String) method.invoke(obj);
				if (value != null && value.matches("^" + startingStr + ".*")) {
					result.add(obj);
				}
			}
		} catch (Exception e) {
			log.error("Exception executing the method " + methodName
					+ " in the class " + clazz.getSimpleName());
		}
		return result;
	}
	
	// setters method
	public void setCluster(Cluster cluster) {
		this.cluster = cluster;
	}
	
	public void setKeyspace(Keyspace keyspace) {
		this.keyspace = keyspace;
	}
	
	public void setHectorObjectMapper(EntityManagerImpl hectorObjectMapper) {
		this.hom = hectorObjectMapper;
	}
}