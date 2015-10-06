package org.metadatacenter.jsonss.renderer;

import org.metadatacenter.jsonss.core.settings.EmptyCellLocationDirectiveSetting;
import org.metadatacenter.jsonss.core.settings.EmptyLiteralValueDirectiveSetting;
import org.metadatacenter.jsonss.core.settings.ReferenceDirectivesSettings;
import org.metadatacenter.jsonss.core.settings.ReferenceTypeDirectiveSetting;
import org.metadatacenter.jsonss.parser.node.ReferenceCellLocationSpecificationNode;
import org.metadatacenter.jsonss.parser.node.ReferenceNode;
import org.metadatacenter.jsonss.parser.node.StringLiteralNode;
import org.metadatacenter.jsonss.parser.node.ValueExtractionFunctionArgumentNode;
import org.metadatacenter.jsonss.parser.node.ValueExtractionFunctionNode;
import org.metadatacenter.jsonss.rendering.ReferenceRendering;
import org.metadatacenter.jsonss.rendering.StringLiteralRendering;
import org.metadatacenter.jsonss.rendering.text.TextReferenceRendering;
import org.metadatacenter.jsonss.rendering.text.TextStringLiteralRendering;
import org.metadatacenter.jsonss.ss.CellLocation;
import org.metadatacenter.jsonss.ss.SpreadSheetDataSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TextReferenceRenderer implements ReferenceRenderer
{
  private final ReferenceDirectivesSettings defaultReferenceDirectiveSettings;
  private final SpreadSheetDataSource dataSource;

  public TextReferenceRenderer(SpreadSheetDataSource dataSource,
      ReferenceDirectivesSettings defaultReferenceDirectiveSettings)
  {
    this.dataSource = dataSource;
    this.defaultReferenceDirectiveSettings = defaultReferenceDirectiveSettings;
  }

  @Override public Optional<TextReferenceRendering> renderReference(ReferenceNode referenceNode,
      ReferenceRendererContext referenceRendererContext) throws RendererException
  {
    ReferenceCellLocationSpecificationNode referenceCellLocationSpecificationNode = referenceNode
        .getReferenceCellLocationSpecificationNode();
    ReferenceTypeDirectiveSetting referenceType = referenceNode.getReferenceType();

    if (referenceCellLocationSpecificationNode.hasLiteral()) {
      String literalReferenceValue = referenceCellLocationSpecificationNode.getLiteral();
      if (referenceType.isString())
        literalReferenceValue = "\"" + literalReferenceValue + "\"";

      return Optional.of(new TextReferenceRendering(literalReferenceValue, referenceType));
    } else {
      CellLocation cellLocation = this.dataSource
          .resolveCellLocation(referenceNode.getReferenceCellLocationSpecificationNode(),
              referenceRendererContext.getCurrentCellLocation());
      String resolvedReferenceValue = ReferenceUtil
          .resolveReferenceValue(dataSource, referenceNode, referenceRendererContext.getCurrentCellLocation());

      resolvedReferenceValue = resolvedReferenceValue.replace("\"", "\\\"");

      if (resolvedReferenceValue.isEmpty() && referenceNode.getActualEmptyCellLocationSetting()
          == EmptyCellLocationDirectiveSetting.SKIP_IF_EMPTY_LOCATION)
        return Optional.empty();

      if (resolvedReferenceValue.isEmpty() && referenceNode.getActualEmptyCellLocationSetting()
          == EmptyCellLocationDirectiveSetting.WARNING_IF_EMPTY_LOCATION) {
        // TODO Warn in log file
        return Optional.empty();
      }

      if (referenceType.isLiteral()) {
        String literalReferenceValue = processLiteralReferenceValue(cellLocation, resolvedReferenceValue, referenceNode,
            referenceRendererContext);

        if (literalReferenceValue.isEmpty() && referenceNode.getActualEmptyLiteralValueDirectiveSetting()
            == EmptyLiteralValueDirectiveSetting.SKIP_IF_EMPTY_LITERAL)
          return Optional.empty();

        if (literalReferenceValue.isEmpty() && referenceNode.getActualEmptyLiteralValueDirectiveSetting()
            == EmptyLiteralValueDirectiveSetting.WARNING_IF_EMPTY_LITERAL) {
          // TODO Warn in log file
          return Optional.empty();
        }

        if (referenceType.isString())
          literalReferenceValue = "\"" + literalReferenceValue + "\"";

        return Optional.of(new TextReferenceRendering(literalReferenceValue, referenceType));
      } else
        throw new InternalRendererException(
            "unknown reference type " + referenceType + " for reference " + referenceNode);
    }
  }

  private String processLiteralReferenceValue(CellLocation cellLocation, String rawLocationValue,
      ReferenceNode referenceNode, ReferenceRendererContext referenceRendererContext) throws RendererException
  {
    String sourceValue = rawLocationValue.replace("\"", "\\\"");
    String processedReferenceValue;

    if (sourceValue.isEmpty() && !referenceNode.getActualDefaultLiteralValue().isEmpty())
      processedReferenceValue = referenceNode.getActualDefaultLiteralValue();
    else
      processedReferenceValue = sourceValue;

    if (referenceNode.hasValueExtractionFunctionNode())
      processedReferenceValue = generateReferenceValue(processedReferenceValue,
          referenceNode.getValueExtractionFunctionNode(), referenceRendererContext);

    if (processedReferenceValue.isEmpty() && referenceNode.getActualEmptyLiteralValueDirectiveSetting()
        == EmptyLiteralValueDirectiveSetting.ERROR_IF_EMPTY_LITERAL)
      throw new RendererException("empty literal value in reference " + referenceNode + " at location " + cellLocation);

    return processedReferenceValue;
  }

  // Tentative. Need a more principled way of finding and invoking functions.

  private String generateReferenceValue(String sourceValue, ValueExtractionFunctionNode valueExtractionFunctionNode,
      ReferenceRendererContext referenceRendererContext) throws RendererException
  {
    List<String> arguments = new ArrayList<>();

    if (valueExtractionFunctionNode.hasArguments()) {
      for (ValueExtractionFunctionArgumentNode argumentNode : valueExtractionFunctionNode.getArgumentNodes()) {
        String argumentValue = generateValueExtractionFunctionArgument(argumentNode, referenceRendererContext);
        arguments.add(argumentValue);
      }
    }
    return ReferenceUtil.evaluateReferenceValue(valueExtractionFunctionNode.getFunctionName(),
        valueExtractionFunctionNode.getFunctionID(), arguments, sourceValue,
        valueExtractionFunctionNode.hasArguments());
  }

  private Optional<? extends StringLiteralRendering> renderStringLiteral(StringLiteralNode stringLiteralNode)
  {
    return Optional.of(new TextStringLiteralRendering(stringLiteralNode.getValue()));
  }

  /**
   * Arguments to value extraction functions cannot be dropped if the reference resolves to nothing.
   */
  private String generateValueExtractionFunctionArgument(
      ValueExtractionFunctionArgumentNode valueExtractionFunctionArgumentNode,
      ReferenceRendererContext referenceRendererContext) throws RendererException
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
      Optional<? extends ReferenceRendering> referenceRendering = renderReference(referenceNode,
          referenceRendererContext);

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
