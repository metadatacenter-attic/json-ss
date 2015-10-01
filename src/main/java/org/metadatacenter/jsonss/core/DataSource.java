package org.metadatacenter.jsonss.core;

import org.metadatacenter.jsonss.parser.node.ReferenceNode;
import org.metadatacenter.jsonss.parser.node.ReferenceSourceSpecificationNode;
import org.metadatacenter.jsonss.renderer.RendererException;
import org.metadatacenter.jsonss.ss.CellLocation;
import org.metadatacenter.jsonss.ss.CellRange;

import java.util.Optional;

/**
 * Interface describing a data source seen by a JSON-SS renderer.
 */
public interface DataSource
{
  CellLocation resolveCellLocation(ReferenceSourceSpecificationNode referenceSourceSpecificationNode,
    Optional<CellLocation> currentCellLocation) throws RendererException;

  String getCellLocationValue(CellLocation cellLocation, ReferenceNode referenceNode) throws RendererException;

  CellRange getEnclosingCellRange();
}
