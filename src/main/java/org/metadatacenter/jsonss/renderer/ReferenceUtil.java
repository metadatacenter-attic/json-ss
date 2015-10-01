package org.metadatacenter.jsonss.renderer;

import org.metadatacenter.jsonss.parser.JSONSSParserConstants;
import org.metadatacenter.jsonss.parser.node.ReferenceNode;
import org.metadatacenter.jsonss.ss.CellLocation;
import org.metadatacenter.jsonss.ss.SpreadSheetDataSource;

import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class ReferenceUtil implements JSONSSParserConstants
{
  public static String resolveReferenceValue(SpreadSheetDataSource dataSource, ReferenceNode referenceNode,
    Optional<CellLocation> currentCellLocation) throws RendererException
  {
    CellLocation cellLocation = dataSource
      .resolveCellLocation(referenceNode.getReferenceSourceSpecificationNode(), currentCellLocation);
    String rawLocationValue = dataSource.getCellLocationValue(cellLocation, referenceNode); // Deals with shifting
    String referenceValue;

    if (rawLocationValue == null || rawLocationValue.isEmpty()) {
      referenceValue = referenceNode.getActualDefaultLocationValue();
    } else {
      referenceValue = rawLocationValue;
    }

    if (referenceValue.isEmpty()) {
      switch (referenceNode.getActualEmptyLocationDirective()) {
      case ERROR_IF_EMPTY_LOCATION:
        throw new RendererException("empty location " + cellLocation + " in reference " + referenceNode);
      case WARNING_IF_EMPTY_LOCATION: // NO-OP
      }
    }
    return referenceValue;
  }

  public static String evaluateReferenceValue(String functionName, int functionID, List<String> arguments,
    String defaultValue, boolean hasExplicitArguments) throws RendererException
  {
    String processedReferenceValue = "";

    switch (functionID) {
    case TO_UPPER_CASE:
      if (hasExplicitArguments) {
        if (arguments.size() != 1)
          throw new RendererException("function " + functionName + " expecting one argument, got " + arguments.size());
        processedReferenceValue = arguments.get(0).toUpperCase();
      } else
        processedReferenceValue = defaultValue.toUpperCase();
      break;
    case TO_LOWER_CASE:
      if (hasExplicitArguments) {
        if (arguments.size() != 1)
          throw new RendererException(
            "function " + functionName + " expecting only one argument, got " + arguments.size());
        processedReferenceValue = arguments.get(0).toLowerCase();
      } else
        processedReferenceValue = defaultValue.toLowerCase();
      break;
    case TRIM:
      if (hasExplicitArguments) {
        if (arguments.size() != 1)
          throw new RendererException(
            "function " + functionName + " expecting only one argument, got " + arguments.size());
        processedReferenceValue = arguments.get(0).trim();
      } else
        processedReferenceValue = defaultValue.trim();
      break;
    case REVERSE:
      if (hasExplicitArguments) {
        if (arguments.size() != 1)
          throw new RendererException(
            "function " + functionName + " expecting only one argument, got " + arguments.size());
        processedReferenceValue = reverse(arguments.get(0));
      } else
        processedReferenceValue = reverse(defaultValue);
      break;
    case CAPTURING:
      if (arguments.size() == 1) {
        processedReferenceValue = capture(defaultValue, arguments.get(0));
      } else if (arguments.size() == 2) {
        processedReferenceValue = capture(arguments.get(0), arguments.get(1));
      } else
        throw new RendererException(
          "function " + functionName + " expecting one or two arguments, got " + arguments.size());
      break;
    case PREPEND:
      if (arguments.size() == 1) {
        processedReferenceValue = arguments.get(0) + defaultValue;
      } else if (arguments.size() == 2) {
        processedReferenceValue = arguments.get(0) + arguments.get(1);
      } else
        throw new RendererException(
          "function " + functionName + " expecting one or two arguments, got " + arguments.size());
      break;
    case APPEND:
      if (arguments.size() == 1) {
        processedReferenceValue = defaultValue + arguments.get(0);
      } else if (arguments.size() == 2) {
        processedReferenceValue = defaultValue + arguments.get(0) + arguments.get(1);
      } else
        throw new RendererException(
          "function " + functionName + " expecting one or two arguments, got " + arguments.size());
      break;
    case REPLACE:
      if (arguments.size() == 2) {
        processedReferenceValue = defaultValue.replace(arguments.get(0), arguments.get(1));
      } else if (arguments.size() == 3) {
        processedReferenceValue = arguments.get(0).replace(arguments.get(1), arguments.get(2));
      } else
        throw new RendererException(
          "function " + functionName + " expecting two or three arguments, got " + arguments.size());
      break;
    case REPLACE_ALL:
      if (arguments.size() == 2) {
        processedReferenceValue = defaultValue.replaceAll(arguments.get(0), arguments.get(1));
      } else if (arguments.size() == 3) {
        processedReferenceValue = arguments.get(0).replaceAll(arguments.get(1), arguments.get(2));
      } else
        throw new RendererException(
          "function " + functionName + " expecting two or three arguments, got " + arguments.size());
      break;
    case REPLACE_FIRST:
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
}
