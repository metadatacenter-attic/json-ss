package org.metadatacenter.jsonss.parser.node;

import org.metadatacenter.jsonss.parser.ASTDefaultReferenceType;
import org.metadatacenter.jsonss.parser.ASTReferenceDirective;
import org.metadatacenter.jsonss.parser.InternalParseException;
import org.metadatacenter.jsonss.parser.Node;
import org.metadatacenter.jsonss.parser.ParseException;
import org.metadatacenter.jsonss.parser.ParserUtil;

public class ReferenceDirectiveNode implements JSONSSNode
{
  private DefaultReferenceTypeNode defaultReferenceTypeNode;

  public ReferenceDirectiveNode(ASTReferenceDirective node) throws ParseException
  {
    for (int i = 0; i < node.jjtGetNumChildren(); i++) {
      Node child = node.jjtGetChild(i);

      if (ParserUtil.hasName(child, "DefaultReferenceType")) {
        this.defaultReferenceTypeNode = new DefaultReferenceTypeNode((ASTDefaultReferenceType)child);
      } else
        throw new InternalParseException("invalid child node " + child + " to MMExpression");
    }
  }

  public DefaultReferenceTypeNode getDefaultReferenceTypeNode()
  {
    return this.defaultReferenceTypeNode;
  }

  public boolean hasDefaultReferenceType()
  {
    return this.defaultReferenceTypeNode != null;
  }

  @Override public String getNodeName()
  {
    return "Directive";
  }

  public String toString()
  {
    String representation = "";

    if (hasDefaultReferenceType())
      representation += this.defaultReferenceTypeNode.toString();

    return representation;
  }

}
