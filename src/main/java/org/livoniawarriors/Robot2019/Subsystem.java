package org.livoniawarriors.Robot2019;

public interface Subsystem {
    void init();

    void update(boolean enabled);

    void dispose() throws Exception;
}
