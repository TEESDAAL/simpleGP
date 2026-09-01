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
    exports gp.impl.genetic_operator;
    exports gp.impl.genetic_operator.multitree;
    exports gp.impl.individual;
    exports gp.impl.individual.multitree;
    exports gp.impl.initializer;
    exports gp.impl.selector;
    exports gp.impl.selector.random;
    exports gp.impl.statistic;

    exports util;
    exports util.operator;
    exports util.random;
    exports util.stream_util;
}