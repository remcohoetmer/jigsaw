package b;


import a.A;

public class B {
  public String getName() {
    return "B" + new A().getName();
  }

  public static void main(String[] args) {
    System.out.println("*" + new B().getName());
  }
}