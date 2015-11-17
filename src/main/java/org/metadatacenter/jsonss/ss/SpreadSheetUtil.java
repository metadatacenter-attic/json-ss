package org.metadatacenter.jsonss.ss;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.metadatacenter.jsonss.exceptions.JSONSSException;

public class SpreadSheetUtil
{
  static public void checkColumnSpecification(String columnSpecification) throws JSONSSException
  {
    if (columnSpecification.isEmpty())
      throw new JSONSSException("empty column specification");

    if (!columnSpecification.equals("+")) {
      for (int i = 0; i < columnSpecification.length(); i++) {
        char c = columnSpecification.charAt(i);
        if (!isAlpha(c))
          throw new JSONSSException("invalid column specification " + columnSpecification);
      }
    }
  }

  static public void checkRowSpecification(String rowSpecification) throws JSONSSException
  {
    if (rowSpecification.isEmpty())
      throw new JSONSSException("empty row specification");

    if (!rowSpecification.equals("*")) {
      for (int i = 0; i < rowSpecification.length(); i++) {
        char c = rowSpecification.charAt(i);
        if (!isNumeric(c))
          throw new JSONSSException("invalid row specification " + rowSpecification);
      }
    }
  }

  /**
   *
   * @param columnName
   * @return 1-based column number
   * @throws JSONSSException
   */
  static public int columnName2Number(String columnName) throws JSONSSException
  {
    int pos = 0;

    checkColumnSpecification(columnName);

    for (int i = 0; i < columnName.length(); i++) {
      pos *= 26;
      try {
        pos += Integer.parseInt(columnName.substring(i, i + 1), 36) - 9;
      } catch (NumberFormatException e) {
        throw new JSONSSException("invalid column name " + columnName);
      }
    }
    return pos;
  }

  /**
   *
   * @param columnNumber 1-based
   * @return
   */
  static public String columnNumber2Name(int columnNumber)
  {
    String col = "";
    while (columnNumber > 0) {
      columnNumber--;
      col = (char)(columnNumber % 26 + 65) + col;
      columnNumber = columnNumber / 26;
    }
    return col;
  }

  /**
   *
   * @param row 1-based
   * @return
   * @throws JSONSSException
   */
  static public int row2Number(String row) throws JSONSSException
  {
    if (row.isEmpty())
      throw new JSONSSException("empty row number");

    try {
      return Integer.parseInt(row);
    } catch (NumberFormatException e) {
      throw new JSONSSException(row + " is not a valid row number");
    }
  }

  /**
   *
   * @param sheet
   * @param columnSpecification
   * @return 1-basec column number
   * @throws JSONSSException
   */
  public static int getColumnNumber(Sheet sheet, String columnSpecification) throws JSONSSException
  {
    checkColumnSpecification(columnSpecification);
    int columnNumber = columnName2Number(columnSpecification);
    return columnNumber; // 0-indexed
  }

  /**
   *
   * @param sheet
   * @param rowSpecification
   * @return 1-based row number
   * @throws JSONSSException
   */
  public static int getRowNumber(Sheet sheet, String rowSpecification) throws JSONSSException
  {
    checkRowSpecification(rowSpecification);
    int rowNumber = Integer.parseInt(rowSpecification);
    return rowNumber; // 0-indexed
  }

  public static Sheet getSheet(Workbook workbook, String sheetName) throws JSONSSException
  {
    Sheet sheet = workbook.getSheet(sheetName);

    if (sheet == null)
      throw new JSONSSException("invalid sheet name " + sheetName);

    return sheet;
  }

  private static boolean isAlpha(char c) { return c >= 'A' && c <= 'Z'; }

  private static boolean isNumeric(char c) { return c >= '0' && c <= '9'; }
} 
