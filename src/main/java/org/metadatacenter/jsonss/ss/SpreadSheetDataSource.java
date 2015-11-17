package org.metadatacenter.jsonss.ss;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.metadatacenter.jsonss.core.DataSource;
import org.metadatacenter.jsonss.core.settings.ShiftDirectiveSetting;
import org.metadatacenter.jsonss.exceptions.JSONSSException;
import org.metadatacenter.jsonss.parser.node.ReferenceCellLocationSpecificationNode;
import org.metadatacenter.jsonss.parser.node.ReferenceNode;
import org.metadatacenter.jsonss.renderer.InternalRendererException;
import org.metadatacenter.jsonss.renderer.RendererException;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SpreadSheetDataSource implements DataSource
{
  private final Workbook workbook;

  private final Map<String, Sheet> sheetMap = new HashMap<>();

  public SpreadSheetDataSource(Workbook workbook) throws JSONSSException
  {
    this.workbook = workbook;

    for (int i = 0; i < workbook.getNumberOfSheets(); i++)
      sheetMap.put(workbook.getSheetName(i), workbook.getSheetAt(i));
  }

  @Override public CellLocation getCellLocation(
      ReferenceCellLocationSpecificationNode referenceCellLocationSpecificationNode, CellLocation currentCellLocation)
      throws RendererException
  {
    Pattern p = Pattern.compile("(\\*|[a-zA-Z]+)(\\*|[0-9]+)"); // ( \* | [a-zA-z]+ ) ( \* | [0-9]+ )
    Matcher m = p.matcher(referenceCellLocationSpecificationNode.getLocation());
    CellLocation resolvedCellLocation;
    Sheet sheet;

    if (referenceCellLocationSpecificationNode.hasSource()) {
      String sheetName = referenceCellLocationSpecificationNode.getSource();

      if (!hasWorkbook()) {
        throw new RendererException("sheet name '" + sheetName + "' specified but there is no active workbook");
      }
      sheet = getWorkbook().getSheet(sheetName);

      if (sheet == null)
        throw new RendererException("invalid sheet name '" + sheetName + "'");

    } else { // No sheet name specified - use the sheet name from the current location
      String sheetName = currentCellLocation.getSheetName();
      sheet = getWorkbook().getSheet(sheetName);

      if (sheet == null)
        throw new RendererException("invalid sheet name '" + sheetName + "'");
    }

    if (m.find()) {
      String columnSpecification = m.group(1);
      String rowSpecification = m.group(2);

      if (columnSpecification == null) {
        throw new RendererException(
            "missing column specification in location " + referenceCellLocationSpecificationNode);
      }
      if (rowSpecification == null) {
        throw new RendererException("missing row specification in location " + referenceCellLocationSpecificationNode);
      }
      boolean isColumnWildcard = "*".equals(columnSpecification);
      boolean isRowWildcard = "*".equals(rowSpecification);
      int columnNumber, rowNumber;

      try {
        if (isColumnWildcard) {
          columnNumber = currentCellLocation.getColumnNumber();
        } else {
          columnNumber = SpreadSheetUtil.getColumnNumber(sheet, columnSpecification) - 1;
        }
        if (isRowWildcard) {
          rowNumber = currentCellLocation.getRowNumber();
        } else {
          rowNumber = SpreadSheetUtil.getRowNumber(sheet, rowSpecification) - 1;
        }
      } catch (JSONSSException e) {
        throw new RendererException(
            "invalid source specification " + referenceCellLocationSpecificationNode + " - " + e.getMessage());
      }
      resolvedCellLocation = new CellLocation(sheet.getSheetName(), columnNumber, rowNumber);
    } else {
      throw new RendererException("invalid source specification " + referenceCellLocationSpecificationNode);
    }
    return resolvedCellLocation;
  }

  @Override public String getCellLocationValue(CellLocation cellLocation, ReferenceNode referenceNode)
      throws RendererException
  {
    if (referenceNode.getActualShiftDirectiveSetting() != ShiftDirectiveSetting.NO_SHIFT)
      return getCellLocationValueWithShifting(cellLocation, referenceNode);
    else
      return getCellLocationValue(cellLocation);
  }

  @Override public CellRange getDefaultEnclosingCellRange()
  {
    Sheet firstSheet = this.workbook.getSheetAt(0);
    String firstSheetName = firstSheet.getSheetName();
    int firstRow = firstSheet.getFirstRowNum();
    int lastRow = firstSheet.getLastRowNum();
    int firstColumn = 0;
    int lastColumn = 0;

    for (int currentRow = firstRow; currentRow < lastRow; currentRow++) {
      int currentNumberOfColumns = firstSheet.getRow(currentRow).getLastCellNum();
      if (lastColumn < currentNumberOfColumns)
        lastColumn = currentNumberOfColumns;
    }

    CellLocation startRange = new CellLocation(firstSheetName, firstColumn, firstRow);
    CellLocation finishRange = new CellLocation(firstSheetName, lastColumn, lastRow);

    return new CellRange(startRange, finishRange);
  }

  private Workbook getWorkbook()
  {
    return workbook;
  }

  private boolean hasWorkbook()
  {
    return workbook != null;
  }

  private String getCellLocationValue(CellLocation cellLocation) throws RendererException
  {
    int columnNumber = cellLocation.getColumnNumber();
    int rowNumber = cellLocation.getRowNumber();

    Sheet sheet = workbook.getSheet(cellLocation.getSheetName());
    Row row = sheet.getRow(rowNumber);
    if (row == null) {
      throw new RendererException(
          "invalid source specification @" + cellLocation + " - row is out of range");
    }
    Cell cell = row.getCell(columnNumber);
    return getStringValue(cell);
  }

  private String getCellLocationValueWithShifting(CellLocation cellLocation, ReferenceNode referenceNode)
      throws RendererException
  {
    String sheetName = cellLocation.getSheetName();
    Sheet sheet = this.workbook.getSheet(sheetName);
    String shiftedLocationValue = getCellLocationValue(cellLocation);

    if (shiftedLocationValue == null || shiftedLocationValue.isEmpty()) {
      switch (referenceNode.getActualShiftDirectiveSetting()) {
      case SHIFT_LEFT:
        int firstColumnNumber = 0;
        for (int currentColumn = cellLocation.getColumnNumber();
             currentColumn >= firstColumnNumber; currentColumn--) {
          shiftedLocationValue = getCellLocationValue(
              new CellLocation(sheetName, currentColumn, cellLocation.getRowNumber()));
          if (shiftedLocationValue != null && !shiftedLocationValue.isEmpty()) {
            break;
          }
        }
        return shiftedLocationValue;
      case SHIFT_RIGHT:
        int lastColumnNumber = sheet.getRow(cellLocation.getRowNumber()).getLastCellNum();
        for (int currentColumn = cellLocation.getColumnNumber();
             currentColumn <= lastColumnNumber; currentColumn++) {
          shiftedLocationValue = getCellLocationValue(
              new CellLocation(sheetName, currentColumn, cellLocation.getRowNumber()));
          if (shiftedLocationValue != null && !shiftedLocationValue.isEmpty()) {
            break;
          }
        }
        return shiftedLocationValue;
      case SHIFT_DOWN:
        int lastRowNumber = sheet.getLastRowNum();
        for (int currentRow = cellLocation.getRowNumber(); currentRow <= lastRowNumber; currentRow++) {
          shiftedLocationValue = getCellLocationValue(
              new CellLocation(sheetName, cellLocation.getColumnNumber(), currentRow));
          if (shiftedLocationValue != null && !shiftedLocationValue.isEmpty()) {
            break;
          }
        }
        return shiftedLocationValue;
      case SHIFT_UP:
        int firstRowNumber = 0;
        for (int currentRow = cellLocation.getRowNumber(); currentRow >= firstRowNumber; currentRow--) {
          shiftedLocationValue = getCellLocationValue(
              new CellLocation(sheetName, cellLocation.getColumnNumber(), currentRow));
          if (shiftedLocationValue != null && !shiftedLocationValue.isEmpty()) {
            break;
          }
        }
        return shiftedLocationValue;
      default:
        throw new InternalRendererException("Unknown shift setting " + referenceNode.getActualShiftDirectiveSetting());
      }
    } else {
      referenceNode.setShiftedCellLocation(cellLocation);
      return shiftedLocationValue;
    }
  }

  private String getStringValue(Cell cell)
  {
    if (cell == null) {
      return "";
    }
    switch (cell.getCellType()) {
    case Cell.CELL_TYPE_BLANK:
      return "";
    case Cell.CELL_TYPE_STRING:
      return cell.getStringCellValue();
    case Cell.CELL_TYPE_NUMERIC:
      if (isInteger(cell.getNumericCellValue())) // check if the numeric is an integer or double
        return Integer.toString((int)cell.getNumericCellValue());
      else
        return Double.toString(cell.getNumericCellValue());
    case Cell.CELL_TYPE_BOOLEAN:
      return Boolean.toString(cell.getBooleanCellValue());
    case Cell.CELL_TYPE_FORMULA:
      return Double.toString(cell.getNumericCellValue());
    default:
      return "";
    }
  }

  private boolean isInteger(double number)
  {
    return (number == Math.floor(number) && !Double.isInfinite(number));
  }
}
