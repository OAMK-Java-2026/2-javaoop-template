# 2-javaoop

Welcome! This topic has one or more small Java exercises to work through.

## What you'll learn

- Designing a class from a UML diagram, including choosing the right
  access modifiers (`private`/`protected`/`public`) for fields and methods.
- Writing constructors that initialize member variables.
- Writing getters and setters, including ones that enforce a constraint
  (e.g. a value that can't exceed a maximum).
- Object composition: a class that holds and manages an array of another
  class's objects.
- Overriding `toString()` to produce a custom, human-readable
  representation of an object.

## The exercises

| Exercise | Name | File | Points |
|---|---|---|---|
| 1 | Apartment | `src/main/java/exercises/Apartment.java` | 1 |
| 2 | Vehicle | `src/main/java/exercises/Vehicle.java` | 1 |
| 3 | Garage | `src/main/java/exercises/Garage.java` | 2 |

Each exercise has a `TODO` in its class to fill in, and a matching test file
you can use to check your work as you go. You don't need to touch the test
files — they're just there to help you see how you're doing.

There's also a `Main.java` with a `main` method, so you have something
runnable from the start — it doesn't do anything yet, it's just there so
your "Run" button works right away.

## Exercise descriptions

### 1. Apartment (1p)

Create a class called `Apartment` based on the UML class diagram below:

![Apartment class UML](uml_apartment.png)

Pay attention to the access modifiers for each member variable or method.

As you see, a constructor with two parameters is needed: the number of
tenants as the 1st parameter and the area of the apartment as the 2nd
parameter.

Add method `heatingCost` taking the electricity price (kw/h) as `Float`.
Then, the method calculates the heating costs as
`numOfTenants * area * price` and returns it.

### 2. Vehicle (1p)

Create Java class `Vehicle` that has

- Protected member variable `fuelConsumption` that is type `Double`. This
  stores how much fuel the vehicle uses per 100km.
- Protected member variables `tankSize` and `gas` that are type `Double`,
  and store the maximum tank capacity and current amount of fuel in the
  vehicle as litres.
- Constructor with parameters that initializes the member variables
  `tankSize` and `fuelConsumption`. Constructor also fills up the tank.
- Getter for `tankSize`.
- Getter & setter for `gas`, which will either take gas or put more gas
  into the tank. Obviously, it's not possible to add more fuel than the
  tank capacity.
- Public method `drive`, that takes distance driven (kilometers) as
  parameter of type `Double`. The method calculates and removes the fuel
  the drive has taken and returns the remaining amount of fuel in the tank.
  Obviously, the tank can't have a negative amount of fuel.

### 3. Garage (2p)

Create class `Garage` that hosts four vehicles as an array. Reuse the class
`Vehicle` from the previous exercise. Additionally:

- The vehicle array is private.
- The class has a constructor. By default all vehicles in the array are
  initialized as `null`.
- Method `addVehicle(Integer position, Double tankSize, Double fuelConsumption)`
  creates a new `Vehicle` at the given position in the array. The method
  does not return anything.
- Method `drive(Integer position, Double distance)` calls the `drive`
  method of the `Vehicle` in `position` with parameter `distance`. If
  there is no vehicle in the array in `position`, `null` is returned. If
  there is a vehicle, the result from the `drive` method for the `Vehicle`
  class is returned.
- Method `refuel` (without parameters) that fills out the tanks in all
  vehicles and returns the amount of fuel needed. Fuel is consumed in
  vehicles when the `drive` method of the `Vehicle` class is called.
- Override the method `toString` (from the `Object` class), that returns
  the garage information as one line per vehicle in the garage. If there
  is no vehicle in a slot, report it as empty.

  For example:

  ```
  Vehicle 1: 57.48l gas left
  Vehicle 2: 0.00l gas left
  Vehicle 3: 93.84l gas left
  Vehicle 4: empty
  ```

## Step by step

1. **Clone this repo in VS Code**: open VS Code, click the **Source Control**
   icon in the left sidebar, then **Clone Repository**. Paste this repo's
   URL, pick a folder to clone it into, and click **Open** when VS Code asks
   whether to open the cloned repo.
2. **Run the tests before changing anything**, just to see where you're
   starting from. Click the flask-shaped **Testing** icon in the left
   sidebar, then the play button at the top of the Test Explorer panel —
   everything will be red at first, and that's completely normal.
3. **Implement each exercise** in its source file, one at a time.
4. **Re-run the tests** after each change to see your progress. Prefer a
   terminal? `mvn test` does the same check for all exercises at once.
5. **Work locally** until everything passes.
6. **Push your work back** to the GitHub organization when you're ready,
   using the **Source Control** view:
   - Hover over **Changes** and click the **+** icon to stage all your
     changes (or stage files one by one).
   - Type a commit message in the box at the top, e.g. `Exercises done`.
   - Click the arrow next to the **Commit** button and choose
     **Commit & Push** — this commits and pushes to GitHub in one step.
7. **Assignment completed — good job!**
