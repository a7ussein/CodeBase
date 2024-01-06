package frc.robot;

import com.revrobotics.CANSparkMax.IdleMode;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.autonomous.AutonomousBase;
import frc.robot.autonomous.driveForward;
import frc.robot.autonomous.DoNothing;

 
public class Robot extends TimedRobot {
    // Auto Selection:
    private static final String kDefaultAuto = "Nothing Auto";
    private static final String kDepositAndDriveForward = "Mobility";
  
    private final SendableChooser<String> auto_chooser = new SendableChooser<>();
    private Components components = new Components();
    private AutonomousBase autonomous;

  @Override
  public void robotInit() {
    // Auto Selection:
    auto_chooser.setDefaultOption("Nothing Auto", kDefaultAuto);
    auto_chooser.addOption("Mobility", kDepositAndDriveForward);
    SmartDashboard.putData("Auto choices", auto_chooser);
    
    // all components inilizations are in the components.java file  
    components.init();
    }

  @Override
  public void robotPeriodic() {
  }

  @Override
    public void autonomousInit() {
        // Auto Stuff:
        switch (auto_chooser.getSelected()) {
            case kDepositAndDriveForward:
                autonomous = new driveForward(components);
            break;
            case kDefaultAuto:
                autonomous = new DoNothing(components);
            break;
            default:
                autonomous = new DoNothing(components);
            break;
        }

        autonomous.init();
    }
    
  @Override
  public void autonomousPeriodic() {
    autonomous.periodic();
  }

  @Override
    public void teleopInit() {
        enableDrivingBreak(true);
        components.leftEncoder.setPosition(0);
        components.rightEncoder.setPosition(0);

        // imput ramping
        components.rightFrontMotor.setOpenLoopRampRate(0.5);
        components.rightBackMotor.setOpenLoopRampRate(0.5);
        components.leftFrontMotor.setOpenLoopRampRate(0.5);
        components.leftBackMotor.setOpenLoopRampRate(0.5);
    }

    @Override
    public void teleopPeriodic() {
        // drive controls
        double Speed = -components.driveController.getRawAxis(1) * 0.9; // for this axis: up is negative, down is positive
        double turn = -components.driveController.getRawAxis(4) * 0.44;
        if (components.driveController.getRightBumper()) { // if the RightBumber is pressed then slow mode is going to be enabled
          components.drive.arcadeDrive(Speed / 2, turn/2);
        } else if (components.driveController.getLeftBumper()) { // if both right and left bumbers are pressed then ultra slow mode is going to be enabled
          components.drive.arcadeDrive(0, 0);
        } else {
          components.drive.arcadeDrive(Speed, turn); // if no button is pressed the "input ramping" is going to be on
        }
    }

  @Override
  public void disabledInit() {
    enableDrivingBreak(true);  }
  
  @Override
  public void disabledPeriodic() {}

      // Functions
      private void enableDrivingBreak(boolean on) {
        IdleMode dMotormode;
        if (on) {
            dMotormode = IdleMode.kBrake;
        } else {
            dMotormode = IdleMode.kCoast;
        }
        components.leftFrontMotor.setIdleMode(dMotormode);
        components.leftBackMotor.setIdleMode(dMotormode);
        components.rightFrontMotor.setIdleMode(dMotormode);
        components.rightBackMotor.setIdleMode(dMotormode);
        // armMotor.setIdleMode(dMotormode);
    }
}
