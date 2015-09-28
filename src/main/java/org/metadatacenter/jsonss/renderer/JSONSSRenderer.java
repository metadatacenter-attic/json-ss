package org.metadatacenter.jsonss.renderer;

import org.metadatacenter.jsonss.parser.node.JSONArrayNode;
import org.metadatacenter.jsonss.parser.node.JSONExpressionNode;
import org.metadatacenter.jsonss.parser.node.JSONObjectNode;
import org.metadatacenter.jsonss.parser.node.JSONValueNode;
import org.metadatacenter.jsonss.rendering.Rendering;
import org.metadatacenter.jsonss.ss.SpreadSheetDataSource;

import java.util.Optional;

public interface JSONSSRenderer
{
  Optional<? extends Rendering> renderJSONExpression(JSONExpressionNode jsonExpressionNode) throws RendererException;

  public void changeDataSource(SpreadSheetDataSource source);

	public ReferenceRendererConfiguration getReferenceRendererConfiguration();
}
