package org.metadatacenter.jsonss.ss;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.metadatacenter.jsonss.core.DataSource;
import org.metadatacenter.jsonss.exceptions.JSONSSException;
import org.metadatacenter.jsonss.parser.JSONSSParserConstants;
import org.metadatacenter.jsonss.parser.node.ReferenceNode;
import org.metadatacenter.jsonss.parser.node.ReferenceSourceSpecificationNode;
import org.metadatacenter.jsonss.renderer.InternalRendererException;
import org.metadatacenter.jsonss.renderer.RendererException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SpreadSheetDataSource implements DataSource, JSONSSParserConstants
{
  private final Workbook workbook;
  private Optional<CellLocation> currentCellLocation;

  private Map<String, Sheet> sheetMap = new HashMap<>();

  public SpreadSheetDataSource(Workbook workbook) throws JSONSSException
  {
    this.workbook = workbook;
    currentCellLocation = Optional.empty();

		/*
     * Populate the sheets from the workbook
		 */
    for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
      sheetMap.put(workbook.getSheetName(i), workbook.getSheetAt(i));
    }
  }

  public void setCurrentCellLocation(CellLocation cellLocation)
  {
    currentCellLocation = Optional.of(cellLocation);
  }

  public Optional<CellLocation> getCurrentCellLocation()
  {
    return currentCellLocation;
  }

  public boolean hasCurrentCellLocation()
  {
    return currentCellLocation != null;
  }

  public Workbook getWorkbook()
  {
    return workbook;
  }

  public boolean hasWorkbook()
  {
    return workbook != null;
  }

  public List<Sheet> getSheets()
  {
    return new ArrayList<>(sheetMap.values());
  }

  public List<String> getSheetNames()
  {
    return new ArrayList<>(sheetMap.keySet());
  }

  public Map<String, Sheet> getSheetMap()
  {
    return Collections.unmodifiableMap(sheetMap);
  }

  public String getCellLocationValue(CellLocation location, ReferenceNode referenceNode) throws RendererException
  {
    String locationValue = getCellLocationValue(location);
    if (referenceNode.getActualShiftDirective() != NO_SHIFT) {
      locationValue = getCellLocationValueWithShifting(location, referenceNode);
    }
    return locationValue;
  }

  public String getCellLocationValue(CellLocation cellLocation) throws RendererException
  {
    int columnNumber = cellLocation.getColumnNumber();
    int rowNumber = cellLocation.getRowNumber();

    Sheet sheet = workbook.getSheet(cellLocation.getSheetName());
    Row row = sheet.getRow(rowNumber);
    if (row == null) {
      throw new RendererException(
        "Invalid source specification @" + cellLocation + " - row " + cellLocation.getPhysicalRowNumber() + " is out of range");
    }
    Cell cell = row.getCell(columnNumber);
    return getStringValue(cell);
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

  public String getCellLocationValueWithShifting(CellLocation cellLocation, ReferenceNode referenceNode)
    throws RendererException
  {
    String sheetName = cellLocation.getSheetName();
    Sheet sheet = this.workbook.getSheet(sheetName);
    String shiftedLocationValue = getCellLocationValue(cellLocation);

    if (shiftedLocationValue == null || shiftedLocationValue.isEmpty()) {
      switch (referenceNode.getActualShiftDirective()) {
      case SHIFT_LEFT:
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
      case SHIFT_RIGHT:
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
      case SHIFT_DOWN:
        int lastRowNumber = sheet.getLastRowNum() + 1;
        for (int currentRow = cellLocation.getPhysicalRowNumber(); currentRow <= lastRowNumber; currentRow++) {
          shiftedLocationValue = getCellLocationValue(
            new CellLocation(sheetName, cellLocation.getPhysicalColumnNumber(), currentRow));
          if (shiftedLocationValue != null && !shiftedLocationValue.isEmpty()) {
            break;
          }
        }
        return shiftedLocationValue;
      case SHIFT_UP:
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

  public CellLocation resolveCellLocation(ReferenceSourceSpecificationNode referenceSourceSpecificationNode)
    throws RendererException
  {
    Pattern p = Pattern.compile("(\\*|[a-zA-Z]+)(\\*|[0-9]+)"); // ( \* | [a-zA-z]+ ) ( \* | [0-9]+ )
    Matcher m = p.matcher(referenceSourceSpecificationNode.getLocation());
    CellLocation resolvedCellLocation;
    Sheet sheet;

    if (!currentCellLocation.isPresent())
      throw new RendererException("current location not set");

    if (referenceSourceSpecificationNode.hasSource()) {
      String sheetName = referenceSourceSpecificationNode.getSource();

      if (!hasWorkbook()) {
        throw new RendererException("sheet name '" + sheetName + "' specified but there is no active workbook");
      }
      sheet = getWorkbook().getSheet(sheetName);

      if (sheet == null) {
        throw new RendererException("invalid sheet name '" + sheetName + "'");
      }
    } else { // No sheet name specified - use the sheet name from the current location
      String sheetName = getCurrentCellLocation().get().getSheetName();
      sheet = getWorkbook().getSheet(sheetName);

      if (sheet == null) {
        throw new RendererException("invalid sheet name '" + sheetName + "'");
      }
    }

    if (m.find()) {
      String columnSpecification = m.group(1);
      String rowSpecification = m.group(2);

      if (columnSpecification == null) {
        throw new RendererException("missing column specification in location " + referenceSourceSpecificationNode);
      }
      if (rowSpecification == null) {
        throw new RendererException("missing row specification in location " + referenceSourceSpecificationNode);
      }
      boolean isColumnWildcard = "*".equals(columnSpecification);
      boolean isRowWildcard = "*".equals(rowSpecification);
      int columnNumber, rowNumber;

      try {
        if (isColumnWildcard) {
          columnNumber = getCurrentCellLocation().get().getPhysicalColumnNumber();
        } else {
          columnNumber = SpreadSheetUtil.getColumnNumber(sheet, columnSpecification);
        }
        if (isRowWildcard) {
          rowNumber = currentCellLocation.get().getPhysicalRowNumber();
        } else {
          rowNumber = SpreadSheetUtil.getRowNumber(sheet, rowSpecification);
        }
      } catch (JSONSSException e) {
        throw new RendererException("invalid source specification " + referenceSourceSpecificationNode + " - " + e.getMessage());
      }
      resolvedCellLocation = new CellLocation(sheet.getSheetName(), columnNumber, rowNumber);
    } else {
      throw new RendererException("invalid source specification " + referenceSourceSpecificationNode);
    }
    return resolvedCellLocation;
  }
}
