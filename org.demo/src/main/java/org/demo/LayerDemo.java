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
  Path venusPath = new File("org.venus/target/classes").toPath();
  Path marsPath = new File("org.mars/target/classes").toPath();

  private void show1Layer() throws Exception {
    ModuleLayer parent = ModuleLayer.boot();

    Configuration config = parent.configuration().resolve(ModuleFinder.of(venusPath, marsPath), ModuleFinder.of(), Set.of("org.venus", "org.mars"));
// als je ze in dezelfde classloader stopt gaat het mis vanwege org.common
   ModuleLayer layer = parent.defineModulesWithOneLoader(config, ClassLoader.getSystemClassLoader());
  //  ModuleLayer layer = parent.defineModulesWithManyLoaders(config, ClassLoader.getSystemClassLoader());

    Planet planet;

    planet = findPlanet(layer, "org.venus.Venus");
    System.out.println("Naam: " + planet.describe());

    planet = findPlanet(layer, "org.mars.Mars");
    System.out.println("Naam: " + planet.describe());
    // Common Module is zichtbaar in beide classloaders
    findClass(layer, "org.venus", "org.common.Common");
    findClass(layer, "org.mars", "org.common.Common");

  }

  public void show2Layers() throws Exception {

    ModuleLayer parent = ModuleLayer.boot();
    Configuration venusConfig = parent.configuration().resolve(ModuleFinder.of(venusPath), ModuleFinder.of(), Set.of("org.venus"));
    Configuration marsConfig = parent.configuration().resolve(ModuleFinder.of(marsPath), ModuleFinder.of(), Set.of("org.mars"));

    ModuleLayer venusLayer = parent.defineModulesWithOneLoader(venusConfig, ClassLoader.getSystemClassLoader());
    ModuleLayer marsLayer = parent.defineModulesWithOneLoader(marsConfig, ClassLoader.getSystemClassLoader());


    Planet planet;

    planet = findPlanet(venusLayer, "org.venus.Venus");
    System.out.println("Naam: " + planet.describe());

    planet = findPlanet(marsLayer, "org.mars.Mars");
    System.out.println("Naam: " + planet.describe());
    findClass(venusLayer, "org.venus", "org.common.Common");
    findClass(marsLayer, "org.mars", "org.common.Common");

  }

  public void show2LayersSameModule() throws Exception {

    ModuleLayer parent = ModuleLayer.boot();
    Configuration venusConfig1 = parent.configuration().resolve(ModuleFinder.of(venusPath), ModuleFinder.of(), Set.of("org.venus"));
    Configuration venusConfig2 = parent.configuration().resolve(ModuleFinder.of(venusPath), ModuleFinder.of(), Set.of("org.venus"));

    ModuleLayer venusLayer1 = parent.defineModulesWithOneLoader(venusConfig1, ClassLoader.getSystemClassLoader());
    ModuleLayer venusLayer2 = parent.defineModulesWithOneLoader(venusConfig2, ClassLoader.getSystemClassLoader());


    Planet planet;

    planet = findPlanet(venusLayer1, "org.venus.Venus");
    System.out.println("Naam: " + planet.describe());
    planet = findPlanet(venusLayer2, "org.venus.Venus");
    System.out.println("Naam: " + planet.describe());


  }

  public static void main(String[] args) throws Exception {
    new LayerDemo().show1Layer();
   // new LayerDemo().show2Layers();
   // new LayerDemo().show2LayersSameModule();

  }

  private static Planet findPlanet(ModuleLayer layer, String planetName) throws ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException {
    String moduleName = extractModuleName(planetName);
    ClassLoader loader = layer.findLoader(moduleName);
    //System.out.println(String.format("ModuleName: %s LoaderName: %s", moduleName, loader));

    Class<?> c = loader.loadClass(planetName);
    if (c.getConstructors().length == 1 && c.getConstructors()[0].getParameterCount() == 0
      && Arrays.asList(c.getGenericInterfaces()).contains(Planet.class)) {
      Constructor<Planet> cons = (Constructor<Planet>) c.getConstructors()[0];
      Planet planet = cons.newInstance();
      return planet;
    }
    throw new IllegalArgumentException(String.format("Planet {} not found", planetName));
  }

  private static void findClass(ModuleLayer layer, String moduleName, String className) throws ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException {
    ClassLoader loader = layer.findLoader(moduleName);

    Class<?> c = loader.loadClass(className);
    System.out.println(String.format("%s LoaderName: %s %s", c, moduleName, c.getClassLoader()));
  }

  private static String extractModuleName(String className) {
    int index = className.lastIndexOf('.');
    if (index < 0) {
      throw new IllegalArgumentException("Class not in a package: " + className);
    }
    return className.substring(0, index);
  }

}
