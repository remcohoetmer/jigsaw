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
    Path venusPath = new File("org.venus/target/classes").toPath();
    Path marsPath = new File("org.mars/target/classes").toPath();

    ModuleLayer parent = ModuleLayer.boot();
    ModuleFinder venusFinder = ModuleFinder.of(venusPath);
    ModuleFinder marsFinder = ModuleFinder.of(marsPath);

    Configuration venusConfig = parent.configuration().resolve(venusFinder, ModuleFinder.of(), Set.of("org.venus"));
    Configuration venus2Config = parent.configuration().resolve(venusFinder, ModuleFinder.of(), Set.of("org.venus"));
    Configuration marsConfig= parent.configuration().resolve(marsFinder, ModuleFinder.of(), Set.of("org.mars"));
 /*
    final ClassLoader venusClassLoader = new CustomClassLoader(ClassLoader.getSystemClassLoader(), "Venus");
    final ClassLoader venus2ClassLoader = new CustomClassLoader(ClassLoader.getSystemClassLoader(), "Venus");
    final ClassLoader marsClassLoader = new CustomClassLoader(ClassLoader.getSystemClassLoader(), "Mars");
*/

    ModuleLayer venusLayer = parent.defineModulesWithOneLoader(venusConfig, ClassLoader.getSystemClassLoader());
    ModuleLayer venus2Layer = parent.defineModulesWithOneLoader(venus2Config, ClassLoader.getSystemClassLoader());
    ModuleLayer marsLayer = parent.defineModulesWithOneLoader(marsConfig, ClassLoader.getSystemClassLoader());
/*
    venusLayer.defineModules(venusConfig, List.of(parent), s -> {
      if (s.equals("org.venus")) {
        return venusClassLoader;
      }
      return ClassLoader.getSystemClassLoader();
    });

    marsLayer.defineModules(marsConfig, List.of(parent), s -> {
      if (s.equals("org.mars")) {
        return marsClassLoader;
      }
      return ClassLoader.getSystemClassLoader();
    });
*/
    Planet planet;

    planet = findPlanet(venusLayer, "org.venus.Venus");


    planet = findPlanet(venusLayer, "org.venus.Venus");
    System.out.println("Naam: " + planet.getName() + planet.getClass().getClassLoader());


    planet = findPlanet(venus2Layer, "org.venus.Venus");
    System.out.println("Naam: " + planet.getName() + planet.getClass().getClassLoader());

    planet = findPlanet(marsLayer, "org.mars.Mars");
    System.out.println("Naam: " + planet.getName());

    System.out.println(venusLayer.toString());
    /*
    cf = parent.configuration().resolve(finder, ModuleFinder.of(), Set.of("org.mars"));

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
