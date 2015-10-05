package org.metadatacenter.jsonss.rendering;

public interface ReferenceRendering extends Rendering
{
  String getRawValue();

  boolean isLiteral();
}
