package org.metadatacenter.jsonss.parser.node;

import org.metadatacenter.jsonss.parser.ASTShiftSetting;
import org.metadatacenter.jsonss.parser.ParseException;
import org.metadatacenter.jsonss.parser.ParserUtil;
import org.metadatacenter.jsonss.parser.JSONSSParserConstants;

public class ShiftDirectiveNode implements MMNode, JSONSSParserConstants
{
  private final int shiftSetting;

  public ShiftDirectiveNode(ASTShiftSetting node) throws ParseException
  {
    this.shiftSetting = node.shiftSetting;
  }

  public boolean isNoShift() { return this.shiftSetting == MM_NO_SHIFT; }

  public boolean isShiftLeft() { return this.shiftSetting == MM_SHIFT_LEFT; }

  public boolean isShiftRight() { return this.shiftSetting == MM_SHIFT_RIGHT; }

  public boolean isShiftUp() { return this.shiftSetting == MM_SHIFT_UP; }

  public boolean isShiftDown() { return this.shiftSetting == MM_SHIFT_DOWN; }

  public int getShiftSetting() { return this.shiftSetting; }

  public String getShiftSettingName() { return ParserUtil.getTokenName(this.shiftSetting); }

  @Override public String getNodeName()
  {
    return "ShiftDirective";
  }

  public String toString() { return getShiftSettingName(); }
}
