package org.mm.parser.node;

import org.mm.parser.ASTReference;
import org.mm.parser.ASTStringLiteral;
import org.mm.parser.ASTValueExtractionFunctionArgument;
import org.mm.parser.InternalParseException;
import org.mm.parser.Node;
import org.mm.parser.ParseException;
import org.mm.parser.ParserUtil;

public class ValueExtractionFunctionArgumentNode implements MMNode
{
  private ReferenceNode referenceNode;
  private StringLiteralNode stringLiteralNode;

  public ValueExtractionFunctionArgumentNode(ASTValueExtractionFunctionArgument node) throws ParseException
  {
    if (node.jjtGetNumChildren() != 1)
      throw new InternalParseException("expecting one child node for node " + getNodeName());
    else {
      Node child = node.jjtGetChild(0);
      if (ParserUtil.hasName(child, "StringLiteral"))
        this.stringLiteralNode = new StringLiteralNode((ASTStringLiteral)child);
      else if (ParserUtil.hasName(child, "Reference"))
        this.referenceNode = new ReferenceNode((ASTReference)child);
      else
        throw new InternalParseException("unexpected child node " + child + " for node " + getNodeName());
    }
  }

  public ReferenceNode getReferenceNode()
  {
    return this.referenceNode;
  }

  public StringLiteralNode getStringLiteralNode()
  {
    return this.stringLiteralNode;
  }

  public boolean isStringLiteralNode()
  {
    return this.stringLiteralNode != null;
  }

  public boolean isReferenceNode()
  {
    return this.referenceNode != null;
  }

  @Override public String getNodeName()
  {
    return "ValueExtractionFunctionArgument";
  }

  public String toString()
  {
    if (isStringLiteralNode())
      return this.stringLiteralNode.toString();
    else if (isReferenceNode())
      return this.referenceNode.toString();
    else
      return "";
  }
}
