package org.mm.rendering;

import org.mm.core.ReferenceType;

public interface ReferenceRendering extends Rendering
{
  ReferenceType getReferenceType();

  String getRawValue();

  boolean isLiteral();
}
