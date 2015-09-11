package org.metadatacenter.jsonss.parser.node;

import org.metadatacenter.jsonss.core.ReferenceType;
import org.metadatacenter.jsonss.parser.ParseException;
import org.metadatacenter.jsonss.parser.ASTReferenceTypeDirective;
import org.metadatacenter.jsonss.parser.JSONSSParserConstants;

public class ReferenceTypeDirectiveNode implements JSONSSNode, JSONSSParserConstants
{
  private final ReferenceType referenceType;

  public ReferenceTypeDirectiveNode(ASTReferenceTypeDirective node) throws ParseException
  {
    this.referenceType = new ReferenceType(node.referenceType);
  }

  public ReferenceTypeDirectiveNode(int type)
  {
    this.referenceType = new ReferenceType(type);
  }

  public ReferenceTypeDirectiveNode(ReferenceType referenceType)
  {
    this.referenceType = referenceType;
  }

  public ReferenceType getReferenceType()
  {
    return this.referenceType;
  }

  @Override public String getNodeName()
  {
    return "ReferenceTypeDirective";
  }

  public String toString()
  {
    return this.referenceType.toString();
  }
}
