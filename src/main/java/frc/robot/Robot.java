package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.revrobotics.CANSparkMax.IdleMode;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.autonomous.AutonomousBase;
import frc.robot.autonomous.DepositAndDriveForward;
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
                autonomous = new DepositAndDriveForward(components);
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
        enableIntakeBreak(true);

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
        double vAngleTest = components.gyro.getYComplementaryAngle();
        vAngleTest = vAngleTest * -1; // rio is mounted backwards

        if (components.driveController.getRightBumper()) { // if the RightBumber is pressed then slow mode is going to be enabled
          components.drive.arcadeDrive(Speed / 2, turn/2);
        } else if (components.driveController.getLeftBumper()) { // if both right and left bumbers are pressed then ultra slow mode is going to be enabled
          components.drive.arcadeDrive(0, 0);
        } else {
          components.drive.arcadeDrive(Speed, turn); // if the no button is pressed the "input ramping" is going to be on
        }

        // auto balance using the A Button
        if (components.driveController.getAButton()) {
            if (vAngleTest > 2) {
              components.drive.tankDrive(0.27, 0.27);
            }
            if (vAngleTest < -2) {
              components.drive.tankDrive(-0.27, -0.27);
            }
        }



        // intake RaisingMotor Control
        double raisingPower = components.intakeController.getRawAxis(1);
        // deadBand -- just cuz, why not?
        if (Math.abs(raisingPower) < 0.05) {
            raisingPower = 0;
        }

        // going forward
        if (raisingPower < 0 && components.frontLimitSensor.get()) {
            raisingPower = 0;
        }

        // going backward
        if (raisingPower > 0 && components.backLimitSensor.get()) {
            raisingPower = 0;
        }

        if ((raisingPower < 0 && !components.frontLimitSensor.get()) || (raisingPower > 0 && !components.backLimitSensor.get())) {
          components.raisingMotor.set(raisingPower * -0.6);
        } else {
          components.raisingMotor.set(0);
        }

        // intake Rollers control
        double rollersPower = 0;

        /*
         * Operator can only intake using the A button if there is no Cube in the intake
         * Operator can only outtake using the Y button if there is a cube in the intake
         * if something happens to the sensor then the operator can just use the right bumber for intake and the left bumber for outtake 
         */
        if(components.intakeController.getAButton()) {
            rollersPower = -0.7;
        }else if(components.intakeController.getYButton()) {
            rollersPower = 1; 
        }
        components.rollerMotor.set(ControlMode.PercentOutput, rollersPower);
    }

  @Override
  public void disabledInit() {
    enableDrivingBreak(true);
    enableIntakeBreak(true);
  }
  
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

    private void enableIntakeBreak(boolean on) {
        NeutralMode iMotorMode;
        if (on) {
            iMotorMode = NeutralMode.Brake;
        } else {
            iMotorMode = NeutralMode.Coast;
        }

        components.raisingMotor.setNeutralMode(iMotorMode);
        components.rollerMotor.setNeutralMode(iMotorMode);
    }

}
