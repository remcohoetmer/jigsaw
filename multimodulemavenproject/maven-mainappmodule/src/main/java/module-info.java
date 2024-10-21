module com.baeldung.mainappmodule {
    
    requires com.baeldung.entitymodule;
    requires com.baeldung.daomodule;
    requires com.baeldung.userdaomodule;

    uses com.baeldung.daomodule.Dao;

}
