package org.mm.exceptions;

@SuppressWarnings("serial")
public class JSONSSException extends Exception
{
	public JSONSSException(String message)
	{
		super(message);
	}

	public JSONSSException(String message, Throwable cause)
	{
		super(message, cause);
	}
}
