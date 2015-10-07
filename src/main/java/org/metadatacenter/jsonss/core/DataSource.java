package org.metadatacenter.jsonss.core;

import org.metadatacenter.jsonss.parser.node.ReferenceCellLocationSpecificationNode;
import org.metadatacenter.jsonss.parser.node.ReferenceNode;
import org.metadatacenter.jsonss.renderer.RendererException;
import org.metadatacenter.jsonss.ss.CellLocation;
import org.metadatacenter.jsonss.ss.CellRange;

/**
 * Interface describing a data source seen by a JSON-SS renderer.
 */
public interface DataSource
{
  CellLocation getCellLocation(ReferenceCellLocationSpecificationNode referenceCellLocationSpecificationNode,
    CellLocation currentCellLocation) throws RendererException;

  String getCellLocationValue(CellLocation cellLocation, ReferenceNode referenceNode) throws RendererException;

  CellRange getDefaultEnclosingCellRange();
}
