package org.metadatacenter.jsonss.parser.node;

import org.metadatacenter.jsonss.parser.ASTJSONNull;
import org.metadatacenter.jsonss.parser.ParseException;

public class JSONNullNode implements JSONSSNode
{
  public JSONNullNode(ASTJSONNull node) throws ParseException
  {
  }

  public String getNodeName()
  {
    return "JSONNull";
  }

  public String toString()
  {
    return "null";
  }
}

