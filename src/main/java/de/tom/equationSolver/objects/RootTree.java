package de.tom.equationSolver.objects;

import de.tom.equationSolver.Operation;

import java.util.ArrayList;

public class RootTree {
    private final ArrayList<Operation> operationList;

    public RootTree() {
        operationList = new ArrayList<>();
    }

    public RootTree(ArrayList<Operation> rootBefore) {
        System.out.println("New RootTree of " + rootBefore.toString());
        operationList = rootBefore;
    }

    public ArrayList<Operation> getOperationList() {
        return operationList;
    }

    private int calculateResult() {

        return 0;
    }
}
