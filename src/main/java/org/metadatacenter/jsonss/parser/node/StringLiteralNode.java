
package org.metadatacenter.jsonss.parser.node;

import org.metadatacenter.jsonss.parser.ASTStringLiteral;
import org.metadatacenter.jsonss.parser.JSONSSParserConstants;
import org.metadatacenter.jsonss.parser.ParseException;

public class StringLiteralNode implements JSONSSParserConstants, JSONSSNode
{
	private final String s;

	public StringLiteralNode(ASTStringLiteral node) throws ParseException
	{
		this.s = node.s;
	}

	public String getNodeName()
	{
		return "StringLiteral";
	}

	public String getValue()
	{
		return this.s;
	}

	public String toString()
	{
		return "\"" + this.s + "\"";
	}
}
