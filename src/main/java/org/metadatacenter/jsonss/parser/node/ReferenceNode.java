package org.metadatacenter.jsonss.parser.node;

import org.metadatacenter.jsonss.core.ReferenceDirectives;
import org.metadatacenter.jsonss.parser.ASTDefaultLiteralValueDirective;
import org.metadatacenter.jsonss.parser.ASTDefaultLocationValueDirective;
import org.metadatacenter.jsonss.parser.ASTEmptyLiteralDirective;
import org.metadatacenter.jsonss.parser.ASTEmptyLocationDirective;
import org.metadatacenter.jsonss.parser.ASTReference;
import org.metadatacenter.jsonss.parser.ASTReferenceSourceSpecification;
import org.metadatacenter.jsonss.parser.ASTShiftDirective;
import org.metadatacenter.jsonss.parser.ASTValueExtractionFunction;
import org.metadatacenter.jsonss.parser.InternalParseException;
import org.metadatacenter.jsonss.parser.JSONSSParserConstants;
import org.metadatacenter.jsonss.parser.ParseException;
import org.metadatacenter.jsonss.parser.ParserUtil;
import org.metadatacenter.jsonss.renderer.RendererException;
import org.metadatacenter.jsonss.parser.ASTReferenceTypeDirective;
import org.metadatacenter.jsonss.parser.Node;
import org.metadatacenter.jsonss.ss.CellLocation;

public class ReferenceNode implements JSONSSNode, JSONSSParserConstants
{
  private ReferenceSourceSpecificationNode referenceSourceSpecificationNode;
  private ReferenceTypeDirectiveNode referenceTypeDirectiveNode;
  private DefaultLocationValueDirectiveNode defaultLocationValueDirectiveNode;
  private DefaultLiteralValueDirectiveNode defaultLiteralValueDirectiveNode;
  private EmptyLocationDirectiveNode emptyLocationDirectiveNode;
  private EmptyLiteralDirectiveNode emptyLiteralDirectiveNode;
  private ValueExtractionFunctionNode valueExtractionFunctionNode;
  private ShiftDirectiveNode shiftDirectiveNode;
  private final ReferenceDirectives referenceDirectives;

  public ReferenceNode(ASTReference node) throws ParseException
  {
    for (int i = 0; i < node.jjtGetNumChildren(); i++) {
      Node child = node.jjtGetChild(i);

      if (ParserUtil.hasName(child, "ReferenceSourceSpecification")) {
        this.referenceSourceSpecificationNode = new ReferenceSourceSpecificationNode((ASTReferenceSourceSpecification)child);
      } else if (ParserUtil.hasName(child, "ReferenceTypeDirective")) {
        this.referenceTypeDirectiveNode = new ReferenceTypeDirectiveNode((ASTReferenceTypeDirective)child);
      } else if (ParserUtil.hasName(child, "DefaultLocationValueDirective")) {
        if (this.defaultLocationValueDirectiveNode != null)
          throw new RendererException("only one default location value directive can be specified for a Reference");
        this.defaultLocationValueDirectiveNode = new DefaultLocationValueDirectiveNode((ASTDefaultLocationValueDirective)child);
      } else if (ParserUtil.hasName(child, "DefaultLiteralValueDirective")) {
        if (this.defaultLiteralValueDirectiveNode != null)
          throw new RendererException("only one default literal directive can be specified for a Reference");
        this.defaultLiteralValueDirectiveNode = new DefaultLiteralValueDirectiveNode((ASTDefaultLiteralValueDirective)child);
      } else if (ParserUtil.hasName(child, "EmptyLocationDirective")) {
        if (this.emptyLocationDirectiveNode != null)
          throw new RendererException("only one empty location directive can be specified for a Reference");
        this.emptyLocationDirectiveNode = new EmptyLocationDirectiveNode((ASTEmptyLocationDirective)child);
      } else if (ParserUtil.hasName(child, "EmptyLiteralDirective")) {
        if (this.emptyLiteralDirectiveNode != null)
          throw new RendererException("only one empty literal directive can be specified for a Reference");
        this.emptyLiteralDirectiveNode = new EmptyLiteralDirectiveNode((ASTEmptyLiteralDirective)child);
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

    this.referenceDirectives = new ReferenceDirectives(node.defaultReferenceDirectives);

    if (this.referenceSourceSpecificationNode == null)
      throw new RendererException("missing source specification in reference " + toString());

    if (this.referenceTypeDirectiveNode == null) { // No entity type specified by the user - use default type
      this.referenceTypeDirectiveNode = new ReferenceTypeDirectiveNode(node.defaultReferenceDirectives.getDefaultReferenceType());
    } else
      this.referenceDirectives.setExplicitlySpecifiedReferenceType(this.referenceTypeDirectiveNode.getReferenceType());

    if (this.defaultLocationValueDirectiveNode != null)
      this.referenceDirectives
        .setExplicitlySpecifiedDefaultLocationValue(this.defaultLocationValueDirectiveNode.getDefaultLocationValue());

    if (this.defaultLiteralValueDirectiveNode != null)
      this.referenceDirectives.setExplicitlySpecifiedDefaultLiteral(
        this.defaultLiteralValueDirectiveNode.getDefaultLiteralValue());

    if (this.emptyLocationDirectiveNode != null)
      this.referenceDirectives
        .setHasExplicitlySpecifiedEmptyLocationDirective(this.emptyLocationDirectiveNode.getEmptyLocationSetting());

    if (this.emptyLiteralDirectiveNode != null)
      this.referenceDirectives
        .setHasExplicitlySpecifiedEmptyLiteralDirective(this.emptyLiteralDirectiveNode.getEmptyLiteralSetting());

    if (this.shiftDirectiveNode != null)
      this.referenceDirectives.setHasExplicitlySpecifiedShiftDirective(this.shiftDirectiveNode.getShiftSetting());
  }

  @Override public String getNodeName()
  {
    return "Reference";
  }

  public ReferenceSourceSpecificationNode getReferenceSourceSpecificationNode()
  {
    return this.referenceSourceSpecificationNode;
  }

  public ReferenceTypeDirectiveNode getReferenceTypeDirectiveNode()
  {
    return this.referenceTypeDirectiveNode;
  }

  public DefaultLiteralValueDirectiveNode getDefaultLiteralValueDirectiveNode()
  {
    return this.defaultLiteralValueDirectiveNode;
  }


  public ShiftDirectiveNode getShiftDirectiveNode()
  {
    return this.shiftDirectiveNode;
  }

  public EmptyLiteralDirectiveNode getEmptyLiteralDirectiveNode()
  {
    return this.emptyLiteralDirectiveNode;
  }

  public EmptyLocationDirectiveNode getEmptyLocationDirectiveNode()
  {
    return this.emptyLocationDirectiveNode;
  }

  public boolean hasExplicitlySpecifiedReferenceType()
  {
    return this.referenceDirectives.hasExplicitlySpecifiedReferenceType();
  }

  public boolean hasExplicitlySpecifiedDefaultLocationValue()
  {
    return this.referenceDirectives.hasExplicitlySpecifiedDefaultLocationValue();
  }

  public boolean hasExplicitlySpecifiedDefaultLiteral()
  {
    return this.referenceDirectives.hasExplicitlySpecifiedDefaultLiteral();
  }

  public boolean hasExplicitlySpecifiedEmptyLiteralDirective()
  {
    return this.referenceDirectives.hasExplicitlySpecifiedEmptyLiteralDirective();
  }

  public boolean hasExplicitlySpecifiedEmptyLocationDirective()
  {
    return this.referenceDirectives.hasExplicitlySpecifiedEmptyLocationDirective();
  }

  public boolean hasExplicitlySpecifiedShiftDirective()
  {
    return this.referenceDirectives.hasExplicitlySpecifiedShiftDirective();
  }

  public int getActualEmptyLocationDirective()
  {
    return this.referenceDirectives.getActualEmptyLocationDirective();
  }

  public int getActualEmptyLiteralDirective()
  {
    return this.referenceDirectives.getActualEmptyLiteralDirective();
  }

  public int getActualShiftDirective()
  {
    return this.referenceDirectives.getActualShiftDirective();
  }

  public String getActualDefaultLocationValue()
  {
    return this.referenceDirectives.getActualDefaultLocationValue();
  }

  public String getActualDefaultLiteral()
  {
    return this.referenceDirectives.getActualDefaultLiteral();
  }

  public DefaultLocationValueDirectiveNode getDefaultLocationValueDirectiveNode()
  {
    return this.defaultLocationValueDirectiveNode;
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
    return this.referenceDirectives.getShiftedCellLocation() != null;
  }

  public void setShiftedCellLocation(CellLocation cellLocation)
  {
    this.referenceDirectives.setShiftedCellLocation(cellLocation);
  }

  public CellLocation getShiftedLocation()
  {
    return this.referenceDirectives.getShiftedCellLocation();
  }

  public boolean hasExplicitOptions()
  {
    return this.referenceDirectives.hasExplicitlySpecifiedOptions();
  }

	@Override public String toString()
  {
    String representation = "";
    boolean atLeastOneOptionProcessed = false;

    representation += getReferenceSourceSpecificationNode();

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

    if (hasExplicitlySpecifiedDefaultLocationValue()) {
      if (atLeastOneOptionProcessed)
        representation += " ";
      representation += this.defaultLocationValueDirectiveNode;
      atLeastOneOptionProcessed = true;
    }

    if (hasExplicitlySpecifiedDefaultLiteral()) {
      if (atLeastOneOptionProcessed)
        representation += " ";
      representation += this.defaultLiteralValueDirectiveNode;
      atLeastOneOptionProcessed = true;
    }

    if (hasExplicitlySpecifiedEmptyLocationDirective()) {
      if (atLeastOneOptionProcessed)
        representation += " ";
      representation += this.emptyLocationDirectiveNode;
      atLeastOneOptionProcessed = true;
    }

    if (hasExplicitlySpecifiedEmptyLiteralDirective()) {
      if (atLeastOneOptionProcessed)
        representation += " ";
      representation += this.emptyLiteralDirectiveNode;
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
}
