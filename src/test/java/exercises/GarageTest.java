package exercises;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
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

    // `new Garage()` and `garage.toString()` always compile (default constructor,
    // inherited Object.toString), but addVehicle/drive/refuel don't exist until
    // implemented, so those are looked up via reflection instead — a missing
    // method just fails the individual test below instead of failing test-compile
    // for every exercise. README specifies Integer/Double parameters throughout,
    // so lookups use the boxed classes rather than int.class/double.class.
    private static void addVehicle(Garage garage, int position, double tankSize, double fuelConsumption) {
        try {
            Method method = Garage.class.getDeclaredMethod("addVehicle", Integer.class, Double.class, Double.class);
            method.setAccessible(true);
            method.invoke(garage, position, tankSize, fuelConsumption);
        } catch (ReflectiveOperationException e) {
            throw new AssertionError(
                    "Garage.addVehicle(Integer, Double, Double) method is missing", e);
        }
    }

    private static Double drive(Garage garage, int position, double distance) {
        try {
            Method method = Garage.class.getDeclaredMethod("drive", Integer.class, Double.class);
            method.setAccessible(true);
            return (Double) method.invoke(garage, position, distance);
        } catch (ReflectiveOperationException e) {
            throw new AssertionError("Garage.drive(Integer, Double) method is missing", e);
        }
    }

    private static double refuel(Garage garage) {
        try {
            Method method = Garage.class.getDeclaredMethod("refuel");
            method.setAccessible(true);
            return (double) method.invoke(garage);
        } catch (ReflectiveOperationException e) {
            throw new AssertionError("Garage.refuel() method is missing", e);
        }
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
        addVehicle(garage, 0, 60.0, 8.0);
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
        addVehicle(garage, 1, 50.0, 10.0);
        assertEquals(40.0, drive(garage, 1, 100.0), 0.001);
    }

    @Test
    void driveReturnsNullWhenNoVehicleAtPosition() {
        Garage garage = new Garage();
        assertNull(drive(garage, 2, 100.0));
    }

    @Test
    void refuelFillsAllTanksAndReturnsFuelNeeded() {
        Garage garage = new Garage();
        addVehicle(garage, 0, 50.0, 10.0);
        addVehicle(garage, 2, 40.0, 8.0);
        drive(garage, 0, 100.0);
        drive(garage, 2, 100.0);
        assertEquals(18.0, refuel(garage), 0.001);
    }

    @Test
    void refuelOnEmptyGarageReturnsZero() {
        Garage garage = new Garage();
        assertEquals(0.0, refuel(garage), 0.001);
    }

    @Test
    void refuelRestoresFullTanksAfterwards() {
        Garage garage = new Garage();
        addVehicle(garage, 3, 45.0, 9.0);
        drive(garage, 3, 200.0);
        refuel(garage);
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
        addVehicle(garage, 2, 93.84, 7.0);
        assertEquals(
                "Vehicle 1: empty\n"
                        + "Vehicle 2: empty\n"
                        + "Vehicle 3: 93.84l gas left\n"
                        + "Vehicle 4: empty",
                garage.toString());
    }
}
