package edu.umn.cs.csci3081w.project.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;



public class PassengerFactoryTest {

  /**
   * Setup deterministic operations before each test run.
   */
  @BeforeEach
  public void setUp() {
    PassengerFactory.DETERMINISTIC = true;
    PassengerFactory.DETERMINISTIC_NAMES_COUNT = 0;
    PassengerFactory.DETERMINISTIC_DESTINATION_COUNT = 0;
    RandomPassengerGenerator.DETERMINISTIC = true;
  }

  /**
   * Tests generate function.
   */
  @Test
  public void testGenerate() {
    assertEquals(3, PassengerFactory.generate(1, 10).getDestination());

  }

  /**
   * Tests generate function.
   */
  @Test
  public void nameGeneration() {
    assertEquals("Goldy", PassengerFactory.nameGeneration());

  }

  @Test
  void testNonDeterministicNameGeneration() {
    PassengerFactory.DETERMINISTIC = false;

    String name1 = PassengerFactory.nameGeneration();
    String name2 = PassengerFactory.nameGeneration();

    assertNotNull(name1);
    assertNotNull(name2);
    assertNotEquals("", name1);
    assertNotEquals("", name2);

    assertTrue(Character.isUpperCase(name1.charAt(0)));
    assertTrue(Character.isUpperCase(name2.charAt(0)));
  }

}
