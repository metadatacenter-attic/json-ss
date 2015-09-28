package org.metadatacenter.jsonss.parser.node;

import org.metadatacenter.jsonss.parser.ASTJSONArray;
import org.metadatacenter.jsonss.parser.ASTJSONValue;
import org.metadatacenter.jsonss.parser.InternalParseException;
import org.metadatacenter.jsonss.parser.Node;
import org.metadatacenter.jsonss.parser.ParseException;
import org.metadatacenter.jsonss.parser.ParserUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class JSONArrayNode implements JSONSSNode
{
  private final List<JSONValueNode> elements = new ArrayList<>();

  public JSONArrayNode(ASTJSONArray node) throws ParseException
  {
    for (int i = 0; i < node.jjtGetNumChildren(); i++) {
      Node child = node.jjtGetChild(i);
      if (ParserUtil.hasName(child, "JSONValue")) {
        JSONValueNode jsonValueNode = new JSONValueNode((ASTJSONValue)child);
        this.elements.add(jsonValueNode);
      } else
        throw new InternalParseException("unexpected child node " + child + " for " + getNodeName());
    }
  }

  @Override public String getNodeName()
  {
    return "JSONArray";
  }

  public List<JSONValueNode> getElements() { return Collections.unmodifiableList(this.elements); }

  @Override public String toString()
  {
    StringBuffer sb = new StringBuffer("[");
    boolean isFirst = true;

    for (JSONValueNode element : elements) {
      if (!isFirst)
        sb.append(", ");
      sb.append(element.toString());
      isFirst = false;
    }
    sb.append("]");

    return sb.toString();
  }
}