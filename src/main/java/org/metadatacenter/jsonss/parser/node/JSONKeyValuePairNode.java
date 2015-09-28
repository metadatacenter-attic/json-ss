package org.metadatacenter.jsonss.parser.node;

import org.metadatacenter.jsonss.parser.ASTJSONKeyValuePair;
import org.metadatacenter.jsonss.parser.ASTJSONString;
import org.metadatacenter.jsonss.parser.ASTJSONValue;
import org.metadatacenter.jsonss.parser.InternalParseException;
import org.metadatacenter.jsonss.parser.Node;
import org.metadatacenter.jsonss.parser.ParseException;
import org.metadatacenter.jsonss.parser.ParserUtil;

public class JSONKeyValuePairNode implements JSONSSNode
{
  private String key;
  private JSONValueNode jsonValueNode;

  public JSONKeyValuePairNode(ASTJSONKeyValuePair node) throws ParseException
  {
    if (node.jjtGetNumChildren() != 2)
      throw new InternalParseException("expecting two children for node " + getNodeName());
    else {
      Node keyChild = node.jjtGetChild(0);
      if (ParserUtil.hasName(keyChild, "JSONString")) {
        JSONStringNode jsonStringNode = new JSONStringNode((ASTJSONString)keyChild);
        this.key = jsonStringNode.getString();
      } else
        throw new InternalParseException("unexpected key child node " + keyChild + " for node " + getNodeName());

      Node valueChild = node.jjtGetChild(1);
      if (ParserUtil.hasName(valueChild, "JSONValue")) {
        this.jsonValueNode = new JSONValueNode((ASTJSONValue)valueChild);
      } else
        throw new InternalParseException("unexpected value child node " + valueChild + " for node " + getNodeName());

    }
  }

  @Override public String getNodeName()
  {
    return "JSONKeyValuePair";
  }

  public String getKey() { return this.key; }

  public JSONValueNode getJSONValueNode() { return this.jsonValueNode; }

  public String toString()
  {
    return "\"" + this.key + "\":" + this.jsonValueNode.toString();
  }
}