package org.metadatacenter.jsonss.core.settings;

public class ReferenceSettings
{
  private String defaultLocationValue = "";
  private String defaultLiteralValue = "";
  private ReferenceTypeSetting referenceTypeSetting = ReferenceTypeSetting.JSON_STRING;
  private EmptyLocationSetting emptyLocationSetting = EmptyLocationSetting.PROCESS_IF_EMPTY_LOCATION;
  private EmptyLiteralSetting emptyLiteralSetting = EmptyLiteralSetting.PROCESS_IF_EMPTY_LITERAL;
  private ShiftSetting shiftSetting = ShiftSetting.NO_SHIFT;

  public ReferenceTypeSetting getReferenceTypeSetting()
  {
    return referenceTypeSetting;
  }

  public void setReferenceTypeSetting(ReferenceTypeSetting value)
  {
    referenceTypeSetting = value;
  }

  public String getDefaultLocationValue() { return this.defaultLocationValue; }

  public void setDefaultLocationValue(String defaultLocationValue) { this.defaultLocationValue = defaultLocationValue; }

  public String getDefaultLiteralValue() { return this.defaultLiteralValue; }

  public void setDefaultLiteralValue(String defaultLiteralValue) { this.defaultLiteralValue = defaultLiteralValue; }

  public EmptyLocationSetting getEmptyLocationSetting()
  {
    return emptyLocationSetting;
  }

  public void setEmptyLocationSetting(EmptyLocationSetting value)
  {
    this.emptyLocationSetting = value;
  }

  public EmptyLiteralSetting getEmptyLiteralSetting()
  {
    return emptyLiteralSetting;
  }

  public void setEmptyLiteralSetting(EmptyLiteralSetting value)
  {
    this.emptyLiteralSetting = value;
  }

  public ShiftSetting getShiftSetting()
  {
    return shiftSetting;
  }

  public void setShiftSetting(ShiftSetting value)
  {
    this.shiftSetting = value;
  }
}