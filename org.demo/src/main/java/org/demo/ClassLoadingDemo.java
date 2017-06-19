package org.demo;


import org.planet.Planet;

import java.io.File;
import java.lang.module.Configuration;
import java.lang.module.ModuleFinder;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class ClassLoadingDemo {
  public static void main(String[] args) throws Exception {
    Path venusPath = new File("org.venus/target/classes").toPath();


    ModuleLayer parent = ModuleLayer.boot();
    ModuleFinder venusFinder = ModuleFinder.of(venusPath);

    Configuration venusConfig = parent.configuration().resolve(venusFinder, ModuleFinder.of(), Set.of("org.venus"));

    final ClassLoader venusClassLoader = new CustomClassLoader(ClassLoader.getSystemClassLoader(), "Venus");

    //ModuleLayer venusLayer = parent.defineModulesWithOneLoader(venusConfig, venusClassLoader);

    ModuleLayer.Controller venusController = parent.defineModules(venusConfig, List.of(parent), s -> {

      System.out.println("Check: " + s + s.equals("org.venus"));
      if (s.equals("org.venus")) {
        return venusClassLoader;
      }
      System.exit(0);
      return ClassLoader.getSystemClassLoader();
    });
    ModuleLayer venusLayer= venusController.layer();

    Planet planet = findPlanet(venusLayer, "org.venus.Venus");

    System.out.println("Naam: " + planet.getName());

    /*

    planet = findPlanet(layer, "org.mars.Mars");
    System.out.println("Naam: " + planet.getName());
    File file = new File("org.demo/target/classes" + "/module-info.class");
    System.out.println(CustomClassLoader.loadFile(file));

*/
  }

  private static Planet findPlanet(ModuleLayer layer, String planetName) throws ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException {
    String moduleName = extractModuleName(planetName);
    ClassLoader loader = layer.findLoader(moduleName);
    System.out.println(String.format("ModuleName: %s LoaderName: %s", moduleName, loader));

    Class<?> c = loader.loadClass(planetName);
    if (c.getConstructors().length == 1 && c.getConstructors()[0].getParameterCount() == 0
      && Arrays.asList(c.getGenericInterfaces()).contains(Planet.class)) {
      Constructor<Planet> cons = (Constructor<Planet>) c.getConstructors()[0];
      Planet planet = cons.newInstance();
      return planet;
    }
    throw new IllegalArgumentException(String.format("Planet {} not found", planetName));
  }

  private static String extractModuleName(String className) {
    int index = className.lastIndexOf('.');
    if (index < 0) {
      throw new IllegalArgumentException("Class not in a package: " + className);
    }
    return className.substring(0, index);
  }

}
