package org.metadatacenter.jsonss.ss;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.metadatacenter.jsonss.core.DataSource;
import org.metadatacenter.jsonss.exceptions.JSONSSException;
import org.metadatacenter.jsonss.parser.JSONSSParserConstants;
import org.metadatacenter.jsonss.parser.node.ReferenceNode;
import org.metadatacenter.jsonss.parser.node.ReferenceCellLocationSpecificationNode;
import org.metadatacenter.jsonss.renderer.InternalRendererException;
import org.metadatacenter.jsonss.renderer.RendererException;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
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

  @Override public CellLocation resolveCellLocation(ReferenceCellLocationSpecificationNode referenceCellLocationSpecificationNode,
    Optional<CellLocation> currentCellLocation) throws RendererException
  {
    Pattern p = Pattern.compile("(\\*|[a-zA-Z]+)(\\*|[0-9]+)"); // ( \* | [a-zA-z]+ ) ( \* | [0-9]+ )
    Matcher m = p.matcher(referenceCellLocationSpecificationNode.getLocation());
    CellLocation resolvedCellLocation;
    Sheet sheet;

    if (!currentCellLocation.isPresent())
      throw new RendererException("current location not set");

    if (referenceCellLocationSpecificationNode.hasSource()) {
      String sheetName = referenceCellLocationSpecificationNode.getSource();

      if (!hasWorkbook()) {
        throw new RendererException("sheet name '" + sheetName + "' specified but there is no active workbook");
      }
      sheet = getWorkbook().getSheet(sheetName);

      if (sheet == null)
        throw new RendererException("invalid sheet name '" + sheetName + "'");

    } else { // No sheet name specified - use the sheet name from the current location
      String sheetName = currentCellLocation.get().getSheetName();
      sheet = getWorkbook().getSheet(sheetName);

      if (sheet == null)
        throw new RendererException("invalid sheet name '" + sheetName + "'");
    }

    if (m.find()) {
      String columnSpecification = m.group(1);
      String rowSpecification = m.group(2);

      if (columnSpecification == null) {
        throw new RendererException("missing column specification in location " + referenceCellLocationSpecificationNode);
      }
      if (rowSpecification == null) {
        throw new RendererException("missing row specification in location " + referenceCellLocationSpecificationNode);
      }
      boolean isColumnWildcard = "*".equals(columnSpecification);
      boolean isRowWildcard = "*".equals(rowSpecification);
      int columnNumber, rowNumber;

      try {
        if (isColumnWildcard) {
          columnNumber = currentCellLocation.get().getPhysicalColumnNumber();
        } else {
          columnNumber = SpreadSheetUtil.getColumnNumber(sheet, columnSpecification);
        }
        if (isRowWildcard) {
          rowNumber = currentCellLocation.get().getPhysicalRowNumber();
        } else {
          rowNumber = SpreadSheetUtil.getRowNumber(sheet, rowSpecification);
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
    if (referenceNode.getActualShiftDirective() != JSONSSParserConstants.NO_SHIFT)
      return getCellLocationValueWithShifting(cellLocation, referenceNode);
    else
      return getCellLocationValue(cellLocation);
  }

  @Override public CellRange getDefaultEnclosingCellRange()
  {
    return null; // TODO
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
        "invalid source specification @" + cellLocation + " - row " + cellLocation.getPhysicalRowNumber()
          + " is out of range");
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
      switch (referenceNode.getActualShiftDirective()) {
      case JSONSSParserConstants.SHIFT_LEFT:
        int firstColumnNumber = 1;
        for (int currentColumn = cellLocation.getPhysicalColumnNumber();
             currentColumn >= firstColumnNumber; currentColumn--) {
          shiftedLocationValue = getCellLocationValue(
            new CellLocation(sheetName, currentColumn, cellLocation.getPhysicalRowNumber()));
          if (shiftedLocationValue != null && !shiftedLocationValue.isEmpty()) {
            break;
          }
        }
        return shiftedLocationValue;
      case JSONSSParserConstants.SHIFT_RIGHT:
        int lastColumnNumber = sheet.getRow(cellLocation.getRowNumber()).getLastCellNum();
        for (int currentColumn = cellLocation.getPhysicalColumnNumber();
             currentColumn <= lastColumnNumber; currentColumn++) {
          shiftedLocationValue = getCellLocationValue(
            new CellLocation(sheetName, currentColumn, cellLocation.getPhysicalRowNumber()));
          if (shiftedLocationValue != null && !shiftedLocationValue.isEmpty()) {
            break;
          }
        }
        return shiftedLocationValue;
      case JSONSSParserConstants.SHIFT_DOWN:
        int lastRowNumber = sheet.getLastRowNum() + 1;
        for (int currentRow = cellLocation.getPhysicalRowNumber(); currentRow <= lastRowNumber; currentRow++) {
          shiftedLocationValue = getCellLocationValue(
            new CellLocation(sheetName, cellLocation.getPhysicalColumnNumber(), currentRow));
          if (shiftedLocationValue != null && !shiftedLocationValue.isEmpty()) {
            break;
          }
        }
        return shiftedLocationValue;
      case JSONSSParserConstants.SHIFT_UP:
        int firstRowNumber = 1;
        for (int currentRow = cellLocation.getPhysicalRowNumber(); currentRow >= firstRowNumber; currentRow--) {
          shiftedLocationValue = getCellLocationValue(
            new CellLocation(sheetName, cellLocation.getPhysicalColumnNumber(), currentRow));
          if (shiftedLocationValue != null && !shiftedLocationValue.isEmpty()) {
            break;
          }
        }
        return shiftedLocationValue;
      default:
        throw new InternalRendererException("Unknown shift setting " + referenceNode.getActualShiftDirective());
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
