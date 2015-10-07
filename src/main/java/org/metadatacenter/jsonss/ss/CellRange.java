package org.metadatacenter.jsonss.ss;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Optional;

public class CellRange implements Iterable<CellLocation>
{
  private final CellLocation startCellLocation;
  private final CellLocation finishCellLocation;

  public CellRange(CellLocation startCellLocation, CellLocation finishCellLocation)
  {
    this.startCellLocation = startCellLocation;
    this.finishCellLocation = finishCellLocation;
  }

  public CellLocation getStartCellLocation() { return this.startCellLocation; }

  public CellLocation getFinishCellLocation() { return this.finishCellLocation; }

  public Optional<CellLocation> nextCellLocation(CellLocation currentCellLocation)
  {
    if (currentCellLocation.equals(finishCellLocation))
      return Optional.empty();
    else {
      int columnNumber = currentCellLocation.getColumnNumber();
      int rowNumber = currentCellLocation.getRowNumber();

      if (columnNumber < this.startCellLocation.getColumnNumber())
        return Optional.empty();

      if (rowNumber < this.startCellLocation.getRowNumber())
        return Optional.empty();

      if (rowNumber < this.finishCellLocation.getRowNumber())
        rowNumber++;
      else {
        if (columnNumber != this.finishCellLocation.getColumnNumber()) {
          columnNumber++;
          rowNumber = 0;
        } else
          return Optional.empty();
      }
      return Optional.of(new CellLocation(currentCellLocation.getSheetName(), rowNumber, columnNumber));
    }
  }

  @Override public String toString()
  {
    return startCellLocation.getFullyQualifiedCellLocation() + ":" + finishCellLocation.getCellLocation();
  }

  @Override public Iterator<CellLocation> iterator()
  {
    return new CellRangeIterator(this);
  }

  private static final class CellRangeIterator implements Iterator<CellLocation>
  {
    private final CellRange enclosingCellRange;
    private CellLocation cursor;

    public CellRangeIterator(CellRange enclosingCellRange)
    {
      this.enclosingCellRange = enclosingCellRange;
      this.cursor = enclosingCellRange.getStartCellLocation();
    }

    public boolean hasNext()
    {
      return this.enclosingCellRange.nextCellLocation(this.cursor).isPresent();
    }

    public CellLocation next()
    {
      if (this.hasNext()) {
        CellLocation current = cursor;
        cursor = this.enclosingCellRange.nextCellLocation(cursor).get();
        return current;
      } else
        throw new NoSuchElementException();
    }

    public void remove()
    {
      throw new UnsupportedOperationException();
    }
  }
}
