
package org.mm.parser.node;

import org.mm.parser.ASTDefaultLiteral;
import org.mm.parser.JSONSSParserConstants;
import org.mm.parser.ParseException;
import org.mm.parser.ParserUtil;

public class DefaultLiteralDirectiveNode implements MMNode, JSONSSParserConstants
{
	private final String defaultLiteral;

	public DefaultLiteralDirectiveNode(ASTDefaultLiteral node) throws ParseException
	{
		this.defaultLiteral = node.defaultLiteral;
	}

	public String getDefaultLiteral()
	{
		return this.defaultLiteral;
	}

	@Override public String getNodeName()
	{
		return "DefaultLiteral";
	}

	public String toString()
	{
		return ParserUtil.getTokenName(MM_DEFAULT_LITERAL) + "=\"" + this.defaultLiteral + "\"";
	}

	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null || obj.getClass() != this.getClass())
			return false;
		DefaultLiteralDirectiveNode dv = (DefaultLiteralDirectiveNode)obj;
		return this.defaultLiteral != null && dv.defaultLiteral != null && this.defaultLiteral
			.equals(dv.defaultLiteral);
	}

	public int hashCode()
	{
		int hash = 15;

		hash = hash + (null == this.defaultLiteral ? 0 : this.defaultLiteral.hashCode());

		return hash;
	}
}
