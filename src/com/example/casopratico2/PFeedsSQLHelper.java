package com.example.casopratico2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PFeedsSQLHelper extends SQLiteOpenHelper {
	/**
	 * Construtor
	 * 
	 * @param context
	 */
	public PFeedsSQLHelper(Context context) {
		super(context, PFeedsDB.DB_NAME, null, PFeedsDB.DB_VERSION);
	}

	/**
	 * Criação da base de dados
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		if (db.isReadOnly()) {
			db = getWritableDatabase();
		}
		db.execSQL("CREATE TABLE " + PFeedsDB.Posts.NOME_TABELA + " ("
				+ PFeedsDB.Posts._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ PFeedsDB.Posts.TITLE + " TEXT," + PFeedsDB.Posts.LINK
				+ " TEXT UNIQUE," + PFeedsDB.Posts.COMMENTS + " TEXT,"
				+ PFeedsDB.Posts.PUB_DATE + " INTEGER" + PFeedsDB.Posts.CREATOR
				+ " TEXT, " + PFeedsDB.Posts.DESCRIPTION + " TEXT,"
				+ PFeedsDB.Posts.FRASE + " TEXT" + ")");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// não precisa de fazer nada
	}
}