package com.example.casopratico2;

import android.provider.BaseColumns;

public class TSFFeedsDB {
	// Nome da base de dados
	public static final String DB_NAME = "jogo.db";
	// vers�o da base de dados
	public static final int DB_VERSION = 1;

	// Esta classe n�o deve ser instanciada
	private TSFFeedsDB() {
	}

	// Defini��o da tabela posts
	public static final class Posts implements BaseColumns {
		// Esta classe n�o deve ser instanciada
		private Posts() {
		}

		// ordem por defeito
		public static final String DEFAULT_SORT_ORDER = "_ID DESC";
		// Simplifica��o dos nomes de campos e tabela de constantes para
		// facilitar altera��es na estrutura interna da BD
		public static final String NOME_TABELA = "feeds";
		public static final String _ID = "_id";
		public static final String TITLE = "title";
		public static final String LINK = "link";
		public static final String DESCRIPTION = "description";
		public static final String PUB_DATE = "pub_date";
		public static final String FRASE = "frase";
		public static final String _COUNT = "6";
	}
}
