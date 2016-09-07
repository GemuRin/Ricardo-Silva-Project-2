package com.example.casopratico2;

import com.example.casopratico2.TSFFeedsDB.Posts;

import android.app.Activity;
import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.TextView;

public class TsfNoticiaActivity extends Activity {
	private TextView titulot;
	private TextView datat;
	private WebView conteudot;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.noticiatsf);
		titulot = (TextView) findViewById(R.id.feedTitulot);
		// Ao premir o título a janela será encerrada
		titulot.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		datat = (TextView) findViewById(R.id.feedDatat);
		conteudot = (WebView) findViewById(R.id.feedConteudot);
		// para forçar o redimensionamento da página
		conteudot.getSettings().setLoadWithOverviewMode(true);
		conteudot.getSettings().setUseWideViewPort(true);
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onStart() {
		super.onStart();
		try {
			Bundle extras = getIntent().getExtras();
			long idNoticia = extras.getLong("idNoticia");
			final String[] columnas = new String[] { Posts._ID, // 0
					Posts.TITLE, // 1
					Posts.PUB_DATE, // 2
					Posts.LINK // 3
			};
			Uri uri = TSFFeedsProvider.CONTENT_URI;
			uri = ContentUris.withAppendedId(uri, idNoticia);
			Log.d("uri", "" + uri);
			// Query "managed": A atividade fica encarregada de fechar e voltar
			// a
			// carregar o cursor quando for necessario
			Cursor cursor = managedQuery(uri, columnas, null, null,
					Posts.PUB_DATE + " DESC");
			// Queremos entrar sem alterar os dados para recarregar o cursor
			cursor.setNotificationUri(getContentResolver(), uri);
			// Para que a atividade se encarregue de manusear o cursor
			// segundo os seus ciclos de vida
			startManagingCursor(cursor);
			// mostramos os dados do cursor na vista
			int id = (int) idNoticia;
			if (cursor.moveToPosition(id - 1)) {
				titulot.setText(cursor.getString(1));
				java.text.DateFormat dateFormat = DateFormat
						.getLongDateFormat(TsfNoticiaActivity.this);
				datat.setText(dateFormat.format(cursor.getLong(2)));
				String texto = new String(cursor.getString(3).getBytes(),
						"utf-8");
				String link = cursor.getString(3);
				conteudot.loadUrl(link);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}