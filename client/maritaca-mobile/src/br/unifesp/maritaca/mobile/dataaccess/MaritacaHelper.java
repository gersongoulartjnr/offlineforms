package br.unifesp.maritaca.mobile.dataaccess;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import br.unifesp.maritaca.mobile.dataaccess.model.MaritacaAnswer;
import br.unifesp.maritaca.mobile.dataaccess.model.MaritacaGroup;
import br.unifesp.maritaca.mobile.dataaccess.model.MaritacaLog;
import br.unifesp.maritaca.mobile.dataaccess.model.MaritacaSetting;
import br.unifesp.maritaca.mobile.dataaccess.model.MaritacaUser;
import br.unifesp.maritaca.mobile.model.Question;
import br.unifesp.maritaca.mobile.model.components.BarCodeInformation;
import br.unifesp.maritaca.mobile.model.components.ComponentType;
import br.unifesp.maritaca.mobile.model.components.form.MoneyQuestion;
import br.unifesp.maritaca.mobile.util.Constants;

import java.io.FileWriter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

/**
 * 
 * @author Jimmy Valverde S&aacute;nchez
 * @version 0.1.6
 *
 */

public class MaritacaHelper extends SQLiteOpenHelper {
	
	private SQLiteDatabase dbReadable;
	private SQLiteDatabase dbWritable;
	private Cursor cursor;
	private Context context;

	public MaritacaHelper(Context context) {
		super(context, Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION);
		dbReadable = this.getReadableDatabase();
		dbWritable = this.getWritableDatabase();
		context = context;

	}

	@Override
	public void onCreate(SQLiteDatabase db) {	
		//Create users table

		db.execSQL("CREATE TABLE " + Constants.URL_FORM + " (" +
				 Constants.URL_FORM_URL + " URL TEXT);");

        db.execSQL("CREATE TABLE " + Constants.NAME_FORM + " (" +
                Constants.NAME_FORM_NAME + " NAME TEXT);");

		db.execSQL("CREATE TABLE " + Constants.USER_TABLE_NAME + " (" +
				Constants.USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + 
				Constants.USER_EMAIL + " TEXT NOT NULL, " +
				Constants.USER_ACCESSTOKEN + " TEXT NOT NULL, " +
				Constants.USER_REFRESHTOKEN + " TEXT NOT NULL, " +
				Constants.USER_INIT_DATE + " INTEGER NOT NULL, " +
				Constants.USER_EXP_DATE + " INTEGER NOT NULL, " +
				Constants.USER_FORM_ID + " TEXT" +
			");");
		//Create answers_group table
		db.execSQL("CREATE TABLE " + Constants.GROUP_TABLE_NAME + " (" +
				Constants.GROUP_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
				Constants.GROUP_TIMESTAMP + " TIMESTAMP NOT NULL, " +
				Constants.GROUP_VALID + " INTEGER DEFAULT 0 NOT NULL, " +
				Constants.USER_ID + " INTEGER NOT NULL, " +
				"FOREIGN KEY("+Constants.USER_ID+") REFERENCES "+Constants.USER_TABLE_NAME+"("+Constants.USER_ID+") ON DELETE CASCADE ON UPDATE CASCADE" +
			");");
		//Create answers table
		db.execSQL("CREATE TABLE " + Constants.ANSWER_TABLE_NAME + " (" +
				Constants.ANSWER_ID + " INTEGER NOT NULL, " +
				//Constants.ANSWER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + 
				Constants.ANSWER_TYPE + " TEXT NOT NULL, " +
				Constants.ANSWER_SUBTYPE + " TEXT, " +
				Constants.ANSWER_VALUE + " TEXT, " +
				Constants.GROUP_ID + " INTEGER NOT NULL, " +
				"FOREIGN KEY(" + Constants.GROUP_ID + ") REFERENCES " + Constants.GROUP_TABLE_NAME + "(" + Constants.GROUP_ID + ") ON DELETE CASCADE ON UPDATE CASCADE" +
				");");
		//Create errors_log table
		db.execSQL("CREATE TABLE " + Constants.LOG_TABLE_NAME + " (" +
                Constants.LOG_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Constants.LOG_MESSAGE + " TEXT NOT NULL, " +
                Constants.LOG_DATE + " TIMESTAMP NOT NULL, " +
                Constants.LOG_ANDROIDVERSION + " TEXT NOT NULL, " +
                Constants.USER_ID + " INTEGER NOT NULL, " +
                "FOREIGN KEY(" + Constants.USER_ID + ") REFERENCES " + Constants.USER_TABLE_NAME + "(" + Constants.USER_ID + ") ON DELETE CASCADE ON UPDATE CASCADE" +
                ");");
		//Create settings table
		db.execSQL("CREATE TABLE " + Constants.SETTINGS_TABLE_NAME + " (" +
				Constants.SETTINGS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
				Constants.SETTINGS_SYNC_ACTIVE + " INTEGER NOT NULL, " +
				Constants.SETTINGS_SYNC_INTERVAL + " INTEGER NOT NULL, " +
				Constants.SETTINGS_COLLECT_DATA_MODE + " INTEGER DEFAULT 0 NOT NULL, " +
				Constants.SETTINGS_SENDING_DATA_AUTO + " INTEGER DEFAULT 0 NOT NULL, " +
				Constants.SETTINGS_LAST_SYNC_LOCAL_TIME + " INTEGER DEFAULT 0 NOT NULL, " +
				Constants.SETTINGS_LAST_SYNC_SERVER_TIME + " INTEGER DEFAULT 0 NOT NULL, " +
				Constants.USER_ID + " INTEGER NOT NULL, " +
				"FOREIGN KEY("+Constants.USER_ID+") REFERENCES "+Constants.USER_TABLE_NAME+"("+Constants.USER_ID+") ON DELETE CASCADE ON UPDATE CASCADE" + 
				");");
		//Trigger
		/*db.execSQL("CREATE TRIGGER delete_answers_with BEFORE DELETE ON "+Constants.GROUP_TABLE_NAME
			       +  " FOR EACH ROW BEGIN"
			       +         " DELETE FROM "+Constants.ANSWER_TABLE_NAME+" WHERE "+Constants.ANSWER_TABLE_NAME+"."+Constants.GROUP_ID+"="+Constants.GROUP_TABLE_NAME+"."+Constants.GROUP_ID
			       +  " END;");		
		
		if(!db.isReadOnly()){
			db.execSQL("PRAGMA foreign_keys = ON;");
		}*/
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		//Update settings table
		//db.execSQL("ALTER TABLE " + Constants.SETTINGS_TABLE_NAME + " ADD COLUMN " +
		//			Constants.SETTINGS_SENDING_DATA_AUTO + " INTEGER DEFAULT '0' NOT NULL");
	}
	
	public void onClose() {
		if(cursor != null)
			cursor.close();
		if(dbReadable != null)
			dbReadable.close();
		if(dbWritable != null)
			dbWritable.close();
	}
	
	public Long getLastSyncTime(Integer userId, String lastSyncTime){
		cursor = dbReadable.query(Constants.SETTINGS_TABLE_NAME, new String[] {lastSyncTime}, Constants.USER_ID + "= ?", new String[] {userId.toString()}, null, null, null);
		long lastSyncData = 0;
		if(cursor != null && cursor.getCount() == 1) {
			if(cursor.moveToNext()) {
				lastSyncData = cursor.getLong(cursor.getColumnIndexOrThrow(lastSyncTime));
				cursor.close();
			}
		}
		return lastSyncData;
	}
	
	/**
	 * 
	 * @param lastSyncLocalTime
	 * @return
	 */
	public boolean updateLastSyncTime(Long lastSyncLocalTime, Long lastSyncServerTime, Integer userId) {
		ContentValues cv = new ContentValues();
		cv.put(Constants.SETTINGS_LAST_SYNC_LOCAL_TIME, lastSyncLocalTime);
		cv.put(Constants.SETTINGS_LAST_SYNC_SERVER_TIME, lastSyncServerTime);
		long res = 0;
		res = dbWritable.update(Constants.SETTINGS_TABLE_NAME, cv, Constants.USER_ID+ "= ?", new String[] {userId.toString()});
		return (res > 0) ? true : false;
	}

	/**
	 * @param userId
	 * @return
	 */
	public MaritacaSetting getMaritacaSetting(Integer userId){
		cursor = dbReadable.query(Constants.SETTINGS_TABLE_NAME,
				new String[]{Constants.SETTINGS_SYNC_ACTIVE, Constants.SETTINGS_SYNC_INTERVAL, Constants.SETTINGS_COLLECT_DATA_MODE, Constants.SETTINGS_SENDING_DATA_AUTO},
				Constants.USER_ID + "= ?",
				new String[]{userId.toString()}, null, null, null);
		if(cursor != null && cursor.getCount() == 1) {
			if(cursor.moveToNext()) {	
				MaritacaSetting ms = new MaritacaSetting((cursor.getInt(cursor.getColumnIndexOrThrow(Constants.SETTINGS_SYNC_ACTIVE)) == 1 ? true : false), 
														  cursor.getInt(cursor.getColumnIndexOrThrow(Constants.SETTINGS_SYNC_INTERVAL)),
														  (cursor.getInt(cursor.getColumnIndexOrThrow(Constants.SETTINGS_COLLECT_DATA_MODE)) == 1 ? true : false),
														 (cursor.getInt(cursor.getColumnIndexOrThrow(Constants.SETTINGS_SENDING_DATA_AUTO)) == 1 ? true : false));
				cursor.close();
				return ms;
			}
		}		
		return null;
	}
	
	/**
	 * This method returns is the synchronization is active or not
	 * @param userId
	 * @return
	 */
	public boolean isSyncActive(Integer userId){
		cursor = dbReadable.query(Constants.SETTINGS_TABLE_NAME, new String[] {Constants.SETTINGS_SYNC_ACTIVE}, Constants.USER_ID + "= ?", new String[] {userId.toString()}, null, null, null);
		int syncActive = 0;
		if(cursor != null && cursor.getCount() == 1) {
			if(cursor.moveToNext()) {	
				syncActive = cursor.getInt(cursor.getColumnIndexOrThrow(Constants.SETTINGS_SYNC_ACTIVE));
				cursor.close();
			}
		}		
		return syncActive == 0 ? false : true;
	}
	
	/**
	 * True whether the Collect Data Mode is List, otherwise it will false
	 * @param userId
	 * @return
	 */
	public boolean isListTheCollectDataMode(Integer userId){
		cursor = dbReadable.query(Constants.SETTINGS_TABLE_NAME, new String[] {Constants.SETTINGS_COLLECT_DATA_MODE}, Constants.USER_ID + "= ?", new String[] {userId.toString()}, null, null, null);
		int collectDataMode = 0;
		if(cursor != null && cursor.getCount() == 1) {
			if(cursor.moveToNext()) {
				collectDataMode = cursor.getInt(cursor.getColumnIndexOrThrow(Constants.SETTINGS_COLLECT_DATA_MODE));
				cursor.close();
			}
		}		
		return collectDataMode == 0 ? false : true;
	}
	
	/**
	 * True whether the Sending Data Mode is Auto, otherwise it will false
	 * @param userId
	 * @return
	 */
	public boolean isSendingDataAuto(Integer userId){
		cursor = dbReadable.query(Constants.SETTINGS_TABLE_NAME, new String[] {Constants.SETTINGS_SENDING_DATA_AUTO}, Constants.USER_ID + "= ?", new String[] {userId.toString()}, null, null, null);
		int sendingAuto = 0;
		if(cursor != null && cursor.getCount() == 1) {
			if(cursor.moveToNext()) {	
				sendingAuto = cursor.getInt(cursor.getColumnIndexOrThrow(Constants.SETTINGS_SENDING_DATA_AUTO));
				cursor.close();
			}
		}		
		return sendingAuto == 0 ? false : true;
	}
	
	/**
	 * This method return the time to make a synchronization.
	 * @param userId
	 * @return time
	 */
	public Integer getSyncInteval(Integer userId){
		cursor = dbReadable.query(Constants.SETTINGS_TABLE_NAME, new String[] {Constants.SETTINGS_SYNC_INTERVAL}, Constants.USER_ID + "= ?", new String[] {userId.toString()}, null, null, null);
		int syncInterval = 0;
		if(cursor != null && cursor.getCount() == 1) {
			if(cursor.moveToNext()) {
				syncInterval = cursor.getInt(cursor.getColumnIndexOrThrow(Constants.SETTINGS_SYNC_INTERVAL));
				cursor.close();
			}
		}
		return syncInterval;
	}
	
	/**
	 * 
	 * @param userId
	 * @return
	 */
	public boolean existSettingsRow(Integer userId){
		boolean res = false;
		cursor = dbReadable.query(Constants.SETTINGS_TABLE_NAME, new String[] {Constants.USER_ID}, Constants.USER_ID + "= ?", new String[] {userId.toString()}, null, null, null);		
		if(cursor != null && cursor.getCount() == 1) {
			res = true;
		}
		cursor.close();
		return res;
	}
	
	/**
	 * 
	 * @param isSyncActive
	 * @param syncInterval
	 * @param userId
	 * @return
	 */
	public boolean saveSettings(boolean isSyncActive, Integer syncInterval, boolean isCollectDataList, boolean isSendingAuto, Integer userId) {
		ContentValues cv = new ContentValues();
		cv.put(Constants.SETTINGS_SYNC_ACTIVE, (isSyncActive ? 1 : 0));
		cv.put(Constants.SETTINGS_SYNC_INTERVAL, syncInterval.toString());
		long res = 0;
		if(existSettingsRow(userId)) { // update
			cv.put(Constants.SETTINGS_COLLECT_DATA_MODE, (isCollectDataList ? 1 : 0));
			cv.put(Constants.SETTINGS_SENDING_DATA_AUTO, (isSendingAuto ? 1 : 0));
			res = dbWritable.update(Constants.SETTINGS_TABLE_NAME, cv, Constants.USER_ID+ "= ?", new String[] {userId.toString()});
		}
		else { // create

			res = dbWritable.insert(Constants.SETTINGS_TABLE_NAME, null, cv);
		}
		return  (res > 0) ? true : false;
	}


	/**
	 * Checks if user exists in the database
	 * @param email
	 * @return
	 */
	public boolean userExists(String email) {
		boolean flag = false;
		cursor = dbReadable.query(Constants.USER_TABLE_NAME, new String[] {Constants.USER_EMAIL}, Constants.USER_EMAIL + "= ?", new String[] {email}, null, null, null);
		if(cursor != null && cursor.getCount() == 1) {
			cursor.close();
			flag = true;
		}		
		return flag;
	}
	
	/**
	 * Gets an valid user but if there are two valid users, the fields access_token, refresh_token and expiration_date will be reset	
	 * @return
	 */
	public MaritacaUser getValidUser() {
		cursor = dbReadable.query(Constants.USER_TABLE_NAME, 
									new String[] {Constants.USER_ID, Constants.USER_EMAIL, Constants.USER_ACCESSTOKEN, Constants.USER_REFRESHTOKEN, Constants.USER_EXP_DATE, Constants.USER_INIT_DATE}, 
									Constants.USER_ACCESSTOKEN + "<> ? AND " + Constants.USER_REFRESHTOKEN + " <> ? AND " + Constants.USER_EXP_DATE + " <> ?", 
									new String[] {"", "", "0"}, 
									null, null, null);
		
		if(cursor != null && cursor.getCount() == 1) {
			if(cursor.moveToNext()) {
				try {
					//id - email - accesstoken - refreshtoken - expdate - initdate
					MaritacaUser mUser = new MaritacaUser(
						cursor.getInt(0),  	
						cursor.getString(1), 
						cursor.getString(2), 
						cursor.getString(3), 
						getExpirationDate(cursor.getLong(4), cursor.getLong(5)), 
						cursor.getLong(5));
					cursor.close();
					return mUser;
				}
				catch(Exception e) {
					cursor.close();
					return null;
				}
			}
		}
		else if(cursor != null && cursor.getCount() > 1) {
			resetUsers();	
			cursor.close();
			return null;
		}
		cursor.close();
		return null;
	}
	
	/**
	 * 
	 * @param username
	 * @return
	 */
	public Long getExpirationDateByUsername(String username) {
		Long expDate = null;		
		cursor = dbReadable.query(Constants.USER_TABLE_NAME, 
				new String[] {Constants.USER_EXP_DATE, Constants.USER_INIT_DATE}, 
				Constants.USER_EMAIL + " = ? AND " + Constants.USER_ACCESSTOKEN + " <> ? AND " + Constants.USER_REFRESHTOKEN + " <> ? AND " + Constants.USER_EXP_DATE + " <> ?", 
				new String[] {username, "", "", "0"}, 
				null, null, null);
		
		if(cursor != null && cursor.getCount() == 1) {
			if(cursor.moveToNext()) {
				try {
					expDate = getExpirationDate(cursor.getLong(0), cursor.getLong(1));
				}
				catch(Exception e) {
					expDate = null;
				}
			}
		}
		cursor.close();
		return expDate;
	}
	
	/**
	 * Get the minutes between expiration and current time
	 * @param expDate
	 * @param initDate
	 * @return
	 */
	private long getExpirationDate(Long expDate, Long initDate) {
		if(expDate == null || initDate == null)
			return 0;
		try {
			Calendar expirationDate = new GregorianCalendar();
			expirationDate.setTime(new Date(initDate));
			expirationDate.add(Calendar.SECOND, expDate.intValue());			
			return expirationDate.getTimeInMillis() - 	System.currentTimeMillis();
		}
		catch(Exception e) {
			return 0;
		}
	}
	
	/**
	 * This method saves a new user
	 * @param email
	 * @param accessToken
	 * @param initDate
	 * @param expDate
	 * @param formId
	 * @return
	 */
	public boolean addUser(String email, String accessToken, String refreshToken, long initDate, int expDate, String formId) {
		boolean isSuccessful = false;
		ContentValues cv = new ContentValues();
		cv.put(Constants.USER_EMAIL, email);
		cv.put(Constants.USER_ACCESSTOKEN, accessToken);
		cv.put(Constants.USER_REFRESHTOKEN, refreshToken);
		cv.put(Constants.USER_INIT_DATE, initDate);
		cv.put(Constants.USER_EXP_DATE, expDate);
		cv.put(Constants.USER_FORM_ID, formId);
				
		long res = dbWritable.insert(Constants.USER_TABLE_NAME, null, cv);
		if(res > 0) {
			isSuccessful = true;
		}
		return isSuccessful;
	}
	
	/**
	 * 
	 * @param email
	 * @param accessToken
	 * @param refreshToken
	 * @param initDate
	 * @param expDate
	 * @return
	 */
	public boolean updateUser(String email, String accessToken, String refreshToken, long initDate, int expDate) {
		boolean isSuccessful = false;
		ContentValues cv = new ContentValues();
		cv.put(Constants.USER_ACCESSTOKEN, accessToken);
		cv.put(Constants.USER_REFRESHTOKEN, refreshToken);
		cv.put(Constants.USER_INIT_DATE, initDate);
		cv.put(Constants.USER_EXP_DATE, expDate);
				
		long res = dbWritable.update(Constants.USER_TABLE_NAME, cv, Constants.USER_EMAIL + "= ?", new String[]{email});
		if(res > 0)
			isSuccessful = true;
		return isSuccessful;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean resetUsers() {
		boolean isSuccessful = false;
		ContentValues cv = new ContentValues();
		cv.put(Constants.USER_ACCESSTOKEN, "");
		cv.put(Constants.USER_REFRESHTOKEN, "");
		cv.put(Constants.USER_EXP_DATE, 0);
				
		long res = dbWritable.update(Constants.USER_TABLE_NAME, cv, null, null);
		if(res > 0)
			isSuccessful = true;
		return isSuccessful;
	}
	
	public boolean updateFormInUser(String email, String formId) {
		boolean isSuccessful = false;
		if(email != null && !"".equals(email) && formId != null && !"".equals(formId)) {
			ContentValues cv = new ContentValues();
			cv.put(Constants.USER_FORM_ID, formId);
					
			long res = dbWritable.update(Constants.USER_TABLE_NAME, cv, Constants.USER_EMAIL + "= ?", new String[] {email});
			if(res > 0)
				isSuccessful = true;
		}
		return isSuccessful;
	}
	
	/**
	 * 
	 * @param email
	 * @param accessToken
	 * @param refreshToken
	 * @param expDate
	 * @return
	 */
	public boolean saveUserData(String email, String accessToken, String refreshToken, int expDate) {
		if(userExists(email)) {
			return updateUser(email, accessToken, refreshToken, System.currentTimeMillis(), expDate);
		}
		else {
			boolean result = addUser(email, accessToken, refreshToken, System.currentTimeMillis(), expDate, null);
			if (result) {
				this.saveSettings(false, 0, false, false, getValidUser().getId());
			}
			return result;
		}
	}
	
	public boolean saveAnswers(List<Question> answers) {
		boolean isSuccessful = false;
	//	if(mUser != null && mUser.getId() != null) {
			dbWritable.beginTransaction();
			try {
				//update user -> formId
			//	if(updateFormInUser(mUser.getEmail(), mUser.getFormId())) {
					ContentValues groupCv = new ContentValues();
					groupCv.put(Constants.GROUP_TIMESTAMP, System.currentTimeMillis());
					groupCv.put(Constants.GROUP_VALID, 1);
				//	long groupRes = dbWritable.insert(Constants.GROUP_TABLE_NAME, null, groupCv);
					long groupRes=0;
					//if(groupRes > 0) {
						ContentValues answerCv;
						for(Question q : answers) {
							answerCv = new ContentValues();
							answerCv.put(Constants.ANSWER_ID, q.getId());
							answerCv.put(Constants.ANSWER_TYPE, q.getComponentType().getDescription());							
							if(q.getComponentType().equals(ComponentType.BARCODE) && q.getValue() != null) {
								BarCodeInformation data = (BarCodeInformation)q.getValue();								
								answerCv.put(Constants.ANSWER_SUBTYPE, data.getType() != null ? data.getType() : "");
								answerCv.put(Constants.ANSWER_VALUE, data.getCode() != null ? data.getCode() : "");
							}
							else if(q.getComponentType().equals(ComponentType.MONEY) && q.getValue() != null){
								MoneyQuestion money = (MoneyQuestion) q;
								answerCv.put(Constants.ANSWER_SUBTYPE, money.getCurrency());
								answerCv.put(Constants.ANSWER_VALUE, q.getValue().toString());
							}
							else if(q.getComponentType().equals(ComponentType.COMBOBOX) && q.getValue() != null){
								answerCv.put(Constants.ANSWER_SUBTYPE, "");
								answerCv.put(Constants.ANSWER_VALUE, !q.getValue().equals("{}") ? q.getValue().toString() : "");
							}
							else {
								answerCv.put(Constants.ANSWER_SUBTYPE, "");
								answerCv.put(Constants.ANSWER_VALUE, q.getValue() != null ? q.getValue().toString() : "");
							}
							answerCv.put(Constants.GROUP_ID, groupRes);
							dbWritable.insert(Constants.ANSWER_TABLE_NAME,null, answerCv);
						//}
						isSuccessful = true;
					}				
					//
					dbWritable.setTransactionSuccessful();
			//	}
			}
			catch(Exception e) {
				//TODO: tx
				Log.e(this.getClass().getName(), e.getMessage());
			}
			finally {
				dbWritable.endTransaction();
			}
		//}
		return isSuccessful;		
	}
	
	public MaritacaUser getAnswersByUser(MaritacaUser mUser) {		
		SQLiteQueryBuilder sqb = new SQLiteQueryBuilder();
		sqb.setTables(Constants.USER_TABLE_NAME+" INNER JOIN "+Constants.GROUP_TABLE_NAME+" INNER JOIN "+Constants.ANSWER_TABLE_NAME+
				" ON ("+Constants.USER_TABLE_NAME+"."+Constants.USER_ID+"="+Constants.GROUP_TABLE_NAME+"."+Constants.USER_ID+" AND "+
					Constants.GROUP_TABLE_NAME+"."+Constants.GROUP_ID+"="+Constants.ANSWER_TABLE_NAME+"."+Constants.GROUP_ID+")" +
					" WHERE "+Constants.GROUP_TABLE_NAME+"."+Constants.GROUP_VALID +"=? AND "+Constants.USER_TABLE_NAME+"."+Constants.USER_EMAIL+"=?");
		String[] cols = new String[] {Constants.USER_TABLE_NAME+"."+Constants.USER_FORM_ID, Constants.GROUP_TABLE_NAME+"."+Constants.GROUP_ID, 
				Constants.GROUP_TABLE_NAME+"."+Constants.GROUP_TIMESTAMP, 
				Constants.ANSWER_TABLE_NAME+"."+Constants.ANSWER_ID, Constants.ANSWER_TABLE_NAME+"."+Constants.ANSWER_TYPE, 
				Constants.ANSWER_TABLE_NAME+"."+Constants.ANSWER_SUBTYPE, Constants.ANSWER_TABLE_NAME+"."+Constants.ANSWER_VALUE, 
				Constants.USER_TABLE_NAME+"."+Constants.USER_ID};
		String query = sqb.buildQuery(cols, null, null, null, null, null, null);
		//SELECT user.email, user.form_id, answer.id, answer.type, answer.subtype, answer.value 
		//FROM user INNER JOIN answers_group INNER JOIN answer ON (user.user_id=answers_group.user_id AND answers_group.group_id=answer.group_id) 
		//WHERE user.email=?

		try {
			cursor = dbWritable.rawQuery(query, new String[] {"1", mUser.getEmail()});
			if(cursor != null) {
				//form_id - group_id - group_timestamp - *answer_id - answer_typ - answer_sub - answer_val - user_id
				List<MaritacaGroup> mGroups = new ArrayList<MaritacaGroup>();
				MaritacaGroup mGroup = null;
				List<MaritacaAnswer> mAnswers = null;
				boolean flag = false;
				while(cursor.moveToNext()) {
					if(!flag) {
						mUser.setFormId(cursor.getString(0));
						mUser.setId(cursor.getInt(7));
						flag = true;
					}
					mGroup = new MaritacaGroup(cursor.getInt(1), null, new ArrayList<MaritacaAnswer>());
					int index = mGroups.indexOf(mGroup);
					if(index > -1) {
						mGroups.get(index).getAnswers().add(new MaritacaAnswer(cursor.getInt(3), cursor.getString(4), cursor.getString(5), cursor.getString(6)));
					}
					else {
						mGroup.setTimestamp(cursor.getLong(2));
						mAnswers = new ArrayList<MaritacaAnswer>();
						mAnswers.add(new MaritacaAnswer(cursor.getInt(3), cursor.getString(4), cursor.getString(5), cursor.getString(6)));
						mGroup.setAnswers(mAnswers);
						mGroups.add(mGroup);
					}
				}
				mUser.setAnswersGroup(mGroups);				
			}
		}
		catch(Exception e) {
			//TODO:
			Log.e(this.getClass().getName(), e.getMessage());
		}
		finally {
			if(cursor != null)
				cursor.close();
		}
		return mUser;
	}
	
	
	

	
	
	
	
	
	
	
	
	
	/**
	 * 
	 * @param mUser
	 * @return
	 */
	public List<MaritacaGroup> getAnswersGroupsByUser(MaritacaUser mUser){
		List<MaritacaGroup> mGroups = null;
		SQLiteQueryBuilder sqb = new SQLiteQueryBuilder();
		sqb.setTables(Constants.USER_TABLE_NAME+" INNER JOIN "+Constants.GROUP_TABLE_NAME+
				" ON ("+Constants.USER_TABLE_NAME+"."+Constants.USER_ID+"="+Constants.GROUP_TABLE_NAME+"."+Constants.USER_ID+")" +
					" WHERE "+Constants.USER_TABLE_NAME+"."+Constants.USER_EMAIL+"=?");
		String[] cols = new String[] {Constants.GROUP_TABLE_NAME+"."+Constants.GROUP_ID, 
				Constants.GROUP_TABLE_NAME+"."+Constants.GROUP_TIMESTAMP};
		String query = sqb.buildQuery(cols, null, null, null, null, null, null);
		//
		try {
			cursor = dbWritable.rawQuery(query, new String[] {mUser.getEmail()});
			if(cursor != null) {
				//
				mGroups = new ArrayList<MaritacaGroup>();
				MaritacaGroup mGroup = null;			
				while(cursor.moveToNext()) {					
					mGroup = new MaritacaGroup(cursor.getInt(0), cursor.getLong(1));
					mGroups.add(mGroup);
				}								
			}
		}
		catch(Exception e) {
			//TODO:
			Log.e(this.getClass().getName(), e.getMessage());
		}
		finally {
			if(cursor != null)
				cursor.close();
		}
		return mGroups;
	}
	
	public List<MaritacaAnswer> getAnswerByAnswerGroup(Integer answerGroupId){
		List<MaritacaAnswer> answers = null;
		SQLiteQueryBuilder sqb = new SQLiteQueryBuilder();
		sqb.setTables(Constants.GROUP_TABLE_NAME+" INNER JOIN "+Constants.ANSWER_TABLE_NAME+
				" ON ("+Constants.GROUP_TABLE_NAME+"."+Constants.GROUP_ID+"="+Constants.ANSWER_TABLE_NAME+"."+Constants.GROUP_ID+")" +
					" WHERE "+Constants.GROUP_TABLE_NAME+"."+Constants.GROUP_ID+"=?");
		String[] cols = new String[] { 
				Constants.ANSWER_TABLE_NAME+"."+Constants.ANSWER_ID, Constants.ANSWER_TABLE_NAME+"."+Constants.ANSWER_TYPE, 
				Constants.ANSWER_TABLE_NAME+"."+Constants.ANSWER_SUBTYPE, Constants.ANSWER_TABLE_NAME+"."+Constants.ANSWER_VALUE};
		String query = sqb.buildQuery(cols, null, null, null, null, null, null);
		//
		try {
			cursor = dbWritable.rawQuery(query, new String[] {String.valueOf(answerGroupId)});
			if(cursor != null) {
				//
				answers = new ArrayList<MaritacaAnswer>();
				MaritacaAnswer answer = null;			
				while(cursor.moveToNext()) {					
					answer = new MaritacaAnswer(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3));
					answers.add(answer);
				}								
			}
		}
		catch(Exception e) {
			//TODO:
			Log.e(this.getClass().getName(), e.getMessage());
		}
		finally {
			if(cursor != null)
				cursor.close();
		}
		return answers;
	}
	
	public void deleteAnswersAndAnswersGroupByUserId(String userId) {
		String clause = Constants.GROUP_TABLE_NAME+"."+Constants.USER_ID+"=?";
		long res = dbWritable.delete(Constants.GROUP_TABLE_NAME, clause, new String[] {userId});
		Log.d("delete", "delete:answers-> " + res);
	}
	
	public boolean existsAnswerByIdAndByGroupNoValid(Long answerGroupId, Integer answerId){
    	boolean exists = false;
    	SQLiteQueryBuilder sqb = new SQLiteQueryBuilder();
		sqb.setTables(Constants.ANSWER_TABLE_NAME + " INNER JOIN " + Constants.GROUP_TABLE_NAME + " ON (" +
				Constants.GROUP_TABLE_NAME + "." + Constants.GROUP_ID + "=" + Constants.ANSWER_TABLE_NAME + "." + Constants.GROUP_ID + ") WHERE " +
				Constants.GROUP_TABLE_NAME + "." + Constants.GROUP_ID + "=? AND " + Constants.ANSWER_TABLE_NAME + "." + Constants.ANSWER_ID + "=?");
		
		String[] cols = new String[] { 
				Constants.ANSWER_TABLE_NAME+"."+Constants.ANSWER_ID};
		String query = sqb.buildQuery(cols, null, null, null, null, null, null);
		
		try {
			cursor = dbWritable.rawQuery(query, new String[] {String.valueOf(answerGroupId), String.valueOf(answerId)});
			if(cursor != null && cursor.getCount() == 1) {
				exists = true;
			}
		}
		catch(Exception e) {
			//TODO:
			Log.e(this.getClass().getName(), e.getMessage());
		}
		finally {
			if(cursor != null)
				cursor.close();
		}    	    	
    	return exists;
    }
	
    public MaritacaGroup getAnswerGroupNoValid(Integer userId){
    	MaritacaGroup group = null;
    	cursor = dbReadable.query(Constants.GROUP_TABLE_NAME, 
				new String[] {Constants.GROUP_ID}, 
				Constants.GROUP_VALID + "= ? AND " + Constants.USER_ID + " = ?", 
				new String[] {"0", userId.toString()}, 
				null, null, null);
    	
    	if(cursor != null && cursor.getCount() == 1) {
			if(cursor.moveToNext()) {
				try{
					//group_id
					group = new MaritacaGroup();						
						group.setGroupId(cursor.getInt(0));
					cursor.close();
					return group;
				} catch(Exception e){
					cursor.close();
					return null;
				}
			}
    	} else if(cursor != null && cursor.getCount() > 1){
    		//resetGroups no valids
    		cursor.close();
    		return null;
    	}
    	cursor.close();
    	return null;
    }
    
    public MaritacaAnswer getAnswerByUserWithGroupNoValid(Integer userId, Integer answerId){
    	MaritacaAnswer answer = null;
    	
    	MaritacaGroup group = getAnswerGroupNoValid(userId);
    	if(group != null){
    		answer = getAnswerByGroupAndUser(userId.toString(), group.getGroupId(), answerId);
    	}
    	return answer;
    }
    
   
        
	public MaritacaAnswer getAnswerByGroupAndUser(String userId, Integer answerGroupId, Integer answerId){
    	MaritacaAnswer answer = null;
    	SQLiteQueryBuilder sqb = new SQLiteQueryBuilder();
		sqb.setTables(Constants.ANSWER_TABLE_NAME+" INNER JOIN "+Constants.GROUP_TABLE_NAME+" INNER JOIN "+Constants.USER_TABLE_NAME+
				" ON ("+Constants.USER_TABLE_NAME+"."+Constants.USER_ID+"="+Constants.GROUP_TABLE_NAME+"."+Constants.USER_ID+" AND "+
				Constants.GROUP_TABLE_NAME+"."+Constants.GROUP_ID+"="+Constants.ANSWER_TABLE_NAME+"."+Constants.GROUP_ID+") WHERE "+
				Constants.USER_TABLE_NAME+"."+Constants.USER_ID+"=? AND "+Constants.GROUP_TABLE_NAME+"."+Constants.GROUP_ID+"=? AND "+
				Constants.ANSWER_TABLE_NAME+"."+Constants.ANSWER_ID+"=?");
		
		String[] cols = new String[] { 
				Constants.ANSWER_TABLE_NAME+"."+Constants.ANSWER_ID, Constants.ANSWER_TABLE_NAME+"."+Constants.ANSWER_TYPE, 
				Constants.ANSWER_TABLE_NAME+"."+Constants.ANSWER_SUBTYPE, Constants.ANSWER_TABLE_NAME+"."+Constants.ANSWER_VALUE};
		String query = sqb.buildQuery(cols, null, null, null, null, null, null);
		try {
			cursor = dbWritable.rawQuery(query, new String[] {userId, String.valueOf(answerGroupId), String.valueOf(answerId)});
			if(cursor != null) {
				while(cursor.moveToNext()) {					
					answer = new MaritacaAnswer(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3));
				}								
			}
		}
		catch(Exception e) {
			//TODO:
			Log.e(this.getClass().getName(), e.getMessage());
		}
		finally {
			if(cursor != null)
				cursor.close();
		}    	    	
        return answer;        
    }
    
    public boolean saveAnswerItem2(MaritacaUser mUser, Question question, List<Question> questions){
    	boolean isSuccessful = false;
    	if(mUser != null && mUser.getId() != null) {
    		dbWritable.beginTransaction();
			try {
				//update user -> formId //??
				if(updateFormInUser(mUser.getEmail(), mUser.getFormId())) {
					MaritacaGroup group = getAnswerGroupNoValid(mUser.getId());
					long groupId = 0;
					if(group != null){
						groupId = group.getGroupId();
					} else {
						ContentValues groupCv = new ContentValues();
						groupCv.put(Constants.GROUP_TIMESTAMP, System.currentTimeMillis());
						groupCv.put(Constants.GROUP_VALID, 0);
						groupCv.put(Constants.USER_ID, mUser.getId());
						groupId = dbWritable.insert(Constants.GROUP_TABLE_NAME, null, groupCv);
					}
					
					if(groupId > 0){						
						if(existsAnswerByIdAndByGroupNoValid(groupId, question.getId())){//update
							ContentValues answerCv = new ContentValues();
							answerCv.put(Constants.ANSWER_TYPE, question.getComponentType().getDescription());
							if(question.getComponentType().equals(ComponentType.BARCODE) && question.getValue() != null) {
								BarCodeInformation data = (BarCodeInformation)question.getValue();								
								answerCv.put(Constants.ANSWER_SUBTYPE, data.getType() != null ? data.getType() : "");
								answerCv.put(Constants.ANSWER_VALUE, data.getCode() != null ? data.getCode() : "");
							}
							else if(question.getComponentType().equals(ComponentType.MONEY) && question.getValue() != null){
								MoneyQuestion money = (MoneyQuestion) question;
								answerCv.put(Constants.ANSWER_SUBTYPE, money.getCurrency());
								answerCv.put(Constants.ANSWER_VALUE, question.getValue().toString());
							}
							else if(question.getComponentType().equals(ComponentType.COMBOBOX) && question.getValue() != null){
								answerCv.put(Constants.ANSWER_SUBTYPE, "");
								answerCv.put(Constants.ANSWER_VALUE, !question.getValue().equals("{}") ? question.getValue().toString() : "");
							}
							else {
								answerCv.put(Constants.ANSWER_SUBTYPE, "");
								answerCv.put(Constants.ANSWER_VALUE, question.getValue() != null ? question.getValue().toString() : "");
							}							
							
							long res = 0;
							res = dbWritable.update(Constants.ANSWER_TABLE_NAME, answerCv, Constants.ANSWER_ID+ "= ?", new String[] {String.valueOf(question.getId())});
							if(res > 0) {
								isSuccessful = true; 
							} else {
								isSuccessful = false;
							}
							
						} else{//insert
							ContentValues answerCv;
							for(Question q : questions) {
								if(q.getId() != question.getId()){
									q.setValue(q.getDefault());
								}								
									
								answerCv = new ContentValues();
								answerCv.put(Constants.ANSWER_ID, q.getId());
								answerCv.put(Constants.ANSWER_TYPE, q.getComponentType().getDescription());							
								if(q.getComponentType().equals(ComponentType.BARCODE) && q.getValue() != null) {
									BarCodeInformation data = (BarCodeInformation)q.getValue();								
									answerCv.put(Constants.ANSWER_SUBTYPE, data.getType() != null ? data.getType() : "");
									answerCv.put(Constants.ANSWER_VALUE, data.getCode() != null ? data.getCode() : "");
								}
								else if(q.getComponentType().equals(ComponentType.MONEY) && q.getValue() != null){
									MoneyQuestion money = (MoneyQuestion) q;
									answerCv.put(Constants.ANSWER_SUBTYPE, money.getCurrency());
									answerCv.put(Constants.ANSWER_VALUE, q.getValue().toString());
								}
								else if(q.getComponentType().equals(ComponentType.COMBOBOX) && q.getValue() != null){
									answerCv.put(Constants.ANSWER_SUBTYPE, "");
									answerCv.put(Constants.ANSWER_VALUE, !q.getValue().equals("{}") ? q.getValue().toString() : "");
								}
								else {
									answerCv.put(Constants.ANSWER_SUBTYPE, "");
									answerCv.put(Constants.ANSWER_VALUE, q.getValue() != null ? q.getValue().toString() : "");
								}
								answerCv.put(Constants.GROUP_ID, groupId);
								dbWritable.insert(Constants.ANSWER_TABLE_NAME, null, answerCv);
							}
							isSuccessful = true;
						}				
						//
						dbWritable.setTransactionSuccessful();
					}					
				}
				
			} catch(Exception e) {
				//TODO: tx
				Log.e(this.getClass().getName(), e.getMessage());
			}
			finally {
				dbWritable.endTransaction();
			}
        }        
        return isSuccessful;
    }
    
    public boolean saveAnswerItem(MaritacaUser mUser, Question question){
    	boolean isSuccessful = false;    	
    	
        if(mUser != null && mUser.getId() != null) {
    		dbWritable.beginTransaction();
			try {
				//update user -> formId //??
				if(updateFormInUser(mUser.getEmail(), mUser.getFormId())) {
					MaritacaGroup group = getAnswerGroupNoValid(mUser.getId());
					long groupId = 0;
					if(group != null){
						groupId = group.getGroupId();
					} else {
						ContentValues groupCv = new ContentValues();
						groupCv.put(Constants.GROUP_TIMESTAMP, System.currentTimeMillis());
						groupCv.put(Constants.GROUP_VALID, 0);
						groupCv.put(Constants.USER_ID, mUser.getId());
						groupId = dbWritable.insert(Constants.GROUP_TABLE_NAME, null, groupCv);
					}				
					
					if(groupId > 0){						
						if(existsAnswerByIdAndByGroupNoValid(groupId, question.getId())){//update
							ContentValues answerCv = new ContentValues();
							answerCv.put(Constants.ANSWER_TYPE, question.getComponentType().getDescription());
							if(question.getComponentType().equals(ComponentType.BARCODE) && question.getValue() != null) {
								BarCodeInformation data = (BarCodeInformation)question.getValue();								
								answerCv.put(Constants.ANSWER_SUBTYPE, data.getType() != null ? data.getType() : "");
								answerCv.put(Constants.ANSWER_VALUE, data.getCode() != null ? data.getCode() : "");
							}
							else if(question.getComponentType().equals(ComponentType.MONEY) && question.getValue() != null){
								MoneyQuestion money = (MoneyQuestion) question;
								answerCv.put(Constants.ANSWER_SUBTYPE, money.getCurrency());
								answerCv.put(Constants.ANSWER_VALUE, question.getValue().toString());
							}
							else if(question.getComponentType().equals(ComponentType.COMBOBOX) && question.getValue() != null){
								answerCv.put(Constants.ANSWER_SUBTYPE, "");
								answerCv.put(Constants.ANSWER_VALUE, !question.getValue().equals("{}") ? question.getValue().toString() : "");
							}
							else {
								answerCv.put(Constants.ANSWER_SUBTYPE, "");
								answerCv.put(Constants.ANSWER_VALUE, question.getValue() != null ? question.getValue().toString() : "");
							}							
							
							long res = 0;
							res = dbWritable.update(Constants.ANSWER_TABLE_NAME, answerCv, Constants.ANSWER_ID+ "= ?", new String[] {String.valueOf(question.getId())});
							if(res > 0) {
								isSuccessful = true; 
							} else {
								isSuccessful = false;
							}
							
						} else{//insert
							ContentValues answerCv = new ContentValues();
							answerCv.put(Constants.ANSWER_ID, question.getId());
							answerCv.put(Constants.ANSWER_TYPE, question.getComponentType().getDescription());
							if(question.getComponentType().equals(ComponentType.BARCODE) && question.getValue() != null) {
								BarCodeInformation data = (BarCodeInformation)question.getValue();								
								answerCv.put(Constants.ANSWER_SUBTYPE, data.getType() != null ? data.getType() : "");
								answerCv.put(Constants.ANSWER_VALUE, data.getCode() != null ? data.getCode() : "");
							}
							else if(question.getComponentType().equals(ComponentType.MONEY) && question.getValue() != null){
								MoneyQuestion money = (MoneyQuestion) question;
								answerCv.put(Constants.ANSWER_SUBTYPE, money.getCurrency());
								answerCv.put(Constants.ANSWER_VALUE, question.getValue().toString());
							}
							else if(question.getComponentType().equals(ComponentType.COMBOBOX) && question.getValue() != null){
								answerCv.put(Constants.ANSWER_SUBTYPE, "");
								answerCv.put(Constants.ANSWER_VALUE, !question.getValue().equals("{}") ? question.getValue().toString() : "");
							}
							else {
								answerCv.put(Constants.ANSWER_SUBTYPE, "");
								answerCv.put(Constants.ANSWER_VALUE, question.getValue() != null ? question.getValue().toString() : "");
							}
							answerCv.put(Constants.GROUP_ID, groupId);
							dbWritable.insert(Constants.ANSWER_TABLE_NAME, null, answerCv);
							
							isSuccessful = true;
						}						
					}
					dbWritable.setTransactionSuccessful();
				}				
			} catch(Exception e) {
				//TODO: tx
				Log.e(this.getClass().getName(), e.getMessage());
			}
			finally {
				dbWritable.endTransaction();
			}
        }        
        return isSuccessful;    	
    }
    
    public List<MaritacaAnswer> getAnswersByGroupNoValid(MaritacaUser mUser) {
    	List<MaritacaAnswer> answers = null;
		SQLiteQueryBuilder sqb = new SQLiteQueryBuilder();
		sqb.setTables(Constants.USER_TABLE_NAME+" INNER JOIN "+Constants.GROUP_TABLE_NAME+" INNER JOIN "+Constants.ANSWER_TABLE_NAME+
				" ON ("+Constants.USER_TABLE_NAME+"."+Constants.USER_ID+"="+Constants.GROUP_TABLE_NAME+"."+Constants.USER_ID+" AND "+
					Constants.GROUP_TABLE_NAME+"."+Constants.GROUP_ID+"="+Constants.ANSWER_TABLE_NAME+"."+Constants.GROUP_ID+")" +
					" WHERE "+Constants.GROUP_TABLE_NAME+"."+Constants.GROUP_VALID +"=? AND "+Constants.USER_TABLE_NAME+"."+Constants.USER_EMAIL+"=?");
		String[] cols = new String[] {Constants.ANSWER_TABLE_NAME+"."+Constants.ANSWER_ID, Constants.ANSWER_TABLE_NAME+"."+Constants.ANSWER_TYPE, 
				Constants.ANSWER_TABLE_NAME+"."+Constants.ANSWER_SUBTYPE, Constants.ANSWER_TABLE_NAME+"."+Constants.ANSWER_VALUE };
		String query = sqb.buildQuery(cols, null, null, null, null, null, null);

		try {
			cursor = dbWritable.rawQuery(query, new String[] {"0", mUser.getEmail()});
			if(cursor != null) {
				answers = new ArrayList<MaritacaAnswer>();
				while(cursor.moveToNext()) {
					//answer_id - answer_typ - answer_sub - answer_val
					//Log.i("Info", ">0: " + cursor.getInt(0) + " >1: " + cursor.getString(1) + " >2: " + cursor.getString(2) + " >3: " + cursor.getString(3));
					answers.add(new MaritacaAnswer(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3)));
				}
			}
		}
		catch(Exception e) {
			//TODO:
			Log.e(this.getClass().getName(), e.getMessage());
		}
		finally {
			if(cursor != null)
				cursor.close();
		}
		return answers;
	}
    
    /**
     * Update group_valid to 1, and with this the answer_group can be sent to the server
     * @param userId
     */
    public boolean updateAnswerGroup(Integer userId){
    	MaritacaGroup group = getAnswerGroupNoValid(userId);
    	if(group != null){    	
	    	ContentValues cv = new ContentValues();
			cv.put(Constants.GROUP_VALID, 1);
			long res = 0;
			res = dbWritable.update(Constants.GROUP_TABLE_NAME, cv, Constants.GROUP_ID+ "= ?", new String[] {String.valueOf(group.getGroupId())});
			return (res > 0) ? true : false;
    	} else {
    		return false;
    	}    	
    }
    
    public void deleteGroupAndAnswers(Integer userId, Integer answerGroupId){    	
        String clause = Constants.GROUP_TABLE_NAME+"."+Constants.USER_ID+"=? AND " +
                Constants.GROUP_TABLE_NAME+"."+Constants.GROUP_ID+"=?";
        long res = dbWritable.delete(Constants.GROUP_TABLE_NAME, clause, new String[]{String.valueOf(userId), String.valueOf(answerGroupId)});
    }
    
    public boolean deleteGroupNoValidAndAnswers(Integer userId){
    	MaritacaGroup group = getAnswerGroupNoValid(userId);
    	if(group != null){
    		deleteGroupAndAnswers(userId, group.getGroupId());
    		return true;
    	} else {
    		return false;
    	}
    }
	
	public int getCollectNumber(String email) {
		if("".equals(email))
			return 0;
		int count = 0;
		SQLiteQueryBuilder sqb = new SQLiteQueryBuilder();
		sqb.setTables(Constants.USER_TABLE_NAME+" INNER JOIN "+Constants.GROUP_TABLE_NAME+
				" ON ("+Constants.USER_TABLE_NAME+"."+Constants.USER_ID+"="+Constants.GROUP_TABLE_NAME+"."+Constants.USER_ID+")" +
					" WHERE "+Constants.GROUP_TABLE_NAME+"."+Constants.GROUP_VALID +"=? AND "+Constants.USER_TABLE_NAME+"."+Constants.USER_EMAIL+"=?");
		String[] cols = new String[] {Constants.USER_TABLE_NAME+"."+Constants.USER_ID, Constants.GROUP_TABLE_NAME+"."+Constants.GROUP_ID};
		String query = sqb.buildQuery(cols, null, null, null, null, null, null);
		try {
			cursor = dbWritable.rawQuery(query, new String[] {"1", email});
			if(cursor != null)
				count = cursor.getCount();
		}
		catch(Exception e) {
			//TODO:
			Log.e(this.getClass().getName(), e.getMessage());
		}
		finally {
			if(cursor != null)
				cursor.close();
		}
		return count;
	}
	
	public boolean addError(Integer userId, 	MaritacaLog log) {
		boolean isSuccessful = false;
		ContentValues cv = new ContentValues();
		cv.put(Constants.USER_ID, userId);
		cv.put(Constants.LOG_MESSAGE, log.getMessage());
		cv.put(Constants.LOG_DATE, log.getTimestamp());
		cv.put(Constants.LOG_ANDROIDVERSION, log.getAndroidVersion());
				
		long res = dbWritable.insert(Constants.LOG_TABLE_NAME, null, cv);
		if(res > 0)
			isSuccessful = true;
		return isSuccessful;
	}
	
	public void deleteLogs() {
		long res = dbWritable.delete(Constants.LOG_TABLE_NAME, null, null);
		Log.d("delete", "delete:logs-> "+res);
	}
	
	public List<MaritacaLog> getLogs() {
		List<MaritacaLog> logs = null;
		try {
			//String[] cols = new String[] {Constants.LOG_MESSAGE, Constants.LOG_DATE, Constants.LOG_ANDROIDVERSION };
			//cursor = dbReadable.query(Constants.LOG_TABLE_NAME, cols, null, null, null, null, null);			
			SQLiteQueryBuilder sqb = new SQLiteQueryBuilder();
			sqb.setTables(Constants.LOG_TABLE_NAME+" INNER JOIN "+Constants.USER_TABLE_NAME+
					" ON ("+Constants.LOG_TABLE_NAME+"."+Constants.USER_ID +"="+Constants.USER_TABLE_NAME+"."+Constants.USER_ID+")");
			String[] cols = new String[] {Constants.USER_TABLE_NAME+"."+Constants.USER_EMAIL, Constants.LOG_TABLE_NAME+"."+Constants.LOG_MESSAGE, 
					Constants.LOG_TABLE_NAME+"."+Constants.LOG_DATE, Constants.LOG_TABLE_NAME+"."+Constants.LOG_ANDROIDVERSION };
			String query = sqb.buildQuery(cols, null, null, null, null, null, null);
			cursor = dbReadable.rawQuery(query, null);
			if(cursor != null) {
				MaritacaLog log = null;
				logs = new ArrayList<MaritacaLog>(cursor.getCount());
				while(cursor.moveToNext()) {
					log = new MaritacaLog(cursor.getString(0), cursor.getString(1), cursor.getLong(2), cursor.getString(3));
					logs.add(log);
				}
			}
		}
		catch(Exception e) {
			//TODO:
			Log.e(this.getClass().getName(), e.getMessage());
		}
		finally {
			if(cursor != null)
				cursor.close();
		}		
		return logs;
	}

	public void deleteAnswersAndAnswerGroup(String answerGroupId) {
		String clause = Constants.ANSWER_TABLE_NAME+"."+Constants.GROUP_ID+"=?";
		long res = dbWritable.delete(Constants.ANSWER_TABLE_NAME, clause, new String[] {answerGroupId});
		Log.d("delete", "delete:answers-> "+res);
		
		clause = Constants.GROUP_TABLE_NAME+"."+Constants.GROUP_ID+"=?";
		res = dbWritable.delete(Constants.GROUP_TABLE_NAME, clause, new String[] {answerGroupId});
		Log.d("delete", "delete:answers-> "+res);
	}
	
	

	
	 //Maritaca vLite
    public MaritacaAnswer getAnswer(Integer answerId){
    	MaritacaAnswer answer = null;
    	SQLiteQueryBuilder sqb = new SQLiteQueryBuilder();
    	
    	
		
    	sqb.setTables(Constants.ANSWER_TABLE_NAME + " WHERE " + Constants.ANSWER_TABLE_NAME + "." + Constants.ANSWER_ID + "=?");
    	
    	String[] cols = new String[] { 
				Constants.ANSWER_TABLE_NAME+"."+Constants.ANSWER_ID, Constants.ANSWER_TABLE_NAME+"."+Constants.ANSWER_TYPE, 
				Constants.ANSWER_TABLE_NAME+"."+Constants.ANSWER_SUBTYPE, Constants.ANSWER_TABLE_NAME+"."+Constants.ANSWER_VALUE};
		String query = sqb.buildQuery(cols, null, null, null, null, null, null);
		try {
			cursor = dbWritable.rawQuery(query, new String[] {String.valueOf(answerId)});
			if(cursor != null) {
				while(cursor.moveToNext()) {					
					answer = new MaritacaAnswer(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3));
				}								
			}
		}
		catch(Exception e) {
			//TODO:
			Log.e(this.getClass().getName(), e.getMessage());
		}
		finally {
			if(cursor != null)
				cursor.close();
		}    	
    	
    	return answer;
    	
    }    
    
    public List<MaritacaGroup> getAnswersLocal(){
    	MaritacaUser mUser = new MaritacaUser();
		List<MaritacaGroup> mGroups = null;
		SQLiteQueryBuilder sqb = new SQLiteQueryBuilder();
		sqb.setTables(Constants.USER_TABLE_NAME+" INNER JOIN "+Constants.GROUP_TABLE_NAME+
				" ON ("+Constants.USER_TABLE_NAME+"."+Constants.USER_ID+"="+Constants.GROUP_TABLE_NAME+"."+Constants.USER_ID+")" +
					" WHERE "+Constants.USER_TABLE_NAME+"."+Constants.USER_EMAIL+"=?");
		String[] cols = new String[] {Constants.GROUP_TABLE_NAME+"."+Constants.GROUP_ID, 
				Constants.GROUP_TABLE_NAME+"."+Constants.GROUP_TIMESTAMP};
		String query = sqb.buildQuery(cols, null, null, null, null, null, null);
		//
		try {
			cursor = dbWritable.rawQuery(query, new String[] {mUser.getEmail()});
			if(cursor != null) {
				//
				mGroups = new ArrayList<MaritacaGroup>();
				MaritacaGroup mGroup = null;			
				while(cursor.moveToNext()) {					
					mGroup = new MaritacaGroup(cursor.getInt(0), cursor.getLong(1));
					mGroups.add(mGroup);
				}								
			}
		}
		catch(Exception e) {
			//TODO:
			Log.e(this.getClass().getName(), e.getMessage());
		}
		finally {
			if(cursor != null)
				cursor.close();
		}
		return mGroups;
	}
    
    
    
    
    
    public void getAnswers() {	

		SQLiteQueryBuilder sqb = new SQLiteQueryBuilder();
		sqb.setTables(Constants.ANSWER_TABLE_NAME);
		String[] cols = new String[] {Constants.ANSWER_TABLE_NAME+"."+Constants.ANSWER_VALUE};
		String query = sqb.buildQuery(cols, null, null, null, null, null, null);
		
		try {
			
			FileWriter writer = new FileWriter(Constants.PATH_CSV);
			
			int qtdQuestions = Constants.LIST_QUESTIONS.size();
			
			List<Question> listaQuestoes = Constants.LIST_QUESTIONS;
			
			for(int i=0;i<qtdQuestions;i++){
				writer.append(listaQuestoes.get(i).getLabel());
				writer.append(',');
			}
			writer.append('\n');
			
			cursor=dbReadable.rawQuery(query, null);
			String resp;
			if(cursor != null) {

				int cont=1;
				while(cursor.moveToNext()) {
					
						if(cont==qtdQuestions){
							resp = cursor.getString(0);
							writer.append(resp);
							writer.append('\n');
							cont=1;
						}
						else
						{
							resp = cursor.getString(0);
                            resp = resp.replace(",","..");
							writer.append(resp);
							writer.append(',');
							cont++;
						}
						
				}
				
				writer.flush();
			    writer.close();
			    
			    
							
			}
		}
		catch(Exception e) {
			//TODO:
			Log.e(this.getClass().getName(), e.getMessage());
		}
		finally {
			if(cursor != null)
				cursor.close();
		}
	}


    public Object[] getAnswersGSTeste() {

        SQLiteQueryBuilder sqb = new SQLiteQueryBuilder();
        sqb.setTables(Constants.ANSWER_TABLE_NAME);
        String[] cols = new String[] {Constants.ANSWER_TABLE_NAME+"."+Constants.ANSWER_VALUE, Constants.ANSWER_TABLE_NAME+"."+Constants.ANSWER_TYPE};
        String query = sqb.buildQuery(cols, null, null, null, null, null, null);


		cursor=dbReadable.rawQuery(query, null);

		Object resp[] = new Object[cursor.getCount()];

        int qtdQuestions = cursor.getCount();
        try {

            if(cursor != null) {

                int i=1;
                String respAux,respFor;
                String tipo;
                while(cursor.moveToNext()) {
					if(i==qtdQuestions) {
                        respAux = cursor.getString(0);
                        tipo = cursor.getString(1);
                        if (respAux == "") {
                            respAux = "-";
                        }

                        if(tipo.equals(ComponentType.TEXT.getDescription())){
                            resp[i - 1] = respAux;
                        }
                        else {

                            String check = "{\"";
                            String checkFim = "\"}";
                            String checkmeio = "\":\"";
                            String aspas = "\"";
                            respAux = respAux.replace(check,"");
                            respAux = respAux.replace(checkFim,"");
                            respAux = respAux.replace(checkmeio,"");
                            respAux = respAux.replace(aspas,"");

							if (tipo.equals(ComponentType.RADIOBOX.getDescription())  || tipo.equals(ComponentType.COMBOBOX.getDescription())) {
								if(respAux.equals("")){
									resp[i-1]="";
								}else {

									try {
										respAux = respAux.substring(1, respAux.length());
										resp[i - 1] = respAux;
									} catch (Exception e) {
										resp[i - 1] = "";
									}
								}

							}

							if (tipo.equals(ComponentType.CHECKBOX.getDescription())) {
								if(respAux.equals("{}")){
									resp[i-1]="";
								}else {
									try {
										String respArray[] = respAux.split(",");
										for (int z = 0; z < respArray.length; z++) {
											respArray[z] = respArray[z].substring(1, respArray[z].length());
										}
										resp[i - 1] = respArray;
									} catch (Exception e) {
										resp[i - 1] = "";
									}
								}
							}
                        }

                        i = 1;
                    }
					else {
                        respAux = cursor.getString(0);
                        tipo = cursor.getString(1);
                        if (respAux == "") {
                            respAux = "-";
                        }

                        if(tipo.equals(ComponentType.TEXT.getDescription())){
                            resp[i - 1] = respAux;
                        }
                        else {
                            String check = "{\"";
                            String checkFim = "\"}";
                            String checkmeio = "\":\"";
                            String aspas = "\"";
                            respAux = respAux.replace(check,"");
                            respAux = respAux.replace(checkFim,"");
                            respAux = respAux.replace(checkmeio,"");
                            respAux = respAux.replace(aspas,"");

                            if (tipo.equals(ComponentType.RADIOBOX.getDescription())  || tipo.equals(ComponentType.COMBOBOX.getDescription())) {
								if(respAux.equals("")){
									resp[i-1]="";
								}else {

									try {
										respAux = respAux.substring(1, respAux.length());
										resp[i - 1] = respAux;
									} catch (Exception e) {
										resp[i - 1] = "";
									}
								}

                            }

                            if (tipo.equals(ComponentType.CHECKBOX.getDescription())) {
								if(respAux.equals("{}")){
									resp[i-1]="";
								}else {
									try {
										String respArray[] = respAux.split(",");
										for (int z = 0; z < respArray.length; z++) {
											respArray[z] = respArray[z].substring(1, respArray[z].length());
										}
										resp[i - 1] = respArray;
									} catch (Exception e) {
										resp[i - 1] = "";
									}
								}
                            }
                        }

                        i++;
					}
                }
                if(cursor!=null){
                    cursor.close();
                }

            }
        }
        catch(Exception e) {
            //TODO:
            Log.e(this.getClass().getName(), e.getMessage());
        }

		return resp;
    }


    
	public void deleteAnswers() {
		long res = dbWritable.delete(Constants.ANSWER_TABLE_NAME,null,null);
		Log.d("delete", "delete:answers-> " + res);
	}

	public void insertURL(String url){
		dbWritable.delete(Constants.URL_FORM, null, null);
		ContentValues cv = new ContentValues();
		cv.put("URL", url);
		dbWritable.insert(Constants.URL_FORM, null, cv);

	}

	public void insertNameForm(String name){
		dbWritable.delete(Constants.NAME_FORM, null, null);
		ContentValues cv = new ContentValues();
		cv.put("NAME", name);
		dbWritable.insert(Constants.NAME_FORM, null, cv);

	}

	public String loadURLForms(){
		String url;

		SQLiteQueryBuilder sqb = new SQLiteQueryBuilder();
		sqb.setTables(Constants.URL_FORM);
		String[] cols = new String[] {Constants.URL_FORM+"."+Constants.URL_FORM_URL};
		String query = sqb.buildQuery(cols, null, null, null, null, null, null);

		cursor=dbReadable.rawQuery(query, null);
			if (cursor.moveToFirst()) {
				url = cursor.getString(0);
				cursor.close();
				return url;
			} else return null;

	}

	public String loadNameForms(){
		String url;

		SQLiteQueryBuilder sqb = new SQLiteQueryBuilder();
		sqb.setTables(Constants.NAME_FORM);
		String[] cols = new String[] {Constants.NAME_FORM+"."+Constants.NAME_FORM_NAME};
		String query = sqb.buildQuery(cols, null, null, null, null, null, null);

		cursor=dbReadable.rawQuery(query, null);
		if (cursor.moveToFirst()) {
			url = cursor.getString(0);
			cursor.close();
			return url;
		} else return null;

	}
}
