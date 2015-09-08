package org.metadatacenter.jsonss.rendering;

public interface StringLiteralRendering extends Rendering
{
  String getRawValue();

  boolean isLiteral();
}
