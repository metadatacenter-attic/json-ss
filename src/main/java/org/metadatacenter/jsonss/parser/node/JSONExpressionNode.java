package org.metadatacenter.jsonss.parser.node;

import org.metadatacenter.jsonss.parser.ASTJSONArray;
import org.metadatacenter.jsonss.parser.ASTJSONExpression;
import org.metadatacenter.jsonss.parser.ASTJSONObject;
import org.metadatacenter.jsonss.parser.InternalParseException;
import org.metadatacenter.jsonss.parser.Node;
import org.metadatacenter.jsonss.parser.ParseException;
import org.metadatacenter.jsonss.parser.ParserUtil;

public class JSONExpressionNode implements JSONSSNode
{
  private JSONObjectNode jsonObjectNode;
  private JSONArrayNode jsonArrayNode;

  public JSONExpressionNode(ASTJSONExpression node) throws ParseException
  {
    if (node.jjtGetNumChildren() != 1)
      throw new InternalParseException("expecting one child of node " + getNodeName());
    else {
      Node child = node.jjtGetChild(0);
      if (ParserUtil.hasName(child, "JSONObject"))
        this.jsonObjectNode = new JSONObjectNode((ASTJSONObject)child);
      else if (ParserUtil.hasName(child, "JSONArray"))
        this.jsonArrayNode = new JSONArrayNode((ASTJSONArray)child);
      else
        throw new InternalParseException("unexpected child node " + child + " for node " + getNodeName());
    }
  }

  @Override public String getNodeName()
  {
    return "JSONExpression";
  }

  public boolean isJSONObject()
  {
    return this.jsonObjectNode != null;
  }

  public boolean isJSONArray()
  {
    return this.jsonArrayNode != null;
  }

  public JSONObjectNode getJSONObjectNode()
  {
    return this.jsonObjectNode;
  }

  public JSONArrayNode getJSONArrayNode()
  {
    return this.jsonArrayNode;
  }

  @Override public String toString()
  {
    return "TODO";
  }
}
