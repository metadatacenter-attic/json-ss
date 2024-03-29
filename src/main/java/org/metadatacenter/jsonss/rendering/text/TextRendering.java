package org.metadatacenter.jsonss.rendering.text;

import org.metadatacenter.jsonss.rendering.Rendering;

public class TextRendering implements Rendering
{
	private final String rendering;

	public TextRendering(String rendering)
	{
		this.rendering = rendering;
	}

	public String getRendering()
	{
		return this.rendering;
	}

	@Override public String toString()
	{
		return getRendering();
	}
}
