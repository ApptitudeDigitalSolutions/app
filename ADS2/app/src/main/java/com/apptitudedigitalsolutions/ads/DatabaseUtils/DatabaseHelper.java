package com.apptitudedigitalsolutions.ads.DatabaseUtils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.apptitudedigitalsolutions.ads.Application.ADSApplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Elliot on 21/10/2016.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DB_NAME= "ads1.db";
    public static final String TABLE_NAME= "user_crednetial.db";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS user_crednetials (id INTEGER , username TEXT , name TEXT ,passcode TEXT, company_id TEXT, endpoint TEXT);");
        db.execSQL("CREATE TABLE IF NOT EXISTS ac_audio (id INTEGER PRIMARY KEY AUTOINCREMENT, candidate_id INT , question_id INT ,ac_id INT, filename TEXT, activity_type TEXT, length INT, sent INT , deleted INT);");
        Log.e("ME","DB created ");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("CREATE TABLE IF NOT EXISTS user_crednetials (id INTEGER , username TEXT , name TEXT ,passcode TEXT, company_id TEXT, endpoint TEXT);");
        db.execSQL("CREATE TABLE IF NOT EXISTS ac_audio (id INTEGER PRIMARY KEY AUTOINCREMENT, candidate_id INT , question_id INT ,ac_id INT, filename TEXT, activity_type TEXT ,length INT, sent INT , deleted INT);");
    }

    public boolean insertuserData(String username, String name, String passcode, String company_id, String endpoint){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("id",1);
        cv.put("username",username);
        cv.put("name", name);
        cv.put("passcode", passcode);
        cv.put("company_id", company_id);
        cv.put("endpoint", endpoint);
        long result = db.insert("user_crednetials",null,cv);
        if(result == 1){
            return true;
        }else{
            return false;
        }
        // reset references to these properites in

    }


    public boolean insertNewAudio(int candidate_id, int question_id, int ac_id, String filename, String activity_type ,int length , int sent, int deleted){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("candidate_id",candidate_id);
        cv.put("question_id", question_id);
        cv.put("ac_id", ac_id);
        cv.put("filename", filename);
        cv.put("activity_type", activity_type);
        cv.put("length", length);
        cv.put("sent", sent);
        cv.put("deleted", deleted);
        long result = db.insert("ac_audio",null,cv);
        if(result == 1){
            return true;
        }else{
            return false;
        }
    }

    public JSONArray getAllAudioFileReferences(String candidateid,String activity_type){
        JSONArray results = new JSONArray();
        SQLiteDatabase db = this.getWritableDatabase();
        String queryd = "SELECT * FROM ac_audio WHERE candidate_id ="+ candidateid +" AND activity_type = \'"+activity_type+"\';";
        Cursor res = db.rawQuery(queryd,null);

        StringBuffer buffer =  new StringBuffer();
        while(res.moveToNext()){
            JSONObject audioBlob = new JSONObject();

            String candidate_id = String.valueOf(buffer.append(res.getString(1)));
            buffer = new StringBuffer();
            String question_id = String.valueOf(buffer.append(res.getString(2)));
            buffer = new StringBuffer();
            String ac_id = String.valueOf(buffer.append(res.getString(3)));
            buffer = new StringBuffer();
            String filename = String.valueOf(buffer.append(res.getString(4)));
            buffer = new StringBuffer();
            String length = String.valueOf(buffer.append(res.getString(5)));
            buffer = new StringBuffer();
            String sent = String.valueOf(buffer.append(res.getString(6)));
            buffer = new StringBuffer();
            String deleted = String.valueOf(buffer.append(res.getString(7)));

            try {
                audioBlob.put("candidate_id",candidate_id);
                audioBlob.put("question_id",question_id);
                audioBlob.put("ac_id",ac_id);
                audioBlob.put("filename",filename);
                audioBlob.put("length",length);
                audioBlob.put("sent",sent);
                audioBlob.put("deleted",deleted);

                results.put(audioBlob);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        Log.e("ADS>", String.valueOf(results));
        return results;

    }



    public boolean nukeUserData(){

        SQLiteDatabase db = this.getWritableDatabase();
//        db.execSQL("DROP TABLE user_crednetials;");
//        db.execSQL("CREATE TABLE IF NOT EXISTS user_crednetials (id INTEGER, username TEXT , name TEXT ,passcode TEXT, company_id TEXT, endpoint TEXT);");
        db.execSQL("UPDATE user_crednetials SET id = 1, username = \"\" , name = \"\"  ,passcode = \"\" , company_id = \"\" , endpoint = \"\");");

        return true;
//        String id = "1";
//        ContentValues cv = new ContentValues();
//        cv.put("username","");
//        cv.put("name", "");
//        cv.put("passcode", "");
//        cv.put("company_id", "");
//        long result = db.update("user_crednetials",cv,"id = ?",new String[]{id});
//        if(result == 1){
//            return true;
//        }else{
//            return false;
//        }
    }

    public ArrayList<String> getUsernamePasscodeEndpointArray(){
        ArrayList<String> results = new ArrayList<String>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM user_crednetials;",null);
        StringBuffer buffer =  new StringBuffer();
        while(res.moveToNext()){

            String username = String.valueOf(buffer.append(res.getString(1)));
            buffer = new StringBuffer();
            String name = String.valueOf(buffer.append(res.getString(2)));
            buffer = new StringBuffer();
            String passcode = String.valueOf(buffer.append(res.getString(3)));
            buffer = new StringBuffer();
            String company_id = String.valueOf(buffer.append(res.getString(4)));
            buffer = new StringBuffer();
            String endpoint = String.valueOf(buffer.append(res.getString(5)));


            results.add(endpoint);
            results.add(company_id);
            results.add(passcode);
            results.add(name);
            results.add(username);
            break;
        }
        return results;

    }

}
