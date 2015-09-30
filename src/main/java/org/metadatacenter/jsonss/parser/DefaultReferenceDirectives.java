package org.metadatacenter.jsonss.parser;

import org.metadatacenter.jsonss.core.ReferenceType;

public class DefaultReferenceDirectives
{
  private final ReferenceType defaultReferenceType;
  private final String defaultCellLocationValue;
  private final String defaultLiteralValue;
  private final int defaultEmptyCellLocationDirective;
  private final int defaultEmptyLiteralDirective;
  private final int defaultShiftDirective;

  public DefaultReferenceDirectives(int defaultReferenceType, String defaultLocationValue, String defaultLiteralValue,
    int defaultShiftDirective, int defaultEmptyCellLocationDirective, int defaultEmptyLiteralDirective)
  {
    this.defaultReferenceType = new ReferenceType(defaultReferenceType);
    this.defaultCellLocationValue = defaultLocationValue;
    this.defaultLiteralValue = defaultLiteralValue;
    this.defaultEmptyCellLocationDirective = defaultEmptyCellLocationDirective;
    this.defaultEmptyLiteralDirective = defaultEmptyLiteralDirective;
    this.defaultShiftDirective = defaultShiftDirective;
  }

  public ReferenceType getDefaultReferenceType()
  {
    return this.defaultReferenceType;
  }

  public String getDefaultCellLocationValue()
  {
    return this.defaultCellLocationValue;
  }

  public String getDefaultLiteralValue()
  {
    return this.defaultLiteralValue;
  }

  public int getDefaultShiftDirective()
  {
    return this.defaultShiftDirective;
  }

  public int getDefaultEmptyCellLocationDirective()
  {
    return this.defaultEmptyCellLocationDirective;
  }

  public int getDefaultEmptyLiteralDirective()
  {
    return this.defaultEmptyLiteralDirective;
  }
}