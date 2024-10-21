package com.baeldung.daomodule;

import com.baeldung.entitymodule.User;

import java.util.List;

public interface Dao {

    List<User> findAll();

}
