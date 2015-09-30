package org.metadatacenter.jsonss.renderer.text;

import org.metadatacenter.jsonss.parser.node.JSONArrayNode;
import org.metadatacenter.jsonss.parser.node.JSONBooleanNode;
import org.metadatacenter.jsonss.parser.node.JSONExpressionNode;
import org.metadatacenter.jsonss.parser.node.JSONNullNode;
import org.metadatacenter.jsonss.parser.node.JSONNumberNode;
import org.metadatacenter.jsonss.parser.node.JSONObjectNode;
import org.metadatacenter.jsonss.parser.node.JSONStringNode;
import org.metadatacenter.jsonss.parser.node.JSONValueNode;
import org.metadatacenter.jsonss.renderer.DefaultReferenceRenderer;
import org.metadatacenter.jsonss.renderer.InternalRendererException;
import org.metadatacenter.jsonss.renderer.JSONSSRenderer;
import org.metadatacenter.jsonss.renderer.ReferenceRendererConfiguration;
import org.metadatacenter.jsonss.renderer.RendererException;
import org.metadatacenter.jsonss.rendering.text.TextRendering;
import org.metadatacenter.jsonss.ss.SpreadSheetDataSource;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class TextRenderer implements JSONSSRenderer
{
  private final DefaultReferenceRenderer referenceRenderer;

  public TextRenderer(SpreadSheetDataSource dataSource)
  {
    this.referenceRenderer = new DefaultReferenceRenderer(dataSource);
  }

  @Override public Optional<? extends TextRendering> renderJSONExpression(JSONExpressionNode jsonExpressionNode)
    throws RendererException
  {
    if (jsonExpressionNode.isJSONArray())
      return renderJSONArray(jsonExpressionNode.getJSONArrayNode());
    else if (jsonExpressionNode.isJSONObject())
      return renderJSONObject(jsonExpressionNode.getJSONObjectNode());
    else
      throw new InternalRendererException("unknown " + jsonExpressionNode.getNodeName() + " node type");
  }

  @Override public Optional<? extends TextRendering> renderJSONObject(JSONObjectNode jsonObjectNode)
    throws RendererException
  {
    Map<String, JSONValueNode> keyValuePairs = jsonObjectNode.getKeyValuePairs();

    StringBuffer sb = new StringBuffer("{");
    boolean isFirst = true;

    for (String key : keyValuePairs.keySet()) {
      JSONValueNode jsonValueNode = keyValuePairs.get(key);
      Optional<? extends TextRendering> jsonValueRendering = renderJSONValue(jsonValueNode);
      if (jsonValueRendering.isPresent()) {
        if (!isFirst)
          sb.append(", ");
        sb.append("\"" + key + "\": " + jsonValueRendering.get().getRendering());
        isFirst = false;
      }
    }
    sb.append("}");

    return Optional.of(new TextRendering(sb.toString()));
  }

  @Override public Optional<? extends TextRendering> renderJSONArray(JSONArrayNode jsonArrayNode)
    throws RendererException
  {
    List<JSONValueNode> jsonValues = jsonArrayNode.getElements();

    StringBuffer sb = new StringBuffer("[");
    boolean isFirst = true;

    for (JSONValueNode jsonValueNode : jsonValues) {
      Optional<? extends TextRendering> jsonValueRendering = renderJSONValue(jsonValueNode);
      if (jsonValueRendering.isPresent()) {
        if (!isFirst)
          sb.append(", ");
        sb.append(jsonValueRendering.get().getRendering());
        isFirst = false;
      }
    }
    sb.append("]");

    return Optional.of(new TextRendering(sb.toString()));
  }

  @Override public Optional<? extends TextRendering> renderJSONValue(JSONValueNode jsonValueNode)
    throws RendererException
  {
    if (jsonValueNode.isJSONArray())
      return renderJSONArray(jsonValueNode.getJSONArrayNode());
    else if (jsonValueNode.isJSONObject())
      return renderJSONObject(jsonValueNode.getJSONObjectNode());
    else if (jsonValueNode.isJSONString())
      return renderJSONString(jsonValueNode.getJSONStringNode());
    else if (jsonValueNode.isJSONNumber())
      return renderJSONNumber(jsonValueNode.getJSONNumberNode());
    else if (jsonValueNode.isJSONBoolean())
      return renderJSONBoolean(jsonValueNode.getJSONBooleanNode());
    else if (jsonValueNode.isJSONNull())
      return renderJSONNull(jsonValueNode.getJSONNullNode());
    else if (jsonValueNode.isReference())
      return referenceRenderer.renderReference(jsonValueNode.getReferenceNode());
    else
      throw new InternalRendererException("unknown " + jsonValueNode.getNodeName() + " node type");
  }

  @Override public Optional<? extends TextRendering> renderJSONString(JSONStringNode jsonStringNode)
    throws RendererException
  {
    return Optional.of(new TextRendering("\"" + jsonStringNode.getString() + "\""));
  }

  @Override public Optional<? extends TextRendering> renderJSONNumber(JSONNumberNode jsonNumberNode)
    throws RendererException
  {
    return Optional.of(new TextRendering("" + jsonNumberNode.getNumber()));
  }

  @Override public Optional<? extends TextRendering> renderJSONBoolean(JSONBooleanNode jsonBooleanNode)
    throws RendererException
  {
    return Optional.of(new TextRendering("" + jsonBooleanNode.getBoolean()));
  }

  @Override public Optional<? extends TextRendering> renderJSONNull(JSONNullNode jsonNullNode) throws RendererException
  {
    return Optional.of(new TextRendering("null"));
  }

  @Override public void updateDataSource(SpreadSheetDataSource dataSource)
  {
    this.referenceRenderer.updateDataSource(dataSource);
  }

  @Override public ReferenceRendererConfiguration getReferenceRendererConfiguration()
  {
    return this.referenceRenderer.getReferenceRendererConfiguration();
  }
}
