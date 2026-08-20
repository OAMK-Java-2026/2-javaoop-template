package exercises;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertEquals;

class VehicleTest {

    // Looked up via reflection instead of `new Vehicle(...)` / `.drive(...)` etc.
    // so this file compiles (and every other exercise's tests keep running) even
    // before Vehicle is implemented — a missing constructor/method just fails
    // the individual test below instead of failing test-compile for everyone.
    // README specifies Double parameters/fields throughout, so lookups use
    // Double.class rather than double.class.
    private static Vehicle newVehicle(double tankSize, double fuelConsumption) {
        try {
            Constructor<Vehicle> constructor = Vehicle.class.getDeclaredConstructor(Double.class, Double.class);
            constructor.setAccessible(true);
            return constructor.newInstance(tankSize, fuelConsumption);
        } catch (ReflectiveOperationException e) {
            throw new AssertionError("Vehicle(Double tankSize, Double fuelConsumption) constructor is missing", e);
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

    private static double drive(Vehicle vehicle, double distance) {
        try {
            Method method = Vehicle.class.getDeclaredMethod("drive", Double.class);
            method.setAccessible(true);
            return (double) method.invoke(vehicle, distance);
        } catch (ReflectiveOperationException e) {
            throw new AssertionError("Vehicle.drive(Double) method is missing", e);
        }
    }

    private static String unitLabel(Vehicle vehicle) {
        try {
            Method method = Vehicle.class.getDeclaredMethod("unitLabel");
            method.setAccessible(true);
            return (String) method.invoke(vehicle);
        } catch (ReflectiveOperationException e) {
            throw new AssertionError("Vehicle.unitLabel() method is missing", e);
        }
    }

    @Test
    void constructorFillsUpTheTank() {
        Vehicle vehicle = newVehicle(50.0, 8.0);
        assertEquals(50.0, getGas(vehicle), 0.001);
    }

    @Test
    void getTankSizeReturnsTheConfiguredSize() {
        Vehicle vehicle = newVehicle(60.0, 5.5);
        assertEquals(60.0, getTankSize(vehicle), 0.001);
    }

    @Test
    void setGasUpdatesTheCurrentAmount() {
        Vehicle vehicle = newVehicle(50.0, 8.0);
        setGas(vehicle, 20.0);
        assertEquals(20.0, getGas(vehicle), 0.001);
    }

    @Test
    void setGasCannotExceedTankCapacity() {
        Vehicle vehicle = newVehicle(50.0, 8.0);
        setGas(vehicle, 999.0);
        assertEquals(50.0, getGas(vehicle), 0.001);
    }

    @Test
    void driveConsumesFuelBasedOnConsumptionRate() {
        Vehicle vehicle = newVehicle(50.0, 8.0);
        assertEquals(42.0, drive(vehicle, 100.0), 0.001);
    }

    @Test
    void driveReturnsRemainingGasAfterMultipleDrives() {
        Vehicle vehicle = newVehicle(50.0, 10.0);
        drive(vehicle, 100.0);
        assertEquals(30.0, drive(vehicle, 100.0), 0.001);
    }

    @Test
    void driveCannotMakeTankGoNegative() {
        Vehicle vehicle = newVehicle(50.0, 8.0);
        setGas(vehicle, 5.0);
        assertEquals(0.0, drive(vehicle, 100.0), 0.001);
    }

    @Test
    void driveWithShortDistanceUsesProportionalFuel() {
        Vehicle vehicle = newVehicle(50.0, 8.0);
        assertEquals(46.0, drive(vehicle, 50.0), 0.001);
    }

    @Test
    void driveWithZeroDistanceDoesNotConsumeFuel() {
        Vehicle vehicle = newVehicle(50.0, 8.0);
        assertEquals(50.0, drive(vehicle, 0.0), 0.001);
    }

    @Test
    void unitLabelDescribesRemainingFuel() {
        Vehicle vehicle = newVehicle(50.0, 8.0);
        assertEquals("l gas left", unitLabel(vehicle));
    }
}
