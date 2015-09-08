package org.metadatacenter.jsonss.parser.node;

import org.metadatacenter.jsonss.parser.ASTMMDefaultReferenceType;
import org.metadatacenter.jsonss.parser.ASTMMDirective;
import org.metadatacenter.jsonss.parser.InternalParseException;
import org.metadatacenter.jsonss.parser.Node;
import org.metadatacenter.jsonss.parser.ParseException;
import org.metadatacenter.jsonss.parser.ParserUtil;

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
