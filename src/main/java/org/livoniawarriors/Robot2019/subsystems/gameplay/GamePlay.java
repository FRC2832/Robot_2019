package org.livoniawarriors.Robot2019.subsystems.gameplay;

import org.livoniawarriors.Robot2019.ICsvLogger;
import org.livoniawarriors.Robot2019.ISubsystem;
import org.livoniawarriors.Robot2019.Robot;
import org.livoniawarriors.Robot2019.subsystems.diagnostic.IDiagnosable;

public class GamePlay implements ISubsystem, IDiagnosable {

	private Elevator elevator;

	@Override
	public void init() {
		elevator = new Elevator();
	}

	@Override
	public void update(boolean enabled) {
		elevator.update(enabled);
	}

	@Override
	public void dispose() throws Exception {

    }

    @Override
    public void csv(ICsvLogger csv) {

    }
	}

	@Override
	public void diagnose() {
		elevator.diagnose();
	}
}
