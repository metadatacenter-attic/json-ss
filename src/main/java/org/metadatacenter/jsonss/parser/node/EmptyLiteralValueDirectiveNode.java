package org.metadatacenter.jsonss.parser.node;

import org.metadatacenter.jsonss.core.settings.EmptyLiteralValueDirectiveSetting;
import org.metadatacenter.jsonss.parser.ASTEmptyLiteralValueDirective;
import org.metadatacenter.jsonss.parser.ParseException;
import org.metadatacenter.jsonss.parser.ParserUtil;

public class EmptyLiteralValueDirectiveNode implements JSONSSNode
{
  private final EmptyLiteralValueDirectiveSetting emptyLiteralValueDirectiveSetting;

  public EmptyLiteralValueDirectiveNode(ASTEmptyLiteralValueDirective node) throws ParseException
  {
    this.emptyLiteralValueDirectiveSetting = node.emptyLiteralValueDirectiveSetting;
  }

  public EmptyLiteralValueDirectiveSetting getEmptyLiteralValueDirectiveSetting() { return this.emptyLiteralValueDirectiveSetting; }

  @Override public String getNodeName()
  {
    return "EmptyLiteralValueDirective";
  }

  public String getEmptyLiteralDirectiveSettingName()
  {
    return ParserUtil.getTokenName(this.emptyLiteralValueDirectiveSetting.getConstant());
  }

  public String toString() { return getEmptyLiteralDirectiveSettingName(); }
}
