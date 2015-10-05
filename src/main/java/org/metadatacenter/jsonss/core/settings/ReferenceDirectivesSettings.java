package org.metadatacenter.jsonss.core.settings;

public class ReferenceDirectivesSettings
{
  private final ReferenceTypeDirectiveSetting referenceTypeDirectiveSetting;
  private final EmptyCellLocationDirectiveSetting emptyCellLocationDirectiveSetting;
  private final EmptyLiteralValueDirectiveSetting emptyLiteralValueDirectiveSetting;
  private final ShiftDirectiveSetting shiftDirectiveSetting;
  private final String defaultCellLocationValue;
  private final String defaultLiteralValue;

  public ReferenceDirectivesSettings()
  {
    this.referenceTypeDirectiveSetting = ReferenceTypeDirectiveSetting.JSON_STRING;
    this.emptyCellLocationDirectiveSetting = EmptyCellLocationDirectiveSetting.PROCESS_IF_EMPTY_LOCATION;
    this.emptyLiteralValueDirectiveSetting = EmptyLiteralValueDirectiveSetting.PROCESS_IF_EMPTY_LITERAL;
    this.shiftDirectiveSetting = ShiftDirectiveSetting.NO_SHIFT;
    this.defaultCellLocationValue = "";
    this.defaultLiteralValue = "";
  }

  public ReferenceDirectivesSettings(ReferenceTypeDirectiveSetting referenceTypeDirectiveSetting,
    EmptyCellLocationDirectiveSetting emptyCellLocationDirectiveSetting, EmptyLiteralValueDirectiveSetting emptyLiteralValueDirectiveSetting, ShiftDirectiveSetting shiftDirectiveSetting,
    String defaultCellLocationValue, String defaultLiteralValue)
  {
    this.referenceTypeDirectiveSetting = referenceTypeDirectiveSetting;
    this.emptyCellLocationDirectiveSetting = emptyCellLocationDirectiveSetting;
    this.emptyLiteralValueDirectiveSetting = emptyLiteralValueDirectiveSetting;
    this.shiftDirectiveSetting = shiftDirectiveSetting;
    this.defaultCellLocationValue = defaultCellLocationValue;
    this.defaultLiteralValue = defaultLiteralValue;
  }

  public ReferenceDirectivesSettings(ReferenceDirectivesSettings referenceDirectivesSettings)
  {
    this.referenceTypeDirectiveSetting = referenceDirectivesSettings.referenceTypeDirectiveSetting;
    this.emptyCellLocationDirectiveSetting = referenceDirectivesSettings.emptyCellLocationDirectiveSetting;
    this.emptyLiteralValueDirectiveSetting = referenceDirectivesSettings.emptyLiteralValueDirectiveSetting;
    this.shiftDirectiveSetting = referenceDirectivesSettings.shiftDirectiveSetting;
    this.defaultCellLocationValue = referenceDirectivesSettings.defaultCellLocationValue;
    this.defaultLiteralValue = referenceDirectivesSettings.defaultLiteralValue;
  }

  public ReferenceTypeDirectiveSetting getReferenceTypeDirectiveSetting()
  {
    return this.referenceTypeDirectiveSetting;
  }

  public EmptyCellLocationDirectiveSetting getEmptyCellLocationDirectiveSetting()
  {
    return this.emptyCellLocationDirectiveSetting;
  }

  public EmptyLiteralValueDirectiveSetting getEmptyLiteralValueDirectiveSetting()
  {
    return this.emptyLiteralValueDirectiveSetting;
  }

  public ShiftDirectiveSetting getShiftDirectiveSetting()
  {
    return this.shiftDirectiveSetting;
  }

  public String getDefaultCellLocationValue() { return this.defaultCellLocationValue; }

  public String getDefaultLiteralValue() { return this.defaultLiteralValue; }
}