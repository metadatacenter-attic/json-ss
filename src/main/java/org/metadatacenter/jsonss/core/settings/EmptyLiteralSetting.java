package org.metadatacenter.jsonss.core.settings;

import org.metadatacenter.jsonss.parser.JSONSSParserConstants;

public enum EmptyLiteralSetting
{
  ERROR_IF_EMPTY_LITERAL(JSONSSParserConstants.ERROR_IF_EMPTY_LITERAL),
  WARNING_IF_EMPTY_LITERAL(JSONSSParserConstants.WARNING_IF_EMPTY_LITERAL),
  SKIP_IF_EMPTY_LITERAL(JSONSSParserConstants.SKIP_IF_EMPTY_LITERAL),
  PROCESS_IF_EMPTY_LITERAL(JSONSSParserConstants.PROCESS_IF_EMPTY_LITERAL);

  private int value;

  private EmptyLiteralSetting(int value) {
    this.value = value;
  }

  public int getConstant()
  {
    return value;
  }
};


