package org.metadatacenter.jsonss.renderer.text;

import org.metadatacenter.jsonss.parser.node.JSONArrayNode;
import org.metadatacenter.jsonss.parser.node.JSONBooleanNode;
import org.metadatacenter.jsonss.parser.node.JSONExpressionNode;
import org.metadatacenter.jsonss.parser.node.JSONNullNode;
import org.metadatacenter.jsonss.parser.node.JSONNumberNode;
import org.metadatacenter.jsonss.parser.node.JSONObjectNode;
import org.metadatacenter.jsonss.parser.node.JSONStringNode;
import org.metadatacenter.jsonss.parser.node.JSONValueNode;
import org.metadatacenter.jsonss.renderer.InternalRendererException;
import org.metadatacenter.jsonss.renderer.JSONSSRenderer;
import org.metadatacenter.jsonss.renderer.ReferenceRendererConfiguration;
import org.metadatacenter.jsonss.renderer.RendererException;
import org.metadatacenter.jsonss.renderer.TextReferenceRenderer;
import org.metadatacenter.jsonss.rendering.text.TextRendering;
import org.metadatacenter.jsonss.ss.CellLocation;
import org.metadatacenter.jsonss.ss.CellRange;
import org.metadatacenter.jsonss.ss.SpreadSheetDataSource;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class TextRenderer implements JSONSSRenderer
{
  private final TextReferenceRenderer referenceRenderer;
  private final CellRange enclosingCellRange;
  private Optional<CellLocation> currentCellLocation;

  public TextRenderer(SpreadSheetDataSource dataSource, ReferenceRendererConfiguration referenceRendererConfiguration)
  {
    this.referenceRenderer = new TextReferenceRenderer(dataSource, referenceRendererConfiguration);
    this.enclosingCellRange = dataSource.getEnclosingCellRange();
    this.currentCellLocation = Optional.of(this.enclosingCellRange.getStartRange());
  }

  @Override public Optional<? extends TextRendering> renderJSONExpression(JSONExpressionNode jsonExpressionNode)
      throws RendererException
  {
    if (jsonExpressionNode.isJSONArray())
      return renderJSONArray(jsonExpressionNode.getJSONArrayNode(), this.enclosingCellRange, this.currentCellLocation);
    else if (jsonExpressionNode.isJSONObject())
      return renderJSONObject(jsonExpressionNode.getJSONObjectNode(), this.enclosingCellRange,
          this.currentCellLocation);
    else
      throw new InternalRendererException("unknown " + jsonExpressionNode.getNodeName() + " node type");
  }

  @Override public Optional<? extends TextRendering> renderJSONObject(JSONObjectNode jsonObjectNode,
      CellRange enclosingCellRange, Optional<CellLocation> currentCellLocation) throws RendererException
  {
    Map<String, JSONValueNode> keyValuePairs = jsonObjectNode.getKeyValuePairs();

    StringBuffer sb = new StringBuffer("{");
    boolean isFirst = true;

    for (String key : keyValuePairs.keySet()) {
      JSONValueNode jsonValueNode = keyValuePairs.get(key);
      Optional<? extends TextRendering> jsonValueRendering = renderJSONValue(jsonValueNode, enclosingCellRange,
          currentCellLocation);

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

  @Override public Optional<? extends TextRendering> renderJSONArray(JSONArrayNode jsonArrayNode,
      CellRange enclosingCellRange, Optional<CellLocation> currentCellLocation) throws RendererException
  {
    List<JSONValueNode> jsonValues = jsonArrayNode.getElements();

    StringBuffer sb = new StringBuffer("[");
    boolean isFirst = true;

    for (JSONValueNode jsonValueNode : jsonValues) {
      Optional<? extends TextRendering> jsonValueRendering = renderJSONValue(jsonValueNode, enclosingCellRange,
          currentCellLocation);

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

  @Override public Optional<? extends TextRendering> renderJSONValue(JSONValueNode jsonValueNode,
      CellRange enclosingCellRange, Optional<CellLocation> currentCellLocation) throws RendererException
  {
    if (jsonValueNode.isJSONArray())
      return renderJSONArray(jsonValueNode.getJSONArrayNode(), enclosingCellRange, currentCellLocation);
    else if (jsonValueNode.isJSONObject())
      return renderJSONObject(jsonValueNode.getJSONObjectNode(), enclosingCellRange, currentCellLocation);
    else if (jsonValueNode.isJSONString())
      return renderJSONString(jsonValueNode.getJSONStringNode());
    else if (jsonValueNode.isJSONNumber())
      return renderJSONNumber(jsonValueNode.getJSONNumberNode());
    else if (jsonValueNode.isJSONBoolean())
      return renderJSONBoolean(jsonValueNode.getJSONBooleanNode());
    else if (jsonValueNode.isJSONNull())
      return renderJSONNull(jsonValueNode.getJSONNullNode());
    else if (jsonValueNode.isReference())
      return referenceRenderer
          .renderReference(jsonValueNode.getReferenceNode(), enclosingCellRange, currentCellLocation);
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
}
