package org.mm.rendering;

public interface StringLiteralRendering extends Rendering
{
  String getRawValue();

  boolean isLiteral();
}
