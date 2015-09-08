package org.metadatacenter.jsonss.core.settings;

import org.metadatacenter.jsonss.parser.JSONSSParserConstants;

public enum EmptyLiteralSetting
{
  ERROR_IF_EMPTY_LITERAL(JSONSSParserConstants.MM_ERROR_IF_EMPTY_LITERAL),
  WARNING_IF_EMPTY_LITERAL(JSONSSParserConstants.MM_WARNING_IF_EMPTY_LITERAL),
  SKIP_IF_EMPTY_LITERAL(JSONSSParserConstants.MM_SKIP_IF_EMPTY_LITERAL),
  PROCESS_IF_EMPTY_LITERAL(JSONSSParserConstants.MM_PROCESS_IF_EMPTY_LITERAL);

  private int value;

  private EmptyLiteralSetting(int value) {
    this.value = value;
  }

  public int getConstant()
  {
    return value;
  }
};


