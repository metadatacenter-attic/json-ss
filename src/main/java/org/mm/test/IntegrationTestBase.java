package org.mm.test;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.mm.core.settings.ReferenceSettings;
import org.mm.exceptions.JSONSSException;
import org.mm.parser.ASTExpression;
import org.mm.parser.JSONSSParser;
import org.mm.parser.ParseException;
import org.mm.parser.SimpleNode;
import org.mm.parser.node.ExpressionNode;
import org.mm.parser.node.MMExpressionNode;
import org.mm.renderer.text.TextRenderer;
import org.mm.rendering.text.TextRendering;
import org.mm.ss.SpreadSheetDataSource;
import org.mm.ss.SpreadsheetLocation;

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
	protected static final String SHEET2 = "Sheet2";
	protected static final String SHEET3 = "Sheet3";
	protected static final String DEFAULT_SHEET = SHEET1;
	protected static final Set<Label> EMPTY_CELL_SET = Collections.emptySet();
	protected static final SpreadsheetLocation DEFAULT_CURRENT_LOCATION = new SpreadsheetLocation(SHEET1, 1, 1);
	protected static final String DEFAULT_PREFIX = ":";

	protected Workbook createWorkbook(String sheetName, Set<Label> cells) throws IOException
	{
		Workbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet(sheetName);
		
		Map<Integer, Row> buffer = new HashMap<Integer, Row>();
		for (Label cell : cells) {
			int rownum = cell.getRowIndex();
			Row row = buffer.get(rownum);
			if (row == null) {
				row = sheet.createRow(rownum);
				buffer.put(rownum, row);
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

	protected MMExpressionNode parseExpression(String expression, ReferenceSettings settings) throws ParseException
	{
		JSONSSParser parser = new JSONSSParser(new ByteArrayInputStream(expression.getBytes()), settings, -1);
		SimpleNode simpleNode = parser.expression();
		ExpressionNode expressionNode = new ExpressionNode((ASTExpression)simpleNode);

		return expressionNode.getMMExpressionNode();
	}

	protected Optional<? extends TextRendering> createTextRendering(String expression, ReferenceSettings settings)
			throws JSONSSException, IOException, ParseException
	{
		return createTextRendering(DEFAULT_SHEET, EMPTY_CELL_SET, expression, settings);
	}

	protected Optional<? extends TextRendering> createTextRendering(String sheetName, Set<Label> cells,
			SpreadsheetLocation currentLocation, String expression, ReferenceSettings settings)
			throws JSONSSException, IOException, ParseException
	{
		SpreadSheetDataSource dataSource = createSpreadsheetDataSource(sheetName, cells);

		dataSource.setCurrentLocation(currentLocation);

		TextRenderer renderer = new TextRenderer(dataSource);
		MMExpressionNode mmExpressionNode = parseExpression(expression, settings);

		return renderer.renderExpression(mmExpressionNode);
	}

	protected Optional<? extends TextRendering> createTextRendering(String sheetName, String expression,
			ReferenceSettings settings) throws JSONSSException, IOException, ParseException
	{
		return createTextRendering(sheetName, EMPTY_CELL_SET, expression, settings);
	}

	protected Optional<? extends TextRendering> createTextRendering(String sheetName, Set<Label> cells, String expression,
			ReferenceSettings settings) throws JSONSSException, IOException, ParseException
	{
		return createTextRendering(sheetName, cells, DEFAULT_CURRENT_LOCATION, expression, settings);
	}

	/**
	 * @param content      Content of the cell
	 * @param columnNumber 1-based column number
	 * @param rowNumber    1-based row number
	 * @return A cell
	 */
	protected Label createCell(String content, int columnNumber, int rowNumber)
	{
		return new Label(content, columnNumber-1, rowNumber-1); // POI is 0-based
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
