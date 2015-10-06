package org.metadatacenter.jsonss.renderer;

import org.metadatacenter.jsonss.core.settings.ReferenceDirectivesSettings;
import org.metadatacenter.jsonss.ss.CellLocation;
import org.metadatacenter.jsonss.ss.CellRange;

import java.util.Optional;

public class ReferenceRendererContext
{
  private final ReferenceDirectivesSettings directivesSettings;
  private final CellRange enclosingCellRange;
  private final Optional<CellLocation> currentCellLocation;

  public ReferenceRendererContext(ReferenceDirectivesSettings directivesSettings, CellRange enclosingCellRange,
      Optional<CellLocation> currentCellLocation)
  {
    this.directivesSettings = directivesSettings;
    this.enclosingCellRange = enclosingCellRange;
    this.currentCellLocation = currentCellLocation;
  }

  public ReferenceDirectivesSettings getDirectivesSettings()
  {
    return directivesSettings;
  }

  public CellRange getEnclosingCellRange()
  {
    return enclosingCellRange;
  }

  public Optional<CellLocation> getCurrentCellLocation()
  {
    return currentCellLocation;
  }
}
