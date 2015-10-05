package org.metadatacenter.jsonss.parser.node;

import org.metadatacenter.jsonss.parser.ASTDefaultCellLocationValueDirective;
import org.metadatacenter.jsonss.parser.JSONSSParserConstants;
import org.metadatacenter.jsonss.parser.ParseException;
import org.metadatacenter.jsonss.parser.ParserUtil;

public class DefaultCellLocationValueDirectiveNode implements JSONSSNode, JSONSSParserConstants
{
  private final String defaultCellLocationValue;

  public DefaultCellLocationValueDirectiveNode(ASTDefaultCellLocationValueDirective node) throws ParseException
  {
    this.defaultCellLocationValue = node.defaultCellLocationValue;
  }

  public String getDefaultCellLocationValue() { return this.defaultCellLocationValue; }

  @Override public String getNodeName()
  {
    return "DefaultCellLocationValueDirective";
  }

  public String toString()
  {
    String representation =
        ParserUtil.getTokenName(DEFAULT_LOCATION_VALUE) + "=\"" + this.defaultCellLocationValue + "\"";

    return representation;
  }

  public boolean equals(Object obj)
  {
    if (this == obj)
      return true;
    if (obj == null || obj.getClass() != this.getClass())
      return false;
    DefaultCellLocationValueDirectiveNode dv = (DefaultCellLocationValueDirectiveNode)obj;
    return this.defaultCellLocationValue != null && dv.defaultCellLocationValue != null && this.defaultCellLocationValue
        .equals(dv.defaultCellLocationValue);
  }

  public int hashCode()
  {
    int hash = 14;

    hash = hash + (null == this.defaultCellLocationValue ? 0 : this.defaultCellLocationValue.hashCode());

    return hash;
  }
}
