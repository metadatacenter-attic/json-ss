package org.mm.parser;

import org.mm.core.ReferenceType;

public class DefaultReferenceDirectives
{
  private final ReferenceType defaultReferenceType;

  private final String defaultLocationValue;
  private final String defaultLiteral;

  private final int defaultEmptyLocationDirective;
  private final int defaultEmptyLiteralDirective;

  private int defaultShiftDirective;

  public DefaultReferenceDirectives(int defaultReferenceType, String defaultLocationValue, String defaultLiteral,
      int defaultShiftDirective, int defaultEmptyLocationDirective, int defaultEmptyLiteralDirective)
  {
    this.defaultReferenceType = new ReferenceType(defaultReferenceType);
    this.defaultLocationValue = defaultLocationValue;
    this.defaultLiteral = defaultLiteral;
    this.defaultShiftDirective = defaultShiftDirective;
    this.defaultEmptyLocationDirective = defaultEmptyLocationDirective;
    this.defaultEmptyLiteralDirective = defaultEmptyLiteralDirective;
  }

  public void setDefaultShiftDirective(int shiftDirective)
  {
    this.defaultShiftDirective = shiftDirective;
  }

  public ReferenceType getDefaultReferenceType()
  {
    return this.defaultReferenceType;
  }

  public String getDefaultLocationValue()
  {
    return this.defaultLocationValue;
  }

  public String getDefaultLiteral()
  {
    return this.defaultLiteral;
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