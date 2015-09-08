package org.metadatacenter.jsonss.renderer;

import java.util.Optional;

import org.metadatacenter.jsonss.parser.node.ReferenceNode;
import org.metadatacenter.jsonss.rendering.Rendering;

public interface ReferenceRenderer
{
	Optional<? extends Rendering> renderReference(ReferenceNode node) throws RendererException;
}
