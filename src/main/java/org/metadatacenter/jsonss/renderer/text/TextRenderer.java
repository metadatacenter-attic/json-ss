package org.metadatacenter.jsonss.renderer.text;

import org.metadatacenter.jsonss.core.settings.ReferenceDirectivesSettings;
import org.metadatacenter.jsonss.parser.node.JSONArrayNode;
import org.metadatacenter.jsonss.parser.node.JSONBooleanNode;
import org.metadatacenter.jsonss.parser.node.JSONExpressionNode;
import org.metadatacenter.jsonss.parser.node.JSONNullNode;
import org.metadatacenter.jsonss.parser.node.JSONNumberNode;
import org.metadatacenter.jsonss.parser.node.JSONObjectNode;
import org.metadatacenter.jsonss.parser.node.JSONStringNode;
import org.metadatacenter.jsonss.parser.node.JSONValueNode;
import org.metadatacenter.jsonss.parser.node.RangeReferenceNode;
import org.metadatacenter.jsonss.renderer.InternalRendererException;
import org.metadatacenter.jsonss.renderer.JSONSSRenderer;
import org.metadatacenter.jsonss.renderer.ReferenceRendererContext;
import org.metadatacenter.jsonss.renderer.RendererException;
import org.metadatacenter.jsonss.renderer.TextReferenceRenderer;
import org.metadatacenter.jsonss.rendering.text.TextRendering;
import org.metadatacenter.jsonss.ss.CellLocation;
import org.metadatacenter.jsonss.ss.CellRange;
import org.metadatacenter.jsonss.ss.SpreadSheetDataSource;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class TextRenderer implements JSONSSRenderer
{
  private final SpreadSheetDataSource dataSource;
  private final ReferenceRendererContext baseReferenceRendererContext;
  private final TextReferenceRenderer referenceRenderer;

  public TextRenderer(SpreadSheetDataSource dataSource, ReferenceDirectivesSettings defaultReferenceDirectivesSettings)
  {
    this.dataSource = dataSource;
    this.baseReferenceRendererContext = new ReferenceRendererContext(defaultReferenceDirectivesSettings,
      dataSource.getDefaultEnclosingCellRange());
    this.referenceRenderer = new TextReferenceRenderer(dataSource);
  }

  @Override public Optional<? extends TextRendering> renderJSONExpression(JSONExpressionNode jsonExpressionNode)
    throws RendererException
  {
    if (jsonExpressionNode.isJSONArray())
      return renderJSONArray(jsonExpressionNode.getJSONArrayNode(), this.baseReferenceRendererContext);
    else if (jsonExpressionNode.isJSONObject())
      return renderJSONObject(jsonExpressionNode.getJSONObjectNode(), this.baseReferenceRendererContext);
    else
      throw new InternalRendererException("unknown " + jsonExpressionNode.getNodeName() + " node type");
  }

  @Override public Optional<? extends TextRendering> renderJSONObject(JSONObjectNode jsonObjectNode,
    ReferenceRendererContext enclosingReferenceRendererContext) throws RendererException
  {
    if (jsonObjectNode.hasRangeReferenceNode()) {
      RangeReferenceNode rangeReferenceNode = jsonObjectNode.getRangeReferenceNode();
      CellLocation startCellLocation = this.dataSource
        .getCellLocation(rangeReferenceNode.getStartCellLocationSpecificationNode(),
          enclosingReferenceRendererContext.getCurrentCellLocation());
      CellLocation finishCellLocation = this.dataSource
        .getCellLocation(rangeReferenceNode.getFinishCellLocationSpecification(),
          enclosingReferenceRendererContext.getCurrentCellLocation());
      CellRange cellRange = new CellRange(startCellLocation, finishCellLocation);
      ReferenceDirectivesSettings referenceDirectivesSettings = rangeReferenceNode
        .getCurrentReferenceDirectivesSettings(enclosingReferenceRendererContext.getReferenceDirectivesSettings());

      boolean isFirst = true;
      StringBuffer sb = new StringBuffer("[");
      Iterator<CellLocation> cellLocationIterator = cellRange.iterator();
      while (cellLocationIterator.hasNext()) {
        CellLocation currentCellLocation = cellLocationIterator.next();
        ReferenceRendererContext currentReferenceRenderContext = new ReferenceRendererContext(
          referenceDirectivesSettings, cellRange, currentCellLocation);
        if (!isFirst)
          sb.append(", ");
        sb.append(getRendering(jsonObjectNode, currentReferenceRenderContext));
        isFirst = false;
      }

      sb.append("]");

      return Optional.of(new TextRendering(sb.toString()));
    } else {
      return Optional.of(new TextRendering(getRendering(jsonObjectNode, enclosingReferenceRendererContext).toString()));
    }
  }

  private StringBuffer getRendering(JSONObjectNode jsonObjectNode,
    ReferenceRendererContext enclosingReferenceRendererContext) throws RendererException
  {
    Map<String, JSONValueNode> keyValuePairs = jsonObjectNode.getKeyValuePairs();

    StringBuffer sb = new StringBuffer("{");
    boolean isFirst = true;

    for (String key : keyValuePairs.keySet()) {
      JSONValueNode jsonValueNode = keyValuePairs.get(key);
      Optional<? extends TextRendering> jsonValueRendering = renderJSONValue(jsonValueNode,
        enclosingReferenceRendererContext);

      if (jsonValueRendering.isPresent()) {
        if (!isFirst)
          sb.append(", ");
        sb.append("\"" + key + "\": " + jsonValueRendering.get().getRendering());
        isFirst = false;
      }
    }
    sb.append("}");

    return sb;
  }

  @Override public Optional<? extends TextRendering> renderJSONArray(JSONArrayNode jsonArrayNode,
    ReferenceRendererContext enclosingReferenceRendererContext) throws RendererException
  {
    List<JSONValueNode> jsonValues = jsonArrayNode.getElements();

    StringBuffer sb = new StringBuffer("[");
    boolean isFirst = true;

    for (JSONValueNode jsonValueNode : jsonValues) {
      Optional<? extends TextRendering> jsonValueRendering = renderJSONValue(jsonValueNode,
        enclosingReferenceRendererContext);

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
    ReferenceRendererContext enclosingReferenceRendererContext) throws RendererException
  {
    if (jsonValueNode.isJSONArray())
      return renderJSONArray(jsonValueNode.getJSONArrayNode(), enclosingReferenceRendererContext);
    else if (jsonValueNode.isJSONObject())
      return renderJSONObject(jsonValueNode.getJSONObjectNode(), enclosingReferenceRendererContext);
    else if (jsonValueNode.isJSONString())
      return renderJSONString(jsonValueNode.getJSONStringNode());
    else if (jsonValueNode.isJSONNumber())
      return renderJSONNumber(jsonValueNode.getJSONNumberNode());
    else if (jsonValueNode.isJSONBoolean())
      return renderJSONBoolean(jsonValueNode.getJSONBooleanNode());
    else if (jsonValueNode.isJSONNull())
      return renderJSONNull(jsonValueNode.getJSONNullNode());
    else if (jsonValueNode.isReference())
      return referenceRenderer.renderReference(jsonValueNode.getReferenceNode(), enclosingReferenceRendererContext);
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
