package org.metadatacenter.jsonss.parser;

import org.metadatacenter.jsonss.core.ReferenceType;

public class DefaultReferenceDirectives
{
  private final ReferenceType defaultReferenceType;
  private final String defaultLocationValue;
  private final String defaultLiteralValue;
  private final int defaultEmptyLocationDirective;
  private final int defaultEmptyLiteralDirective;
  private final int defaultShiftDirective;

  public DefaultReferenceDirectives(int defaultReferenceType, String defaultLocationValue, String defaultLiteralValue,
    int defaultShiftDirective, int defaultEmptyLocationDirective, int defaultEmptyLiteralDirective)
  {
    this.defaultReferenceType = new ReferenceType(defaultReferenceType);
    this.defaultLocationValue = defaultLocationValue;
    this.defaultLiteralValue = defaultLiteralValue;
    this.defaultEmptyLocationDirective = defaultEmptyLocationDirective;
    this.defaultEmptyLiteralDirective = defaultEmptyLiteralDirective;
    this.defaultShiftDirective = defaultShiftDirective;
  }

  public ReferenceType getDefaultReferenceType()
  {
    return this.defaultReferenceType;
  }

  public String getDefaultLocationValue()
  {
    return this.defaultLocationValue;
  }

  public String getDefaultLiteralValue()
  {
    return this.defaultLiteralValue;
  }

  public int getDefaultShiftDirective()
  {
    return this.defaultShiftDirective;
  }

  public int getDefaultEmptyLocationDirective()
  {
    return this.defaultEmptyLocationDirective;
  }

  public int getDefaultEmptyLiteralDirective()
  {
    return this.defaultEmptyLiteralDirective;
  }
}