package org.demo;

/*
import java.lang.module.Configuration;
import java.lang.module.ModuleFinder;
import java.lang.reflect.Layer;

public class LayerDemo {
  public static void main(String[] args) {
    ModuleFinder finder = ModuleFinder.of(dir1, dir2, dir3);

    Layer parent = Layer.boot();

    Configuration cf = parent.configuration().resolve(finder, ModuleFinder.of(), Set.of("myapp"));

    ClassLoader scl = ClassLoader.getSystemClassLoader();

    Layer layer = parent.defineModulesWithOneLoader(cf, scl);

    Class<?> c = layer.findLoader("myapp").loadClass("app.Main");
  }

}
*/
