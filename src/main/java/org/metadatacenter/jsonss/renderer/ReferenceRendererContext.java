package org.metadatacenter.jsonss.renderer;

import org.metadatacenter.jsonss.core.settings.ReferenceDirectivesSettings;
import org.metadatacenter.jsonss.ss.CellLocation;
import org.metadatacenter.jsonss.ss.CellRange;

public class ReferenceRendererContext
{
  private final ReferenceDirectivesSettings referenceDirectivesSettings;
  private final CellRange enclosingCellRange;
  private final CellLocation currentCellLocation;

  public ReferenceRendererContext(ReferenceDirectivesSettings referenceDirectivesSettings, CellRange enclosingCellRange,
    CellLocation currentCellLocation)
  {
    this.referenceDirectivesSettings = referenceDirectivesSettings;
    this.enclosingCellRange = enclosingCellRange;
    this.currentCellLocation = currentCellLocation;
  }

  public ReferenceRendererContext(ReferenceDirectivesSettings referenceDirectivesSettings, CellRange enclosingCellRange)
  {
    this.referenceDirectivesSettings = referenceDirectivesSettings;
    this.enclosingCellRange = enclosingCellRange;
    this.currentCellLocation = enclosingCellRange.getStartCellLocation();
  }

  public ReferenceDirectivesSettings getReferenceDirectivesSettings()
  {
    return this.referenceDirectivesSettings;
  }

  public CellRange getEnclosingCellRange()
  {
    return this.enclosingCellRange;
  }

  public CellLocation getCurrentCellLocation()
  {
    return this.currentCellLocation;
  }
}
