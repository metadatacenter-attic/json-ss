package org.mm.parser.node;

import org.mm.parser.ASTMMExpression;
import org.mm.parser.InternalParseException;
import org.mm.parser.Node;
import org.mm.parser.ParseException;

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
