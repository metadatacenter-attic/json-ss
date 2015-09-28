package org.metadatacenter.jsonss.core.settings;

import org.metadatacenter.jsonss.parser.JSONSSParserConstants;

public enum ReferenceTypeSetting {
  JSON_STRING(JSONSSParserConstants.JSON_STRING),
  JSON_BOOLEAN(JSONSSParserConstants.JSON_BOOLEAN),
  JSON_NUMBER(JSONSSParserConstants.JSON_NUMBER);

  private int value;

  private ReferenceTypeSetting(int value) {
    this.value = value;
  }

  public int getConstant()
  {
    return value;
  }
};
