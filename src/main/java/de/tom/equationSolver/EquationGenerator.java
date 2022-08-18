package de.tom.equationSolver;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.StringJoiner;

public class EquationGenerator {

    public void generateEquation(int numberAmount) {

        if (numberAmount <= 0) {
            return;
        }

        List<Integer> numbers = new ArrayList<>();
        final Random random = new Random();
        // wenn eine einzelne Zahl maximal 10 sein kan so kann das ergebnis maximal 10 * Anzahl der Ziffern sein.
        final int result = random.nextInt(numberAmount * 10) + 1;

        // Numern zwischen 1 - 10 generieren
        while (numbers.size() <= numberAmount) {
            final int randomNumber = random.nextInt(10) + 1;
            numbers.add(randomNumber);
        }

        // check if the equation is interesting
        if (numberAmount == getAmountOfItem(numbers, numbers.get(0))) {
            System.out.println("The equation isn't really interesting. So some numbers changed!");
        }

        displayEquation(numbers, result);
    }

    public void displayEquation(List<Integer> list, int result) {
        StringJoiner stringJoiner = new StringJoiner(" o ");
        list.forEach(integer -> stringJoiner.add(integer + ""));
        System.out.println(stringJoiner.toString() + " = " + result);
    }

    public long getAmountOfItem(List<Integer> list, int item) {
        return list.stream().filter(integer -> integer == item).count();
    }

}
