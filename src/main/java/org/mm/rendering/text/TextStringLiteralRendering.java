package org.mm.rendering.text;

import org.mm.rendering.StringLiteralRendering;

public class TextStringLiteralRendering implements StringLiteralRendering
{
  private final String rendering;

  public TextStringLiteralRendering(String rendering)
  {
    this.rendering = rendering;
  }

  public String getRendering()
  {
    return this.rendering;
  }

  @Override public String toString()
  {
    return getRendering();
  }

  @Override public String getRawValue()
  {
    return rendering;
  }

  @Override public boolean isLiteral()
  {
    return true;
  }
}
