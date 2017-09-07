package org.mars;

import org.common.Common;
import org.planet.Planet;

public class Mars implements Planet {
  public String describe() {
    return "Mars common.type="+ Common.type;
  }
}