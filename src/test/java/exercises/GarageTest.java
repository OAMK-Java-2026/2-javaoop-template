package exercises;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class GarageTest {

    private static Locale originalLocale;

    @BeforeAll
    static void forceUsLocale() {
        // toString() formats decimals via the platform default locale, which
        // uses a comma on e.g. Finnish machines instead of the period these
        // assertions expect — pin it so the test behaves the same on every
        // student's computer and in CI regardless of the OS locale.
        originalLocale = Locale.getDefault();
        Locale.setDefault(Locale.US);
    }

    @AfterAll
    static void restoreLocale() {
        Locale.setDefault(originalLocale);
    }

    @Test
    void newGarageReportsAllSlotsEmpty() {
        Garage garage = new Garage();
        assertEquals(
                "Vehicle 1: empty\n"
                        + "Vehicle 2: empty\n"
                        + "Vehicle 3: empty\n"
                        + "Vehicle 4: empty",
                garage.toString());
    }

    @Test
    void addVehiclePlacesItAtTheGivenPosition() {
        Garage garage = new Garage();
        garage.addVehicle(0, 60.0, 8.0);
        assertEquals(
                "Vehicle 1: 60.00l gas left\n"
                        + "Vehicle 2: empty\n"
                        + "Vehicle 3: empty\n"
                        + "Vehicle 4: empty",
                garage.toString());
    }

    @Test
    void driveReturnsRemainingGasForExistingVehicle() {
        Garage garage = new Garage();
        garage.addVehicle(1, 50.0, 10.0);
        assertEquals(40.0, garage.drive(1, 100.0), 0.001);
    }

    @Test
    void driveReturnsNullWhenNoVehicleAtPosition() {
        Garage garage = new Garage();
        assertNull(garage.drive(2, 100.0));
    }

    @Test
    void refuelFillsAllTanksAndReturnsFuelNeeded() {
        Garage garage = new Garage();
        garage.addVehicle(0, 50.0, 10.0);
        garage.addVehicle(2, 40.0, 8.0);
        garage.drive(0, 100.0);
        garage.drive(2, 100.0);
        assertEquals(18.0, garage.refuel(), 0.001);
    }

    @Test
    void refuelOnEmptyGarageReturnsZero() {
        Garage garage = new Garage();
        assertEquals(0.0, garage.refuel(), 0.001);
    }

    @Test
    void refuelRestoresFullTanksAfterwards() {
        Garage garage = new Garage();
        garage.addVehicle(3, 45.0, 9.0);
        garage.drive(3, 200.0);
        garage.refuel();
        assertEquals(
                "Vehicle 1: empty\n"
                        + "Vehicle 2: empty\n"
                        + "Vehicle 3: empty\n"
                        + "Vehicle 4: 45.00l gas left",
                garage.toString());
    }

    @Test
    void toStringShowsAllFourVehiclesRegardlessOfOrder() {
        Garage garage = new Garage();
        garage.addVehicle(2, 93.84, 7.0);
        assertEquals(
                "Vehicle 1: empty\n"
                        + "Vehicle 2: empty\n"
                        + "Vehicle 3: 93.84l gas left\n"
                        + "Vehicle 4: empty",
                garage.toString());
    }
}
