package exercises;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class VehicleTest {

    @Test
    void constructorFillsUpTheTank() {
        Vehicle vehicle = new Vehicle(50.0, 8.0);
        assertEquals(50.0, vehicle.getGas(), 0.001);
    }

    @Test
    void getTankSizeReturnsTheConfiguredSize() {
        Vehicle vehicle = new Vehicle(60.0, 5.5);
        assertEquals(60.0, vehicle.getTankSize(), 0.001);
    }

    @Test
    void setGasUpdatesTheCurrentAmount() {
        Vehicle vehicle = new Vehicle(50.0, 8.0);
        vehicle.setGas(20.0);
        assertEquals(20.0, vehicle.getGas(), 0.001);
    }

    @Test
    void setGasCannotExceedTankCapacity() {
        Vehicle vehicle = new Vehicle(50.0, 8.0);
        vehicle.setGas(999.0);
        assertEquals(50.0, vehicle.getGas(), 0.001);
    }

    @Test
    void driveConsumesFuelBasedOnConsumptionRate() {
        Vehicle vehicle = new Vehicle(50.0, 8.0);
        assertEquals(42.0, vehicle.drive(100.0), 0.001);
    }

    @Test
    void driveReturnsRemainingGasAfterMultipleDrives() {
        Vehicle vehicle = new Vehicle(50.0, 10.0);
        vehicle.drive(100.0);
        assertEquals(30.0, vehicle.drive(100.0), 0.001);
    }

    @Test
    void driveCannotMakeTankGoNegative() {
        Vehicle vehicle = new Vehicle(50.0, 8.0);
        vehicle.setGas(5.0);
        assertEquals(0.0, vehicle.drive(100.0), 0.001);
    }

    @Test
    void driveWithShortDistanceUsesProportionalFuel() {
        Vehicle vehicle = new Vehicle(50.0, 8.0);
        assertEquals(46.0, vehicle.drive(50.0), 0.001);
    }

    @Test
    void driveWithZeroDistanceDoesNotConsumeFuel() {
        Vehicle vehicle = new Vehicle(50.0, 8.0);
        assertEquals(50.0, vehicle.drive(0.0), 0.001);
    }
}
