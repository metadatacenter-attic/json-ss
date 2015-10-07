package org.metadatacenter.jsonss.core;

import org.metadatacenter.jsonss.core.settings.EmptyCellLocationDirectiveSetting;
import org.metadatacenter.jsonss.core.settings.EmptyLiteralValueDirectiveSetting;
import org.metadatacenter.jsonss.core.settings.ReferenceDirectivesSettings;
import org.metadatacenter.jsonss.core.settings.ReferenceTypeDirectiveSetting;
import org.metadatacenter.jsonss.core.settings.ShiftDirectiveSetting;
import org.metadatacenter.jsonss.ss.CellLocation;

public class ReferenceDirectivesHandler
{
  private final ReferenceDirectivesSettings defaultReferenceDirectiveSettings;

  private boolean hasExplicitlySpecifiedReferenceTypeDirective;
  private boolean hasExplicitlySpecifiedDefaultCellLocationValueDirective;
  private boolean hasExplicitlySpecifiedDefaultLiteralValueDirective;
  private boolean hasExplicitlySpecifiedShiftDirective;
  private boolean hasExplicitlySpecifiedEmptyCellLocationDirective;
  private boolean hasExplicitlySpecifiedEmptyLiteralDirective;

  private ReferenceTypeDirectiveSetting explicitlySpecifiedReferenceTypeDirectiveSetting;
  private ShiftDirectiveSetting explicitlySpecifiedShiftDirectiveSetting;
  private EmptyCellLocationDirectiveSetting explicitlySpecifiedEmptyCellLocationDirectiveSetting;
  private EmptyLiteralValueDirectiveSetting explicitlySpecifiedEmptyLiteralValueDirectiveSetting;
  private CellLocation shiftedCellLocation;
  private String explicitlySpecifiedDefaultCellLocationValue;
  private String explicitlySpecifiedDefaultLiteralValue;

  public ReferenceDirectivesHandler(ReferenceDirectivesSettings defaultReferenceDirectiveSettings)
  {
    this.defaultReferenceDirectiveSettings = defaultReferenceDirectiveSettings;
  }

  public ReferenceDirectivesSettings getActualReferenceDirectivesSettings()
  {
    return new ReferenceDirectivesSettings(getActualReferenceTypeDirectiveSetting(),
      getActualEmptyCellLocationDirectiveSetting(), getActualEmptyLiteralValueDirectiveSetting(),
      getActualShiftDirectiveSetting(), getActualDefaultCellLocationValue(), getActualDefaultLiteralValue());
  }

  public boolean hasExplicitlySpecifiedOptions()
  {
    return this.hasExplicitlySpecifiedReferenceTypeDirective
      || this.hasExplicitlySpecifiedDefaultCellLocationValueDirective
      || this.hasExplicitlySpecifiedDefaultLiteralValueDirective || this.hasExplicitlySpecifiedShiftDirective
      || this.hasExplicitlySpecifiedEmptyCellLocationDirective || this.hasExplicitlySpecifiedEmptyLiteralDirective;
  }

  public boolean hasExplicitlySpecifiedReferenceTypeDirective()
  {
    return this.hasExplicitlySpecifiedReferenceTypeDirective;
  }

  public void setReferenceTypeDirective(ReferenceTypeDirectiveSetting referenceTypeDirectiveSetting)
  {
    this.explicitlySpecifiedReferenceTypeDirectiveSetting = referenceTypeDirectiveSetting;
    this.hasExplicitlySpecifiedReferenceTypeDirective = true;
  }

  public ReferenceTypeDirectiveSetting getActualReferenceTypeDirectiveSetting()
  {
    return hasExplicitlySpecifiedReferenceTypeDirective() ?
      this.explicitlySpecifiedReferenceTypeDirectiveSetting :
      this.defaultReferenceDirectiveSettings.getReferenceTypeDirectiveSetting();
  }

  public boolean hasExplicitlySpecifiedShiftDirective()
  {
    return this.hasExplicitlySpecifiedShiftDirective;
  }

  public void setShiftDirective(ShiftDirectiveSetting shiftDirectiveSetting)
  {
    this.hasExplicitlySpecifiedShiftDirective = true;
    this.explicitlySpecifiedShiftDirectiveSetting = shiftDirectiveSetting;
  }

  public ShiftDirectiveSetting getActualShiftDirectiveSetting()
  {
    return hasExplicitlySpecifiedShiftDirective() ?
      this.explicitlySpecifiedShiftDirectiveSetting :
      this.defaultReferenceDirectiveSettings.getShiftDirectiveSetting();
  }

  public boolean hasExplicitlySpecifiedEmptyCellLocationDirective()
  {
    return this.hasExplicitlySpecifiedEmptyCellLocationDirective;
  }

  public void setEmptyCellLocationDirective(EmptyCellLocationDirectiveSetting emptyCellLocationDirectiveSetting)
  {
    this.hasExplicitlySpecifiedEmptyCellLocationDirective = true;
    this.explicitlySpecifiedEmptyCellLocationDirectiveSetting = emptyCellLocationDirectiveSetting;
  }

  public EmptyCellLocationDirectiveSetting getActualEmptyCellLocationDirectiveSetting()
  {
    return hasExplicitlySpecifiedEmptyCellLocationDirective() ?
      this.explicitlySpecifiedEmptyCellLocationDirectiveSetting :
      this.defaultReferenceDirectiveSettings.getEmptyCellLocationDirectiveSetting();
  }

  public boolean hasExplicitlySpecifiedEmptyLiteralDirective()
  {
    return this.hasExplicitlySpecifiedEmptyLiteralDirective;
  }

  public void setEmptyLiteralDirective(EmptyLiteralValueDirectiveSetting emptyLiteralValueDirectiveSetting)
  {
    this.hasExplicitlySpecifiedEmptyLiteralDirective = true;
    this.explicitlySpecifiedEmptyLiteralValueDirectiveSetting = emptyLiteralValueDirectiveSetting;
  }

  public EmptyLiteralValueDirectiveSetting getActualEmptyLiteralValueDirectiveSetting()
  {
    return hasExplicitlySpecifiedEmptyLiteralDirective() ?
      this.explicitlySpecifiedEmptyLiteralValueDirectiveSetting :
      this.defaultReferenceDirectiveSettings.getEmptyLiteralValueDirectiveSetting();
  }

  public CellLocation getShiftedCellLocation()
  {
    return this.shiftedCellLocation;
  }

  public void setShiftedCellLocation(CellLocation shiftedCellLocation)
  {
    this.shiftedCellLocation = shiftedCellLocation;
  }

  public boolean hasExplicitlySpecifiedDefaultCellLocationValue()
  {
    return this.hasExplicitlySpecifiedDefaultCellLocationValueDirective;
  }

  public void setDefaultCellLocationValue(String cellLocationValue)
  {
    this.hasExplicitlySpecifiedDefaultCellLocationValueDirective = true;
    this.explicitlySpecifiedDefaultCellLocationValue = cellLocationValue;
  }

  public String getActualDefaultCellLocationValue()
  {
    return hasExplicitlySpecifiedDefaultCellLocationValue() ?
      this.explicitlySpecifiedDefaultCellLocationValue :
      this.defaultReferenceDirectiveSettings.getDefaultCellLocationValue();
  }

  public boolean hasExplicitlySpecifiedDefaultLiteralValue()
  {
    return this.hasExplicitlySpecifiedDefaultLiteralValueDirective;
  }

  public void setDefaultLiteralValue(String literalValue)
  {
    this.hasExplicitlySpecifiedDefaultLiteralValueDirective = true;
    this.explicitlySpecifiedDefaultLiteralValue = literalValue;
  }

  public String getActualDefaultLiteralValue()
  {
    return hasExplicitlySpecifiedDefaultLiteralValue() ?
      this.explicitlySpecifiedDefaultLiteralValue :
      this.defaultReferenceDirectiveSettings.getDefaultLiteralValue();
  }
}