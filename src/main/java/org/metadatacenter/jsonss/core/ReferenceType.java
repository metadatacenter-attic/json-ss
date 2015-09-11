package org.metadatacenter.jsonss.core;

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
    return isString() || isInteger() || isNumeric() || isBoolean();
  }

  public boolean isString()
  {
    return this.type == JSON_STRING;
  }

  public boolean isInteger()
  {
    return this.type == JSON_INTEGER;
  }

  public boolean isNumeric()
  {
    return this.type == JSON_NUMERIC;
  }

  public boolean isBoolean()
  {
    return this.type == JSON_BOOLEAN;
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
