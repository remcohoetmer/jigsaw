package org.venus;

import org.common.Common;
import org.planet.Planet;

public class Venus implements Planet {
  @Override
  public String describe() {
    return "Venus common.type=" + Common.type;
  }
}