package org.firstinspires.ftc.teamcode;


import static org.firstinspires.ftc.teamcode.RED_FRONT_1_DATA.A1;
import static org.firstinspires.ftc.teamcode.RED_FRONT_1_DATA.A1_HEADING;
import static org.firstinspires.ftc.teamcode.RED_FRONT_1_DATA.A2;
import static org.firstinspires.ftc.teamcode.RED_FRONT_1_DATA.ORIGIN;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.InstantAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;


@Autonomous(name = "AUTO_TEST_JEGGY", group = "VORTEX")

public class TEST_AUTO extends LinearOpMode
{

        private boolean IS_SHOOTING = false;
        private MecanumDrive DRIVE;
        private DcMotorEx SHOOT_MOTOR;
        private DcMotorEx INTAKE1_MOTOR;
        private Servo ANGLE_SERVO;
        private CRServo INTAKE2_SERVO;

        private final Pose2d STARTING = RED_FRONT_1_DATA.STARTING_POSE;


                @Override
        public void runOpMode()
        {

                INIT_HARDWARE();


                telemetry.addData("&[EVENT]", "[INIT OK.]");
                telemetry.update();


                waitForStart();


                INTAKE1_MOTOR.setPower(1);


                Actions.runBlocking(DRIVE.actionBuilder(STARTING)

                        .strafeTo(ORIGIN)

                        .stopAndAdd(new SHOOT_ACTION())

                        .strafeToLinearHeading(A1, A1_HEADING)

                        .strafeTo(A2)

                        .strafeTo(ORIGIN)

                        .build()
                );

        }


        public class SHOOT_ACTION implements Action
        {

                private boolean INITIALIZED = false;


                @Override
                public boolean run(@NonNull TelemetryPacket telemetryPacket)
                {

                        if (!INITIALIZED)
                        {

                                performSmartBurstShoot(780, 0.85, 3);

                                INITIALIZED = true;

                        }

                        return IS_SHOOTING;

                }
        }

        public void performSmartBurstShoot(double targetVel, double anglePos, int numBalls) {

                IS_SHOOTING = true;


                telemetry.addData("Action", "Smart Burst Firing Started");
                telemetry.update();

                // 1. 設定角度，馬達開始加速
                ANGLE_SERVO.setPosition(anglePos);
                SHOOT_MOTOR.setVelocity(targetVel);

                for (int i = 1; i <= numBalls; i++) {
                        ElapsedTime recoveryTimer = new ElapsedTime();

                        // 2. 動態等待轉速回血 (最多只等 1.5 秒，防止死機卡住)
                        while (opModeIsActive() && recoveryTimer.milliseconds() < 1500) {
                                double currentVel = SHOOT_MOTOR.getVelocity();

                                // 【核心邏輯】：如果轉速恢復到目標轉速的 95% (容許約 40 ticks 誤差)
                                // 就立刻打破等待迴圈，準備開火！這樣可以將等待時間極限壓縮。
                                if (currentVel >= (targetVel - 40)) {
                                        break;
                                }
                        }

                        telemetry.addData("Action", "Shooting Ball " + i);
                        telemetry.update();

                        // 3. 送出單一顆球的動作
                        // (根據你原本的 Code，我假設 INTAKE2_SERVO 轉動是送球。你可能需要微調 sleep 的時間，讓它剛好只送一粒球)
                        INTAKE2_SERVO.setPower(1.0);
                        sleep(600); // 調整這個時間，讓它剛好推進一粒球就停


                        INTAKE2_SERVO.setPower(0);
                }

                // 4. 全部打完，降回怠速
                        SHOOT_MOTOR.setVelocity(550);


                IS_SHOOTING = false;
        }


        private void INIT_HARDWARE()
        {

                DRIVE = new MecanumDrive(hardwareMap, STARTING);


                SHOOT_MOTOR = hardwareMap.get(DcMotorEx.class, "SHOOT_MOTOR");
                ANGLE_SERVO = hardwareMap.get(Servo.class, "ANGLE_SERVO");
                INTAKE1_MOTOR = hardwareMap.get(DcMotorEx.class, "INTAKE1_MOTOR");
                INTAKE2_SERVO = hardwareMap.get(CRServo.class, "INTAKE2_SERVO");


                INTAKE2_SERVO.setDirection(CRServo.Direction.REVERSE);
                SHOOT_MOTOR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                SHOOT_MOTOR.setVelocityPIDFCoefficients(30, 3, 0, 12);

        }

}
