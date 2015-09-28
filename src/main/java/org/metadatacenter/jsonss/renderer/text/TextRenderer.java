package org.metadatacenter.jsonss.renderer.text;

import org.metadatacenter.jsonss.core.ReferenceType;
import org.metadatacenter.jsonss.parser.JSONSSParserConstants;
import org.metadatacenter.jsonss.parser.node.JSONExpressionNode;
import org.metadatacenter.jsonss.parser.node.ReferenceNode;
import org.metadatacenter.jsonss.parser.node.SourceSpecificationNode;
import org.metadatacenter.jsonss.parser.node.StringLiteralNode;
import org.metadatacenter.jsonss.parser.node.ValueExtractionFunctionArgumentNode;
import org.metadatacenter.jsonss.parser.node.ValueExtractionFunctionNode;
import org.metadatacenter.jsonss.renderer.InternalRendererException;
import org.metadatacenter.jsonss.renderer.ReferenceRenderer;
import org.metadatacenter.jsonss.renderer.ReferenceRendererConfiguration;
import org.metadatacenter.jsonss.renderer.ReferenceUtil;
import org.metadatacenter.jsonss.renderer.JSONSSRenderer;
import org.metadatacenter.jsonss.renderer.RendererException;
import org.metadatacenter.jsonss.rendering.ReferenceRendering;
import org.metadatacenter.jsonss.rendering.StringLiteralRendering;
import org.metadatacenter.jsonss.rendering.text.TextReferenceRendering;
import org.metadatacenter.jsonss.rendering.text.TextRendering;
import org.metadatacenter.jsonss.rendering.text.TextStringLiteralRendering;
import org.metadatacenter.jsonss.ss.SpreadSheetDataSource;
import org.metadatacenter.jsonss.ss.SpreadsheetLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TextRenderer extends ReferenceRendererConfiguration
  implements JSONSSRenderer, ReferenceRenderer, JSONSSParserConstants
{
  private SpreadSheetDataSource dataSource;

  public TextRenderer(SpreadSheetDataSource dataSource)
  {
    this.dataSource = dataSource;
  }

  @Override public void changeDataSource(SpreadSheetDataSource dataSource)
  {
    // Logging data source has been updated
    this.dataSource = dataSource;
  }

  @Override public ReferenceRendererConfiguration getReferenceRendererConfiguration()
  {
    return this;
  }

  @Override public Optional<? extends TextRendering> renderJSONExpression(JSONExpressionNode jsonExpressionNode)
    throws RendererException
  {
    throw new InternalRendererException("not implemented " + jsonExpressionNode.getNodeName());
  }

  @Override public Optional<TextReferenceRendering> renderReference(ReferenceNode referenceNode)
    throws RendererException
  {
    SourceSpecificationNode sourceSpecificationNode = referenceNode.getSourceSpecificationNode();
    ReferenceType referenceType = referenceNode.getReferenceTypeDirectiveNode().getReferenceType();

    if (sourceSpecificationNode.hasLiteral()) {
      String literalValue = sourceSpecificationNode.getLiteral();
      return Optional.of(new TextReferenceRendering(literalValue, referenceType));
    } else {
      SpreadsheetLocation location = ReferenceUtil.resolveLocation(dataSource, referenceNode);
      String resolvedReferenceValue = ReferenceUtil.resolveReferenceValue(dataSource, referenceNode);

      if (resolvedReferenceValue.isEmpty() && referenceNode.getActualEmptyLocationDirective() == SKIP_IF_EMPTY_LOCATION)
        return Optional.empty();

      if (resolvedReferenceValue.isEmpty()
        && referenceNode.getActualEmptyLocationDirective() == WARNING_IF_EMPTY_LOCATION) {
        // TODO Warn in log files
        return Optional.empty();
      }

      if (referenceType.isLiteral()) {
        String literalReferenceValue = processOWLLiteralReferenceValue(location, resolvedReferenceValue, referenceNode);

        if (literalReferenceValue.isEmpty() && referenceNode.getActualEmptyLiteralDirective() == SKIP_IF_EMPTY_LITERAL)
          return Optional.empty();

        if (literalReferenceValue.isEmpty()
          && referenceNode.getActualEmptyLiteralDirective() == WARNING_IF_EMPTY_LITERAL) {
          // TODO Warn in log file
          return Optional.empty();
        }

        return Optional.of(new TextReferenceRendering(literalReferenceValue, referenceType));
      } else
        throw new InternalRendererException(
          "unknown reference type " + referenceType + " for reference " + referenceNode);
    }
  }

  private String processOWLLiteralReferenceValue(SpreadsheetLocation location, String rawLocationValue,
    ReferenceNode referenceNode) throws RendererException
  {
    String sourceValue = rawLocationValue.replace("\"", "\\\"");
    String processedReferenceValue = "";

    if (sourceValue.isEmpty() && !referenceNode.getActualDefaultLiteral().isEmpty())
      processedReferenceValue = referenceNode.getActualDefaultLiteral();

    if (processedReferenceValue.isEmpty() && referenceNode.getActualEmptyLiteralDirective() == ERROR_IF_EMPTY_LITERAL)
      throw new RendererException("empty literal in reference " + referenceNode + " at location " + location);

    return processedReferenceValue;
  }

  // Tentative. Need a more principled way of finding and invoking functions. What about calls to Excel?

  private String generateReferenceValue(String sourceValue, ValueExtractionFunctionNode valueExtractionFunctionNode)
    throws RendererException
  {
    List<String> arguments = new ArrayList<>();
    if (valueExtractionFunctionNode.hasArguments()) {
      for (ValueExtractionFunctionArgumentNode argumentNode : valueExtractionFunctionNode.getArgumentNodes()) {
        String argumentValue = generateValueExtractionFunctionArgument(argumentNode);
        arguments.add(argumentValue);
      }
    }
    return ReferenceUtil.evaluateReferenceValue(valueExtractionFunctionNode.getFunctionName(),
      valueExtractionFunctionNode.getFunctionID(), arguments, sourceValue, valueExtractionFunctionNode.hasArguments());
  }

  private Optional<? extends StringLiteralRendering> renderStringLiteral(StringLiteralNode stringLiteralNode)
  {
    return Optional.of(new TextStringLiteralRendering(stringLiteralNode.getValue()));
  }

  /**
   * Arguments to value extraction functions cannot be dropped if the reference resolves to nothing.
   */
  private String generateValueExtractionFunctionArgument(
    ValueExtractionFunctionArgumentNode valueExtractionFunctionArgumentNode) throws RendererException
  {
    if (valueExtractionFunctionArgumentNode.isStringLiteralNode()) {
      Optional<? extends StringLiteralRendering> literalRendering = renderStringLiteral(
        valueExtractionFunctionArgumentNode.getStringLiteralNode());
      if (literalRendering.isPresent()) {
        return literalRendering.get().getRawValue();
      } else
        throw new RendererException("empty literal for value extraction function argument");
    } else if (valueExtractionFunctionArgumentNode.isReferenceNode()) {
      ReferenceNode referenceNode = valueExtractionFunctionArgumentNode.getReferenceNode();
      Optional<? extends ReferenceRendering> referenceRendering = renderReference(referenceNode);
      if (referenceRendering.isPresent()) {
        if (referenceRendering.get().isLiteral()) {
          return referenceRendering.get().getRawValue();
        } else
          throw new RendererException("expecting literal reference for value extraction function argument, got "
            + valueExtractionFunctionArgumentNode);
      } else
        throw new RendererException("empty reference " + referenceNode + " for value extraction function argument");
    } else
      throw new InternalRendererException(
        "unknown child for node " + valueExtractionFunctionArgumentNode.getNodeName());
  }
}
