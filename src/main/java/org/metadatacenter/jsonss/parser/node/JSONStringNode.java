package org.metadatacenter.jsonss.parser.node;

import org.metadatacenter.jsonss.parser.ASTJSONString;
import org.metadatacenter.jsonss.parser.ParseException;

public class JSONStringNode implements JSONSSNode
{
  private final String s;

  public JSONStringNode(ASTJSONString node) throws ParseException
  {
    this.s = node.s;
  }

  public String getString()
  {
    return this.s;
  }

  public String getNodeName()
  {
    return "JSONString";
  }

  public String toString()
  {
    return this.s;
  }
}
