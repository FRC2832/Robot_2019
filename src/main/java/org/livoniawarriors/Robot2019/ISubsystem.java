package org.livoniawarriors.Robot2019;

public interface ISubsystem {
    void init();

    void update(boolean enabled);

    void dispose() throws Exception;

    void csv(ICsvLogger csv);

    void diagnose();
}
