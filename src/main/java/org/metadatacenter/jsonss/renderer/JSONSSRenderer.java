package org.metadatacenter.jsonss.renderer;

import org.metadatacenter.jsonss.parser.node.JSONArrayNode;
import org.metadatacenter.jsonss.parser.node.JSONBooleanNode;
import org.metadatacenter.jsonss.parser.node.JSONExpressionNode;
import org.metadatacenter.jsonss.parser.node.JSONNullNode;
import org.metadatacenter.jsonss.parser.node.JSONNumberNode;
import org.metadatacenter.jsonss.parser.node.JSONObjectNode;
import org.metadatacenter.jsonss.parser.node.JSONStringNode;
import org.metadatacenter.jsonss.parser.node.JSONValueNode;
import org.metadatacenter.jsonss.rendering.Rendering;
import org.metadatacenter.jsonss.ss.SpreadSheetDataSource;

import java.util.Optional;

public interface JSONSSRenderer
{
  Optional<? extends Rendering> renderJSONExpression(JSONExpressionNode jsonExpressionNode) throws RendererException;

  Optional<? extends Rendering> renderJSONObject(JSONObjectNode jsonObjectNode) throws RendererException;

  Optional<? extends Rendering> renderJSONArray(JSONArrayNode jsonArrayNode) throws RendererException;

  Optional<? extends Rendering> renderJSONValue(JSONValueNode jsonValueNode) throws RendererException;

  Optional<? extends Rendering> renderJSONString(JSONStringNode jsonStringNode) throws RendererException;

  Optional<? extends Rendering> renderJSONNumber(JSONNumberNode jsonNumberNode) throws RendererException;

  Optional<? extends Rendering> renderJSONBoolean(JSONBooleanNode jsonBooleanNode) throws RendererException;

  Optional<? extends Rendering> renderJSONNull(JSONNullNode jsonNullNode) throws RendererException;

  public void updateDataSource(SpreadSheetDataSource source);

	public ReferenceRendererConfiguration getReferenceRendererConfiguration();
}
