package de.tom.equationSolver;

import de.tom.equationSolver.objects.RootTree;

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

        // Nummern zwischen 1 - 10 generieren
        while (numbers.size() < numberAmount) {
            final int randomNumber = random.nextInt(10) + 1;
            numbers.add(randomNumber);
        }

        // Eine Gleichung mit einer Zahl beinhaltet keinen Operator
        if (numbers.size() == 1) {
            displayEquation(numbers, numbers.get(0));
            return;
        }

        // check if the equation is interesting
        if (numberAmount == getAmountOfItem(numbers, numbers.get(0))) {
            System.out.println("The equation isn't really interesting. So some numbers changed!");
            numbers.remove(0);
            numbers.add(random.nextInt(10) + 1);
        }

        // alle berechenbaren Ergebnisse
        //    final List<Integer> possibleResults = getPossibleResults(numbers);
        // wähle ein berechenbares Ergebnis aus
        //    final int result = possibleResults.get(random.nextInt(possibleResults.size()));

        final int result = 10;

        final ArrayList<RootTree> rootTrees = generateTree(new ArrayList<>(), new ArrayList<>(), 3, 2);

        displayEquation(numbers, result);
    }

    public List<Integer> getPossibleResults(List<Integer> numbers) {
        List<Integer> list = new ArrayList<>();

        // Behandele den Fall, dass nur eine Operation benutzt wird
        for (int i = 0; i < Operation.values().length; i++) {
            final Operation operation = Operation.values()[i];
            int result = numbers.get(0);
            for (int j = 1; j < numbers.size(); j++) {
                int number = numbers.get(j);
                if (result < 0) break;
                switch (operation) {
                    case ADDITION -> result += number;
                    case SUBTRACTION -> result -= number;
                    case MULTIPLIKATION -> result *= number;
                    case DIVISION -> result /= number;
                }
            }
            System.out.println(numbers + " - " + operation.name() + " - " + result);
            // negative Ergebnisse sind ausgeschlossen
            if (result > 0) {
                list.add(result);
            }
        }

        return list;
    }

    public ArrayList<RootTree> generateTree(ArrayList<RootTree> trees, ArrayList<RootTree> lastTrees, int deap, int currentLayer) {
        if (deap == 0 || deap == 1) {
            return trees;
        }

        if (currentLayer == 2) {
            for (Operation value : Operation.values()) {
                final RootTree rootTree = new RootTree(getOperations(new ArrayList<>(), Operation.ADDITION));
                trees.add(rootTree);
                lastTrees.add(rootTree);
            }
        } else {
            final int size = lastTrees.size();
            lastTrees.clear();
            for (int i = 0; i < size; i++) {
                final ArrayList<Operation> operationList = trees.get(i).getOperationList();
                for (Operation value : Operation.values()) {
                    final RootTree rootTree = new RootTree(getOperations(operationList, value));
                    lastTrees.add(rootTree);
                    trees.add(rootTree);
                }
            }
        }

        if (deap >= currentLayer) {
            generateTree(trees, lastTrees, deap, ++currentLayer);
            return trees;
        }

        return trees;
    }

    private static ArrayList<Operation> getOperations(ArrayList<Operation> operationList, Operation operation) {
        final ArrayList<Operation> addition = (ArrayList<Operation>) operationList.clone();
        addition.add(operation);
        return addition;
    }

    public boolean hasPair(List<Integer> numbers) {
        // 1 + 4 * 1 * 5 = 21
        // 1 * 4 * 1 * 5 = 20
        for (int i = 0; i < numbers.size() - 1; i++) {
            final int left = numbers.get(i);
            final int right = numbers.get(i + 1);
            if (left != 1 && right != 1) {
                return true;
            }
        }
        return false;
    }

    public void displayEquation(List<Integer> list, int result) {
        StringJoiner stringJoiner = new StringJoiner(" o ");
        list.forEach(integer -> stringJoiner.add(integer + ""));
        System.out.println(stringJoiner + " = " + result);
    }

    public long getAmountOfItem(List<Integer> list, int item) {
        return list.stream().filter(integer -> integer == item).count();
    }

}
