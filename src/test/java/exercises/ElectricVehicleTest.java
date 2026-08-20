package exercises;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ElectricVehicleTest {

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
    void constructorFullyChargesTheBattery() {
        ElectricVehicle car = new ElectricVehicle(50.0, 10.0, 0.2);
        assertEquals(50.0, car.getGas(), 0.001);
    }

    @Test
    void getTankSizeReturnsTheConfiguredBatteryCapacity() {
        ElectricVehicle car = new ElectricVehicle(50.0, 10.0, 0.2);
        assertEquals(50.0, car.getTankSize(), 0.001);
    }

    @Test
    void isAVehicle() {
        Vehicle car = new ElectricVehicle(50.0, 10.0, 0.2);
        assertEquals(50.0, car.getGas(), 0.001);
    }

    @Test
    void driveConsumesLessEnergyThanAPlainVehicleWouldDueToRegenBraking() {
        ElectricVehicle car = new ElectricVehicle(50.0, 10.0, 0.2);
        // A plain Vehicle with the same tank/consumption would be left with
        // 40.0 after driving 100km; regen braking should leave more charge.
        assertEquals(42.0, car.drive(100.0), 0.001);
    }

    @Test
    void driveReturnsRemainingChargeAfterMultipleDrives() {
        ElectricVehicle car = new ElectricVehicle(50.0, 10.0, 0.2);
        car.drive(100.0);
        assertEquals(34.0, car.drive(100.0), 0.001);
    }

    @Test
    void driveCannotMakeTheBatteryGoNegative() {
        ElectricVehicle car = new ElectricVehicle(10.0, 100.0, 0.5);
        assertEquals(0.0, car.drive(100.0), 0.001);
    }

    @Test
    void setGasChargesTheBatteryLikeAnyOtherVehicle() {
        ElectricVehicle car = new ElectricVehicle(50.0, 10.0, 0.2);
        car.drive(100.0);
        car.setGas(999.0);
        assertEquals(50.0, car.getGas(), 0.001);
    }

    @Test
    void unitLabelDescribesRemainingBatteryChargeInsteadOfGas() {
        ElectricVehicle car = new ElectricVehicle(50.0, 10.0, 0.2);
        assertEquals("kWh battery left", car.unitLabel());
    }

    @Test
    void garageStoresAnElectricVehicleThroughTheVehicleOverload() {
        Garage garage = new Garage();
        ElectricVehicle car = new ElectricVehicle(50.0, 10.0, 0.2);
        garage.addVehicle(0, car);
        assertEquals(
                "Vehicle 1: 50.00kWh battery left\n"
                        + "Vehicle 2: empty\n"
                        + "Vehicle 3: empty\n"
                        + "Vehicle 4: empty",
                garage.toString());
    }

    @Test
    void garageDrivesAnElectricVehiclePolymorphically() {
        Garage garage = new Garage();
        garage.addVehicle(1, new ElectricVehicle(50.0, 10.0, 0.2));
        assertEquals(42.0, garage.drive(1, 100.0), 0.001);
    }

    @Test
    void garageMixesPlainAndElectricVehicles() {
        Garage garage = new Garage();
        garage.addVehicle(0, 60.0, 8.0);
        garage.addVehicle(1, new ElectricVehicle(50.0, 10.0, 0.2));
        assertEquals(
                "Vehicle 1: 60.00l gas left\n"
                        + "Vehicle 2: 50.00kWh battery left\n"
                        + "Vehicle 3: empty\n"
                        + "Vehicle 4: empty",
                garage.toString());
    }

    @Test
    void garageRefuelRechargesAnElectricVehicleAndReturnsAmountNeeded() {
        Garage garage = new Garage();
        garage.addVehicle(2, new ElectricVehicle(50.0, 10.0, 0.2));
        garage.drive(2, 100.0);
        assertEquals(8.0, garage.refuel(), 0.001);
        assertEquals(
                "Vehicle 1: empty\n"
                        + "Vehicle 2: empty\n"
                        + "Vehicle 3: 50.00kWh battery left\n"
                        + "Vehicle 4: empty",
                garage.toString());
    }

    @Test
    void addVehicleWithVehicleOverloadOverwritesWhateverWasAtThatPosition() {
        Garage garage = new Garage();
        garage.addVehicle(3, 60.0, 8.0);
        garage.addVehicle(3, new ElectricVehicle(20.0, 10.0, 0.2));
        assertEquals(
                "Vehicle 1: empty\n"
                        + "Vehicle 2: empty\n"
                        + "Vehicle 3: empty\n"
                        + "Vehicle 4: 20.00kWh battery left",
                garage.toString());
    }
}
