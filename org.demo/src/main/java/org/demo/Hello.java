package org.demo;

import org.mars.Mars;
import org.planet.Planet;

public class Hello {
  public static void main(String[] args) {

    Planet mars = new Mars();
    System.out.println("Hello " + mars.getName());
  }
}
