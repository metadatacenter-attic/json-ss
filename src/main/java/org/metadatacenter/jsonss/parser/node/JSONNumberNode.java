package org.metadatacenter.jsonss.parser.node;

import org.metadatacenter.jsonss.parser.ASTJSONNumber;
import org.metadatacenter.jsonss.parser.ParseException;

public class JSONNumberNode implements JSONSSNode
{
  private final Number n;

  public JSONNumberNode(ASTJSONNumber node) throws ParseException
  {
    this.n = node.n;
  }

  public Number getNumber()
  {
    return this.n;
  }

  public String getNodeName()
  {
    return "JSONNumber";
  }

  public String toString()
  {
    return "" + this.n;
  }
}

