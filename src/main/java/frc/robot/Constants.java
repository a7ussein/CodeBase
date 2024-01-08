package frc.robot;

import edu.wpi.first.math.filter.SlewRateLimiter;

public class Constants {
    public static class drivingConstants {
        public static final int driveController = 0;
        public static final int intakeController = 1;
    }

    public static class variables{
        public static final double encoder2inch = 1/ 8.46; 
        public static final SlewRateLimiter limiter = new SlewRateLimiter(1);
    }

}