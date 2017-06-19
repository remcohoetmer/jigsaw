package org.demo;


import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

public class CustomClassLoader extends ClassLoader {

  private final String planetName;
  private final List<String> classesToLoad;
  private final String location;

  public CustomClassLoader(ClassLoader parent, String planetName) {
    super(parent);
    this.planetName = planetName;
    classesToLoad = List.of("org.common.Common", "org." + planetName.toLowerCase() + "." + planetName);
    location = "file:org." + planetName.toLowerCase() + "/target/classes/";
  }

  public Class loadClass(String name) throws ClassNotFoundException {
    System.out.println("Load class" + name);
    if (!classesToLoad.contains(name))
      return super.loadClass(name);

    String fileName = name.replace('.', '/') + ".class";
    String url = location + fileName;
    try {
      URL myUrl = new URL(url);
      URLConnection connection = myUrl.openConnection();
      try (InputStream input = connection.getInputStream()) {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int data = input.read();
        while (data != -1) {
          buffer.write(data);
          data = input.read();
        }
        input.close();

        byte[] classData = buffer.toByteArray();
        return defineClass(name, classData, 0, classData.length);
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static byte[] loadFile(java.io.File f) throws IOException {

    try (InputStream input = new FileInputStream(f)) {
      ByteArrayOutputStream buffer = new ByteArrayOutputStream();
      int data = input.read();
      while (data != -1) {
        buffer.write(data);
        data = input.read();
      }
      input.close();

      return buffer.toByteArray();
    }
  }
}