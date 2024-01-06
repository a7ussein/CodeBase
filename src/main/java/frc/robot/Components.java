package frc.robot;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import com.revrobotics.RelativeEncoder;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.cscore.UsbCamera;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;

public class Components {
    
    //controllers
    public XboxController driveController = new XboxController(Constants.drivingConstants.driveController);
    public XboxController intakeController = new XboxController(Constants.drivingConstants.intakeController);

    // Driving Motors 
    public CANSparkMax leftFrontMotor = new CANSparkMax(2, CANSparkMaxLowLevel.MotorType.kBrushless);
    public CANSparkMax leftBackMotor = new CANSparkMax(1, CANSparkMaxLowLevel.MotorType.kBrushless);
    public CANSparkMax rightFrontMotor = new CANSparkMax(3, CANSparkMaxLowLevel.MotorType.kBrushless);
    public CANSparkMax rightBackMotor = new CANSparkMax(4, CANSparkMaxLowLevel.MotorType.kBrushless);

    public RelativeEncoder leftEncoder = leftFrontMotor.getEncoder();
    public RelativeEncoder rightEncoder = rightFrontMotor.getEncoder();

    MotorControllerGroup leftControllerGroup = new MotorControllerGroup(leftFrontMotor, leftBackMotor);
    MotorControllerGroup rightControllerGroup = new MotorControllerGroup(rightFrontMotor, rightBackMotor);

    public DifferentialDrive drive = new DifferentialDrive(leftControllerGroup, rightControllerGroup);

    
    //camera
    public UsbCamera camera = CameraServer.startAutomaticCapture(0);
    
    public void init() {

        camera.setResolution(400, 222);

        // restore mototors factory defaults
        leftFrontMotor.restoreFactoryDefaults();
        leftBackMotor.restoreFactoryDefaults();
        rightFrontMotor.restoreFactoryDefaults();
        rightBackMotor.restoreFactoryDefaults();
    
        rightControllerGroup.setInverted(true);
        leftControllerGroup.setInverted(false);
    
        leftEncoder.setPosition(0);
        rightEncoder.setPosition(0);
    }
}