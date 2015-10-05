package org.metadatacenter.jsonss.rendering.text;

import org.metadatacenter.jsonss.core.settings.ReferenceTypeDirectiveSetting;
import org.metadatacenter.jsonss.rendering.ReferenceRendering;

public class TextReferenceRendering extends TextRendering implements ReferenceRendering
{
  private final ReferenceTypeDirectiveSetting referenceType;
  private final String rawValue;

  public TextReferenceRendering(String rawValue, ReferenceTypeDirectiveSetting referenceType)
  {
    super(rawValue);
    this.rawValue = rawValue;
    this.referenceType = referenceType;
  }

  @Override public String getRawValue()
  {
    return this.rawValue;
  }

  @Override public boolean isLiteral() { return this.referenceType.isLiteral(); }
}
