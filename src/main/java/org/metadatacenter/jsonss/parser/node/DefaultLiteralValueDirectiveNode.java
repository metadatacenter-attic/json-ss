
package org.metadatacenter.jsonss.parser.node;

import org.metadatacenter.jsonss.parser.ASTDefaultLiteralValueDirective;
import org.metadatacenter.jsonss.parser.JSONSSParserConstants;
import org.metadatacenter.jsonss.parser.ParseException;
import org.metadatacenter.jsonss.parser.ParserUtil;

public class DefaultLiteralValueDirectiveNode implements JSONSSNode, JSONSSParserConstants
{
	private final String defaultLiteralValue;

	public DefaultLiteralValueDirectiveNode(ASTDefaultLiteralValueDirective node) throws ParseException
	{
		this.defaultLiteralValue = node.defaultLiteralValue;
	}

	public String getDefaultLiteralValue()
	{
		return this.defaultLiteralValue;
	}

	@Override public String getNodeName()
	{
		return "DefaultLiteralValue";
	}

	public String toString()
	{
		return ParserUtil.getTokenName(DEFAULT_LITERAL_VALUE) + "=\"" + this.defaultLiteralValue + "\"";
	}

	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null || obj.getClass() != this.getClass())
			return false;
		DefaultLiteralValueDirectiveNode dv = (DefaultLiteralValueDirectiveNode)obj;
		return this.defaultLiteralValue != null && dv.defaultLiteralValue != null && this.defaultLiteralValue
			.equals(dv.defaultLiteralValue);
	}

	public int hashCode()
	{
		int hash = 15;

		hash = hash + (null == this.defaultLiteralValue ? 0 : this.defaultLiteralValue.hashCode());

		return hash;
	}
}
