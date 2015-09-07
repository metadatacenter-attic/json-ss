package org.mm.core.settings;

public enum ReferenceTypeSetting {
  JSON_STRING(0);

  private int value;

  private ReferenceTypeSetting(int value) {
    this.value = value;
  }

  public int getConstant()
  {
    return value;
  }
};
