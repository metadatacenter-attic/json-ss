package org.metadatacenter.jsonss.core.settings;

import org.metadatacenter.jsonss.parser.JSONSSParserConstants;

public enum ShiftDirectiveSetting
{
  NO_SHIFT(JSONSSParserConstants.NO_SHIFT),
  SHIFT_UP(JSONSSParserConstants.SHIFT_UP),
  SHIFT_DOWN(JSONSSParserConstants.SHIFT_DOWN),
  SHIFT_LEFT(JSONSSParserConstants.SHIFT_LEFT),
  SHIFT_RIGHT(JSONSSParserConstants.SHIFT_RIGHT);

  private int value;

  private ShiftDirectiveSetting(int value) {
    this.value = value;
  }

  public int getConstant()
  {
    return value;
  }
};
