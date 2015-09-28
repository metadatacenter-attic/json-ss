package org.metadatacenter.jsonss.parser.node;

import org.metadatacenter.jsonss.parser.ASTJSONBoolean;
import org.metadatacenter.jsonss.parser.ParseException;

public class JSONBooleanNode implements JSONSSNode
{
  private final boolean b;

  public JSONBooleanNode(ASTJSONBoolean node) throws ParseException
  {
    this.b = node.b;
  }

  public boolean getBoolean()
  {
    return this.b;
  }

  public String getNodeName()
  {
    return "JSONBoolean";
  }

  public String toString()
  {
    return "" + this.b;
  }
}
