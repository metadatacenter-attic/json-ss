package org.metadatacenter.jsonss.parser.node;

import org.metadatacenter.jsonss.core.ReferenceDirectivesHandler;
import org.metadatacenter.jsonss.core.settings.EmptyCellLocationDirectiveSetting;
import org.metadatacenter.jsonss.core.settings.EmptyLiteralValueDirectiveSetting;
import org.metadatacenter.jsonss.core.settings.ReferenceDirectivesSettings;
import org.metadatacenter.jsonss.core.settings.ReferenceTypeDirectiveSetting;
import org.metadatacenter.jsonss.core.settings.ShiftDirectiveSetting;
import org.metadatacenter.jsonss.parser.ASTDefaultCellLocationValueDirective;
import org.metadatacenter.jsonss.parser.ASTDefaultLiteralValueDirective;
import org.metadatacenter.jsonss.parser.ASTEmptyCellLocationDirective;
import org.metadatacenter.jsonss.parser.ASTEmptyLiteralValueDirective;
import org.metadatacenter.jsonss.parser.ASTReference;
import org.metadatacenter.jsonss.parser.ASTReferenceQualifiedCellLocationSpecification;
import org.metadatacenter.jsonss.parser.ASTReferenceTypeDirective;
import org.metadatacenter.jsonss.parser.ASTShiftDirective;
import org.metadatacenter.jsonss.parser.ASTValueExtractionFunction;
import org.metadatacenter.jsonss.parser.InternalParseException;
import org.metadatacenter.jsonss.parser.Node;
import org.metadatacenter.jsonss.parser.ParseException;
import org.metadatacenter.jsonss.parser.ParserUtil;
import org.metadatacenter.jsonss.renderer.RendererException;
import org.metadatacenter.jsonss.ss.CellLocation;

public class ReferenceNode implements JSONSSNode
{
  private ReferenceQualifiedCellLocationSpecificationNode referenceQualifiedCellLocationSpecificationNode;
  private ReferenceTypeDirectiveNode referenceTypeDirectiveNode;
  private DefaultCellLocationValueDirectiveNode defaultCellLocationValueDirectiveNode;
  private DefaultLiteralValueDirectiveNode defaultLiteralValueDirectiveNode;
  private EmptyCellLocationDirectiveNode emptyCellLocationDirectiveNode;
  private EmptyLiteralValueDirectiveNode emptyLiteralValueDirectiveNode;
  private ShiftDirectiveNode shiftDirectiveNode;
  private ValueExtractionFunctionNode valueExtractionFunctionNode;

  private final ReferenceDirectivesHandler referenceDirectivesHandler;

  public ReferenceNode(ASTReference node) throws ParseException
  {
    for (int i = 0; i < node.jjtGetNumChildren(); i++) {
      Node child = node.jjtGetChild(i);

      if (ParserUtil.hasName(child, "ReferenceQualifiedCellLocationSpecification")) {
        this.referenceQualifiedCellLocationSpecificationNode = new ReferenceQualifiedCellLocationSpecificationNode(
          (ASTReferenceQualifiedCellLocationSpecification)child);
      } else if (ParserUtil.hasName(child, "ReferenceTypeDirective")) {
        this.referenceTypeDirectiveNode = new ReferenceTypeDirectiveNode((ASTReferenceTypeDirective)child);
      } else if (ParserUtil.hasName(child, "DefaultCellLocationValueDirective")) {
        if (this.defaultCellLocationValueDirectiveNode != null)
          throw new RendererException(
            "only one default cell location value directive can be specified for a Reference");
        this.defaultCellLocationValueDirectiveNode = new DefaultCellLocationValueDirectiveNode(
          (ASTDefaultCellLocationValueDirective)child);
      } else if (ParserUtil.hasName(child, "DefaultLiteralValueDirective")) {
        if (this.defaultLiteralValueDirectiveNode != null)
          throw new RendererException("only one default literal value directive can be specified for a Reference");
        this.defaultLiteralValueDirectiveNode = new DefaultLiteralValueDirectiveNode(
          (ASTDefaultLiteralValueDirective)child);
      } else if (ParserUtil.hasName(child, "EmptyCellLocationDirective")) {
        if (this.emptyCellLocationDirectiveNode != null)
          throw new RendererException("only one empty cell location directive can be specified for a Reference");
        this.emptyCellLocationDirectiveNode = new EmptyCellLocationDirectiveNode((ASTEmptyCellLocationDirective)child);
      } else if (ParserUtil.hasName(child, "EmptyLiteralValueDirective")) {
        if (this.emptyLiteralValueDirectiveNode != null)
          throw new RendererException("only one empty literal value directive can be specified for a Reference");
        this.emptyLiteralValueDirectiveNode = new EmptyLiteralValueDirectiveNode((ASTEmptyLiteralValueDirective)child);
      } else if (ParserUtil.hasName(child, "ShiftDirective")) {
        if (this.shiftDirectiveNode != null)
          throw new RendererException("only one shift directive can be specified for a Reference");
        this.shiftDirectiveNode = new ShiftDirectiveNode((ASTShiftDirective)child);
      } else if (ParserUtil.hasName(child, "ValueExtractionFunction")) {
        if (this.valueExtractionFunctionNode != null)
          throw new RendererException("only one value extraction function can be specified for a Reference");
        this.valueExtractionFunctionNode = new ValueExtractionFunctionNode((ASTValueExtractionFunction)child);
      } else
        throw new InternalParseException("invalid child node " + child + " for " + getNodeName());
    }

    this.referenceDirectivesHandler = new ReferenceDirectivesHandler(node.defaultReferenceDirectivesSettings);

    if (this.referenceQualifiedCellLocationSpecificationNode == null)
      throw new RendererException("missing cell location specification in reference " + toString());

    if (this.referenceTypeDirectiveNode != null)
      this.referenceDirectivesHandler
        .setReferenceTypeDirective(this.referenceTypeDirectiveNode.getReferenceTypeDirectiveSetting());

    if (this.shiftDirectiveNode != null)
      this.referenceDirectivesHandler.setShiftDirective(this.shiftDirectiveNode.getShiftDirectiveSetting());

    if (this.emptyCellLocationDirectiveNode != null)
      this.referenceDirectivesHandler
        .setEmptyCellLocationDirective(this.emptyCellLocationDirectiveNode.getEmptyCellLocationDirectiveSetting());

    if (this.emptyLiteralValueDirectiveNode != null)
      this.referenceDirectivesHandler
        .setEmptyLiteralDirective(this.emptyLiteralValueDirectiveNode.getEmptyLiteralValueDirectiveSetting());

    if (this.defaultCellLocationValueDirectiveNode != null)
      this.referenceDirectivesHandler
        .setDefaultCellLocationValue(this.defaultCellLocationValueDirectiveNode.getDefaultCellLocationValue());

    if (this.defaultLiteralValueDirectiveNode != null)
      this.referenceDirectivesHandler
        .setDefaultLiteralValue(this.defaultLiteralValueDirectiveNode.getDefaultLiteralValue());

  }

  @Override public String getNodeName()
  {
    return "Reference";
  }

  public ReferenceTypeDirectiveSetting getActualReferenceTypeDirectiveSetting()
  {
    return this.referenceDirectivesHandler.getActualReferenceTypeDirectiveSetting();
  }

  public EmptyCellLocationDirectiveSetting getActualEmptyCellLocationDirectiveSetting()
  {
    return this.referenceDirectivesHandler.getActualEmptyCellLocationDirectiveSetting();
  }

  public EmptyLiteralValueDirectiveSetting getActualEmptyLiteralValueDirectiveSetting()
  {
    return this.referenceDirectivesHandler.getActualEmptyLiteralValueDirectiveSetting();
  }

  public ShiftDirectiveSetting getActualShiftDirectiveSetting()
  {
    return this.referenceDirectivesHandler.getActualShiftDirectiveSetting();
  }

  public String getActualDefaultCellLocationValue()
  {
    return this.referenceDirectivesHandler.getActualDefaultCellLocationValue();
  }

  public String getActualDefaultLiteralValue()
  {
    return this.referenceDirectivesHandler.getActualDefaultLiteralValue();
  }

  public boolean hasValueExtractionFunctionNode()
  {
    return this.valueExtractionFunctionNode != null;
  }

  public ValueExtractionFunctionNode getValueExtractionFunctionNode()
  {
    return this.valueExtractionFunctionNode;
  }

  public boolean hasShiftedCellLocation()
  {
    return this.referenceDirectivesHandler.getShiftedCellLocation() != null;
  }

  public void setShiftedCellLocation(CellLocation cellLocation)
  {
    this.referenceDirectivesHandler.setShiftedCellLocation(cellLocation);
  }

  public CellLocation getShiftedLocation()
  {
    return this.referenceDirectivesHandler.getShiftedCellLocation();
  }

  public ReferenceQualifiedCellLocationSpecificationNode getReferenceQualifiedCellLocationSpecificationNode()
  {
    return this.referenceQualifiedCellLocationSpecificationNode;
  }

  /**
   * Take a reference directives settings and override any that have been explicitly specified by this reference.
   */
  public ReferenceDirectivesSettings getCurrentReferenceDirectivesSettings(
    ReferenceDirectivesSettings enclosingReferenceDirectivesSettings)
  {
    ReferenceTypeDirectiveSetting referenceTypeDirectiveSetting = hasExplicitlySpecifiedReferenceType() ?
      getActualReferenceTypeDirectiveSetting() :
      enclosingReferenceDirectivesSettings.getReferenceTypeDirectiveSetting();
    EmptyCellLocationDirectiveSetting emptyCellLocationDirectiveSetting =
      hasExplicitlySpecifiedEmptyCellLocationDirective() ?
        getActualEmptyCellLocationDirectiveSetting() :
        enclosingReferenceDirectivesSettings.getEmptyCellLocationDirectiveSetting();
    EmptyLiteralValueDirectiveSetting emptyLiteralValueDirectiveSetting = hasExplicitlySpecifiedEmptyLiteralDirective() ?
      getActualEmptyLiteralValueDirectiveSetting() :
      enclosingReferenceDirectivesSettings.getEmptyLiteralValueDirectiveSetting();
    ShiftDirectiveSetting shiftDirectiveSetting = hasExplicitlySpecifiedShiftDirective() ?
      getActualShiftDirectiveSetting() :
      enclosingReferenceDirectivesSettings.getShiftDirectiveSetting();
    String defaultCellLocationValue = hasExplicitlySpecifiedDefaultCellLocationValue() ?
      getActualDefaultCellLocationValue() :
      enclosingReferenceDirectivesSettings.getDefaultCellLocationValue();
    String defaultLiteralValue = hasExplicitlySpecifiedDefaultLiteralValue() ?
      getActualDefaultLiteralValue() :
      enclosingReferenceDirectivesSettings.getDefaultLiteralValue();

    return new ReferenceDirectivesSettings(referenceTypeDirectiveSetting, emptyCellLocationDirectiveSetting,
      emptyLiteralValueDirectiveSetting, shiftDirectiveSetting, defaultLiteralValue, defaultCellLocationValue);
  }

  @Override public String toString()
  {
    String representation = "";
    boolean atLeastOneOptionProcessed = false;

    representation += this.referenceQualifiedCellLocationSpecificationNode;

    if (hasExplicitOptions())
      representation += "(";

    if (hasExplicitlySpecifiedReferenceType()) {
      representation += this.referenceTypeDirectiveNode;
      atLeastOneOptionProcessed = true;
    }

    if (hasValueExtractionFunctionNode()) {
      if (atLeastOneOptionProcessed)
        representation += " ";
      else
        atLeastOneOptionProcessed = true;
      representation += this.valueExtractionFunctionNode;
    }

    if (hasExplicitlySpecifiedDefaultCellLocationValue()) {
      if (atLeastOneOptionProcessed)
        representation += " ";
      representation += this.defaultCellLocationValueDirectiveNode;
      atLeastOneOptionProcessed = true;
    }

    if (hasExplicitlySpecifiedDefaultLiteralValue()) {
      if (atLeastOneOptionProcessed)
        representation += " ";
      representation += this.defaultLiteralValueDirectiveNode;
      atLeastOneOptionProcessed = true;
    }

    if (hasExplicitlySpecifiedEmptyCellLocationDirective()) {
      if (atLeastOneOptionProcessed)
        representation += " ";
      representation += this.emptyCellLocationDirectiveNode;
      atLeastOneOptionProcessed = true;
    }

    if (hasExplicitlySpecifiedEmptyLiteralDirective()) {
      if (atLeastOneOptionProcessed)
        representation += " ";
      representation += this.emptyLiteralValueDirectiveNode;
      atLeastOneOptionProcessed = true;
    }

    if (hasExplicitlySpecifiedShiftDirective()) {
      if (atLeastOneOptionProcessed)
        representation += " ";
      representation += this.shiftDirectiveNode;
      atLeastOneOptionProcessed = true;
    }

    if (hasExplicitOptions())
      representation += ")";

    return representation;
  }

  private boolean hasExplicitOptions()
  {
    return this.referenceDirectivesHandler.hasExplicitlySpecifiedOptions();
  }

  private boolean hasExplicitlySpecifiedReferenceType()
  {
    return this.referenceDirectivesHandler.hasExplicitlySpecifiedReferenceTypeDirective();
  }

  private boolean hasExplicitlySpecifiedDefaultCellLocationValue()
  {
    return this.referenceDirectivesHandler.hasExplicitlySpecifiedDefaultCellLocationValue();
  }

  private boolean hasExplicitlySpecifiedDefaultLiteralValue()
  {
    return this.referenceDirectivesHandler.hasExplicitlySpecifiedDefaultLiteralValue();
  }

  private boolean hasExplicitlySpecifiedEmptyLiteralDirective()
  {
    return this.referenceDirectivesHandler.hasExplicitlySpecifiedEmptyLiteralDirective();
  }

  private boolean hasExplicitlySpecifiedEmptyCellLocationDirective()
  {
    return this.referenceDirectivesHandler.hasExplicitlySpecifiedEmptyCellLocationDirective();
  }

  private boolean hasExplicitlySpecifiedShiftDirective()
  {
    return this.referenceDirectivesHandler.hasExplicitlySpecifiedShiftDirective();
  }
}
