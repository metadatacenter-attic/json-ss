package org.metadatacenter.jsonss.parser.node;

import org.metadatacenter.jsonss.parser.ASTReferenceQualifiedCellLocationSpecification;
import org.metadatacenter.jsonss.parser.ParseException;

public class ReferenceQualifiedCellLocationSpecificationNode implements JSONSSNode
{
  private final String sourceSpecification, cellLocationSpecification;
  private final String literal;

  public ReferenceQualifiedCellLocationSpecificationNode(ASTReferenceQualifiedCellLocationSpecification node)
    throws ParseException
  {
    this.sourceSpecification = node.sourceSpecification;
    this.cellLocationSpecification = node.cellLocationSpecification;
    this.literal = node.literal;
  }

  public boolean hasSourceSpecification() { return this.sourceSpecification != null; }

  public boolean hasCellLocationSpecification() { return this.cellLocationSpecification != null; }

  public boolean hasLiteral() { return this.literal != null; }

  public String getSourceSpecification() { return this.sourceSpecification; }

  public String getCellLocationSpecification() { return this.cellLocationSpecification; }

  public String getLiteral() { return this.literal; }

  @Override public String getNodeName()
  {
    return "ReferenceQualifiedCellLocationSpecification";
  }

  public String toString()
  {
    String representation = "@";

    if (hasSourceSpecification())
      representation += "'" + this.sourceSpecification + "'!";

    if (hasCellLocationSpecification())
      representation += this.cellLocationSpecification;
    else // literal
      representation += "\"" + this.literal + "\"";

    return representation;
  }
}
