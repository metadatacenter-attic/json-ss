package org.metadatacenter.jsonss.parser.node;

import org.metadatacenter.jsonss.parser.ASTReferenceCellLocationSpecification;
import org.metadatacenter.jsonss.parser.ParseException;

public class ReferenceCellLocationSpecificationNode implements JSONSSNode
{
  private final String source, location;
  private final String literal;

  public ReferenceCellLocationSpecificationNode(ASTReferenceCellLocationSpecification node) throws ParseException
  {
    this.source = node.source;
    this.location = node.location;
    this.literal = node.literal;
  }

  public boolean hasSource() { return this.source != null; }

  public boolean hasLocation() { return this.location != null; }

  public boolean hasLiteral() { return this.literal != null; }

  public String getSource() { return this.source; }

  public String getLocation() { return this.location; }

  public String getLiteral() { return this.literal; }

  @Override public String getNodeName()
  {
    return "ReferenceCellLocationSpecification";
  }

  public String toString()
  {
    String representation = "@";

    if (hasSource())
      representation += "'" + this.source + "'!";

    if (hasLocation())
      representation += this.location;
    else // literal
      representation += "\"" + this.literal + "\"";

    return representation;
  }
}
