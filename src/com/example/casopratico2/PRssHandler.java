package com.example.casopratico2;

import java.util.Date;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.ext.LexicalHandler;
import org.xml.sax.helpers.DefaultHandler;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;
import android.util.Log;
import com.example.casopratico2.PFeedsDB.Posts;

public class PRssHandler extends DefaultHandler implements LexicalHandler {
	// Onde iremos armazenando os dados do registo a guardar
	ContentValues rssItem;
	// Flags para saber em que nó estamos
	private boolean in_item = false;
	private boolean in_title = false;
	private boolean in_link = false;
	@SuppressWarnings("unused")
	private boolean in_comments = false;
	private boolean in_pubDate = false;
	@SuppressWarnings("unused")
	private boolean in_dcCreator = false;
	private boolean in_description = false;
	@SuppressWarnings("unused")
	private boolean in_CDATA;
	// Dados do fornecedor de conteúdos
	private ContentResolver contentProv;
	private static final String AUTORITIES = "blog.masterd.pt";
	Uri uri = Uri.parse("content://" + AUTORITIES + "/" + Posts.NOME_TABELA);

	public PRssHandler(ContentResolver contentResolver) {
		contentProv = contentResolver;

	}

	public PRssHandler() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void startElement(String namespaceURI, String localName,
			String qName, Attributes atts) throws SAXException {
		// Vamo-nos centrar apenas nos itens
		if (localName.equalsIgnoreCase("item")) {
			in_item = true;
			rssItem = new ContentValues();
		} else if (localName.equalsIgnoreCase(PFeedsDB.Posts.TITLE)) {
			in_title = true;
		} else if (localName.equalsIgnoreCase(PFeedsDB.Posts.LINK)) {
			in_link = true;
		} else if (localName.equalsIgnoreCase(PFeedsDB.Posts.COMMENTS)) {
			in_comments = true;
		} else if (localName.equalsIgnoreCase("pubDate")) {
			in_pubDate = true;
		} else if (localName.equalsIgnoreCase("dc:creator")) {
			in_dcCreator = true;
		} else if (localName.equalsIgnoreCase(PFeedsDB.Posts.DESCRIPTION)) {
			in_description = true;
		}
	}

	@Override
	public void endElement(String namespaceURI, String localName, String qName)
			throws SAXException {
		if (localName.equalsIgnoreCase("item")) {
			contentProv.insert(uri, rssItem);
			rssItem = new ContentValues();
			in_item = false;
		} else if (localName.equalsIgnoreCase(PFeedsDB.Posts.TITLE)) {
			in_title = false;
		} else if (localName.equalsIgnoreCase(PFeedsDB.Posts.LINK)) {
			in_link = false;
		} else if (localName.equalsIgnoreCase(PFeedsDB.Posts.COMMENTS)) {
			in_comments = false;
		} else if (localName.equalsIgnoreCase("pubDate")) {
			in_pubDate = false;
		}

		else if (localName.equalsIgnoreCase("dc:creator")) {
			in_dcCreator = false;
		} else if (localName.equalsIgnoreCase(PFeedsDB.Posts.DESCRIPTION)) {
			in_description = false;
		}
	}

	@Override
	public void characters(char ch[], int start, int length) {
		if (in_item) { // Estamos dentro de um item
			if (in_title) {
				rssItem.put(PFeedsDB.Posts.TITLE, new String(ch, start, length));
			} else if (in_link) {
				rssItem.put(PFeedsDB.Posts.LINK, new String(ch, start, length));
			} else if (in_description) {
				rssItem.put(PFeedsDB.Posts.DESCRIPTION, new String(ch, start,
						length));
				String frase = rssItem.get(PFeedsDB.Posts.DESCRIPTION)
						.toString();
				int fim1 = frase.indexOf(". ") + 1;
				int fim2 = frase.indexOf(", ") + 1;
				if (fim1 < fim2)
					rssItem.put(PFeedsDB.Posts.FRASE, frase.substring(0, fim2));
				else
					rssItem.put(PFeedsDB.Posts.FRASE, frase.substring(0, fim1));
			} else if (in_pubDate) {
				String strDate = new String(ch, start, length);
				Log.d("My debug data", strDate);
				try {
					@SuppressWarnings("deprecation")
					long data = Date.parse(strDate);
					rssItem.put(PFeedsDB.Posts.PUB_DATE, data);
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