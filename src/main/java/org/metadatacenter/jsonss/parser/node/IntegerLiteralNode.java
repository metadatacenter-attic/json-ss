package org.metadatacenter.jsonss.parser.node;

import org.metadatacenter.jsonss.parser.ASTIntegerLiteral;
import org.metadatacenter.jsonss.parser.ParseException;

public class IntegerLiteralNode implements JSONSSNode
{
  private final int value;

  IntegerLiteralNode(ASTIntegerLiteral node) throws ParseException
  {
    this.value = node.value;
  }

  public int getValue() { return this.value; }

  @Override public String getNodeName()
  {
    return "IntegerLiteral";
  }

  public String toString() { return "" + this.value; }
}
