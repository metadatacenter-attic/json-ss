package org.metadatacenter.jsonss.parser.node;

import org.metadatacenter.jsonss.core.settings.ShiftDirectiveSetting;
import org.metadatacenter.jsonss.parser.ASTShiftDirective;
import org.metadatacenter.jsonss.parser.ParseException;
import org.metadatacenter.jsonss.parser.ParserUtil;

public class ShiftDirectiveNode implements JSONSSNode
{
  private final ShiftDirectiveSetting shiftDirectiveSetting;

  public ShiftDirectiveNode(ASTShiftDirective node) throws ParseException
  {
    this.shiftDirectiveSetting = node.shiftDirectiveSetting;
  }

  public ShiftDirectiveSetting getShiftDirectiveSetting() { return this.shiftDirectiveSetting; }

  public String getShiftDirectiveSettingName()
  {
    return ParserUtil.getTokenName(this.shiftDirectiveSetting.getConstant());
  }

  @Override public String getNodeName()
  {
    return "ShiftDirective";
  }

  @Override public String toString() { return getShiftDirectiveSettingName(); }
}
