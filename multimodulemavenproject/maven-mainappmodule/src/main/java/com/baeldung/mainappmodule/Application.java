package com.baeldung.mainappmodule;

import com.baeldung.daomodule.Dao;
import com.baeldung.entitymodule.User;
import com.triodos.remco.Remco;

import java.util.List;
import java.util.ServiceLoader;

public class Application {

  public static void main(String[] args) {

    ServiceLoader<Dao> services = ServiceLoader.load(Dao.class);
    Dao service = services.iterator().next();
    List<User> l = service.findAll();
    l.forEach(d -> System.out.println(d.name()));
    l.forEach(d -> System.out.println(new Remco().get()));

  }

}
