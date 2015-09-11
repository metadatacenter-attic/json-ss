package org.metadatacenter.jsonss.parser.node;

import org.metadatacenter.jsonss.parser.ASTExpression;
import org.metadatacenter.jsonss.parser.ASTReferenceDirective;
import org.metadatacenter.jsonss.parser.InternalParseException;
import org.metadatacenter.jsonss.parser.Node;
import org.metadatacenter.jsonss.parser.ParseException;
import org.metadatacenter.jsonss.parser.ParserUtil;

public class ExpressionNode implements JSONSSNode
{
  private ReferenceDirectiveNode referenceDirectiveNode;

  public ExpressionNode(ASTExpression node) throws ParseException
  {
    for (int i = 0; i < node.jjtGetNumChildren(); i++) {
      Node child = node.jjtGetChild(i);

      if (ParserUtil.hasName(child, "ReferenceDirective")) {
        this.referenceDirectiveNode = new ReferenceDirectiveNode((ASTReferenceDirective)child);
      } else
        throw new InternalParseException("invalid child node " + child + " to " + getNodeName());
    }
  }

  public ReferenceDirectiveNode getReferenceDirectiveNode()
  {
    return this.referenceDirectiveNode;
  }

  public boolean hasReferenceDirective()
  {
    return this.referenceDirectiveNode != null;
  }

  @Override public String getNodeName()
  {
    return "Expression";
  }

  public String toString()
  {
    if (hasReferenceDirective())
      return this.referenceDirectiveNode.toString();
    else
      return "";
  }
}
