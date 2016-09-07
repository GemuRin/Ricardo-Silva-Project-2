package com.example.casopratico2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TSFFeedsSQLHelper extends SQLiteOpenHelper {
	/**
	 * Construtor
	 * 
	 * @param context
	 */
	public TSFFeedsSQLHelper(Context context) {
		super(context, TSFFeedsDB.DB_NAME, null, TSFFeedsDB.DB_VERSION);
	}

	/**
	 * Criação da base de dados
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		if (db.isReadOnly()) {
			db = getWritableDatabase();
		}
		db.execSQL("CREATE TABLE " + TSFFeedsDB.Posts.NOME_TABELA + " ("
				+ TSFFeedsDB.Posts._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ TSFFeedsDB.Posts.TITLE + " TEXT," + TSFFeedsDB.Posts.LINK
				+ " TEXT UNIQUE," + TSFFeedsDB.Posts.DESCRIPTION + " TEXT,"
				+ TSFFeedsDB.Posts.PUB_DATE + " INTEGER"
				+ TSFFeedsDB.Posts.FRASE + " TEXT" + ")");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// não precisa de fazer nada
	}
}