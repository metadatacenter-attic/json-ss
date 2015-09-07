package org.mm.renderer;

import org.mm.parser.JSONSSParserConstants;
import org.mm.parser.node.ReferenceNode;
import org.mm.parser.node.SourceSpecificationNode;
import org.mm.ss.SpreadSheetDataSource;
import org.mm.ss.SpreadsheetLocation;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class ReferenceUtil implements JSONSSParserConstants
{
	public static SpreadsheetLocation resolveLocation(SpreadSheetDataSource dataSource, ReferenceNode referenceNode)
			throws RendererException
	{
		SourceSpecificationNode sourceSpecificationNode = referenceNode.getSourceSpecificationNode();
		return dataSource.resolveLocation(sourceSpecificationNode);
	}

	public static String resolveReferenceValue(SpreadSheetDataSource dataSource, ReferenceNode referenceNode)
		throws RendererException
	{
		SpreadsheetLocation location = resolveLocation(dataSource, referenceNode);
		
		String referenceValue = "";
		String rawLocationValue = dataSource.getLocationValue(location, referenceNode); // Deals with shifting
		if (rawLocationValue == null || rawLocationValue.isEmpty()) {
			referenceValue = referenceNode.getActualDefaultLocationValue();
		} else {
			referenceValue = rawLocationValue;
		}
		if (referenceValue.isEmpty()) {
			switch(referenceNode.getActualEmptyLocationDirective()) {
				case MM_ERROR_IF_EMPTY_LOCATION: throw new RendererException("empty location " + location + " in reference " + referenceNode);
				case MM_WARNING_IF_EMPTY_LOCATION: // NO-OP
			}
		}
		return referenceValue;
	}

	public static String evaluateReferenceValue(String functionName, int functionID, List<String> arguments, String defaultValue,
			boolean hasExplicitArguments) throws RendererException
	{
		String processedReferenceValue = "";
		
		switch (functionID) {
		case MM_TO_UPPER_CASE:
			if (hasExplicitArguments) {
				if (arguments.size() != 1)
					throw new RendererException("function " + functionName + " expecting one argument, got " + arguments.size());
				processedReferenceValue = arguments.get(0).toUpperCase();
			} else
				processedReferenceValue = defaultValue.toUpperCase();
			break;
		case MM_TO_LOWER_CASE:
			if (hasExplicitArguments) {
				if (arguments.size() != 1)
					throw new RendererException(
							"function " + functionName + " expecting only one argument, got " + arguments.size());
				processedReferenceValue = arguments.get(0).toLowerCase();
			} else
				processedReferenceValue = defaultValue.toLowerCase();
			break;
		case MM_TRIM:
			if (hasExplicitArguments) {
				if (arguments.size() != 1)
					throw new RendererException(
							"function " + functionName + " expecting only one argument, got " + arguments.size());
				processedReferenceValue = arguments.get(0).trim();
			} else
				processedReferenceValue = defaultValue.trim();
			break;
		case MM_REVERSE:
			if (hasExplicitArguments) {
				if (arguments.size() != 1)
					throw new RendererException(
							"function " + functionName + " expecting only one argument, got " + arguments.size());
				processedReferenceValue = reverse(arguments.get(0));
			} else
				processedReferenceValue = reverse(defaultValue);
			break;
		case MM_CAPTURING:
			if (arguments.size() == 1) {
				processedReferenceValue = capture(defaultValue, arguments.get(0));
			} else if (arguments.size() == 2) {
				processedReferenceValue = capture(arguments.get(0), arguments.get(1));
			} else
				throw new RendererException(
						"function " + functionName + " expecting one or two arguments, got " + arguments.size());
			break;
		case MM_PREPEND:
			if (arguments.size() == 1) {
				processedReferenceValue = arguments.get(0) + defaultValue;
			} else if (arguments.size() == 2) {
				processedReferenceValue = arguments.get(0) + arguments.get(1);
			} else
				throw new RendererException(
						"function " + functionName + " expecting one or two arguments, got " + arguments.size());
			break;
		case MM_APPEND:
			if (arguments.size() == 1) {
				processedReferenceValue = defaultValue + arguments.get(0);
			} else if (arguments.size() == 2) {
				processedReferenceValue = defaultValue + arguments.get(0) + arguments.get(1);
			} else
				throw new RendererException(
						"function " + functionName + " expecting one or two arguments, got " + arguments.size());
			break;
		case MM_REPLACE:
			if (arguments.size() == 2) {
				processedReferenceValue = defaultValue.replace(arguments.get(0), arguments.get(1));
			} else if (arguments.size() == 3) {
				processedReferenceValue = arguments.get(0).replace(arguments.get(1), arguments.get(2));
			} else
				throw new RendererException(
						"function " + functionName + " expecting two or three arguments, got " + arguments.size());
			break;
		case MM_REPLACE_ALL:
			if (arguments.size() == 2) {
				processedReferenceValue = defaultValue.replaceAll(arguments.get(0), arguments.get(1));
			} else if (arguments.size() == 3) {
				processedReferenceValue = arguments.get(0).replaceAll(arguments.get(1), arguments.get(2));
			} else
				throw new RendererException(
						"function " + functionName + " expecting two or three arguments, got " + arguments.size());
			break;
		case MM_REPLACE_FIRST:
			if (arguments.size() == 2) {
				processedReferenceValue = defaultValue.replaceFirst(arguments.get(0), arguments.get(1));
			} else if (arguments.size() == 3) {
				processedReferenceValue = arguments.get(0).replaceFirst(arguments.get(1), arguments.get(2));
			} else
				throw new RendererException(
						"function " + functionName + " expecting two or three arguments, got " + arguments.size());
			break;
		default:
			throw new RendererException("unknown mapping function " + functionName);
		}
		return processedReferenceValue;
	}

	public static String reverse(String text)
	{
		int i, len = text.length();
		StringBuilder dest = new StringBuilder(len);
		
		for (i = len - 1; i >= 0; i--) {
			dest.append(text.charAt(i));
		}
		return dest.toString();
	}

	public static String capture(String value, String regexExpression) throws RendererException
	{
		try {
			Pattern p = Pattern.compile(regexExpression); // Pull the value out of the location
			Matcher m = p.matcher(value);
			boolean matchFound = m.find();
			String result = "";
			if (matchFound) {
				for (int groupIndex = 1; groupIndex <= m.groupCount(); groupIndex++)
					result += m.group(groupIndex);
			}
			return result;
		} catch (PatternSyntaxException e) {
			throw new RendererException("invalid capturing expression " + regexExpression + ": " + e.getMessage());
		}
	}

	public static String produceIdentifierString(SpreadsheetLocation location)
	{
		StringBuffer sb = new StringBuffer();
		sb.append(location.getSheetName());
		sb.append("_");
		sb.append(location.getCellLocation());
		sb.append("_");
		sb.append(System.currentTimeMillis());
		return sb.toString();
	}

	public static String produceIdentifierString(String text)
	{
		return toCamelCase(text);
	}

	public static String produceIdentifierString()
	{
		return "MM_" + System.currentTimeMillis();
	}

	private static String toCamelCase(final String text)
	{
		final StringBuilder sb = new StringBuilder();
		for (final String word : text.split("\\s+")) {
			if (!word.isEmpty()) {
				if (word.matches("\\p{javaLowerCase}*")) {
					/*
					 * If all the characters are lower-case then upper-case the first letter to conform
					 * with CamelCasing.
					 */
					sb.append(word.substring(0, 1).toUpperCase());
					sb.append(word.substring(1).toLowerCase());
				} else {
					sb.append(word);
				}
			}
		}
		return sb.toString();
	}
}
