package com.project.lexicalphase;


public class TokenPair {

	private String token;
	private String lexeme;
	private int line_number;

	public TokenPair(){}

	public TokenPair(String token, String lexeme, int line_number) {
		this.token = token;
		this.lexeme = lexeme;
		this.line_number = line_number;

	}

	protected int getLine() {
		return line_number;
	}

	protected String getToken() {
		return token;
	}

	protected String getLexeme() {
		return lexeme;
	}

}