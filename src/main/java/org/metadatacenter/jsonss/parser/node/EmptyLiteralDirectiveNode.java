package org.metadatacenter.jsonss.parser.node;

import org.metadatacenter.jsonss.parser.ASTEmptyLiteralDirective;
import org.metadatacenter.jsonss.parser.ParseException;
import org.metadatacenter.jsonss.parser.ParserUtil;
import org.metadatacenter.jsonss.parser.JSONSSParserConstants;

public class EmptyLiteralDirectiveNode implements JSONSSNode, JSONSSParserConstants
{
  private final int emptyLiteralSetting;

  public EmptyLiteralDirectiveNode(ASTEmptyLiteralDirective node) throws ParseException
  {
    this.emptyLiteralSetting = node.emptyLiteralSetting;
  }

  public int getEmptyLiteralSetting() { return this.emptyLiteralSetting; }

  public boolean isErrorIfEmpty() { return this.emptyLiteralSetting == ERROR_IF_EMPTY_LITERAL; }

  public boolean isWarningIfEmpty() { return this.emptyLiteralSetting == WARNING_IF_EMPTY_LITERAL; }

  public boolean isSkipIfEmpty() { return this.emptyLiteralSetting == SKIP_IF_EMPTY_LITERAL; }

  public boolean isProcessIfEmpty() { return this.emptyLiteralSetting == PROCESS_IF_EMPTY_LITERAL; }

  @Override public String getNodeName()
  {
    return "EmptyLiteralDirective";
  }

  public String toString() { return ParserUtil.getTokenName(this.emptyLiteralSetting); }
}
