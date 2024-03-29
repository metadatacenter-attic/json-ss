package org.metadatacenter.jsonss.parser.node;

/**
 * All instances of AST nodes generated by JJTree are mapped to a corresponding strongly typed node (which have the same name minus the AST
 * prefix). This interface should be implemented by all of those nodes.
 */
public interface JSONSSNode
{
  String getNodeName();

  String toString();
} 
