package org.mm.renderer;

import org.mm.parser.node.MMExpressionNode;
import org.mm.rendering.Rendering;
import org.mm.ss.SpreadSheetDataSource;

import java.util.Optional;

public interface Renderer
{
	Optional<? extends Rendering> renderExpression(MMExpressionNode expressionNode) throws RendererException;

	public void changeDataSource(SpreadSheetDataSource source);

	public ReferenceRendererConfiguration getReferenceRendererConfiguration();
}
