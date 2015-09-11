package org.metadatacenter.jsonss.parser.node;

import org.metadatacenter.jsonss.parser.ASTEmptyLocationDirective;
import org.metadatacenter.jsonss.parser.JSONSSParserConstants;
import org.metadatacenter.jsonss.parser.ParseException;
import org.metadatacenter.jsonss.parser.ParserUtil;

public class EmptyLocationDirectiveNode implements JSONSSNode, JSONSSParserConstants
{
  private final int emptyLocationSetting;

  public EmptyLocationDirectiveNode(ASTEmptyLocationDirective node) throws ParseException
  {
    this.emptyLocationSetting = node.emptyLocationSetting;
  }

  public int getEmptyLocationSetting() { return this.emptyLocationSetting; }

  public boolean isErrorIfEmpty() { return this.emptyLocationSetting == ERROR_IF_EMPTY_LOCATION; }

  public boolean isWarningIfEmpty() { return this.emptyLocationSetting == WARNING_IF_EMPTY_LOCATION; }

  public boolean isSkipIfEmpty() { return this.emptyLocationSetting == SKIP_IF_EMPTY_LOCATION; }

  public boolean isProcessIfEmpty() { return this.emptyLocationSetting == PROCESS_IF_EMPTY_LOCATION; }

  @Override public String getNodeName()
  {
    return "EmptyLocationDirective";
  }

  public String toString() { return ParserUtil.getTokenName(this.emptyLocationSetting); }
}
