package org.metadatacenter.jsonss.renderer;

import org.metadatacenter.jsonss.parser.node.ReferenceNode;
import org.metadatacenter.jsonss.rendering.Rendering;
import org.metadatacenter.jsonss.ss.CellLocation;
import org.metadatacenter.jsonss.ss.CellRange;

import java.util.Optional;

public interface ReferenceRenderer
{
  Optional<? extends Rendering> renderReference(ReferenceNode referenceNode, CellRange enclosingCellRange,
    Optional<CellLocation> currentCellLocation) throws RendererException;

  ReferenceRendererConfiguration getReferenceRendererConfiguration();
}
