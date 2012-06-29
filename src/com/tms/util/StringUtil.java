package com.tms.util;

import org.htmlparser.NodeReader;
import org.htmlparser.Parser;
import org.htmlparser.util.ParserException;
import org.htmlparser.util.ParserUtils;
import org.htmlparser.visitors.TextExtractingVisitor;

import java.io.StringReader;

public class StringUtil
{
	private static StringUtil util;

	private StringUtil()
	{

	}

	public static StringUtil getInstance()
	{
		if(util == null)
			util = new StringUtil();
		return util;
	}

	/**
	 * Strips all html tag from string passed into the method
	 * @param s
	 * @return A string stripped of all html tags
	 * @throws ParserException
	 */
	public String filterHtml(String s) throws ParserException
	{
		String converted = s;
		if(!(s == null || "".equals(s)))
		{
			Parser parser = new Parser(new NodeReader(new StringReader(s), s.length()));
			TextExtractingVisitor visitor = new TextExtractingVisitor();
			parser.visitAllNodesWith(visitor);
			converted = ParserUtils.removeEscapeCharacters(visitor.getExtractedText());
		}
		return converted;
    }

	/**
	 * Limits a string to a certain length. Suffixes strings exceeding that length with ...
	 * @param s
	 * @param length
	 * @return A string of limited length
	 */
    public String filterLength(String s, int length)
	{
		String converted = s;
		if(!(s == null || "".equals(s)))
		{
			if(length > 0)
			{
				if(s.length() > length)
					converted = s.substring(0, length) + "...";
			}
		}
		return converted;
	}
}
