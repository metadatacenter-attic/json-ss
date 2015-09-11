
package org.metadatacenter.jsonss.parser.node;

import org.metadatacenter.jsonss.parser.ASTNumericLiteral;
import org.metadatacenter.jsonss.parser.ParseException;

public class NumericLiteralNode implements JSONSSNode
{
	private final double value;

	public NumericLiteralNode(ASTNumericLiteral node) throws ParseException
	{
		this.value = node.value;
	}

	public double getValue()
	{
		return this.value;
	}

	public String getNodeName()
	{
		return "NumericLiteral";
	}

	public String toString()
	{
		return "" + this.value;
	}

}
