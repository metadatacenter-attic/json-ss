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

  public boolean isUntyped()
  {
    return this.type == UNTYPED;
  }

  public boolean isLiteral()
  {
    return isString() || isByte() || isShort() || isFloat() || isInt() || isLong() || isFloat()
      || isDouble() || isBoolean();
  }

  public boolean isString()
  {
    return this.type == JSON_STRING;
  }

  public boolean isByte()
  {
    return this.type == JSON_BYTE;
  }

  public boolean isShort()
  {
    return this.type == JSON_SHORT;
  }

  public boolean isInt()
  {
    return this.type == JSON_INTEGER;
  }

  public boolean isLong()
  {
    return this.type == JSON_LONG;
  }

  public boolean isFloat()
  {
    return this.type == JSON_FLOAT;
  }

  public boolean isDouble()
  {
    return this.type == JSON_DOUBLE;
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
