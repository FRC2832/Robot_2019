package org.livoniawarriors.Robot2019;

import edu.wpi.first.wpilibj.Filesystem;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.junit.Test;

import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;

public class LoggingTest {

    @Test
    public void TestStuff() {
        System.setProperty("log4j.configurationFile", Paths.get(Filesystem.getOperatingDirectory().toString(), "/src/main/deploy/log4j2.xml").toString());
        System.setProperty("logFolder", Paths.get(Filesystem.getOperatingDirectory().toString(), "/logging").toString());
        var logger = LogManager.getLogger("CsvLogger");

        logger.log(Level.getLevel("CSV"), "", "a", "b", "c");
        logger.log(Level.getLevel("CSV"), "", "1", "2", "3");
        assertEquals(0, 0);
    }
}
