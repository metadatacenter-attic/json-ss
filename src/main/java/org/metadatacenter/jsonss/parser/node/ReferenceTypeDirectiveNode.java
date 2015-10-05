package org.metadatacenter.jsonss.parser.node;

import org.metadatacenter.jsonss.core.settings.ReferenceTypeDirectiveSetting;
import org.metadatacenter.jsonss.parser.ASTReferenceTypeDirective;
import org.metadatacenter.jsonss.parser.ParseException;
import org.metadatacenter.jsonss.parser.ParserUtil;

public class ReferenceTypeDirectiveNode implements JSONSSNode
{
  private final ReferenceTypeDirectiveSetting referenceTypeDirectiveSetting;

  public ReferenceTypeDirectiveNode(ASTReferenceTypeDirective node) throws ParseException
  {
    this.referenceTypeDirectiveSetting = node.referenceTypeDirectiveSetting;
  }

  public ReferenceTypeDirectiveSetting getReferenceTypeDirectiveSetting()
  {
    return this.referenceTypeDirectiveSetting;
  }

  @Override public String getNodeName()
  {
    return "ReferenceTypeDirective";
  }

  public String getReferenceTypeDirectiveSettingName()
  {
    return ParserUtil.getTokenName(this.referenceTypeDirectiveSetting.getConstant());
  }

  @Override public String toString()
  {
    return getReferenceTypeDirectiveSettingName();
  }
}
