package de.tom.equationSolver;

import de.tom.equationSolver.logger.Logger;

public class EquationSolver {

    public static void main(String[] args) {

        Logger.setTimestamp();

        EquationGenerator equationGenerator = new EquationGenerator();
        equationGenerator.generateEquation(3);

    }

}
