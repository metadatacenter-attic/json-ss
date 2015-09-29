package org.metadatacenter.jsonss.core;

import org.metadatacenter.jsonss.parser.node.ReferenceNode;
import org.metadatacenter.jsonss.parser.node.ReferenceSourceSpecificationNode;
import org.metadatacenter.jsonss.renderer.RendererException;
import org.metadatacenter.jsonss.ss.SpreadsheetLocation;

import java.util.List;
import java.util.Optional;

/**
 * Interface describing a data source seen by a JSON-SS renderer.
 * Currently this supports spreadsheets only.
 */
public interface DataSource
{
	String getLocationValue(SpreadsheetLocation location, ReferenceNode referenceNode) throws RendererException;

	String getLocationValue(SpreadsheetLocation location) throws RendererException;

	String getLocationValueWithShifting(SpreadsheetLocation location, ReferenceNode referenceNode) throws RendererException;

	void setCurrentLocation(SpreadsheetLocation location);

	Optional<SpreadsheetLocation> getCurrentLocation();

	boolean hasCurrentLocation();

	SpreadsheetLocation resolveLocation(ReferenceSourceSpecificationNode sourceSpecification) throws RendererException;

	List<String> getSheetNames();
}
