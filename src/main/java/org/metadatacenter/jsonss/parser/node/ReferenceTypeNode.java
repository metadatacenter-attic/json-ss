package org.metadatacenter.jsonss.parser.node;

import org.metadatacenter.jsonss.core.ReferenceType;
import org.metadatacenter.jsonss.parser.ParseException;
import org.metadatacenter.jsonss.parser.ASTReferenceType;
import org.metadatacenter.jsonss.parser.JSONSSParserConstants;

public class ReferenceTypeNode implements MMNode, JSONSSParserConstants
{
  private final ReferenceType referenceType;

  public ReferenceTypeNode(ASTReferenceType node) throws ParseException
  {
    this.referenceType = new ReferenceType(node.referenceType);
  }

  public ReferenceTypeNode(int type)
  {
    this.referenceType = new ReferenceType(type);
  }

  public ReferenceTypeNode(ReferenceType referenceType)
  {
    this.referenceType = referenceType;
  }

  public ReferenceType getReferenceType()
  {
    return this.referenceType;
  }

  @Override public String getNodeName()
  {
    return "ReferenceType";
  }

  public String toString()
  {
    return this.referenceType.toString();
  }
}
