package com.kashifirshad.softwareprojects;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by biome on 3/17/2018.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    // Logcat tag
    private static final String LOG = "DatabaseHelper";

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "ProjectsDB.db";

    // Table Names
    private static final String TABLE_USER = "Users";

    private static final String TABLE_PROJECT = "Projects";


    private static final String CREATE_TABLE_USER = "CREATE TABLE IF NOT EXISTS " + TABLE_USER + " ( " +
            "Id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "FirstName TEXT, " +
            "MiddleName TEXT, " +
            "LastName TEXT, " +
            "EmailAddress TEXT NOT NULL, " +
            "SkypeId TEXT, " +
            "WatsAppNo TEXT, " +
            "AddressLine1 TEXT, " +
            "AddressLine2 TEXT, " +
            "City TEXT, " +
            "State TEXT, " +
            "Country TEXT, " +
            "UnReadOnly INTEGER, " +
            "SyncDuration INTEGER, " +
            "CreatedAt DATETIME, " +
            "UpdatedAt DATETIME, " +
            "IsSynched INTEGER, " +
            "ServerId INTEGER," +
            "Password TEXT, " +
            "IsEmailVerified INTEGER, " +
            "IsLoggedIn INTEGER, " +
            "CONSTRAINT uq_email UNIQUE (EmailAddress) "+
            ")";

    private static final String CREATE_TABLE_PROJECT = "CREATE TABLE IF NOT EXISTS " + TABLE_PROJECT + " ( " +
            "Id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "Story TEXT, " +
            "FilePaths TEXT,"+
            "EstimatedHrs TEXT, " +
            "EstimateCost TEXT, " +
            "DeliveryDate DATETIME, " +
            "CreatedAt DATETIME, " +
            "UpdatedAt DATETIME, " +
            "UserId INTEGER, " +
            "ServerUserId INTEGER, " +
            "IsSynched INTEGER, " +
            "IsRead INTEGER, " +
            "ParentId INTEGER, " +
            "ServerId INTEGER, " +
            "ServerParentId INTEGER" +
            ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // creating required tables
        db.execSQL(CREATE_TABLE_USER);
        db.execSQL(CREATE_TABLE_PROJECT);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROJECT);
        // create new tables
        onCreate(db);
    }

    public void emptyDB(){
        SQLiteDatabase db = this.getWritableDatabase();
        onUpgrade(db,0,0);
    }

    /*
    * Creating a Project
    */
    public long createProject(Project proj) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("Story", proj.getStory());
        values.put("FilePaths", proj.getFilePaths());
        values.put("EstimatedHrs", proj.getEstimatedHrs());
        values.put("EstimateCost", proj.getEstimateCost());
        values.put("DeliveryDate", proj.getDeliveryDate());
        values.put("CreatedAt", proj.getCreatedAt());
        values.put("UpdatedAt", proj.getUpdatedAt());
        values.put("UserId", proj.getUser().getId());
        values.put("IsSynched", proj.getIsSynched());
        values.put("ParentId", proj.getParentId());
        values.put("ServerId", proj.getServerId());
        values.put("IsRead", proj.getIsRead());

        // insert row
        long id = db.insert(TABLE_PROJECT, null, values);

        return id;
    }


    /*
    * get single Project
    */
    public Project getProject(long id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_PROJECT +
                " WHERE Id   = " + id;

        Cursor c = db.rawQuery(selectQuery, null);
        Project td = new Project();
        if (c != null && c.moveToFirst()) {


            td.setId(c.getInt(c.getColumnIndex("Id")));
            td.setStory(c.getString(c.getColumnIndex("Story")));
            td.setFilePaths(c.getString(c.getColumnIndex("FilePaths")));
            td.setEstimatedHrs(c.getString(c.getColumnIndex("EstimatedHrs")));
            td.setEstimateCost(c.getString(c.getColumnIndex("EstimateCost")));
            td.setDeliveryDate(c.getString(c.getColumnIndex("DeliveryDate")));
            td.setCreatedAt(c.getString(c.getColumnIndex("CreatedAt")));
            td.setUpdatedAt(c.getString(c.getColumnIndex("UpdatedAt")));
            td.setUserId(c.getInt(c.getColumnIndex("UserId")));
            td.setIsSynched(c.getInt(c.getColumnIndex("IsSynched")));
            td.setParentId(c.getInt(c.getColumnIndex("ParentId")));
            td.setServerId(c.getInt(c.getColumnIndex("ServerId")));
            td.setIsRead(c.getInt(c.getColumnIndex("IsRead")));
        }
        return td;
    }


    public Project getProject(long id, long serverId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery;
        if(id != 0)
            selectQuery = "SELECT  * FROM " + TABLE_PROJECT +
                " WHERE Id   = " + id;
        else
            selectQuery = "SELECT  * FROM " + TABLE_PROJECT +
                    " WHERE ServerId   = " + serverId;

        Cursor c = db.rawQuery(selectQuery, null);
        Project td = new Project();

        if (c != null && c.moveToFirst()) {
            td.setId(c.getInt(c.getColumnIndex("Id")));
            td.setStory(c.getString(c.getColumnIndex("Story")));
            td.setFilePaths(c.getString(c.getColumnIndex("FilePaths")));
            td.setEstimatedHrs(c.getString(c.getColumnIndex("EstimatedHrs")));
            td.setEstimateCost(c.getString(c.getColumnIndex("EstimateCost")));
            td.setDeliveryDate(c.getString(c.getColumnIndex("DeliveryDate")));
            td.setCreatedAt(c.getString(c.getColumnIndex("CreatedAt")));
            td.setUpdatedAt(c.getString(c.getColumnIndex("UpdatedAt")));
            td.setUserId(c.getInt(c.getColumnIndex("UserId")));
            td.setIsSynched(c.getInt(c.getColumnIndex("IsSynched")));
            td.setParentId(c.getInt(c.getColumnIndex("ParentId")));
            td.setServerId(c.getInt(c.getColumnIndex("ServerId")));
            td.setIsRead(c.getInt(c.getColumnIndex("IsRead")));
        }
        return td;
    }

    /*
 * getting all Projects
 * */
    public List<Project> getAllProjects(long userId, int synched, long parentId) {
        List<Project> projects = new ArrayList<Project>();
        String selectQuery = "SELECT  * FROM " + TABLE_PROJECT
                + " WHERE "
                // +"UserId = " + Long.toString(userId)+ " AND +
                + "IsSynched = "+ Integer.toString(synched)
                + " AND ParentId = " + Long.toString(parentId);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c != null)
        if (c.moveToFirst()) {
            do {
                Project td = new Project();
                td.setId(c.getInt(c.getColumnIndex("Id")));
                td.setStory(c.getString(c.getColumnIndex("Story")));
                td.setFilePaths(c.getString(c.getColumnIndex("FilePaths")));
                td.setEstimatedHrs(c.getString(c.getColumnIndex("EstimatedHrs")));
                td.setEstimateCost(c.getString(c.getColumnIndex("EstimateCost")));
                td.setDeliveryDate(c.getString(c.getColumnIndex("DeliveryDate")));
                td.setCreatedAt(c.getString(c.getColumnIndex("CreatedAt")));
                td.setUpdatedAt(c.getString(c.getColumnIndex("UpdatedAt")));
                td.setUserId(c.getInt(c.getColumnIndex("UserId")));
                td.setIsSynched(c.getInt(c.getColumnIndex("IsSynched")));
                td.setParentId(c.getInt(c.getColumnIndex("ParentId")));
                td.setServerId(c.getInt(c.getColumnIndex("ServerId")));
                if(parentId == 0){
                    td.setChildProjects(getAllProjects(userId, synched, c.getInt(c.getColumnIndex("Id")) ));
                }
                // adding to todo list
                projects.add(td);
            } while (c.moveToNext());
        }
        return projects;
    }

    public String getLasUpdatedtProject() {

        String selectQuery = "SELECT IFNULL(MAX(UpdatedAt),'0000-00-00 00:00:00') as LastUpdatedAt FROM "
                + TABLE_PROJECT
                ;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c != null)
            if (c.moveToFirst()) {
                    return  c.getString(c.getColumnIndex("LastUpdatedAt"));
            }
            return "0000-00-00 00:00:00";
    }


    public String getLastUpdatedUser() {

        String selectQuery = "SELECT  IFNULL(MAX(UpdatedAt),'0000-00-00 00:00:00') as LastUpdatedAt FROM "
                + TABLE_USER
                ;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c != null)
            if (c.moveToFirst()) {
                return  c.getString(c.getColumnIndex("LastUpdatedAt"));
            }
        return "0000-00-00 00:00:00";
    }


    /*
 * getting all Projects
 * */
    public List<Project> getChildProjects(long id) {
        List<Project> projects = new ArrayList<Project>();
        String selectQuery = "SELECT  * FROM " + TABLE_PROJECT + " WHERE ParentId = " + Long.toString(id) + " Order By Id";


        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c != null)
            if (c.moveToFirst()) {
                do {
                    Project td = new Project();
                    td.setId(c.getInt(c.getColumnIndex("Id")));
                    td.setStory(c.getString(c.getColumnIndex("Story")));
                    td.setFilePaths(c.getString(c.getColumnIndex("FilePaths")));
                    td.setEstimatedHrs(c.getString(c.getColumnIndex("EstimatedHrs")));
                    td.setEstimateCost(c.getString(c.getColumnIndex("EstimateCost")));
                    td.setDeliveryDate(c.getString(c.getColumnIndex("DeliveryDate")));
                    td.setCreatedAt(c.getString(c.getColumnIndex("CreatedAt")));
                    td.setUpdatedAt(c.getString(c.getColumnIndex("UpdatedAt")));
                    td.setUserId(c.getInt(c.getColumnIndex("UserId")));
                    td.setIsSynched(c.getInt(c.getColumnIndex("IsSynched")));
                    td.setParentId(c.getInt(c.getColumnIndex("ParentId")));
                    td.setServerId(c.getInt(c.getColumnIndex("ServerId")));
                    td.setUser(getUser(c.getInt(c.getColumnIndex("UserId"))));
                    td.setIsRead(c.getInt(c.getColumnIndex("IsRead")));
                    // adding to todo list
                    projects.add(td);
                } while (c.moveToNext());
            }
        return projects;
    }


    public int getOtherProjectCount(String Story, int Id){
        int count = 0;
        String selectQuery = "SELECT COUNT(*) AS CountProject FROM " + TABLE_PROJECT + " WHERE Story = '" + Story.replace("'","\'") +"'"
                +" AND Id != " + Integer.toString(Id);

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null && c.moveToFirst())
        {

            count = (c.getInt(c.getColumnIndex("CountProject")));
        }
        return count;
    }

    /*
 * Updating a Project
 */
    public int updateProject(Project proj) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("Story", proj.getStory());
        values.put("FilePaths", proj.getFilePaths());
        values.put("EstimatedHrs", proj.getEstimatedHrs());
        values.put("EstimateCost", proj.getEstimateCost());
        values.put("DeliveryDate", proj.getDeliveryDate());
        values.put("CreatedAt", proj.getCreatedAt());
        values.put("UpdatedAt", proj.getUpdatedAt());
        values.put("UserId", proj.getUserId());
        values.put("IsSynched", proj.getIsSynched());
        values.put("ParentId", proj.getParentId());
        values.put("ServerId", proj.getServerId());
        values.put("IsRead", proj.getIsRead());

        // updating row
        return db.update(TABLE_PROJECT, values,   " Id = ?",
                new String[] { String.valueOf(proj.getId()) });
    }



    /*
 * Deleting a Project
 */
    public void deleteProjecto(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PROJECT,   " Id = ?",
                new String[] { String.valueOf(id) });
    }

 /* ************** User Funtions ******************** */
 public User getUser(String email){
     User usr = new User();
     String selectQuery = "SELECT * FROM " + TABLE_USER + " WHERE EmailAddress = '" + email.replace("'","\'") +"'";
     SQLiteDatabase db = this.getReadableDatabase();
     Cursor c = db.rawQuery(selectQuery, null);
     if (c != null && c.moveToFirst())
     {

         usr.setId(c.getLong(c.getColumnIndex("Id")));
         usr.setFirstName(c.getString(c.getColumnIndex("FirstName")));
         usr.setLastName(c.getString(c.getColumnIndex("LastName")));
         usr.setMiddleName(c.getString(c.getColumnIndex("MiddleName")));
         usr.setEmailAddress(c.getString(c.getColumnIndex("EmailAddress")));
         usr.setSkypeId(c.getString(c.getColumnIndex("SkypeId")));
         usr.setWatsAppNo(c.getString(c.getColumnIndex("WatsAppNo")));
         usr.setAddressLine1(c.getString(c.getColumnIndex("AddressLine1")));
         usr.setAddressLine2(c.getString(c.getColumnIndex("AddressLine2")));
         usr.setCity(c.getString(c.getColumnIndex("City")));
         usr.setState(c.getString(c.getColumnIndex("State")));
         usr.setCountry(c.getString(c.getColumnIndex("Country")));
         usr.setShowUnreadStoriesOnly(c.getInt(c.getColumnIndex("UnReadOnly")));
         usr.setSyncDuration(c.getInt(c.getColumnIndex("SyncDuration")));
         usr.setCreatedAt(c.getString(c.getColumnIndex("CreatedAt")));
         usr.setUpdatedAt(c.getString(c.getColumnIndex("UpdatedAt")));
         usr.setSynched(c.getInt(c.getColumnIndex("IsSynched")));
         usr.setServerId(c.getInt(c.getColumnIndex("ServerId")));
     }
     return usr;
 }



    public User getLoggedInUser(){
        User usr = new User();
        String selectQuery = "SELECT * FROM " + TABLE_USER + " WHERE IsLoggedIn = 1 LIMIT 1 ";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        if (c != null && c.moveToFirst())
        {
            usr.setId(c.getLong(c.getColumnIndex("Id")));
            usr.setFirstName(c.getString(c.getColumnIndex("FirstName")));
            usr.setLastName(c.getString(c.getColumnIndex("LastName")));
            usr.setMiddleName(c.getString(c.getColumnIndex("MiddleName")));
            usr.setEmailAddress(c.getString(c.getColumnIndex("EmailAddress")));
            usr.setSkypeId(c.getString(c.getColumnIndex("SkypeId")));
            usr.setWatsAppNo(c.getString(c.getColumnIndex("WatsAppNo")));
            usr.setAddressLine1(c.getString(c.getColumnIndex("AddressLine1")));
            usr.setAddressLine2(c.getString(c.getColumnIndex("AddressLine2")));
            usr.setCity(c.getString(c.getColumnIndex("City")));
            usr.setState(c.getString(c.getColumnIndex("State")));
            usr.setCountry(c.getString(c.getColumnIndex("Country")));
            usr.setShowUnreadStoriesOnly(c.getInt(c.getColumnIndex("UnReadOnly")));
            usr.setSyncDuration(c.getInt(c.getColumnIndex("SyncDuration")));
            usr.setCreatedAt(c.getString(c.getColumnIndex("CreatedAt")));
            usr.setUpdatedAt(c.getString(c.getColumnIndex("UpdatedAt")));
            usr.setSynched(c.getInt(c.getColumnIndex("IsSynched")));
            usr.setServerId(c.getInt(c.getColumnIndex("ServerId")));
        }
        return usr;
    }

    public User getUser(long id){
        User usr = new User();
        String selectQuery = "SELECT * FROM " + TABLE_USER + " WHERE Id = '" + Long.toString(id) +"'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        if (c != null && c.moveToFirst())
        {

            usr.setId(c.getInt(c.getColumnIndex("Id")));
            usr.setFirstName(c.getString(c.getColumnIndex("FirstName")));
            usr.setLastName(c.getString(c.getColumnIndex("LastName")));
            usr.setMiddleName(c.getString(c.getColumnIndex("MiddleName")));
            usr.setEmailAddress(c.getString(c.getColumnIndex("EmailAddress")));
            usr.setSkypeId(c.getString(c.getColumnIndex("SkypeId")));
            usr.setWatsAppNo(c.getString(c.getColumnIndex("WatsAppNo")));
            usr.setAddressLine1(c.getString(c.getColumnIndex("AddressLine1")));
            usr.setAddressLine2(c.getString(c.getColumnIndex("AddressLine2")));
            usr.setCity(c.getString(c.getColumnIndex("City")));
            usr.setState(c.getString(c.getColumnIndex("State")));
            usr.setCountry(c.getString(c.getColumnIndex("Country")));
            usr.setShowUnreadStoriesOnly(c.getInt(c.getColumnIndex("UnReadOnly")));
            usr.setSyncDuration(c.getInt(c.getColumnIndex("SyncDuration")));
            usr.setCreatedAt(c.getString(c.getColumnIndex("CreatedAt")));
            usr.setUpdatedAt(c.getString(c.getColumnIndex("UpdatedAt")));
            usr.setSynched(c.getInt(c.getColumnIndex("IsSynched")));
            usr.setServerId(c.getInt(c.getColumnIndex("ServerId")));
        }
        return usr;
    }

    public User getUser(long id, long serverId){
        User usr = new User();
        String selectQuery = "";
        if(id != 0)
            selectQuery = "SELECT * FROM " + TABLE_USER + " WHERE Id = '" + Long.toString(id) +"'";
        else if(serverId != 0)
            selectQuery = "SELECT * FROM " + TABLE_USER + " WHERE ServerId = '" + Long.toString(serverId) +"'";
Log.e("UserQuery***",selectQuery);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        if (c != null && c.moveToFirst())
        {

            usr.setId(c.getInt(c.getColumnIndex("Id")));
            usr.setFirstName(c.getString(c.getColumnIndex("FirstName")));
            usr.setLastName(c.getString(c.getColumnIndex("LastName")));
            usr.setMiddleName(c.getString(c.getColumnIndex("MiddleName")));
            usr.setEmailAddress(c.getString(c.getColumnIndex("EmailAddress")));
            usr.setSkypeId(c.getString(c.getColumnIndex("SkypeId")));
            usr.setWatsAppNo(c.getString(c.getColumnIndex("WatsAppNo")));
            usr.setAddressLine1(c.getString(c.getColumnIndex("AddressLine1")));
            usr.setAddressLine2(c.getString(c.getColumnIndex("AddressLine2")));
            usr.setCity(c.getString(c.getColumnIndex("City")));
            usr.setState(c.getString(c.getColumnIndex("State")));
            usr.setCountry(c.getString(c.getColumnIndex("Country")));
            usr.setShowUnreadStoriesOnly(c.getInt(c.getColumnIndex("UnReadOnly")));
            usr.setSyncDuration(c.getInt(c.getColumnIndex("SyncDuration")));
            usr.setCreatedAt(c.getString(c.getColumnIndex("CreatedAt")));
            usr.setUpdatedAt(c.getString(c.getColumnIndex("UpdatedAt")));
            usr.setSynched(c.getInt(c.getColumnIndex("IsSynched")));
            usr.setServerId(c.getInt(c.getColumnIndex("ServerId")));
        }
        return usr;
    }


    public long createUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("Id", user.getId());
        values.put("FirstName", user.getFirstName());
        values.put("MiddleName", user.getMiddleName());
        values.put("LastName", user.getLastName());
        values.put("EmailAddress", user.getEmailAddress());
        values.put("SkypeId", user.getSkypeId());
        values.put("WatsAppNo", user.WatsAppNo);
        values.put("AddressLine1", user.getAddressLine1());
        values.put("AddressLine2", user.getAddressLine2());
        values.put("City", user.getCity());
        values.put("State", user.getState());
        values.put("Country", user.getCountry());
        values.put("UnReadOnly", user.getShowUnreadStoriesOnly());
        values.put("SyncDuration", user.getSyncDuration());
        values.put("CreatedAt", user.getCreatedAt());
        values.put("UpdatedAt", user.getUpdatedAt());
        values.put("IsSynched", user.getSynched());
        values.put("ServerId", user.getServerId());
        values.put("IsLoggedIn", user.getIsLoggedIn());

        // insert row
        long id = db.insert(TABLE_USER, null, values);
        return id;
    }

    public long updateUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("FirstName", user.getFirstName());
        values.put("MiddleName", user.getMiddleName());
        values.put("LastName", user.getLastName());
        values.put("EmailAddress", user.getEmailAddress());
        values.put("SkypeId", user.getSkypeId());
        values.put("WatsAppNo", user.WatsAppNo);
        values.put("AddressLine1", user.getAddressLine1());
        values.put("AddressLine2", user.getAddressLine2());
        values.put("City", user.getCity());
        values.put("State", user.getState());
        values.put("Country", user.getCountry());
        values.put("UnReadOnly", user.getShowUnreadStoriesOnly());
        values.put("SyncDuration", user.getSyncDuration());
        values.put("UpdatedAt", user.getUpdatedAt());
        values.put("IsSynched", user.getSynched());
        values.put("ServerId", user.getServerId());
        values.put("IsLoggedIn", user.getIsLoggedIn());


        // insert row
        return db.update(TABLE_USER, values,   " Id = ?",
                new String[] { String.valueOf(user.getId()) });
    }



    // closing database
    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }
}
