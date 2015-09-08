
package org.metadatacenter.jsonss.parser.node;

import org.metadatacenter.jsonss.parser.ASTBooleanLiteral;
import org.metadatacenter.jsonss.parser.ParseException;

public class BooleanLiteralNode implements MMNode
{
	private final boolean value;

	public BooleanLiteralNode(ASTBooleanLiteral node) throws ParseException
	{
		this.value = node.value;
	}

	public boolean getValue()
	{
		return this.value;
	}

	public String getNodeName()
	{
		return "BooleanLiteral";
	}

	public String toString()
	{
		return "" + this.value;
	}

} 
