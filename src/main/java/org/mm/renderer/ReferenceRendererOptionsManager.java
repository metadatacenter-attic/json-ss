package org.mm.renderer;

import org.mm.parser.JSONSSParserConstants;
import org.mm.parser.ParserUtil;

public class ReferenceRendererOptionsManager implements JSONSSParserConstants
{
  private final ReferenceRendererConfiguration referenceRenderer;

  public ReferenceRendererOptionsManager(ReferenceRendererConfiguration referenceRenderer)
  {
    this.referenceRenderer = referenceRenderer;
  }

  public String getDefaultReferenceType()
  {
    return getSettingName(this.referenceRenderer.getDefaultReferenceType());
  }

  public String getDefaultEmptyLocation()
  {
    return getSettingName(this.referenceRenderer.getDefaultEmptyLocation());
  }

  public String getDefaultReferenceTypeOptionName()
  {
    return getOptionName(MM_DEFAULT_REFERENCE_TYPE);
  }

  public String getDefaultEmptyLocationOptionName()
  {
    return getOptionName(MM_DEFAULT_EMPTY_LOCATION);
  }

  public String getMappingConfigurationOption(String optionName)
  {
    int optionID = getOptionID(optionName);

    if (optionID == MM_DEFAULT_REFERENCE_TYPE)
      return getSettingName(this.referenceRenderer.getDefaultReferenceType());
    else if (optionID == MM_DEFAULT_EMPTY_LOCATION)
      return getSettingName(this.referenceRenderer.getDefaultEmptyLocation());
    else
      return "unknown option: " + optionName;
  }

  // TODO Test for valid settings for each option
  public void setMappingConfigurationOption(String optionName, String settingName)
  {
    int settingID = getSettingID(settingName);
    int optionID = getOptionID(optionName);

    if (settingID != -1) {
      if (optionID == MM_DEFAULT_REFERENCE_TYPE)
        this.referenceRenderer.setDefaultReferenceType(settingID);
      else if (optionID == MM_DEFAULT_EMPTY_LOCATION)
        this.referenceRenderer.setDefaultEmptyLocation(settingID);
    }
  }

  private String getOptionName(int optionID)
  {
    return ParserUtil.getTokenName(optionID);
  }

  private int getSettingID(String settingName)
  {
    return ParserUtil.getTokenID(settingName);
  }

  private int getOptionID(String optionName)
  {
    return ParserUtil.getTokenID(optionName);
  }

  private String getSettingName(int settingID)
  {
    return ParserUtil.getTokenName(settingID);
  }
}
