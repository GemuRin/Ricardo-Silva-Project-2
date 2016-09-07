package com.example.casopratico2;

import com.example.casopratico2.PFeedsDB.Posts;

import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.SimpleCursorAdapter.ViewBinder;

public class PTitulos extends ListActivity {
	private static final long FREQUENCIA_ATUALIZACAO = 60 * 60 * 1000; // recarrega
	// a
	// cada
	// hora

	private AtualizarPostAsyncTask tarefap;
	ListView lvFeeds;
	SimpleCursorAdapter adapter;
	int[] camposView;
	String[] camposDb;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_PROGRESS);
		setContentView(R.layout.feedspublico);
		configurarAdapter();
	}

	@Override
	public void onResume() {
		super.onResume();
		carregarNoticias();
	}

	@SuppressWarnings("deprecation")
	public void configurarAdapter() {
		// Obtemos todos os artigos da BD
		final String[] colunas = new String[] { Posts._ID, // 0
				Posts.TITLE, // 1
				Posts.FRASE, // 2
				Posts.PUB_DATE, // 3
		};
		Uri uri = Uri.parse("content://blog.masterd.pt/post");
		Cursor cursor = managedQuery(uri, colunas, null, null, Posts.PUB_DATE
				+ " DESC");
		// Queremos saber se alteram dados para
		// recarregar -o cursor
		cursor.setNotificationUri(getContentResolver(), uri);
		// Para que a atividade se encarregue de manusear o cursor
		// conforme os seus ciclos de vida
		startManagingCursor(cursor);
		// Mapeamos as querys SQL para os campos das vistas
		String[] camposDb = new String[] { Posts.TITLE, Posts.FRASE,
				Posts.PUB_DATE };
		int[] camposView = new int[] { R.id.feedTitulop, R.id.feedFrasep,
				R.id.feedDatap };
		// Criamos o adapter
		SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
				R.layout.feeds_itempublico, cursor, camposDb, camposView);
		// A data deve ser formatada
		final java.text.DateFormat dateFormat = DateFormat
				.getLongDateFormat(PTitulos.this);
		adapter.setViewBinder(new ViewBinder() {
			@Override
			public boolean setViewValue(View view, Cursor cursor,
					int columnIndex) {
				if (view.getId() == R.id.feedDatap) {
					long timestamp = cursor.getLong(columnIndex);
					((TextView) view).setText(dateFormat.format(timestamp));
					return true;
				} else {
					return false; // Que se encarregue do adapter
				}
			}
		});
		// Tudo pronto, carregamos o adapter
		setListAdapter(adapter);
	}

	class AtualizarPostAsyncTask extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {
			setBarraProgressoVisivel(true);
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... params) {
			PublicoApplication app = (PublicoApplication) getApplication();
			PRssDownloadHelper.updateRssData(app.getRssUrl(),
					getContentResolver());
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			SharedPreferences prefs = getPreferences(MODE_PRIVATE);
			Editor editor = prefs.edit();
			editor.putLong("ultima_atualizacao", System.currentTimeMillis());
			editor.commit();
			setBarraProgressoVisivel(false);
		}

		@Override
		protected void onCancelled() {
			setBarraProgressoVisivel(false);
			// Se cancelado, da próxima vez que arrancar deverá voltar a
			// carregá-la
			SharedPreferences prefs = getPreferences(MODE_PRIVATE);
			Editor editor = prefs.edit();
			editor.putLong("ultima_atualizacao", 0);
			editor.commit();
			super.onCancelled();
		}
	}

	public void carregarNoticias() {
		SharedPreferences prefs = getPreferences(MODE_PRIVATE);
		long ultima = prefs.getLong("ultima_atualizacao", 0);
		if ((System.currentTimeMillis() - ultima) > FREQUENCIA_ATUALIZACAO) {
			tarefap = new AtualizarPostAsyncTask();
			tarefap.execute();
		}
	}

	@Override
	protected void onStop() {
		// Se estiver a ser executada uma tarefa em segundo plano,
		// esta é parada
		if (tarefap != null
				&& !tarefap.getStatus().equals(AsyncTask.Status.FINISHED)) {
			tarefap.cancel(true);
		}
		super.onStop();
	}

	public void setBarraProgressoVisivel(boolean visible) {
		final Window window = getWindow();
		if (visible) {
			window.setFeatureInt(Window.FEATURE_PROGRESS,
					Window.PROGRESS_VISIBILITY_ON);
			window.setFeatureInt(Window.FEATURE_PROGRESS,
					Window.PROGRESS_INDETERMINATE_ON);
		} else {
			window.setFeatureInt(Window.FEATURE_PROGRESS,
					Window.PROGRESS_VISIBILITY_OFF);
		}
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Intent i = new Intent();
		i.setClass(PTitulos.this, PNoticiaActivity.class);
		i.putExtra("idNoticia", id);
		startActivity(i);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		if (id == android.R.id.home) {
			Intent intent = new Intent(this, MainActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}