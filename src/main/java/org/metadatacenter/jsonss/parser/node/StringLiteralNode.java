
package org.metadatacenter.jsonss.parser.node;

import org.metadatacenter.jsonss.parser.ASTStringLiteral;
import org.metadatacenter.jsonss.parser.JSONSSParserConstants;
import org.metadatacenter.jsonss.parser.ParseException;

public class StringLiteralNode implements StringNode, JSONSSParserConstants
{
	private final String value;

	public StringLiteralNode(ASTStringLiteral node) throws ParseException
	{
		this.value = node.value;
	}

	public String getNodeName()
	{
		return "StringLiteral";
	}

	public String getValue()
	{
		return this.value;
	}

	public String toString()
	{
		return "\"" + this.value + "\"";
	}
}
