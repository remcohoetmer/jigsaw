package org.venus;

import org.common.Common;
import org.planet.Planet;

public class Venus implements Planet {
  @Override
  public String getName() {
    return "Venus common.version=" + Common.version;
  }
}