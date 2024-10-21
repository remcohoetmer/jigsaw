package com.triodos.remco.test;

import com.triodos.remco.Remco;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RemcoTest {
  @Test
  void remco() {
    Remco remco = new Remco();
    assertEquals("Remco", remco.get());
  }
}
