package org.metadatacenter.jsonss.renderer;

import org.metadatacenter.jsonss.core.settings.ReferenceDirectivesSettings;
import org.metadatacenter.jsonss.ss.CellLocation;
import org.metadatacenter.jsonss.ss.CellRange;

import java.util.NoSuchElementException;

public class ReferenceRendererContext
{
  private final ReferenceDirectivesSettings referenceDirectivesSettings;
  private final CellRange enclosingCellRange;

  private CellLocation currentCellLocation;

  public ReferenceRendererContext(ReferenceDirectivesSettings referenceDirectivesSettings, CellRange enclosingCellRange)
  {
    this.referenceDirectivesSettings = referenceDirectivesSettings;
    this.enclosingCellRange = enclosingCellRange;
    this.currentCellLocation = enclosingCellRange.getStartCellLocation();
  }

  public boolean hasNext()
  {
    return this.enclosingCellRange.nextCellLocation(currentCellLocation).isPresent();
  }

  public CellLocation next()
  {
     if (this.enclosingCellRange.nextCellLocation(currentCellLocation).isPresent())
       throw new NoSuchElementException("next() called after end of cell range " + this.enclosingCellRange);

    this.currentCellLocation = this.enclosingCellRange.nextCellLocation(this.currentCellLocation).get();

    return this.currentCellLocation;
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
