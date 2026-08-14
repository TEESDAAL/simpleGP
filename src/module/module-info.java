module ai.weewoo.simpleGP {
    requires java.base;

    // requires org.slf4j; // look into
    exports gp;

    exports gp.core;
    exports gp.core.assessor;
    exports gp.core.breeder;
    exports gp.core.fitness;
    exports gp.core.individal;
    exports gp.core.initializer;
    exports gp.core.selector;
    exports gp.core.statistic;

    exports gp.impl;
    exports gp.impl.assessor;
    exports gp.impl.breeder;
    exports gp.impl.fitness;
    exports gp.impl.genetic_operators;
    exports gp.impl.genetic_operators.multitree;
    exports gp.impl.individual;
    exports gp.impl.individual.multitree;
    exports gp.impl.initializers;
    exports gp.impl.selectors;
    exports gp.impl.selectors.random;
    exports gp.impl.statistic;

    exports utils;
    exports utils.operators;
    exports utils.random;
    exports utils.stream_utils;
}