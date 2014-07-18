package rinkimai.pro;


import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;


public class SQLiteCommandCenter extends SQLiteOpenHelper
{
	private static SQLiteDatabase db;
	private static final String timestamp = "time_stamp";
	private static Cursor cur;
	
	public static void ijungtiAutentifikacija()
	{
		ContentValues eilutesReiksmes = new ContentValues(1);
		eilutesReiksmes.put("user_id", VartotojoDuomenys.getUser_id());
		
		db.update("vartotojai",eilutesReiksmes, null, null);
	}
	
	public static void naujiVartotojoDuomenys()
	{
		ContentValues eilutesReiksmes = new ContentValues(6);
		
        eilutesReiksmes.put("email",VartotojoDuomenys.getEmail());
        eilutesReiksmes.put("password", VartotojoDuomenys.getPassword());
        eilutesReiksmes.put("name", VartotojoDuomenys.getName());
        eilutesReiksmes.put("sure_name", VartotojoDuomenys.getSurename());
        eilutesReiksmes.put("asm_kod", VartotojoDuomenys.getAsm_kod());
        eilutesReiksmes.put("bil_nr", VartotojoDuomenys.getBil_nr());
        
        db.update("vartotojai", eilutesReiksmes, null, null);
	}
	// jei uzsiregistruoj vartotojas duomenis iskart perkalia i sqlite
	public static void registerDataToSql()
	{
		ContentValues eilutesReiksmes = new ContentValues(4);
        eilutesReiksmes.put("email",VartotojoDuomenys.getEmail());
        eilutesReiksmes.put("password", VartotojoDuomenys.getPassword());
        eilutesReiksmes.put("name", VartotojoDuomenys.getName());
        eilutesReiksmes.put("surename", VartotojoDuomenys.getSurename());
        
        db.insert("vartotojai", null, eilutesReiksmes);  
	}
	// jei vartotojas uzregistruotas anksiciau ir ivede tik usename ir password, 
	// tai tik situos duomenis iskart iraso i sqlite 
	public static void loginDataToSql()
	{
        ContentValues eilutesReiksmes = new ContentValues(2);
        eilutesReiksmes.put("email",VartotojoDuomenys.getEmail());
        eilutesReiksmes.put("password", VartotojoDuomenys.getPassword());
        
        db.insert("vartotojai", null, eilutesReiksmes);  
	}
	// perduoda informacija is vidiens duomenu bazes i duomenis talpinanit objiekta
	public static void loadSqliteUserData()
	{
		cur = db.query("vartotojai", null, null, null, null, null, null);
		
		if(cur.getCount() > 0)
		{
			cur.moveToFirst();
			VartotojoDuomenys.dataFromSqlite(/*cur.getString(cur.getColumnIndex("time_stamp")),*/ cur.getString(cur.getColumnIndex("email")),
					cur.getString(cur.getColumnIndex("password")), cur.getString(cur.getColumnIndex("name")),
					cur.getString(cur.getColumnIndex("sure_name")), cur.getString(cur.getColumnIndex("user_id")),
					cur.getString(cur.getColumnIndex("asm_kod")), cur.getString(cur.getColumnIndex("bil_nr")));
		}
	}
	// offline mertodas
	public static ArrayList<HashMap<String, String>> getTableBalsavimai()
	{
		
		ArrayList<HashMap<String, String>> grazinimas = new ArrayList<HashMap<String, String>>();
		cur = db.query("balsavimai", null, (String)null, (String[])null, (String)null, (String)null, (String)null);
		int ilgis = cur.getCount();
		for(int i = 0 ; i < ilgis ; i++)
		{
			if(ilgis > 0)
			{
				cur.moveToNext();
				
				HashMap<String, String> map = new HashMap<String, String>();
	        
				map.put("id", cur.getString(0));
				map.put("pavadinimas", cur.getString(1));
				map.put("ikeltas", cur.getString(2));
				map.put("pabaiga", cur.getString(3));
				
				grazinimas.add(map);
			}
		}
		return grazinimas;
	}
	// offline metodas
	public static ArrayList<HashMap<String, String>> getTableVariantai()
	{
		
		ArrayList<HashMap<String, String>> grazinimas = new ArrayList<HashMap<String, String>>();
		cur = db.query("variantai", null, (String)null, (String[])null, (String)null, (String)null, (String)null);
		int ilgis = cur.getCount();
		for(int i = 0 ; i < ilgis ; i++)
		{
			if(ilgis > 0)
			{
				cur.moveToNext();
				
				HashMap<String, String> map = new HashMap<String, String>();
	        
				map.put("id", cur.getString(0));
				map.put("var_id", cur.getString(1));
				map.put("info", cur.getString(2));
				map.put("variantas", cur.getString(3));
				grazinimas.add(map);
			}
		}
		return grazinimas;
	}
	// offline metodas
	public static HashMap<String,String> metodasArturui(Context context)
	{
		cur = db.query("balsai", null, null, null, null, null, null);
		int ilgis = cur.getCount();
		HashMap<String, String> result = new HashMap<String, String>();
		String[] pavadinimas = {"pavadinimas"};
		String[] variantas = {"variantas"};
		Cursor tmp;
		Cursor tmp2;
        for(int i = 0 ; i < ilgis ; i++)
        {
        	if(ilgis > 0 && db.query("balsavimai", null, null, null, null, null, null).getCount() > 0 && db.query("variantai", null, null, null, null, null, null).getCount() > 0)
        	{
        	cur.moveToNext();
        	tmp = db.query("balsavimai", pavadinimas, "id = "+cur.getString(1), null, null, null, null);
        	tmp.moveToNext();
        	tmp2 = db.query("variantai", variantas, "var_id = "+cur.getString(2), null, null, null, null);
        	tmp2.moveToNext();
        	
        	result.put(tmp.getString(0), tmp2.getString(0));
        	}
        }
		return result;
	}
	
	public static void vidinisBalsavimas(String id, String varId,Context context)
	{
        ContentValues eilutesReiksmes = new ContentValues(3);
        eilutesReiksmes.put("vart_id",VartotojoDuomenys.getUser_id());
        eilutesReiksmes.put("bals_id", id);
        eilutesReiksmes.put("ats_id",varId);
        db.insert("balsai", null, eilutesReiksmes);   
		
//        cur = db.query("balsai", null, (String)null, (String[])null, (String)null, (String)null, (String)null);
//        cur.moveToNext();
	}
	
	private static boolean reikiaPakeitimu(String lentele, JSONObject json)
	{
		try {
			JSONArray isWebo = json.getJSONArray("posts");
			String[] ts = {timestamp};
			Cursor sqliteData = db.query(lentele,ts , null, null, null, null, timestamp);

			// jei vidineje lenteleje duomenu yra
			if(sqliteData.getCount() > 0)
			{
				// tikrina per visa json duomenu masyva ar time stampas yra didesnis nei vidinej lentelej
				for (int i = 0; i < isWebo.length(); i++) {
					sqliteData.moveToNext();
					JSONObject jsonDataRow = isWebo.getJSONObject(i);
					// jei time stampas yra didesnis nei vidinej lentelej
					if( DateHelp.isFirstDateBigger(jsonDataRow.getString(timestamp),sqliteData.getString(0))){
						// reiskias reika atnaujint vidine lentele
						return true;
					}
				}
			}
			// jei vidine lentele tuscia atnaujinimu reikia
			else {
				return true;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		// jei visi patikrinimai sako kad atnaujinimu nereikia...
		return false;
	}
	
	public static void variantaiJsonToSqlite(JSONObject json) throws JSONException
	{
		String lentele = "variantai";
		// isema is json failo duomenu masyva
			JSONArray isWebo = json.getJSONArray("posts");
			if(reikiaPakeitimu(lentele,json))
			{
				db.delete(lentele, null, null);
				for (int i = 0; i < isWebo.length(); i++) {
					JSONObject eilut = isWebo.getJSONObject(i);

	                ContentValues eilutesReiksmes = new ContentValues(5);
	                
	                eilutesReiksmes.put("id", eilut.getInt("id"));
	                eilutesReiksmes.put("var_id", eilut.getInt("var_id"));
	                eilutesReiksmes.put("info",eilut.getString("info"));
	                eilutesReiksmes.put("variantas", eilut.getString("variantas"));
	                eilutesReiksmes.put(timestamp, eilut.getString(timestamp));
	                db.insert(lentele, null, eilutesReiksmes);   
				}
			}
	}

	public static void balsavimaiJsonToSqlite(JSONObject json) throws JSONException
	{
		String lentele = "balsavimai";
			JSONArray isWebo = json.getJSONArray("posts");

			if(reikiaPakeitimu(lentele,json))
			{
				
			db.delete(lentele, null, null);
			for (int i = 0; i < isWebo.length(); i++) {
				JSONObject eilut = isWebo.getJSONObject(i);

                ContentValues eilutesReiksmes = new ContentValues(5);
                
                eilutesReiksmes.put("id", eilut.getInt("id"));
                eilutesReiksmes.put("pavadinimas",eilut.getString("pavadinimas"));
                eilutesReiksmes.put("ikeltas", eilut.getString("ikeltas"));
                eilutesReiksmes.put("pabaiga", eilut.getString("pabaiga"));
                eilutesReiksmes.put(timestamp, eilut.getString(timestamp));
                db.insert(lentele, null, eilutesReiksmes);   
				}
			}
	}
	
	public static void balsaiJsonToSqlite(JSONObject json) throws JSONException
	{
		String lentele = "balsai";
			JSONArray isWebo = json.getJSONArray("posts");

			if(reikiaPakeitimu(lentele,json))
			{
				
			db.delete(lentele, null, null);
			for (int i = 0; i < isWebo.length(); i++) {
				JSONObject eilut = isWebo.getJSONObject(i);

                ContentValues eilutesReiksmes = new ContentValues(4);
                
                eilutesReiksmes.put("vart_id", eilut.getString("vart_id"));
                eilutesReiksmes.put("bals_id",eilut.getString("bals_id"));
                eilutesReiksmes.put("ats_id", eilut.getString("ats_id"));
                eilutesReiksmes.put(timestamp, eilut.getString(timestamp));
                db.insert(lentele, null, eilutesReiksmes);   
				}
			}
	}
	
	
	public static SQLiteDatabase initiateDb(Context context)
	{
		db = new SQLiteCommandCenter(context).getReadableDatabase();
		createTables(db);
		return db;
	}
	// duomenu baze reikia uzdaryt uzsidarant aplikacijai, del to kad meta errorus
	public static void kill()
	{
		db.close();
	}
	//konstruktorius tam kad DatabaseHelperis grazintu duomenu baze, kitaip neiseina
	public SQLiteCommandCenter(Context context) {
		super(context, "Mmusu.db", null, 1);
//		this.context = context;
	}
	
	// kuria lenteles jei neegzistuoja
	private static void createTables(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE IF NOT EXISTS balsai ("
				+ "vart_id text NOT NULL,"
				+ "bals_id int(11) NOT NULL,"
				+ "ats_id int(11) NOT NULL,"
				+ "time_stamp DATETIME NOT NULL);");
		
		db.execSQL("CREATE TABLE IF NOT EXISTS balsavimai ("
				+ "id int(11),"
				+ "pavadinimas text NOT NULL,"
				+ "ikeltas datetime NOT NULL,"
				+ "pabaiga datetime NOT NULL,"
				+ "time_stamp DATETIME NOT NULL) ; ");
		
		db.execSQL("CREATE TABLE IF NOT EXISTS variantai ("
				+ "id int(11) NOT NULL, "
				+ "var_id int(11),"
				+ "info text NOT NULL,  "
				+ "variantas text NOT NULL,"
				+ "time_stamp DATETIME NOT NULL); ");
		
		db.execSQL("CREATE TABLE IF NOT EXISTS vartotojai ("
				+ "email text , "
				+ "password text , "
				+ "name text , "
				+ "sure_name text , "
				+ "user_id text ,"
				+ "asm_kod bigint(20) ,"
				+ "bil_nr int(11) ,"
				+ "time_stamp DATETIME );");
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}
	
	

}
