package frc.robot.autonomous;

import edu.wpi.first.wpilibj.Timer;
import frc.robot.Components;

public abstract class AutonomousBase {
    protected Components components;
    protected double startTime;
    protected double currentTime;
    
    public AutonomousBase(Components components) {
        this.components = components;
    }

    public void init() {
        this.startTime = Timer.getFPGATimestamp();    
        components.leftEncoder.setPosition(0);
    }

    public void periodic() {
        this.currentTime = Timer.getFPGATimestamp();
    }
}