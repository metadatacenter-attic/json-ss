package org.metadatacenter.jsonss.parser.node;

import org.metadatacenter.jsonss.parser.ASTReference;
import org.metadatacenter.jsonss.parser.InternalParseException;
import org.metadatacenter.jsonss.parser.ParseException;
import org.metadatacenter.jsonss.parser.ParserUtil;
import org.metadatacenter.jsonss.parser.ASTStringLiteral;
import org.metadatacenter.jsonss.parser.ASTValueExtractionFunctionArgument;
import org.metadatacenter.jsonss.parser.Node;

public class ValueExtractionFunctionArgumentNode implements JSONSSNode
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
