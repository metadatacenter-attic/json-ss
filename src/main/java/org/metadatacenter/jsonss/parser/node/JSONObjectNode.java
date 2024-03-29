package org.metadatacenter.jsonss.parser.node;

import org.metadatacenter.jsonss.parser.ASTJSONKeyValuePair;
import org.metadatacenter.jsonss.parser.ASTJSONObject;
import org.metadatacenter.jsonss.parser.ASTRangeReference;
import org.metadatacenter.jsonss.parser.InternalParseException;
import org.metadatacenter.jsonss.parser.Node;
import org.metadatacenter.jsonss.parser.ParseException;
import org.metadatacenter.jsonss.parser.ParserUtil;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class JSONObjectNode implements JSONSSNode
{
  private final Map<String, JSONValueNode> keyValuePairs = new LinkedHashMap<>();
  private RangeReferenceNode rangeReferenceNode;

  public JSONObjectNode(ASTJSONObject node) throws ParseException
  {
    for (int i = 0; i < node.jjtGetNumChildren(); i++) {
      Node child = node.jjtGetChild(i);
      if (ParserUtil.hasName(child, "JSONKeyValuePair")) {
        JSONKeyValuePairNode jsonKeyValuePairNode = new JSONKeyValuePairNode((ASTJSONKeyValuePair)child);
        String key = jsonKeyValuePairNode.getKey();
        JSONValueNode jsonValueNode = jsonKeyValuePairNode.getJSONValueNode();
        this.keyValuePairs.put(key, jsonValueNode);
      } else if (ParserUtil.hasName(child, "RangeReference")) {
        this.rangeReferenceNode = new RangeReferenceNode((ASTRangeReference)child);
      } else
        throw new InternalParseException("unexpected child node " + child + " for " + getNodeName());
    }
  }

  @Override public String getNodeName()
  {
    return "JSONObject";
  }

  public boolean hasRangeReferenceNode() { return this.rangeReferenceNode != null; }

  public RangeReferenceNode getRangeReferenceNode() { return this.rangeReferenceNode; }

  public Map<String, JSONValueNode> getKeyValuePairs() { return Collections.unmodifiableMap(this.keyValuePairs); }

  @Override public String toString()
  {
    StringBuffer sb = new StringBuffer("{");

    if (hasRangeReferenceNode())
      sb.append(this.rangeReferenceNode.toString() + " ");

    boolean isFirst = true;
    for (String key : this.keyValuePairs.keySet()) {
      if (!isFirst)
        sb.append(", ");
      sb.append("\"" + key + "\": " + this.keyValuePairs.get(key));
      isFirst = false;
    }
    sb.append("}");

    return sb.toString();
  }
}
