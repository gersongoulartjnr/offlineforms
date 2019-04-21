package br.unifesp.maritaca.business.util;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.joda.time.Days;

import br.unifesp.maritaca.business.answer.dto.AnswerDTO;
import br.unifesp.maritaca.business.enums.ComponentType;
import br.unifesp.maritaca.business.enums.OrderAnswerBy;
import br.unifesp.maritaca.business.enums.OrderFormBy;
import br.unifesp.maritaca.business.enums.OrderGroupBy;
import br.unifesp.maritaca.business.enums.OrderType;
import br.unifesp.maritaca.business.exception.MaritacaException;
import br.unifesp.maritaca.business.exception.ObjectConversionException;
import br.unifesp.maritaca.business.form.dto.FormDTO;
import br.unifesp.maritaca.business.group.dto.GroupDTO;
import br.unifesp.maritaca.persistence.util.UtilsPersistence;

/**
 * This class has generic methods that are use in the persistence 
 * business
 * 
 * @author Maritaca team
 */
public class UtilsBusiness {

	private static Log log = LogFactory.getLog(UtilsBusiness.class);
	
	private static final String HADOOP_FILENAME_SEPARATOR = "-";
	
	/**
	 * This method is used to reflex classes. An example should be from 
	 * entity class to DTO class and vice versa.<br>
	 * This is performed by creating a new instance of the desired class
	 * (passed as the second parameter) and searching the original object
	 * (passed as the first parameters) for getters that have a return
	 * type and name that matches those used by the setters in the desired
	 * class.
	 *  
	 * @param origin is the Source Class that contains the data
	 * @param target is the Target Class that will be reflected
	 * @return An object of the desired class with correspondent attributes
	 * 		   initiated to match the values of the given object.
	 */
	public static final <T> T reflectClasses(Object origin, Class<T> target){
		if(origin == null){
			return null;
		}
		Field currentField = null;
		try {
			T           targetObj    = target.getConstructor().newInstance();			
			List<Field> targetFields = Arrays.asList(target.getDeclaredFields());
			for(Field tField : targetFields){
				currentField = tField;

				String getterName = "get"+UtilsPersistence.toUpperFirst(tField.getName());				
				Method getter;
				try{
					getter = origin.getClass().getDeclaredMethod(getterName);
				} catch (NoSuchMethodException e){
					convertClassLogError(tField, target, origin);
					continue;
				}
				
				String setterName = "set"+UtilsPersistence.toUpperFirst(tField.getName());
				Method setter = null;
				Object value  = null;
				try{
					setter = targetObj.getClass().getDeclaredMethod(setterName, getter.getReturnType());
					value  = getter.invoke(origin);
				} catch (NoSuchMethodException e){
				}
				try{
					if(setter==null){
						setter = targetObj.getClass().getDeclaredMethod(setterName, String.class);
						value  = getter.invoke(origin);
						if(value != null){
							value = value.toString();
						}
					}
				} catch (NoSuchMethodException e){
					convertClassLogError(tField, target, origin);
					continue;
				}								
				if(value == null )
					continue;
				setter.invoke(targetObj, value);
			}			
			return targetObj;
		} catch (Exception e) {
			throw new ObjectConversionException(origin.getClass(), target.getClass(), currentField);
		}		
	}	
	
	/**
	 * This methods logs the error in reflectClasses method.
	 * 
	 * @param tField
	 * @param targetClass
	 * @param originalObj
	 */
	private static void convertClassLogError(Field tField, Class<?> targetClass, Object originalObj){
		log.warn("Property: "				+ tField.getName()	+
				 ", from target class: "	+ targetClass.getSimpleName()	+
				 ", not found in object: " 	+ originalObj.getClass().getSimpleName());		
	}


	/**
	 * This method builds the APK's path in file system.
	 * 
	 * @param formUrl
	 * @return
	 */
	public static String buildApkPathFS(String formUrl) {
		if(formUrl == null)
			return null;
		return  ConstantsBusiness.APK_DIRNAME + File.separator + 
				formUrl + ConstantsBusiness.APK_EXTENSION;
	}
	
	/**
	 * This method builds the path filename.
	 * 
	 * @param formUrl 	form url
	 * @param answUrl 	answer url
	 * @param type		type of data (have to be multimedia)
	 * @param qaId		question id
	 * @return	a path filename, example: audio/wqer22134321/sdfew1432-2
	 */
	public static String buildMultimediaPath(String formUrl, String answerUrl, String type, String qaId) {
		checkValues(formUrl, answerUrl, type, qaId);
		if(!isMultimediaType(type)){
			return null;
		}
		String path =  	type	+ File.separator +
						formUrl + File.separator +
						buildMediaFilename(answerUrl, qaId);
		return path;
	}
	
	/**
	 * This method builds the http uri for the file
	 * 
	 * @param formUrl 	form url
	 * @param answUrl 	answer url
	 * @param type		type of data (have to be multimedia)
	 * @param qaId		question id
	 * @return	a http uri filename, example: 
	 * 			http://localhost/hadoop_mounted/user/alvaro/picture/pfC0uHJChn/asdfq2344-1
	 */
	public static String buildCompleteHttpUri(String formUrl, String answerUrl,	String type, String qaId) {
		checkValues(formUrl, answerUrl, type, qaId);
		String path =  	UtilsPersistence.retrieveConfigFile().getHttpUriServer() + File.separator +
				type	+ File.separator +
				formUrl + File.separator +
				buildMediaFilename(answerUrl, qaId);
		return path;
	}
	
	/**
	 * This method builds form path in file system.
	 * 
	 * @param formUrl
	 * @param type
	 * @return a form path in file system
	 */
	public static String buildFormUrlPath(String formUrl, String type) {
		checkValues(formUrl, type);
		String path =  	UtilsPersistence.retrieveConfigFile().getFilesystemPath() + File.separator +
						type	+ File.separator +
						formUrl;
		return path;
	}
	
	/**
	 * This method returns a filesystem_path from ConfigurationFile.
	 * 
	 * @return
	 */
	public static String getFSPath () {
		return 	UtilsPersistence.retrieveConfigFile().getFilesystemPath() + File.separator;
	}
	
	public static String getHttpUri () {
		return 	UtilsPersistence.retrieveConfigFile().getHttpUriServer() + File.separator;
	}
	
	/**
	 * This method builds a filename
	 * 
	 * @param answUrl 	answer url
	 * @param qaId		question id
	 * @return			filename
	 */
	public static String buildMediaFilename(String answUrl, String qaId) {
		checkValues(answUrl, qaId);
		return answUrl + HADOOP_FILENAME_SEPARATOR + qaId;		
	}
	
	/**
	 * This method checks if the type is multimedia type
	 * 
	 * @param type
	 * @return
	 */
	@SuppressWarnings("incomplete-switch")
	public static boolean isMultimediaType(String type) {
		switch (ComponentType.getComponentType(type)) {
			case PICTURE:
				return true;
			case AUDIO:
				return true;
			case VIDEO:
				return true;
		}
		return false;
	}
	
	private static void checkValues(Object ...objects) {
		boolean result = false;
		for (Object object : objects) {
			result = (object == null ? false : true);
		}
		if (!result) {
			throw new RuntimeException("null values");
		} 
	}
	
	@SuppressWarnings("rawtypes")
	public static <T> void sortGrid(final List<T> list, final Enum orderBy, final OrderType orderType) {
		Collections.sort(list, new Comparator<T>() {
			@SuppressWarnings("unchecked")
			public int compare(T obj1, T obj2){
				try {
					String field = "";
					String getter = "";
					Method getMethod = null;
					
					Object result1 = null;
					Object result2 = null;
					
					if(orderBy instanceof OrderFormBy){
						OrderFormBy ob = (OrderFormBy)orderBy;
						field = ob.getDescription();
						getter = "get" + Character.toUpperCase(field.charAt(0)) + field.substring(1);
						getMethod = FormDTO.class.getMethod(getter);
						
						result1 = getMethod.invoke(obj1);
						result2 = getMethod.invoke(obj2);
						
						// to sort by date
						if (ob == OrderFormBy.DATE) {
							DateFormat dateFormat = new SimpleDateFormat("dd/MM/yy hh:mm a");
							result1 = dateFormat.parse((String)result1);							
							result2 = dateFormat.parse((String)result2);
						}
					}
					else if(orderBy instanceof OrderGroupBy){
						OrderGroupBy ob = (OrderGroupBy)orderBy;
						field = ob.getDescription();
						getter = "get" + Character.toUpperCase(field.charAt(0)) + field.substring(1);
						getMethod = GroupDTO.class.getMethod(getter);
						
						result1 = getMethod.invoke(obj1);
						result2 = getMethod.invoke(obj2);
					}
					else if(orderBy instanceof OrderAnswerBy){
						OrderAnswerBy ob = (OrderAnswerBy)orderBy;
						field = ob.getDescription();
						getter = "get" + Character.toUpperCase(field.charAt(0)) + field.substring(1);
						getMethod = AnswerDTO.class.getMethod(getter);
						
						result1 = getMethod.invoke(obj1);
						result2 = getMethod.invoke(obj2);
						
						// to sort by date
						if (ob == OrderAnswerBy.DATE) {
							DateFormat dateFormat = new SimpleDateFormat("dd/MM/yy hh:mm a");
							result1 = dateFormat.parse((String)result1);							
							result2 = dateFormat.parse((String)result2);
						}
					}
					
					// making insensitive (all to lowercase)
					if (result1 instanceof String && result1 instanceof String){
						result1 = ((String)(result1)).toLowerCase(); 
						result2 = ((String)(result2)).toLowerCase();
					}
					
					if (result1 instanceof Comparable && result2 instanceof Comparable){
						Comparable comp1 = (Comparable) result1;
						Comparable comp2 = (Comparable) result2;
						
						if(orderType.equals(OrderType.ASC)) {
							return comp1.compareTo(comp2);
						} else {
							return comp2.compareTo(comp1);
						}
					} else { // if isn't comparable
						if(orderBy.equals(OrderType.ASC)) {
							return result1.equals(result2) ? 0 : 1;
						} else {
							return result1.equals(result2) ? 1 : 0;
						}
					}					
				} catch (Exception e) {
					throw new MaritacaException(e.getMessage());
				}
			}
		});
	}
	
	public static <T> List<T> pagingGrid(List<T> list, int page, int numRows) {
		int range 		= (page * numRows);
		int startPoint	= range - numRows;
		int endPoint	= (range > list.size()) ? (list.size()-1) : (range-1);
				
		List<T> listDTO = new ArrayList<T>();
		for (int i = startPoint; i <= endPoint; i++) {
			listDTO.add(list.get(i));
		}
		return listDTO;
	}
	
	public static DateTime getDateFromString(String strDate){
		try{
			if(!"".equals(strDate)){
				return new DateTime(strDate);
			}
			else return null;
		}
		catch(Exception ex){
			return null;
		}
	}
	
	public static boolean dateIsLessThanToday(DateTime date){
		boolean isValid = false;
		DateTime today = new DateTime();
		Days days = Days.daysBetween(date, today);
		if(days.getDays() >= 0){
			isValid = true;
		}
		return isValid;
	}
}