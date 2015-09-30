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
  private Optional<SpreadsheetLocation> currentLocation;

  private Map<String, Sheet> sheetMap = new HashMap<>();

  public SpreadSheetDataSource(Workbook workbook) throws JSONSSException
  {
    this.workbook = workbook;
    currentLocation = Optional.empty();

		/*
     * Populate the sheets from the workbook
		 */
    for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
      sheetMap.put(workbook.getSheetName(i), workbook.getSheetAt(i));
    }
  }

  public void setCurrentLocation(SpreadsheetLocation location)
  {
    currentLocation = Optional.of(location);
  }

  public Optional<SpreadsheetLocation> getCurrentLocation()
  {
    return currentLocation;
  }

  public boolean hasCurrentLocation()
  {
    return currentLocation != null;
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

  public String getLocationValue(SpreadsheetLocation location, ReferenceNode referenceNode) throws RendererException
  {
    String locationValue = getLocationValue(location);
    if (referenceNode.getActualShiftDirective() != NO_SHIFT) {
      locationValue = getLocationValueWithShifting(location, referenceNode);
    }
    return locationValue;
  }

  public String getLocationValue(SpreadsheetLocation location) throws RendererException
  {
    int columnNumber = location.getColumnNumber();
    int rowNumber = location.getRowNumber();

    Sheet sheet = workbook.getSheet(location.getSheetName());
    Row row = sheet.getRow(rowNumber);
    if (row == null) {
      throw new RendererException(
        "Invalid source specification @" + location + " - row " + location.getPhysicalRowNumber() + " is out of range");
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

  public String getLocationValueWithShifting(SpreadsheetLocation location, ReferenceNode referenceNode)
    throws RendererException
  {
    String sheetName = location.getSheetName();
    Sheet sheet = this.workbook.getSheet(sheetName);
    String shiftedLocationValue = getLocationValue(location);

    if (shiftedLocationValue == null || shiftedLocationValue.isEmpty()) {
      switch (referenceNode.getActualShiftDirective()) {
      case SHIFT_LEFT:
        int firstColumnNumber = 1;
        for (int currentColumn = location.getPhysicalColumnNumber();
             currentColumn >= firstColumnNumber; currentColumn--) {
          shiftedLocationValue = getLocationValue(
            new SpreadsheetLocation(sheetName, currentColumn, location.getPhysicalRowNumber()));
          if (shiftedLocationValue != null && !shiftedLocationValue.isEmpty()) {
            break;
          }
        }
        return shiftedLocationValue;
      case SHIFT_RIGHT:
        int lastColumnNumber = sheet.getRow(location.getRowNumber()).getLastCellNum();
        for (int currentColumn = location.getPhysicalColumnNumber();
             currentColumn <= lastColumnNumber; currentColumn++) {
          shiftedLocationValue = getLocationValue(
            new SpreadsheetLocation(sheetName, currentColumn, location.getPhysicalRowNumber()));
          if (shiftedLocationValue != null && !shiftedLocationValue.isEmpty()) {
            break;
          }
        }
        return shiftedLocationValue;
      case SHIFT_DOWN:
        int lastRowNumber = sheet.getLastRowNum() + 1;
        for (int currentRow = location.getPhysicalRowNumber(); currentRow <= lastRowNumber; currentRow++) {
          shiftedLocationValue = getLocationValue(
            new SpreadsheetLocation(sheetName, location.getPhysicalColumnNumber(), currentRow));
          if (shiftedLocationValue != null && !shiftedLocationValue.isEmpty()) {
            break;
          }
        }
        return shiftedLocationValue;
      case SHIFT_UP:
        int firstRowNumber = 1;
        for (int currentRow = location.getPhysicalRowNumber(); currentRow >= firstRowNumber; currentRow--) {
          shiftedLocationValue = getLocationValue(
            new SpreadsheetLocation(sheetName, location.getPhysicalColumnNumber(), currentRow));
          if (shiftedLocationValue != null && !shiftedLocationValue.isEmpty()) {
            break;
          }
        }
        return shiftedLocationValue;
      default:
        throw new InternalRendererException("Unknown shift setting " + referenceNode.getActualShiftDirective());
      }
    } else {
      referenceNode.setShiftedLocation(location);
      return shiftedLocationValue;
    }
  }

  public SpreadsheetLocation resolveLocation(ReferenceSourceSpecificationNode referenceSourceSpecificationNode)
    throws RendererException
  {
    Pattern p = Pattern.compile("(\\*|[a-zA-Z]+)(\\*|[0-9]+)"); // ( \* | [a-zA-z]+ ) ( \* | [0-9]+ )
    Matcher m = p.matcher(referenceSourceSpecificationNode.getLocation());
    SpreadsheetLocation resolvedLocation;
    Sheet sheet;

    if (!currentLocation.isPresent())
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
      String sheetName = getCurrentLocation().get().getSheetName();
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
          columnNumber = getCurrentLocation().get().getPhysicalColumnNumber();
        } else {
          columnNumber = SpreadSheetUtil.getColumnNumber(sheet, columnSpecification);
        }
        if (isRowWildcard) {
          rowNumber = currentLocation.get().getPhysicalRowNumber();
        } else {
          rowNumber = SpreadSheetUtil.getRowNumber(sheet, rowSpecification);
        }
      } catch (JSONSSException e) {
        throw new RendererException("invalid source specification " + referenceSourceSpecificationNode + " - " + e.getMessage());
      }
      resolvedLocation = new SpreadsheetLocation(sheet.getSheetName(), columnNumber, rowNumber);
    } else {
      throw new RendererException("invalid source specification " + referenceSourceSpecificationNode);
    }
    return resolvedLocation;
  }
}
