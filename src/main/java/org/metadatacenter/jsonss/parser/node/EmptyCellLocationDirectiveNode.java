package org.metadatacenter.jsonss.parser.node;

import org.metadatacenter.jsonss.core.settings.EmptyCellLocationDirectiveSetting;
import org.metadatacenter.jsonss.parser.ASTEmptyCellLocationDirective;
import org.metadatacenter.jsonss.parser.ParseException;
import org.metadatacenter.jsonss.parser.ParserUtil;

public class EmptyCellLocationDirectiveNode implements JSONSSNode
{
  private final EmptyCellLocationDirectiveSetting emptyCellLocationDirectiveSetting;

  public EmptyCellLocationDirectiveNode(ASTEmptyCellLocationDirective node) throws ParseException
  {
    this.emptyCellLocationDirectiveSetting = node.emptyCellLocationDirectiveSetting;
  }

  public EmptyCellLocationDirectiveSetting getEmptyCellLocationDirectiveSetting() { return this.emptyCellLocationDirectiveSetting; }

  @Override public String getNodeName()
  {
    return "EmptyCellLocationDirective";
  }

  public String getEmptyLocationDirectiveSettingName()
  {
    return ParserUtil.getTokenName(this.emptyCellLocationDirectiveSetting.getConstant());
  }

  @Override public String toString() { return getEmptyLocationDirectiveSettingName(); }
}
