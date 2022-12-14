package de.tom.equationSolver;

import de.tom.equationSolver.logger.LogLevel;
import de.tom.equationSolver.logger.Logger;
import de.tom.equationSolver.objects.RootTree;
import org.mariuszgromada.math.mxparser.Expression;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class EquationGenerator {

    public void generateEquation(int numberAmount) {

        if (numberAmount <= 0) {
            return;
        }

        List<Integer> numbers = new ArrayList<>();
        final Random random = new Random();

        // Nummern zwischen 1 - 9 generieren
        while (numbers.size() < numberAmount) {
            final int randomNumber = random.nextInt(9) + 1;
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
            numbers.add(random.nextInt(9) + 1);
        }
        final ArrayList<RootTree> rootTrees = generateTree(new ArrayList<>(), new ArrayList<>(), numberAmount, 2);

        // alle berechenbaren Ergebnisse
        getPossibleResults(numbers, rootTrees, possibleResults -> {
            // wähle ein berechenbares Ergebnis aus
            final int result = possibleResults.get(random.nextInt(possibleResults.size()));
            // final int result = 10;
            displayEquation(numbers, result);
        });
    }

    // Automat der alle Variationen durchläuft
    public void getPossibleResults(List<Integer> numbers, ArrayList<RootTree> rootTrees, Consumer<List<Integer>> consumer) {
        List<Integer> list = new ArrayList<>();
        List<Integer> blackList = new ArrayList<>();
        List<String> knownEquations = new ArrayList<>();

        String leftSideOfEquation = getLeftSideOfEquation(numbers, true).toString();

        final List<RootTree> relevantTrees = rootTrees.stream().filter(rootTree -> rootTree.getOperationList().size() == numbers.size() - 1).toList();
        for (final RootTree rootTree : relevantTrees) {
            final ArrayList<Operation> operationList = rootTree.getOperationList();

            // Gleichung mit Operator Liste durchrechnen und das Ergebnis auf list hinzufügen sollte es dort noch nicht darauf sein.
            calculateResult(leftSideOfEquation, operationList, (aDouble, equation) -> {
                // ignore known equations
                if (knownEquations.contains(equation)) {
                    return;
                }
                // negative Zahlen sind nicht erlaubt
                if (aDouble > 0) {
                    // nur ganze Zahlen
                    if (aDouble % 1 == 0) {
                        final int parseInt = Integer.parseInt(String.valueOf(aDouble).substring(0, String.valueOf(aDouble).indexOf(".")));
                        // Ignoriere nicht eindeutige Lösungen.
                        if (!blackList.contains(parseInt)) {
                            // doppelte Zahlen werden nicht stärker Gewichtet
                            if (!list.contains(parseInt)) {
                                Logger.log(LogLevel.INFO, equation + " = " + parseInt, equation);
                                list.add(parseInt);
                                knownEquations.add(equation);
                            } else {
                                // Entferne nicht eindeutige Lösungen. (Lösungen mit dem selbem Ergebnis aber verschiedenen Operatoren)
                                list.remove((Integer) parseInt);
                                blackList.add(parseInt);
                            }
                        }
                    }
                }
            });
        }
        consumer.accept(list);
    }

    // Automat der eine spezielle Lösung berechnet
    public void calculateResult(String equation, ArrayList<Operation> operators, BiConsumer<Double, String> consumer) {
        final String equationWithOperations = getEquationWithOperations(equation, operators);
        final Expression expression = new Expression(equationWithOperations);
        final double result = expression.calculate(); //getResult(equation);
        consumer.accept(result, equationWithOperations);
    }

    public String getEquationWithOperations(final String rawEquation, ArrayList<Operation> operators) {
        int operationCount = 0;
        StringBuilder currentEquation = new StringBuilder();
        for (int i = 0; i < rawEquation.length() - 1; i++) {
            if (rawEquation.charAt(i) == 'o') {
                final Operation operation = operators.get(operationCount);
                currentEquation.append(operation.getSymbol());
                operationCount++;
            } else {
                currentEquation.append(rawEquation.charAt(i));
            }
        }
        currentEquation.append(rawEquation.charAt(rawEquation.length() - 1));
        return currentEquation.toString();
    }

    // TODO: Methode nochmal überdenken irgendwie scheinen zu viele RootTrees generiert zu werden.
    public ArrayList<RootTree> generateTree(ArrayList<RootTree> trees, ArrayList<RootTree> lastTrees, int deap, int currentLayer) {
        if (deap == 0 || deap == 1) {
            return trees;
        }

        if (currentLayer == 2) {
            for (Operation value : Operation.values()) {
                final RootTree rootTree = new RootTree(getOperations(new ArrayList<>(), value));
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

    public void displayEquation(List<Integer> list, int result) {
        StringJoiner stringJoiner = getLeftSideOfEquation(list, false);
        System.out.println(stringJoiner + " = " + result);
    }

    private static StringJoiner getLeftSideOfEquation(List<Integer> list, boolean automat) {
        StringJoiner stringJoiner = new StringJoiner(automat ? "o" : " o ");
        list.forEach(integer -> stringJoiner.add(integer + ""));
        return stringJoiner;
    }

    public long getAmountOfItem(List<Integer> list, int item) {
        return list.stream().filter(integer -> integer == item).count();
    }

}
