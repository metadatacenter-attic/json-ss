package org.metadatacenter.jsonss.rendering.text;

import org.metadatacenter.jsonss.core.ReferenceType;
import org.metadatacenter.jsonss.rendering.ReferenceRendering;

public class TextReferenceRendering extends TextRendering implements ReferenceRendering
{
  private final String rawValue;
  private final ReferenceType referenceType;

  public TextReferenceRendering(String rawValue, ReferenceType referenceType)
  {
    super(rawValue);
    this.rawValue = rawValue;
    this.referenceType = referenceType;
  }

  @Override public ReferenceType getReferenceType() { return this.referenceType; }

  @Override public String getRawValue()
  {
    return this.rawValue;
  }

  @Override public boolean isLiteral()
  {
    return this.referenceType.isLiteral();
  }

}
