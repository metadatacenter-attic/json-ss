package org.metadatacenter.jsonss.renderer;

import org.metadatacenter.jsonss.parser.JSONSSParserConstants;

/**
 * Contains common functionality for rendering a JSON-SS reference that
 * may be used by renderer implementations.
 */
public abstract class ReferenceRendererConfiguration implements JSONSSParserConstants
{
  // Configuration options
  public int defaultReferenceType = JSON_STRING;
  public int defaultEmptyLocation = PROCESS_IF_EMPTY_LOCATION;

  public int getDefaultReferenceType()
  {
    return this.defaultReferenceType;
  }

  public int getDefaultEmptyLocation()
  {
    return this.defaultEmptyLocation;
  }

  public void setDefaultReferenceType(int defaultReferenceType)
  {
    this.defaultReferenceType = defaultReferenceType;
  }

  public void setDefaultEmptyLocation(int defaultEmptyLocation)
  {
    this.defaultEmptyLocation = defaultEmptyLocation;
  }
}
