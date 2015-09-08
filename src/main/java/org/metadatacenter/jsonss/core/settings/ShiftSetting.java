package org.metadatacenter.jsonss.core.settings;

import org.metadatacenter.jsonss.parser.JSONSSParserConstants;

public enum ShiftSetting
{
  NO_SHIFT(JSONSSParserConstants.MM_NO_SHIFT),
  SHIFT_UP(JSONSSParserConstants.MM_SHIFT_UP),
  SHIFT_DOWN(JSONSSParserConstants.MM_SHIFT_DOWN),
  SHIFT_LEFT(JSONSSParserConstants.MM_SHIFT_LEFT),
  SHIFT_RIGHT(JSONSSParserConstants.MM_SHIFT_RIGHT);

  private int value;

  private ShiftSetting(int value) {
    this.value = value;
  }

  public int getConstant()
  {
    return value;
  }
};
