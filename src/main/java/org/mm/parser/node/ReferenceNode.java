package org.mm.parser.node;

import org.mm.core.ReferenceDirectives;
import org.mm.parser.ASTDefaultLiteral;
import org.mm.parser.ASTDefaultLocationValue;
import org.mm.parser.ASTEmptyLiteralSetting;
import org.mm.parser.ASTEmptyLocationSetting;
import org.mm.parser.ASTReference;
import org.mm.parser.ASTReferenceType;
import org.mm.parser.ASTShiftSetting;
import org.mm.parser.ASTSourceSpecification;
import org.mm.parser.ASTValueExtractionFunction;
import org.mm.parser.InternalParseException;
import org.mm.parser.JSONSSParserConstants;
import org.mm.parser.Node;
import org.mm.parser.ParseException;
import org.mm.parser.ParserUtil;
import org.mm.renderer.RendererException;
import org.mm.ss.SpreadsheetLocation;

public class ReferenceNode implements MMNode, JSONSSParserConstants
{
  private SourceSpecificationNode sourceSpecificationNode;
  private ReferenceTypeNode referenceTypeNode;
  private DefaultLocationValueDirectiveNode defaultLocationValueDirectiveNode;
  private DefaultLiteralDirectiveNode defaultLiteralDirectiveNode;
  private EmptyLocationDirectiveNode emptyLocationDirectiveNode;
  private EmptyLiteralDirectiveNode emptyLiteralDirectiveNode;
  private ValueExtractionFunctionNode valueExtractionFunctionNode;
  private ShiftDirectiveNode shiftDirectiveNode;
  private final ReferenceDirectives referenceDirectives;

  public ReferenceNode(ASTReference node) throws ParseException
  {
    for (int i = 0; i < node.jjtGetNumChildren(); i++) {
      Node child = node.jjtGetChild(i);

      if (ParserUtil.hasName(child, "SourceSpecification")) {
        this.sourceSpecificationNode = new SourceSpecificationNode((ASTSourceSpecification)child);
      } else if (ParserUtil.hasName(child, "ReferenceType")) {
        this.referenceTypeNode = new ReferenceTypeNode((ASTReferenceType)child);
      } else if (ParserUtil.hasName(child, "DefaultLocationValue")) {
        if (this.defaultLocationValueDirectiveNode != null)
          throw new RendererException("only one default location value directive can be specified for a Reference");
        this.defaultLocationValueDirectiveNode = new DefaultLocationValueDirectiveNode((ASTDefaultLocationValue)child);
      } else if (ParserUtil.hasName(child, "DefaultLiteral")) {
        if (this.defaultLiteralDirectiveNode != null)
          throw new RendererException("only one default literal directive can be specified for a Reference");
        this.defaultLiteralDirectiveNode = new DefaultLiteralDirectiveNode((ASTDefaultLiteral)child);
      } else if (ParserUtil.hasName(child, "EmptyLocationSetting")) {
        if (this.emptyLocationDirectiveNode != null)
          throw new RendererException("only one empty location directive can be specified for a Reference");
        this.emptyLocationDirectiveNode = new EmptyLocationDirectiveNode((ASTEmptyLocationSetting)child);
      } else if (ParserUtil.hasName(child, "EmptyLiteralSetting")) {
        if (this.emptyLiteralDirectiveNode != null)
          throw new RendererException("only one empty literal directive can be specified for a Reference");
        this.emptyLiteralDirectiveNode = new EmptyLiteralDirectiveNode((ASTEmptyLiteralSetting)child);
      } else if (ParserUtil.hasName(child, "ShiftSetting")) {
        if (this.shiftDirectiveNode != null)
          throw new RendererException("only one shift setting directive can be specified for a Reference");
        this.shiftDirectiveNode = new ShiftDirectiveNode((ASTShiftSetting)child);
      } else if (ParserUtil.hasName(child, "ValueExtractionFunction")) {
        if (this.valueExtractionFunctionNode != null)
          throw new RendererException("only one value extraction directive can be specified for a Reference");
        this.valueExtractionFunctionNode = new ValueExtractionFunctionNode((ASTValueExtractionFunction)child);
      } else
        throw new InternalParseException("invalid child node " + child + " for ReferenceNode");
    }

    this.referenceDirectives = new ReferenceDirectives(node.defaultReferenceDirectives);

    if (this.sourceSpecificationNode == null)
      throw new RendererException("missing source specification in reference " + toString());

    if (this.referenceTypeNode == null) { // No entity type specified by the user - use default type
      this.referenceTypeNode = new ReferenceTypeNode(node.defaultReferenceDirectives.getDefaultReferenceType());
    } else
      this.referenceDirectives.setExplicitlySpecifiedReferenceType(this.referenceTypeNode.getReferenceType());

    if (this.defaultLocationValueDirectiveNode != null)
      this.referenceDirectives
        .setExplicitlySpecifiedDefaultLocationValue(this.defaultLocationValueDirectiveNode.getDefaultLocationValue());

    if (this.defaultLiteralDirectiveNode != null)
      this.referenceDirectives.setExplicitlySpecifiedDefaultLiteral(this.defaultLiteralDirectiveNode.getDefaultLiteral());

    if (this.emptyLocationDirectiveNode != null)
      this.referenceDirectives
        .setHasExplicitlySpecifiedEmptyLocationDirective(this.emptyLocationDirectiveNode.getEmptyLocationSetting());

    if (this.emptyLiteralDirectiveNode != null)
      this.referenceDirectives
        .setHasExplicitlySpecifiedEmptyLiteralDirective(this.emptyLiteralDirectiveNode.getEmptyLiteralSetting());

    if (this.shiftDirectiveNode != null)
      this.referenceDirectives.setHasExplicitlySpecifiedShiftDirective(this.shiftDirectiveNode.getShiftSetting());

    checkInvalidExplicitDirectives();
  }

  @Override public String getNodeName()
  {
    return "Reference";
  }

  public ReferenceDirectives getReferenceDirectives()
  {
    return this.referenceDirectives;
  }

  public SourceSpecificationNode getSourceSpecificationNode()
  {
    return this.sourceSpecificationNode;
  }

  public void updateReferenceType(int type)
  {
    this.referenceTypeNode = new ReferenceTypeNode(type);
  }

  public ReferenceTypeNode getReferenceTypeNode()
  {
    return this.referenceTypeNode;
  }

  public DefaultLiteralDirectiveNode getDefaultLiteralDirectiveNode()
  {
    return this.defaultLiteralDirectiveNode;
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

  public boolean hasShiftedLocation()
  {
    return this.referenceDirectives.getShiftedLocation() != null;
  }

  public void setShiftedLocation(SpreadsheetLocation location)
  {
    this.referenceDirectives.setShiftedLocation(location);
  }

  public SpreadsheetLocation getShiftedLocation()
  {
    return this.referenceDirectives.getShiftedLocation();
  }

  public void setDefaultShiftSetting(int defaultShiftSetting)
  {
    this.referenceDirectives.setDefaultShiftDirective(defaultShiftSetting);
  }

  public boolean hasExplicitOptions()
  {
    return this.referenceDirectives.hasExplicitlySpecifiedOptions();
  }

	@Override public String toString()
  {
    String representation = "";
    boolean atLeastOneOptionProcessed = false;

    representation += getSourceSpecificationNode();

    if (hasExplicitOptions())
      representation += "(";

    if (hasExplicitlySpecifiedReferenceType()) {
      representation += this.referenceTypeNode;
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
      representation += this.defaultLiteralDirectiveNode;
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

  private void checkInvalidExplicitDirectives() throws ParseException
  {
    if (this.referenceDirectives.hasExplicitlySpecifiedEmptyLiteralDirective() && !this.referenceTypeNode
      .getReferenceType()
      .isLiteral())
      throw new ParseException(
        "use of empty literal setting in reference " + toString() + " invalid because it is not an OWL literal");

    if (this.referenceDirectives.hasExplicitlySpecifiedReferenceType() && this.referenceTypeNode.getReferenceType().isLiteral())
      throw new ParseException(
        "entity type " + this.referenceTypeNode.getReferenceType().getTypeName() + " in reference " + toString()
          + " should not have defining types because it is an OWL literal");
  }
}
