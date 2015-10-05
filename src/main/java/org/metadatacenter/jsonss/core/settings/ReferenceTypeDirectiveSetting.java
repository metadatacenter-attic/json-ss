package org.metadatacenter.jsonss.core.settings;

import org.metadatacenter.jsonss.parser.JSONSSParserConstants;

public enum ReferenceTypeDirectiveSetting
{
  JSON_STRING(JSONSSParserConstants.JSON_STRING),
  JSON_BOOLEAN(JSONSSParserConstants.JSON_BOOLEAN),
  JSON_NUMBER(JSONSSParserConstants.JSON_NUMBER);

  private int value;

  private ReferenceTypeDirectiveSetting(int value)
  {
    this.value = value;
  }

  public int getConstant()
  {
    return value;
  }

  public boolean isString() { return this.value == JSONSSParserConstants.JSON_STRING; }

  public boolean isBoolean() { return this.value == JSONSSParserConstants.JSON_BOOLEAN; }

  public boolean isNumber() { return this.value == JSONSSParserConstants.JSON_NUMBER; }

  public boolean isLiteral() { return isString() || isNumber() || isBoolean(); }
};
