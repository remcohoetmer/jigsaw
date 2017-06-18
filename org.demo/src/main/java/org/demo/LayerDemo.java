package org.demo;


import org.planet.Planet;

import java.io.File;
import java.lang.module.Configuration;
import java.lang.module.ModuleFinder;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;
import java.util.Set;

public class LayerDemo {
  public static void main(String[] args) throws ClassNotFoundException, IllegalAccessException, InvocationTargetException, InstantiationException {
    Path path1= new File("org.demo/target/classes").toPath();
    Path path2= new File("org.mars/target/classes").toPath();
    Path path3= new File("org.astro/target/classes").toPath();
    ModuleFinder finder = ModuleFinder.of( path1, path2);

    ModuleLayer parent = ModuleLayer.boot();

    Configuration cf = parent.configuration().resolve(finder, ModuleFinder.of(), Set.of("org.mars"));

    ClassLoader scl = ClassLoader.getSystemClassLoader();

    ModuleLayer layer = parent.defineModulesWithOneLoader(cf, scl);

    ClassLoader loader= layer.findLoader("org.mars");
    Class<?> c = loader.loadClass("org.mars.Mars");
    if (c.getConstructors().length==1 && c.getConstructors()[0].getParameterCount()==0) {
      Constructor<?> cons= c.getConstructors()[0];
      Planet planet= (Planet) cons.newInstance();
      System.out.println( "Naam:" + planet.getName());
    }
 //   System.out.println("Hello " + .newInstance());
  }

}
