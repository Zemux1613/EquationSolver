package de.tom.equationSolver.objects;

import de.tom.equationSolver.Operation;

import java.util.ArrayList;

public class RootTree {
    private final ArrayList<Operation> operationList;

    public RootTree() {
        operationList = new ArrayList<>();
    }

    public RootTree(ArrayList<Operation> rootBefore) {
        operationList = rootBefore;
    }

    public ArrayList<Operation> getOperationList() {
        return operationList;
    }
}
