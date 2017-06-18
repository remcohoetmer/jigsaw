package org.mars;

import org.common.Common;
import org.planet.Planet;

public class Mars implements Planet {
  public String getName() {
    return "mars"+ Common.version;
  }
}