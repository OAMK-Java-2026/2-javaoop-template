package exercises;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Exercise1Test {

    @Test
    void addsTwoPositiveNumbers() {
        assertEquals(5, new Exercise1().add(2, 3));
    }

    @Test
    void addsANegativeNumber() {
        assertEquals(-1, new Exercise1().add(2, -3));
    }
}
