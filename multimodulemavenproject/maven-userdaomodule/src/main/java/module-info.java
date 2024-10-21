module com.baeldung.userdaomodule {
    requires com.baeldung.entitymodule;
    requires com.baeldung.daomodule;
    requires transitive com.triodos.remco;
    provides com.baeldung.daomodule.Dao with com.baeldung.userdaomodule.UserDao;
    exports com.baeldung.userdaomodule;
}
