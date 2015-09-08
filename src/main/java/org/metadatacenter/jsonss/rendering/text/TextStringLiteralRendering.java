package org.metadatacenter.jsonss.rendering.text;

import org.metadatacenter.jsonss.rendering.StringLiteralRendering;

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
