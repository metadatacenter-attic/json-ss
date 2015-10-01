package org.metadatacenter.jsonss.ss;

public class CellRange
{
  private final CellLocation startRange;
  private final CellLocation finishRange;

  public CellRange(CellLocation startRange, CellLocation finishRange)
  {
    this.startRange = startRange;
    this.finishRange = finishRange;
  }

  public CellLocation getStartRange() { return this.startRange; }

  public CellLocation getFinishRange() { return this.finishRange; }
}
