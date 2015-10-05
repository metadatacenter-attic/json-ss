package org.metadatacenter.jsonss.core.settings;

public class ReferenceSettings
{
  private final ReferenceTypeSetting referenceTypeSetting;
  private final EmptyLocationSetting emptyLocationSetting;
  private final EmptyLiteralSetting emptyLiteralSetting;
  private final ShiftSetting shiftSetting;
  private final String defaultLocationValue;
  private final String defaultLiteralValue;

  public ReferenceSettings()
  {
    this.referenceTypeSetting = ReferenceTypeSetting.JSON_STRING;
    this.emptyLocationSetting = EmptyLocationSetting.PROCESS_IF_EMPTY_LOCATION;
    this.emptyLiteralSetting = EmptyLiteralSetting.PROCESS_IF_EMPTY_LITERAL;
    this.shiftSetting = ShiftSetting.NO_SHIFT;
    this.defaultLocationValue = "";
    this.defaultLiteralValue = "";
  }

  public ReferenceSettings(ReferenceTypeSetting referenceTypeSetting, EmptyLocationSetting emptyLocationSetting,
      EmptyLiteralSetting emptyLiteralSetting, ShiftSetting shiftSetting, String defaultLocationValue,
      String defaultLiteralValue)
  {
    this.referenceTypeSetting = referenceTypeSetting;
    this.emptyLocationSetting = emptyLocationSetting;
    this.emptyLiteralSetting = emptyLiteralSetting;
    this.shiftSetting = shiftSetting;
    this.defaultLocationValue = defaultLocationValue;
    this.defaultLiteralValue = defaultLiteralValue;
  }

  public ReferenceSettings(ReferenceSettings referenceSettings)
  {
    this.referenceTypeSetting = referenceSettings.referenceTypeSetting;
    this.emptyLocationSetting = referenceSettings.emptyLocationSetting;
    this.emptyLiteralSetting = referenceSettings.emptyLiteralSetting;
    this.shiftSetting = referenceSettings.shiftSetting;
    this.defaultLocationValue = referenceSettings.defaultLocationValue;
    this.defaultLiteralValue = referenceSettings.defaultLiteralValue;
  }

  public ReferenceTypeSetting getReferenceTypeSetting()
  {
    return this.referenceTypeSetting;
  }

  public EmptyLocationSetting getEmptyLocationSetting()
  {
    return emptyLocationSetting;
  }

  public EmptyLiteralSetting getEmptyLiteralSetting()
  {
    return this.emptyLiteralSetting;
  }

  public ShiftSetting getShiftSetting()
  {
    return this.shiftSetting;
  }

  public String getDefaultLocationValue() { return this.defaultLocationValue; }

  public String getDefaultLiteralValue() { return this.defaultLiteralValue; }
}