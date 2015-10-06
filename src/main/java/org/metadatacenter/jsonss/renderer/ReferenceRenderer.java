package org.metadatacenter.jsonss.renderer;

import org.metadatacenter.jsonss.parser.node.ReferenceNode;
import org.metadatacenter.jsonss.rendering.Rendering;

import java.util.Optional;

public interface ReferenceRenderer
{
  Optional<? extends Rendering> renderReference(ReferenceNode referenceNode,
      ReferenceRendererContext referenceRendererContext) throws RendererException;
}
