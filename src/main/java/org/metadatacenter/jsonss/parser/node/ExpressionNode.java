package org.metadatacenter.jsonss.parser.node;

import org.metadatacenter.jsonss.parser.ASTExpression;
import org.metadatacenter.jsonss.parser.InternalParseException;
import org.metadatacenter.jsonss.parser.Node;
import org.metadatacenter.jsonss.parser.ParseException;

public class ExpressionNode implements JSONSSNode
{
  public ExpressionNode(ASTExpression node) throws ParseException
  {
    for (int i = 0; i < node.jjtGetNumChildren(); i++) {
      Node child = node.jjtGetChild(i);

      throw new InternalParseException("invalid child node " + child + " to " + getNodeName());
    }
  }

  @Override public String getNodeName()
  {
    return "Expression";
  }

  public String toString()
  {
    return "TODO";
  }
}
