package org.metadatacenter.jsonss.core.settings;

import org.metadatacenter.jsonss.parser.JSONSSParserConstants;

public enum EmptyCellLocationDirectiveSetting
{
  ERROR_IF_EMPTY_LOCATION(JSONSSParserConstants.ERROR_IF_EMPTY_LOCATION),
  WARNING_IF_EMPTY_LOCATION(JSONSSParserConstants.WARNING_IF_EMPTY_LOCATION),
  SKIP_IF_EMPTY_LOCATION(JSONSSParserConstants.SKIP_IF_EMPTY_LOCATION),
  PROCESS_IF_EMPTY_LOCATION(JSONSSParserConstants.PROCESS_IF_EMPTY_LOCATION);

  private int value;

  private EmptyCellLocationDirectiveSetting(int value) {
    this.value = value;
  }

  public int getConstant()
  {
    return value;
  }
};


