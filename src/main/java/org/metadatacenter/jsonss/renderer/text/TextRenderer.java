package org.metadatacenter.jsonss.renderer.text;

import org.metadatacenter.jsonss.core.ReferenceType;
import org.metadatacenter.jsonss.parser.JSONSSParserConstants;
import org.metadatacenter.jsonss.parser.node.JSONArrayNode;
import org.metadatacenter.jsonss.parser.node.JSONBooleanNode;
import org.metadatacenter.jsonss.parser.node.JSONExpressionNode;
import org.metadatacenter.jsonss.parser.node.JSONNullNode;
import org.metadatacenter.jsonss.parser.node.JSONNumberNode;
import org.metadatacenter.jsonss.parser.node.JSONObjectNode;
import org.metadatacenter.jsonss.parser.node.JSONStringNode;
import org.metadatacenter.jsonss.parser.node.JSONValueNode;
import org.metadatacenter.jsonss.parser.node.ReferenceNode;
import org.metadatacenter.jsonss.parser.node.SourceSpecificationNode;
import org.metadatacenter.jsonss.parser.node.StringLiteralNode;
import org.metadatacenter.jsonss.parser.node.ValueExtractionFunctionArgumentNode;
import org.metadatacenter.jsonss.parser.node.ValueExtractionFunctionNode;
import org.metadatacenter.jsonss.renderer.InternalRendererException;
import org.metadatacenter.jsonss.renderer.JSONSSRenderer;
import org.metadatacenter.jsonss.renderer.ReferenceRenderer;
import org.metadatacenter.jsonss.renderer.ReferenceRendererConfiguration;
import org.metadatacenter.jsonss.renderer.ReferenceUtil;
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
import java.util.Map;
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
    this.dataSource = dataSource;
  }

  @Override public ReferenceRendererConfiguration getReferenceRendererConfiguration()
  {
    return this;
  }

  @Override public Optional<? extends TextRendering> renderJSONExpression(JSONExpressionNode jsonExpressionNode)
      throws RendererException
  {
    if (jsonExpressionNode.isJSONArray())
      return renderJSONArray(jsonExpressionNode.getJSONArrayNode());
    else if (jsonExpressionNode.isJSONObject())
      return renderJSONObject(jsonExpressionNode.getJSONObjectNode());
    else
      throw new InternalRendererException("unknown " + jsonExpressionNode.getNodeName() + " node type");
  }

  @Override public Optional<? extends TextRendering> renderJSONObject(JSONObjectNode jsonObjectNode)
      throws RendererException
  {
    Map<String, JSONValueNode> keyValuePairs = jsonObjectNode.getKeyValuePairs();

    StringBuffer sb = new StringBuffer("{");
    boolean isFirst = true;

    for (String key : keyValuePairs.keySet()) {
      JSONValueNode jsonValueNode = keyValuePairs.get(key);
      Optional<? extends TextRendering> jsonValueRendering = renderJSONValue(jsonValueNode);
      if (jsonValueRendering.isPresent()) {
        if (!isFirst)
          sb.append(", ");
        sb.append("\"" + key + "\": " + jsonValueRendering.get().getRendering());
        isFirst = false;
      }
    }
    sb.append("}");

    return Optional.of(new TextRendering(sb.toString()));
  }

  @Override public Optional<? extends TextRendering> renderJSONArray(JSONArrayNode jsonArrayNode)
      throws RendererException
  {
    List<JSONValueNode> jsonValues = jsonArrayNode.getElements();

    StringBuffer sb = new StringBuffer("[");
    boolean isFirst = true;

    for (JSONValueNode jsonValueNode : jsonValues) {
      Optional<? extends TextRendering> jsonValueRendering = renderJSONValue(jsonValueNode);
      if (jsonValueRendering.isPresent()) {
        if (!isFirst)
          sb.append(", ");
        sb.append(jsonValueRendering.get().getRendering());
        isFirst = false;
      }
    }
    sb.append("]");

    return Optional.of(new TextRendering(sb.toString()));
  }

  @Override public Optional<? extends TextRendering> renderJSONValue(JSONValueNode jsonValueNode)
      throws RendererException
  {
    if (jsonValueNode.isJSONArray())
      return renderJSONArray(jsonValueNode.getJSONArrayNode());
    else if (jsonValueNode.isJSONObject())
      return renderJSONObject(jsonValueNode.getJSONObjectNode());
    else if (jsonValueNode.isJSONString())
      return renderJSONString(jsonValueNode.getJSONStringNode());
    else if (jsonValueNode.isJSONNumber())
      return renderJSONNumber(jsonValueNode.getJSONNumberNode());
    else if (jsonValueNode.isJSONBoolean())
      return renderJSONBoolean(jsonValueNode.getJSONBooleanNode());
    else if (jsonValueNode.isJSONNull())
      return renderJSONNull(jsonValueNode.getJSONNullNode());
    else if (jsonValueNode.isReference())
      return renderReference(jsonValueNode.getReferenceNode());
    else
      throw new InternalRendererException("unknown " + jsonValueNode.getNodeName() + " node type");
  }

  @Override public Optional<? extends TextRendering> renderJSONString(JSONStringNode jsonStringNode)
      throws RendererException
  {
    return Optional.of(new TextRendering("\"" + jsonStringNode.getString() + "\""));
  }

  @Override public Optional<? extends TextRendering> renderJSONNumber(JSONNumberNode jsonNumberNode)
      throws RendererException
  {
    return Optional.of(new TextRendering("" + jsonNumberNode.getNumber()));
  }

  @Override public Optional<? extends TextRendering> renderJSONBoolean(JSONBooleanNode jsonBooleanNode)
      throws RendererException
  {
    return Optional.of(new TextRendering("" + jsonBooleanNode.getBoolean()));
  }

  @Override public Optional<? extends TextRendering> renderJSONNull(JSONNullNode jsonNullNode) throws RendererException
  {
    return Optional.of(new TextRendering("null"));
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
        // TODO Warn in log file
        return Optional.empty();
      }

      if (referenceType.isLiteral()) {
        String literalReferenceValue = processLiteralReferenceValue(location, resolvedReferenceValue, referenceNode);

        if (literalReferenceValue.isEmpty() && referenceNode.getActualEmptyLiteralDirective() == SKIP_IF_EMPTY_LITERAL)
          return Optional.empty();

        if (literalReferenceValue.isEmpty()
            && referenceNode.getActualEmptyLiteralDirective() == WARNING_IF_EMPTY_LITERAL) {
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

  private String processLiteralReferenceValue(SpreadsheetLocation location, String rawLocationValue,
      ReferenceNode referenceNode) throws RendererException
  {
    String sourceValue = rawLocationValue.replace("\"", "\\\"");
    String processedReferenceValue = "";

    if (sourceValue.isEmpty() && !referenceNode.getActualDefaultLiteral().isEmpty())
      processedReferenceValue = referenceNode.getActualDefaultLiteral();
    else
      processedReferenceValue = sourceValue;

    if (processedReferenceValue.isEmpty() && referenceNode.getActualEmptyLiteralDirective() == ERROR_IF_EMPTY_LITERAL)
      throw new RendererException("empty literal in reference " + referenceNode + " at location " + location);

    return processedReferenceValue;
  }

  // Tentative. Need a more principled way of finding and invoking functions.

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
