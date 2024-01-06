package frc.robot.autonomous;

import frc.robot.Components;

public class driveForward extends AutonomousBase {

    public driveForward(Components components) {
        super(components);
    }

    @Override
    public void periodic() {
        super.periodic();
        if ((Math.abs(components.leftEncoder.getPosition()) / 8.46) < 8.5) {
            components.drive.tankDrive(0.3, 0.3);
        } else {
            components.drive.tankDrive(0, 0);
        }
    }
}