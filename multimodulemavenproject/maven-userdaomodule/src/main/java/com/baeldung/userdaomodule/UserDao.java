package com.baeldung.userdaomodule;

import com.baeldung.daomodule.Dao;
import com.baeldung.entitymodule.User;
import com.triodos.remco.Remco;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserDao implements Dao {

  private final Map<Integer, User> users = new HashMap<>();

  public UserDao() {
    users.put(1, new User("Juilie"));
    users.put(2, new User("David"));
  }

  @Override
  public List<User> findAll() {
    return users.values().stream().map(s -> new User(s + new Remco().get())).toList();
  }


}