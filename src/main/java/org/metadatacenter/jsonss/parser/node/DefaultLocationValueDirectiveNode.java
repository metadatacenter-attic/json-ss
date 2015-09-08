package org.metadatacenter.jsonss.parser.node;

import org.metadatacenter.jsonss.parser.ASTDefaultLocationValue;
import org.metadatacenter.jsonss.parser.JSONSSParserConstants;
import org.metadatacenter.jsonss.parser.ParseException;
import org.metadatacenter.jsonss.parser.ParserUtil;

public class DefaultLocationValueDirectiveNode implements MMNode, JSONSSParserConstants
{
  private final String defaultLocationValue;

  public DefaultLocationValueDirectiveNode(ASTDefaultLocationValue node) throws ParseException
  {
    this.defaultLocationValue = node.defaultLocationValue;
  }

  public String getDefaultLocationValue() { return this.defaultLocationValue; }

  @Override public String getNodeName()
  {
    return "DefaultLocationValueDirective";
  }

  public String toString()
  {
    String representation =
        ParserUtil.getTokenName(MM_DEFAULT_LOCATION_VALUE) + "=\"" + this.defaultLocationValue + "\"";

    return representation;
  }

  public boolean equals(Object obj)
  {
    if (this == obj)
      return true;
    if (obj == null || obj.getClass() != this.getClass())
      return false;
    DefaultLocationValueDirectiveNode dv = (DefaultLocationValueDirectiveNode)obj;
    return this.defaultLocationValue != null && dv.defaultLocationValue != null && this.defaultLocationValue
        .equals(dv.defaultLocationValue);
  }

  public int hashCode()
  {
    int hash = 14;

    hash = hash + (null == this.defaultLocationValue ? 0 : this.defaultLocationValue.hashCode());

    return hash;
  }
}
