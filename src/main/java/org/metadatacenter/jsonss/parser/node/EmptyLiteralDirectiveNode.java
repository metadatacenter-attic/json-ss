package org.metadatacenter.jsonss.parser.node;

import org.metadatacenter.jsonss.parser.ASTEmptyLiteralSetting;
import org.metadatacenter.jsonss.parser.ParseException;
import org.metadatacenter.jsonss.parser.ParserUtil;
import org.metadatacenter.jsonss.parser.JSONSSParserConstants;

public class EmptyLiteralDirectiveNode implements MMNode, JSONSSParserConstants
{
  private final int emptyLiteralSetting;

  public EmptyLiteralDirectiveNode(ASTEmptyLiteralSetting node) throws ParseException
  {
    this.emptyLiteralSetting = node.emptyLiteralSetting;
  }

  public int getEmptyLiteralSetting() { return this.emptyLiteralSetting; }

  public boolean isErrorIfEmpty() { return this.emptyLiteralSetting == MM_ERROR_IF_EMPTY_LITERAL; }

  public boolean isWarningIfEmpty() { return this.emptyLiteralSetting == MM_WARNING_IF_EMPTY_LITERAL; }

  public boolean isSkipIfEmpty() { return this.emptyLiteralSetting == MM_SKIP_IF_EMPTY_LITERAL; }

  public boolean isProcessIfEmpty() { return this.emptyLiteralSetting == MM_PROCESS_IF_EMPTY_LITERAL; }

  @Override public String getNodeName()
  {
    return "EmptyLiteralDirective";
  }

  public String toString() { return ParserUtil.getTokenName(this.emptyLiteralSetting); }
}
