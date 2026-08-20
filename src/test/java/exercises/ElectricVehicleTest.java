package exercises;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
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

    // Looked up via reflection instead of calling the constructor/methods
    // directly, so this file compiles (and every other exercise's tests keep
    // running) even before ElectricVehicle is implemented — a missing
    // constructor/method just fails the individual test below instead of
    // failing test-compile for everyone. README specifies Integer/Double
    // parameters throughout, so lookups use the boxed classes rather than
    // int.class/double.class. getGas/getTankSize/setGas are inherited
    // unchanged from Vehicle, so they're looked up on Vehicle.class; drive
    // and unitLabel are overridden, so they're looked up on
    // ElectricVehicle.class — reflection still dispatches virtually to the
    // override when invoked on an ElectricVehicle instance either way.
    private static ElectricVehicle newElectricVehicle(double tankSize, double fuelConsumption, double regenRate) {
        try {
            Constructor<ElectricVehicle> constructor =
                    ElectricVehicle.class.getDeclaredConstructor(Double.class, Double.class, Double.class);
            constructor.setAccessible(true);
            return constructor.newInstance(tankSize, fuelConsumption, regenRate);
        } catch (ReflectiveOperationException e) {
            throw new AssertionError(
                    "ElectricVehicle(Double tankSize, Double fuelConsumption, Double regenRate)"
                            + " constructor is missing",
                    e);
        }
    }

    private static double getGas(Vehicle vehicle) {
        try {
            Method method = Vehicle.class.getDeclaredMethod("getGas");
            method.setAccessible(true);
            return (double) method.invoke(vehicle);
        } catch (ReflectiveOperationException e) {
            throw new AssertionError("Vehicle.getGas() method is missing", e);
        }
    }

    private static double getTankSize(Vehicle vehicle) {
        try {
            Method method = Vehicle.class.getDeclaredMethod("getTankSize");
            method.setAccessible(true);
            return (double) method.invoke(vehicle);
        } catch (ReflectiveOperationException e) {
            throw new AssertionError("Vehicle.getTankSize() method is missing", e);
        }
    }

    private static void setGas(Vehicle vehicle, double gas) {
        try {
            Method method = Vehicle.class.getDeclaredMethod("setGas", Double.class);
            method.setAccessible(true);
            method.invoke(vehicle, gas);
        } catch (ReflectiveOperationException e) {
            throw new AssertionError("Vehicle.setGas(Double) method is missing", e);
        }
    }

    private static double drive(ElectricVehicle car, double distance) {
        try {
            Method method = ElectricVehicle.class.getDeclaredMethod("drive", Double.class);
            method.setAccessible(true);
            return (double) method.invoke(car, distance);
        } catch (ReflectiveOperationException e) {
            throw new AssertionError("ElectricVehicle.drive(Double) override is missing", e);
        }
    }

    private static String unitLabel(ElectricVehicle car) {
        try {
            Method method = ElectricVehicle.class.getDeclaredMethod("unitLabel");
            method.setAccessible(true);
            return (String) method.invoke(car);
        } catch (ReflectiveOperationException e) {
            throw new AssertionError("ElectricVehicle.unitLabel() override is missing", e);
        }
    }

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

    private static void addVehicle(Garage garage, int position, Vehicle vehicle) {
        try {
            Method method = Garage.class.getDeclaredMethod("addVehicle", Integer.class, Vehicle.class);
            method.setAccessible(true);
            method.invoke(garage, position, vehicle);
        } catch (ReflectiveOperationException e) {
            throw new AssertionError("Garage.addVehicle(Integer, Vehicle) method is missing", e);
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
    void constructorFullyChargesTheBattery() {
        ElectricVehicle car = newElectricVehicle(50.0, 10.0, 0.2);
        assertEquals(50.0, getGas(car), 0.001);
    }

    @Test
    void getTankSizeReturnsTheConfiguredBatteryCapacity() {
        ElectricVehicle car = newElectricVehicle(50.0, 10.0, 0.2);
        assertEquals(50.0, getTankSize(car), 0.001);
    }

    @Test
    void isAVehicle() {
        Vehicle car = newElectricVehicle(50.0, 10.0, 0.2);
        assertEquals(50.0, getGas(car), 0.001);
    }

    @Test
    void driveConsumesLessEnergyThanAPlainVehicleWouldDueToRegenBraking() {
        ElectricVehicle car = newElectricVehicle(50.0, 10.0, 0.2);
        // A plain Vehicle with the same tank/consumption would be left with
        // 40.0 after driving 100km; regen braking should leave more charge.
        assertEquals(42.0, drive(car, 100.0), 0.001);
    }

    @Test
    void driveReturnsRemainingChargeAfterMultipleDrives() {
        ElectricVehicle car = newElectricVehicle(50.0, 10.0, 0.2);
        drive(car, 100.0);
        assertEquals(34.0, drive(car, 100.0), 0.001);
    }

    @Test
    void driveCannotMakeTheBatteryGoNegative() {
        ElectricVehicle car = newElectricVehicle(10.0, 100.0, 0.5);
        assertEquals(0.0, drive(car, 100.0), 0.001);
    }

    @Test
    void setGasChargesTheBatteryLikeAnyOtherVehicle() {
        ElectricVehicle car = newElectricVehicle(50.0, 10.0, 0.2);
        drive(car, 100.0);
        setGas(car, 999.0);
        assertEquals(50.0, getGas(car), 0.001);
    }

    @Test
    void unitLabelDescribesRemainingBatteryChargeInsteadOfGas() {
        ElectricVehicle car = newElectricVehicle(50.0, 10.0, 0.2);
        assertEquals("kWh battery left", unitLabel(car));
    }

    @Test
    void garageStoresAnElectricVehicleThroughTheVehicleOverload() {
        Garage garage = new Garage();
        ElectricVehicle car = newElectricVehicle(50.0, 10.0, 0.2);
        addVehicle(garage, 0, car);
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
        addVehicle(garage, 1, newElectricVehicle(50.0, 10.0, 0.2));
        assertEquals(42.0, drive(garage, 1, 100.0), 0.001);
    }

    @Test
    void garageMixesPlainAndElectricVehicles() {
        Garage garage = new Garage();
        addVehicle(garage, 0, 60.0, 8.0);
        addVehicle(garage, 1, newElectricVehicle(50.0, 10.0, 0.2));
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
        addVehicle(garage, 2, newElectricVehicle(50.0, 10.0, 0.2));
        drive(garage, 2, 100.0);
        assertEquals(8.0, refuel(garage), 0.001);
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
        addVehicle(garage, 3, 60.0, 8.0);
        addVehicle(garage, 3, newElectricVehicle(20.0, 10.0, 0.2));
        assertEquals(
                "Vehicle 1: empty\n"
                        + "Vehicle 2: empty\n"
                        + "Vehicle 3: empty\n"
                        + "Vehicle 4: 20.00kWh battery left",
                garage.toString());
    }
}
