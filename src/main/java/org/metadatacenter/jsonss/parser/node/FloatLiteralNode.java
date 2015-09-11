
package org.metadatacenter.jsonss.parser.node;

import org.metadatacenter.jsonss.parser.ASTFloatLiteral;
import org.metadatacenter.jsonss.parser.ParseException;

public class FloatLiteralNode implements JSONSSNode
{
	private final float value;

	public FloatLiteralNode(ASTFloatLiteral node) throws ParseException
	{
		this.value = node.value;
	}

	public float getValue()
	{
		return this.value;
	}

	public String getNodeName()
	{
		return "FloatLiteral";
	}

	public String toString()
	{
		return "" + this.value;
	}

}
