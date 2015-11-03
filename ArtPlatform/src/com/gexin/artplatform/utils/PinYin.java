package com.gexin.artplatform.utils;

import java.util.ArrayList;

import android.annotation.SuppressLint;

import com.gexin.artplatform.utils.HanziToPinyin.Token;

public class PinYin {
	@SuppressLint("DefaultLocale") 
	public static String getPinYin(String input) {
		ArrayList<Token> tokens = HanziToPinyin.getInstance().get(input);
		StringBuilder sb = new StringBuilder();
		if (tokens != null && tokens.size() > 0) {
			for (Token token : tokens) {
				if (Token.PINYIN == token.type) {
					sb.append(token.target);
				} else {
					sb.append(token.source);
				}
			}
		}
		return sb.toString().toLowerCase();
	}
	@SuppressLint("DefaultLocale") 
	public static String getSimplePinYin(String input) {
		ArrayList<Token> tokens = HanziToPinyin.getInstance().get(input);
		StringBuilder sb = new StringBuilder();
		if (tokens != null && tokens.size() > 0) {
			for (Token token : tokens) {
				if (Token.PINYIN == token.type) {
					sb.append(token.target.charAt(0));
				} else {
					sb.append(token.source);
				}
			}
		}
		return sb.toString().toLowerCase();
	}
}
