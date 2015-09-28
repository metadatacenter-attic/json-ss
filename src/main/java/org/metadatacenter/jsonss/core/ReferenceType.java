package org.metadatacenter.jsonss.core;

import org.metadatacenter.jsonss.core.settings.ReferenceTypeSetting;
import org.metadatacenter.jsonss.parser.JSONSSParserConstants;

public class ReferenceType implements JSONSSParserConstants
{
  private final int type;

  public ReferenceType(int type)
  {
    this.type = type;
  }

  public String getTypeName()
  {
    return tokenImage[this.type].substring(1, tokenImage[this.type].length() - 1);
  }

  public boolean isLiteral()
  {
    return isString() || isNumber() || isBoolean();
  }

  public boolean isString()
  {
    return this.type == ReferenceTypeSetting.JSON_STRING.getConstant();
  }

  public boolean isNumber()
  {
    return this.type == ReferenceTypeSetting.JSON_NUMBER.getConstant();
  }

  public boolean isBoolean()
  {
    return this.type == ReferenceTypeSetting.JSON_BOOLEAN.getConstant();
  }

  public String toString()
  {
    return getTypeName();
  }

  public boolean equals(Object obj)
  {
    if (this == obj)
      return true;
    if (obj == null || obj.getClass() != this.getClass())
      return false;
    ReferenceType et = (ReferenceType)obj;
    return this.type == et.type;
  }

  public int hashCode()
  {
    int hash = 15;

    hash = hash + this.type;

    return hash;
  }
}
