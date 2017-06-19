package c;


public class C {
  public String getName() {
    return "C" + new com.foo.a.A().getName();
  }
  public static void main(String[] args)
  {
    System.out.println("*C***************" );
    System.out.println("C:" + new C().getName());
  }

}