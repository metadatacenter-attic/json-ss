package org.metadatacenter.jsonss.parser.node;

import org.metadatacenter.jsonss.parser.ASTJSONArray;
import org.metadatacenter.jsonss.parser.ASTJSONBoolean;
import org.metadatacenter.jsonss.parser.ASTJSONNull;
import org.metadatacenter.jsonss.parser.ASTJSONNumber;
import org.metadatacenter.jsonss.parser.ASTJSONObject;
import org.metadatacenter.jsonss.parser.ASTJSONString;
import org.metadatacenter.jsonss.parser.ASTJSONValue;
import org.metadatacenter.jsonss.parser.InternalParseException;
import org.metadatacenter.jsonss.parser.Node;
import org.metadatacenter.jsonss.parser.ParseException;
import org.metadatacenter.jsonss.parser.ParserUtil;

public class JSONValueNode implements JSONSSNode
{
  private JSONObjectNode jsonObjectNode;
  private JSONArrayNode jsonArrayNode;
  private JSONStringNode jsonStringNode;
  private JSONNumberNode jsonNumberNode;
  private JSONBooleanNode jsonBooleanNode;
  private JSONNullNode jsonNullNode;

  public JSONValueNode(ASTJSONValue node) throws ParseException
  {
    if (node.jjtGetNumChildren() != 1)
      throw new InternalParseException("expecting one child of node " + getNodeName());
    else {
      Node child = node.jjtGetChild(0);
      if (ParserUtil.hasName(child, "JSONObject"))
        this.jsonObjectNode = new JSONObjectNode((ASTJSONObject)child);
      else if (ParserUtil.hasName(child, "JSONArray"))
        this.jsonArrayNode = new JSONArrayNode((ASTJSONArray)child);
      else if (ParserUtil.hasName(child, "JSONString"))
        this.jsonStringNode = new JSONStringNode((ASTJSONString)child);
      else if (ParserUtil.hasName(child, "JSONNumber"))
        this.jsonNumberNode = new JSONNumberNode((ASTJSONNumber)child);
      else if (ParserUtil.hasName(child, "JSONBoolean"))
        this.jsonBooleanNode = new JSONBooleanNode((ASTJSONBoolean)child);
      else if (ParserUtil.hasName(child, "JSONNull"))
        this.jsonNullNode = new JSONNullNode((ASTJSONNull)child);
      else
        throw new InternalParseException("unexpected child node " + child + " for node " + getNodeName());
    }
  }

  public boolean isJSONObject()
  {
    return this.jsonObjectNode != null;
  }

  public boolean isJSONArray()
  {
    return this.jsonArrayNode != null;
  }

  public boolean isJSONString()
  {
    return this.jsonStringNode != null;
  }

  public boolean isJSONNumber()
  {
    return this.jsonNumberNode != null;
  }

  public boolean isJSONBoolean()
  {
    return this.jsonBooleanNode != null;
  }

  public boolean isJSONNull()
  {
    return this.jsonNullNode != null;
  }

  public JSONObjectNode getJSONObjectNode()
  {
    return this.jsonObjectNode;
  }

  public JSONArrayNode getJSONArrayNode()
  {
    return this.jsonArrayNode;
  }

  public JSONStringNode getJSONStringNode()
  {
    return this.jsonStringNode;
  }

  public JSONNumberNode getJSONNumberNode()
  {
    return this.jsonNumberNode;
  }

  public JSONBooleanNode getJSONBooleanNode()
  {
    return this.jsonBooleanNode;
  }

  public JSONNullNode getJSONNullNode()
  {
    return this.jsonNullNode;
  }

  @Override public String getNodeName()
  {
    return "OWLLiteral";
  }

  public String toString()
  {
    if (isJSONObject())
      return this.jsonObjectNode.toString();
    else
      return "INCONSISTENT CHILD NODE";
  }
}