package org.livoniawarriors.Robot2019;

public interface IControlModule {
    void init();

    void start();

    void update();

    void stop();

    boolean isFinished();
}
