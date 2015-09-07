package org.mm.parser.node;

import org.mm.parser.ASTMMDefaultReferenceType;
import org.mm.parser.ASTMMDirective;
import org.mm.parser.InternalParseException;
import org.mm.parser.Node;
import org.mm.parser.ParseException;
import org.mm.parser.ParserUtil;

public class MMDirectiveNode implements MMNode
{
  private MMDefaultReferenceTypeDirectiveNode defaultReferenceTypeNode;

  public MMDirectiveNode(ASTMMDirective node) throws ParseException
  {
    for (int i = 0; i < node.jjtGetNumChildren(); i++) {
      Node child = node.jjtGetChild(i);

      if (ParserUtil.hasName(child, "MMDefaultReferenceType")) {
        this.defaultReferenceTypeNode = new MMDefaultReferenceTypeDirectiveNode((ASTMMDefaultReferenceType)child);
      } else
        throw new InternalParseException("invalid child node " + child + " to MMExpression");
    }
  }

  public MMDefaultReferenceTypeDirectiveNode getDefaultReferenceTypeNode()
  {
    return this.defaultReferenceTypeNode;
  }

  public boolean hasDefaultReferenceType()
  {
    return this.defaultReferenceTypeNode != null;
  }

  @Override public String getNodeName()
  {
    return "MMDirective";
  }

  public String toString()
  {
    String representation = "";

    if (hasDefaultReferenceType())
      representation += this.defaultReferenceTypeNode.toString();

    return representation;
  }

}
