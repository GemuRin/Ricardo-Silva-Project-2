package com.example.casopratico2;

import com.example.casopratico2.CFeedsDB.Posts;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

public class CFeedsProvider extends ContentProvider {
	public static final Uri CONTENT_URI = Uri
			.parse("content://www.cmjornal.xl.pt");
	private static final int POST = 1;
	private static final int POST_ID = 2;
	private static final UriMatcher uriMatcher;
	static {
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI("www.cmjornal.xl.pt", "post", POST);
		uriMatcher.addURI("www.cmjornal.xl.pt", "post/#", POST_ID);
	}
	private SQLiteDatabase CfeedsDB;

	@Override
	public boolean onCreate() {
		Context context = getContext();
		CFeedsSQLHelper dbHelper = new CFeedsSQLHelper(context);
		CfeedsDB = dbHelper.getWritableDatabase();
		return (CfeedsDB == null) ? false : true;
	}

	@Override
	public String getType(Uri uri) {
		switch (uriMatcher.match(uri)) {
		// para conjunto de posts
		case POST:
			return "vnd.android.cursor.dir/vnd.cmjornal.post";
			// para um �nico post
		case POST_ID:
			return "vnd.android.cursor.item/vnd.cmjornal.post";
		default:
			throw new IllegalArgumentException("Unsupported URI: " + uri);
		}
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		long rowID = CfeedsDB.replace(Posts.NOME_TABELA, "", values);
		// se tudo correu bem devolvemos o Uri
		if (rowID > 0) {
			Uri baseUri = Uri.parse("content://www.cmjornal.xl.pt/post");
			Uri _uri = ContentUris.withAppendedId(baseUri, rowID);
			getContext().getContentResolver().notifyChange(_uri, null);
			getContext().getContentResolver().notifyChange(baseUri, null);
			return _uri;
		}
		throw new SQLException("Failed to insert row into " + uri);
	}

	@Override
	public int delete(Uri uri, String where, String[] whereargs) {
		int count = 0;
		switch (uriMatcher.match(uri)) {
		case POST:
			count = CfeedsDB.delete(Posts.NOME_TABELA, where, whereargs);
			break;
		case POST_ID:
			String id = uri.getPathSegments().get(1);
			count = CfeedsDB.delete(Posts.NOME_TABELA,
					Posts._ID
							+ " = "
							+ id
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereargs);
			break;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		int count = 0;
		switch (uriMatcher.match(uri)) {
		case POST:
			count = CfeedsDB.update(Posts.NOME_TABELA, values, selection,
					selectionArgs);
			break;
		case POST_ID:
			count = CfeedsDB.update(Posts.NOME_TABELA, values,
					Posts._ID
							+ " = "
							+ uri.getPathSegments().get(1)
							+ (!TextUtils.isEmpty(selection) ? " AND ("
									+ selection + ')' : ""), selectionArgs);
			break;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		SQLiteQueryBuilder sqlBuilder = new SQLiteQueryBuilder();
		sqlBuilder.setTables(Posts.NOME_TABELA);
		Log.d("TAG", "Entrei no Cursor_Query: " + uriMatcher.match(uri));
		if (uriMatcher.match(uri) == POST_ID) {
			Log.d("TAG", "Entrei no POST_ID");
			sqlBuilder.appendWhere(Posts._ID + " = "
					+ uri.getPathSegments().get(1));
		}
		if (sortOrder == null || sortOrder == "") {
			sortOrder = Posts.DEFAULT_SORT_ORDER;
		}
		Cursor c = sqlBuilder.query(CfeedsDB, projection, selection,
				selectionArgs, null, null, sortOrder);
		// Registamos as altera��es para que
		// os nossos observers entrem
		c.setNotificationUri(getContext().getContentResolver(), uri);
		return c;
	}
}
