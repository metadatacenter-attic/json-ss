package org.metadatacenter.jsonss.parser;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class ParserUtil implements JSONSSParserConstants
{
  private final static Set<String> nodeNames;

  static {
    nodeNames = new HashSet<>();

    Collections.addAll(nodeNames, JSONSSParserTreeConstants.jjtNodeName);
  }

  public static boolean hasName(Node node, String name) throws ParseException
  {
    if (!nodeNames.contains(name))
      throw new ParseException("internal processor error: invalid node name '" + name + "'");

    return node.toString().equals(name);
  }

  public static String getTokenName(int tokenID)
  {
    return tokenImage[tokenID].substring(1, tokenImage[tokenID].length() - 1);
  }

  // Returns -1 if name is invalid
  public static int getTokenID(String tokenName)
  {
    int tokenID = -1;

    for (int i = 0; i < tokenImage.length; i++)
      if (getTokenName(i).equals(tokenName)) {
        tokenID = i;
        break;
      }

    return tokenID;
  }
}
    
