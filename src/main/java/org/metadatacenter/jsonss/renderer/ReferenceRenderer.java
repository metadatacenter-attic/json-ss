package org.metadatacenter.jsonss.renderer;

import org.metadatacenter.jsonss.parser.node.ReferenceNode;
import org.metadatacenter.jsonss.rendering.Rendering;
import org.metadatacenter.jsonss.ss.SpreadSheetDataSource;

import java.util.Optional;

public interface ReferenceRenderer
{
	Optional<? extends Rendering> renderReference(ReferenceNode node) throws RendererException;

	ReferenceRendererConfiguration getReferenceRendererConfiguration();

  void updateDataSource(SpreadSheetDataSource dataSource);
}
