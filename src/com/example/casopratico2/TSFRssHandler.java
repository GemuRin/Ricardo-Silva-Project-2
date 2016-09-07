package com.example.casopratico2;

import java.sql.Date;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.ext.LexicalHandler;
import org.xml.sax.helpers.DefaultHandler;
import com.example.casopratico2.TSFFeedsDB.Posts;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;
import android.util.Log;

public class TSFRssHandler extends DefaultHandler implements LexicalHandler {
	// Onde iremos armazenando os dados do registo a guardar
	ContentValues rssItem;
	// Flags para saber em que nó estamos
	private boolean in_item = false;
	private boolean in_title = false;
	private boolean in_link = false;
	@SuppressWarnings("unused")
	private boolean in_description = false;
	private boolean in_pubDate = false;

	// Dados do fornecedor de conteúdos
	private ContentResolver contentProv;
	private static final String AUTORITIES = "feeds.tsf.pt";
	Uri uri = Uri.parse("content://" + AUTORITIES + "/" + Posts.NOME_TABELA);

	public TSFRssHandler(ContentResolver contentResolver) {
		contentProv = contentResolver;
	}

	public TSFRssHandler() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void startElement(String namespaceURI, String localName,
			String qName, Attributes atts) throws SAXException {
		// Vamo-nos centrar apenas nos itens
		if (localName.equalsIgnoreCase("item")) {
			in_item = true;
			rssItem = new ContentValues();
		} else if (localName.equalsIgnoreCase(TSFFeedsDB.Posts.TITLE)) {
			in_title = true;
		} else if (localName.equalsIgnoreCase(TSFFeedsDB.Posts.LINK)) {
			in_link = true;
		} else if (localName.equalsIgnoreCase(TSFFeedsDB.Posts.DESCRIPTION)) {
			in_description = true;
		} else if (localName.equalsIgnoreCase("pubDate")) {
			in_pubDate = true;
		}
	}

	@Override
	public void endElement(String namespaceURI, String localName, String qName)
			throws SAXException {
		if (localName.equalsIgnoreCase("item")) {
			contentProv.insert(uri, rssItem);
			rssItem = new ContentValues();
			in_item = false;
		} else if (localName.equalsIgnoreCase(TSFFeedsDB.Posts.TITLE)) {
			in_title = false;
		} else if (localName.equalsIgnoreCase(TSFFeedsDB.Posts.LINK)) {
			in_link = false;
		} else if (localName.equalsIgnoreCase("pubDate")) {
			in_pubDate = false;
		}
	}

	@Override
	public void characters(char ch[], int start, int length) {
		if (in_item) { // Estamos dentro de um item
			if (in_title) {
				rssItem.put(TSFFeedsDB.Posts.TITLE, new String(ch, start,
						length));
			} else if (in_link) {
				rssItem.put(TSFFeedsDB.Posts.LINK,
						new String(ch, start, length));
			} else if (in_description) {
				rssItem.put(TSFFeedsDB.Posts.DESCRIPTION, new String(ch, start,
						length));
				String frase = rssItem.get(TSFFeedsDB.Posts.DESCRIPTION)
						.toString();
				int fim1 = frase.indexOf(". ") + 1;
				int fim2 = frase.indexOf(", ") + 1;
				if (fim1 < fim2)
					rssItem.put(TSFFeedsDB.Posts.FRASE,
							frase.substring(0, fim2));
				else
					rssItem.put(TSFFeedsDB.Posts.FRASE,
							frase.substring(0, fim1));
			} else if (in_pubDate) {
				String strDate = new String(ch, start, length);
				Log.d("My debug data", strDate);
				try {
					@SuppressWarnings("deprecation")
					long data = Date.parse(strDate);
					rssItem.put(TSFFeedsDB.Posts.PUB_DATE, data);
				} catch (Exception e) {
					Log.d("RssHandler", "Erro na análise da data");
				}
			}
		}
	}

	@Override
	public void comment(char[] ch, int start, int length) throws SAXException {
	}

	@Override
	public void endCDATA() throws SAXException {
	}

	@Override
	public void endDTD() throws SAXException {
	}

	@Override
	public void endEntity(String name) throws SAXException {
	}

	@Override
	public void startCDATA() throws SAXException {
	}

	@Override
	public void startDTD(String name, String publicId, String systemId)
			throws SAXException {
	}

	@Override
	public void startEntity(String name) throws SAXException {
	}
}
