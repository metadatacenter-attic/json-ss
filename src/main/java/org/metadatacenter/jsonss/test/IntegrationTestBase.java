package org.metadatacenter.jsonss.test;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.metadatacenter.jsonss.core.settings.ReferenceSettings;
import org.metadatacenter.jsonss.exceptions.JSONSSException;
import org.metadatacenter.jsonss.parser.ASTJSONExpression;
import org.metadatacenter.jsonss.parser.JSONSSParser;
import org.metadatacenter.jsonss.parser.ParseException;
import org.metadatacenter.jsonss.parser.SimpleNode;
import org.metadatacenter.jsonss.parser.node.JSONExpressionNode;
import org.metadatacenter.jsonss.renderer.text.TextRenderer;
import org.metadatacenter.jsonss.rendering.text.TextRendering;
import org.metadatacenter.jsonss.ss.CellLocation;
import org.metadatacenter.jsonss.ss.SpreadSheetDataSource;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class IntegrationTestBase
{
  protected static final String SHEET1 = "Sheet1";
  protected static final String DEFAULT_SHEET = SHEET1;
  protected static final Set<Label> EMPTY_CELL_SET = Collections.emptySet();
  protected static final CellLocation DEFAULT_CURRENT_CELL_LOCATION = new CellLocation(SHEET1, 1, 1);

  protected Workbook createWorkbook(String sheetName, Set<Label> cells) throws IOException
  {
    Workbook workbook = new XSSFWorkbook();
    Sheet sheet = workbook.createSheet(sheetName);

    Map<Integer, Row> buffer = new HashMap<>();
    for (Label cell : cells) {
      int rowIndex = cell.getRowIndex();
      Row row = buffer.get(rowIndex);
      if (row == null) {
        row = sheet.createRow(rowIndex);
        buffer.put(rowIndex, row);
      }
      row.createCell(cell.columnIndex).setCellValue(cell.getContent());
    }
    return workbook;
  }

  protected SpreadSheetDataSource createSpreadsheetDataSource(String sheetName, Set<Label> cells)
    throws IOException, JSONSSException
  {
    Workbook workbook = createWorkbook(sheetName, cells);
    return new SpreadSheetDataSource(workbook);
  }

  protected JSONExpressionNode parseJSONExpression(String expression, ReferenceSettings settings) throws ParseException
  {
    JSONSSParser parser = new JSONSSParser(new ByteArrayInputStream(expression.getBytes()), settings, -1);
    SimpleNode simpleNode = parser.json_expression();
    JSONExpressionNode jsonExpressionNode = new JSONExpressionNode((ASTJSONExpression)simpleNode);

    return jsonExpressionNode;
  }

  protected Optional<? extends TextRendering> createTextRendering(String sheetName, Set<Label> cells,
    CellLocation currentCellLocation, String expression, ReferenceSettings settings)
    throws JSONSSException, IOException, ParseException
  {
    SpreadSheetDataSource dataSource = createSpreadsheetDataSource(sheetName, cells);

    dataSource.setCurrentCellLocation(currentCellLocation);

    TextRenderer renderer = new TextRenderer(dataSource);
    JSONExpressionNode jsonExpressionNode = parseJSONExpression(expression, settings);

    return renderer.renderJSONExpression(jsonExpressionNode);
  }

  protected Optional<? extends TextRendering> createTextRendering(String expression, ReferenceSettings settings)
    throws JSONSSException, IOException, ParseException
  {
    return createTextRendering(DEFAULT_SHEET, EMPTY_CELL_SET, expression, settings);
  }

  protected Optional<? extends TextRendering> createTextRendering(String sheetName, Set<Label> cells, String expression,
    ReferenceSettings settings) throws JSONSSException, IOException, ParseException
  {
    return createTextRendering(sheetName, cells, DEFAULT_CURRENT_CELL_LOCATION, expression, settings);
  }

  /**
   * @param content      Content of the cell
   * @param columnNumber 1-based column number
   * @param rowNumber    1-based row number
   * @return A cell
   */
  protected Label createCell(String content, int columnNumber, int rowNumber)
  {
    return new Label(content, columnNumber - 1, rowNumber - 1); // POI is 0-based
  }

  protected Set<Label> createCells(Label... cells)
  {
    Set<Label> cellSet = new HashSet<>();
    Collections.addAll(cellSet, cells);
    return cellSet;
  }

  public class Label
  {
    private String content;
    private int columnIndex;
    private int rowIndex;

    public Label(String content, int columnIndex, int rowIndex)
    {
      this.content = content;
      this.columnIndex = columnIndex;
      this.rowIndex = rowIndex;
    }

    public String getContent()
    {
      return content;
    }

    public int getColumnIndex()
    {
      return columnIndex;
    }

    public int getRowIndex()
    {
      return rowIndex;
    }
  }
}
