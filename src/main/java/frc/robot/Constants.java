package frc.robot;


public final class Constants {
    public static final class DriveConstants {
        public static final int frontLeftDrivePort = 8;
        public static final int frontLeftTurnPort = 11;
        public static final int frontRightDrivePort = 2;
        public static final int frontRightTurnPort = 6;
        public static final int backLeftDrivePort = 5;
        public static final int backLeftTurnPort = 3;
        public static final int backRightDrivePort = 4;
        public static final int backRightTurnPort = 7;

        // The degrees in which the physical position of the motors are zeroed out.
        public static final double frontLeftTurnOffset = 0.47; // ZERO: 81.19 43.38 (PI/2)
        public static final double frontRightTurnOffset = 0.41 + Math.PI; // ZERO: 69.62 37.75
        public static final double backLeftTurnOffset = -0.66; // ZERO: 55.76 120.83
        public static final double backRightTurnOffset = 0.92; // ZERO: 339.64 354.17

        public static final int frontLeftTurnEncoderPort = 3;
        public static final int frontRightTurnEncoderPort = 4;
        public static final int backLeftTurnEncoderPort = 1;
        public static final int backRightTurnEncoderPort = 2;

        public static final double speedScale = .8;
    }

    public static final class AutonomousConstants {

    }

    public static final class OIConstants {
        public static final int xboxControllerPort = 0;
        public static final int fightStickPort = 1;
    }

    public static final class MechanismConstants {

    }

    public static final class PneumaticsConstants {

    }
}
