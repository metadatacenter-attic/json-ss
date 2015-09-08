package org.metadatacenter.jsonss.parser.node;

import org.metadatacenter.jsonss.core.ReferenceType;
import org.metadatacenter.jsonss.parser.ASTMMDefaultReferenceType;
import org.metadatacenter.jsonss.parser.InternalParseException;
import org.metadatacenter.jsonss.parser.Node;
import org.metadatacenter.jsonss.parser.ParseException;
import org.metadatacenter.jsonss.parser.ParserUtil;
import org.metadatacenter.jsonss.parser.ASTReferenceType;

public class MMDefaultReferenceTypeDirectiveNode implements MMNode
{
  private ReferenceTypeNode referenceTypeNode;

  public MMDefaultReferenceTypeDirectiveNode(ASTMMDefaultReferenceType node) throws ParseException
  {
    if (node.jjtGetNumChildren() != 1)
      throw new InternalParseException("expecting one ReferenceType child of node " + getNodeName());
    else {
      Node child = node.jjtGetChild(0);
      if (ParserUtil.hasName(child, "ReferenceType"))
        this.referenceTypeNode = new ReferenceTypeNode((ASTReferenceType)child);
      else
        throw new InternalParseException(
          "expecting ReferenceType child, got " + child + " for node " + getNodeName());
    }
  }

  public ReferenceType getReferenceType()
  {
    return this.referenceTypeNode.getReferenceType();
  }

  @Override public String getNodeName()
  {
    return "MMDefaultReferenceTypeDirective";
  }

  public String toString()
  {
    return "MM:DefaultReferenceType " + this.referenceTypeNode;
  }
}
