package org.metadatacenter.jsonss.core;

import org.metadatacenter.jsonss.parser.node.ReferenceNode;
import org.metadatacenter.jsonss.parser.node.ReferenceSourceSpecificationNode;
import org.metadatacenter.jsonss.renderer.RendererException;
import org.metadatacenter.jsonss.ss.CellLocation;

import java.util.List;
import java.util.Optional;

/**
 * Interface describing a data source seen by a JSON-SS renderer.
 */
public interface DataSource
{
  String getCellLocationValue(CellLocation cellLocation, ReferenceNode referenceNode) throws RendererException;

  String getCellLocationValue(CellLocation cellLocation) throws RendererException;

  String getCellLocationValueWithShifting(CellLocation cellLocation, ReferenceNode referenceNode)
    throws RendererException;

  void setCurrentCellLocation(CellLocation cellLocation);

  Optional<CellLocation> getCurrentCellLocation();

  boolean hasCurrentCellLocation();

  CellLocation resolveCellLocation(ReferenceSourceSpecificationNode sourceSpecification) throws RendererException;

  List<String> getSheetNames();
}
