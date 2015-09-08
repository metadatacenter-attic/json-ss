package org.metadatacenter.jsonss.parser.node;

import org.metadatacenter.jsonss.parser.InternalParseException;
import org.metadatacenter.jsonss.parser.ParseException;
import org.metadatacenter.jsonss.parser.ASTMMExpression;
import org.metadatacenter.jsonss.parser.Node;

public class MMExpressionNode implements MMNode
{

  public MMExpressionNode(ASTMMExpression node) throws ParseException
  {
    for (int i = 0; i < node.jjtGetNumChildren(); i++) {
      Node child = node.jjtGetChild(i);

      throw new InternalParseException("invalid child node " + child + " to Expression");
    }
  }


  public String getNodeName()
  {
    return "ExpressionNode";
  }

  public String toString()
  {
      return "";
  }
}
