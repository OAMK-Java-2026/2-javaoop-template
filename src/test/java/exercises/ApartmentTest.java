package exercises;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ApartmentTest {

    // Looked up via reflection instead of `new Apartment(...)` / `.heatingCost(...)`
    // so this file compiles (and every other exercise's tests keep running) even
    // before Apartment is implemented — a missing constructor/method just fails
    // the individual test below instead of failing test-compile for everyone.
    private static Apartment newApartment(int numOfTenants, int area) {
        try {
            // uml_apartment.png declares Apartment(Integer, Integer) — boxed, not int.
            Constructor<Apartment> constructor = Apartment.class.getDeclaredConstructor(Integer.class, Integer.class);
            constructor.setAccessible(true);
            return constructor.newInstance(numOfTenants, area);
        } catch (ReflectiveOperationException e) {
            throw new AssertionError("Apartment(Integer numOfTenants, Integer area) constructor is missing", e);
        }
    }

    private static float heatingCost(Apartment apartment, float pricePerSquareMeter) {
        try {
            // uml_apartment.png declares heatingCost(Float): Float — boxed, not float.
            Method method = Apartment.class.getDeclaredMethod("heatingCost", Float.class);
            method.setAccessible(true);
            return (float) method.invoke(apartment, pricePerSquareMeter);
        } catch (ReflectiveOperationException e) {
            throw new AssertionError("Apartment.heatingCost(Float) method is missing", e);
        }
    }

    @Test
    void calculatesHeatingCostForTwoTenants() {
        Apartment apartment = newApartment(2, 50);
        assertEquals(15.0f, heatingCost(apartment, 0.15f), 0.001f);
    }

    @Test
    void calculatesHeatingCostForOneTenant() {
        Apartment apartment = newApartment(1, 30);
        assertEquals(6.0f, heatingCost(apartment, 0.2f), 0.001f);
    }

    @Test
    void calculatesHeatingCostForManyTenants() {
        Apartment apartment = newApartment(4, 80);
        assertEquals(48.0f, heatingCost(apartment, 0.15f), 0.001f);
    }

    @Test
    void returnsZeroWhenPriceIsZero() {
        Apartment apartment = newApartment(3, 60);
        assertEquals(0.0f, heatingCost(apartment, 0.0f), 0.001f);
    }

    @Test
    void returnsZeroWhenAreaIsZero() {
        Apartment apartment = newApartment(3, 0);
        assertEquals(0.0f, heatingCost(apartment, 0.2f), 0.001f);
    }

    @Test
    void returnsZeroWhenNumOfTenantsIsZero() {
        Apartment apartment = newApartment(0, 60);
        assertEquals(0.0f, heatingCost(apartment, 0.2f), 0.001f);
    }

    @Test
    void calculatesHeatingCostWithFractionalPrice() {
        Apartment apartment = newApartment(2, 45);
        assertEquals(11.7f, heatingCost(apartment, 0.13f), 0.001f);
    }

    @Test
    void calculatesHeatingCostForLargeApartment() {
        Apartment apartment = newApartment(6, 150);
        assertEquals(135.0f, heatingCost(apartment, 0.15f), 0.001f);
    }
}
