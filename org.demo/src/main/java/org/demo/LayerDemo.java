package org.demo;


import org.planet.Planet;

import java.io.File;
import java.lang.module.Configuration;
import java.lang.module.ModuleFinder;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Set;

public class LayerDemo {
  public static void main(String[] args) throws Exception {
    Path path1 = new File("org.demo/target/classes").toPath();
    Path path2 = new File("org.mars/target/classes").toPath();
    Path path3 = new File("org.venus/target/classes").toPath();
    ModuleFinder finder = ModuleFinder.of(path1, path2, path3);

    ModuleLayer parent = ModuleLayer.boot();

    Configuration cf = parent.configuration().resolve(finder, ModuleFinder.of(), Set.of("org.mars", "org.venus"));

    ClassLoader scl = ClassLoader.getSystemClassLoader();

    ModuleLayer layer = parent.defineModulesWithOneLoader(cf, scl);
    Planet planet;

    planet = findPlanet(layer, "org.venus.Venus");
    System.out.println("Naam: " + planet.getName());
    planet = findPlanet(layer, "org.mars.Mars");
    System.out.println("Naam: " + planet.getName());

  }

  private static Planet findPlanet(ModuleLayer layer, String planetName) throws ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException {
    String packageName = extractPackagename(planetName);

    ClassLoader loader = layer.findLoader(packageName);
    Class<?> c = loader.loadClass(planetName);
    if (c.getConstructors().length == 1 && c.getConstructors()[0].getParameterCount() == 0
      && Arrays.asList(c.getGenericInterfaces()).contains(Planet.class)) {
      Constructor<Planet> cons = (Constructor<Planet>) c.getConstructors()[0];
      Planet planet = cons.newInstance();
      return planet;
    }
    throw new IllegalArgumentException(String.format("Planet {} not found", planetName));
  }

  private static String extractPackagename(String className) {
    int index = className.lastIndexOf('.');
    if (index < 0) {
      throw new IllegalArgumentException("Class not in a package: " + className);
    }
    return className.substring(0, index);
  }

}
