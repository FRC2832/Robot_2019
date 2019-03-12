package org.livoniawarriors.Robot2019;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public final class Helpers {

    public static PIDController buildPIDController(double p, double i, double d, Supplier<Double> input, Consumer<Double> output, double period) {
        return buildPIDController(p, i, d, 0, input, output, period);
    }

    public static PIDController buildPIDController(double p, double i, double d, double f, Supplier<Double> input, Consumer<Double> output, double period) {
        PIDSource pidSource = new PIDSource() {
            PIDSourceType pidSourceType;

            @Override
            public void setPIDSourceType(PIDSourceType pidSource) {
                pidSourceType = pidSource;
            }

            @Override
            public PIDSourceType getPIDSourceType() {
                return pidSourceType;
            }

            @Override
            public double pidGet() {
                return input.get();
            }
        };
        return new PIDController(p, i, d, f, pidSource, output::accept, period);
    }
}
