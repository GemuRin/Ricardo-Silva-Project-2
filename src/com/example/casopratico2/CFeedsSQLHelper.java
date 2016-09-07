package com.example.casopratico2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CFeedsSQLHelper extends SQLiteOpenHelper {
	/**
	 * Construtor
	 * 
	 * @param context
	 */
	public CFeedsSQLHelper(Context context) {
		super(context, CFeedsDB.DB_NAME, null, CFeedsDB.DB_VERSION);
	}

	/**
	 * Criação da base de dados
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		if (db.isReadOnly()) {
			db = getWritableDatabase();
		}
		db.execSQL("CREATE TABLE " + CFeedsDB.Posts.NOME_TABELA + " ("
				+ CFeedsDB.Posts._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ CFeedsDB.Posts.TITLE + " TEXT," + CFeedsDB.Posts.LINK
				+ " TEXT UNIQUE," + CFeedsDB.Posts.DESCRIPTION + " TEXT,"
				+ CFeedsDB.Posts.PUB_DATE + " INTEGER" + ")");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// não precisa de fazer nada
	}
}