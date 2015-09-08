package org.metadatacenter.jsonss.rendering;

import org.metadatacenter.jsonss.core.ReferenceType;

public interface ReferenceRendering extends Rendering
{
  ReferenceType getReferenceType();

  String getRawValue();

  boolean isLiteral();
}
