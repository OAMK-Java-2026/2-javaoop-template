package exercises;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Exercise2Test {

    @Test
    void multipliesTwoPositiveNumbers() {
        assertEquals(6, new Exercise2().multiply(2, 3));
    }

    @Test
    void multiplyingByZeroIsZero() {
        assertEquals(0, new Exercise2().multiply(5, 0));
    }
}
