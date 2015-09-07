package org.mm.core.settings;

import org.mm.parser.JSONSSParserConstants;

public enum EmptyLocationSetting
{
  ERROR_IF_EMPTY_LOCATION(JSONSSParserConstants.MM_ERROR_IF_EMPTY_LOCATION),
  WARNING_IF_EMPTY_LOCATION(JSONSSParserConstants.MM_WARNING_IF_EMPTY_LOCATION),
  SKIP_IF_EMPTY_LOCATION(JSONSSParserConstants.MM_SKIP_IF_EMPTY_LOCATION),
  PROCESS_IF_EMPTY_LOCATION(JSONSSParserConstants.MM_PROCESS_IF_EMPTY_LOCATION);

  private int value;

  private EmptyLocationSetting(int value) {
    this.value = value;
  }

  public int getConstant()
  {
    return value;
  }
};


