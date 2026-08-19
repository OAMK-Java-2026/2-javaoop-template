package exercises;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ApartmentTest {

    @Test
    void calculatesHeatingCostForTwoTenants() {
        Apartment apartment = new Apartment(2, 50);
        assertEquals(15.0f, apartment.heatingCost(0.15f), 0.001f);
    }

    @Test
    void calculatesHeatingCostForOneTenant() {
        Apartment apartment = new Apartment(1, 30);
        assertEquals(6.0f, apartment.heatingCost(0.2f), 0.001f);
    }

    @Test
    void calculatesHeatingCostForManyTenants() {
        Apartment apartment = new Apartment(4, 80);
        assertEquals(48.0f, apartment.heatingCost(0.15f), 0.001f);
    }

    @Test
    void returnsZeroWhenPriceIsZero() {
        Apartment apartment = new Apartment(3, 60);
        assertEquals(0.0f, apartment.heatingCost(0.0f), 0.001f);
    }

    @Test
    void returnsZeroWhenAreaIsZero() {
        Apartment apartment = new Apartment(3, 0);
        assertEquals(0.0f, apartment.heatingCost(0.2f), 0.001f);
    }

    @Test
    void returnsZeroWhenNumOfTenantsIsZero() {
        Apartment apartment = new Apartment(0, 60);
        assertEquals(0.0f, apartment.heatingCost(0.2f), 0.001f);
    }

    @Test
    void calculatesHeatingCostWithFractionalPrice() {
        Apartment apartment = new Apartment(2, 45);
        assertEquals(11.7f, apartment.heatingCost(0.13f), 0.001f);
    }

    @Test
    void calculatesHeatingCostForLargeApartment() {
        Apartment apartment = new Apartment(6, 150);
        assertEquals(135.0f, apartment.heatingCost(0.15f), 0.001f);
    }
}
