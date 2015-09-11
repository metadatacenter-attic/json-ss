package org.metadatacenter.jsonss.renderer;

import org.metadatacenter.jsonss.parser.node.ExpressionNode;
import org.metadatacenter.jsonss.rendering.Rendering;
import org.metadatacenter.jsonss.ss.SpreadSheetDataSource;

import java.util.Optional;

public interface Renderer
{
	Optional<? extends Rendering> renderExpression(ExpressionNode expressionNode) throws RendererException;

	public void changeDataSource(SpreadSheetDataSource source);

	public ReferenceRendererConfiguration getReferenceRendererConfiguration();
}
