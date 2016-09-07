package com.example.casopratico2;

import java.net.URL;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import android.content.ContentResolver;

public class CRssDownloadHelper {
	public static void updateRssData(String rssUrl,
			ContentResolver contentResolver) {
		try {
			URL url = new URL(rssUrl);
			// Obtemos o SAXParser
			SAXParserFactory spf = SAXParserFactory.newInstance();
			SAXParser saxParser = spf.newSAXParser();
			// Criamos o Handler
			CRssHandler rssHandler = new CRssHandler(contentResolver);
			// RssHandler rssHandler = new RssHandler();
			// Definimos o manuseador léxico
			saxParser
					.setProperty(
							"http://xml.org/sax/properties/lexical-handler",
							rssHandler);
			// Obtemos o Reader
			XMLReader xr = saxParser.getXMLReader();
			xr.setContentHandler(rssHandler);
			// Analisamos o conteúdo
			InputSource is = new InputSource(url.openStream());
			is.setEncoding("utf-8");
			xr.parse(is);
			// A análise foi concluída
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}