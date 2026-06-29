package org.firstinspires.ftc.teamcode;


import static org.firstinspires.ftc.teamcode.RED_FRONT_1_DATA.A1;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.teamcode.RED_FRONT_1_DATA;


// test comment by anson
// 2nd test


@Autonomous(name = "AUTO_TEST_JEGGY", group = "VORTEX")

public class TEST_AUTO extends LinearOpMode
{

        private MecanumDrive DRIVE;

        private final Pose2d STARTING = RED_FRONT_1_DATA.STARTING_POSE;


                @Override
        public void runOpMode()
        {

                INIT_ODOMETRY_AND_MECANUM();


                telemetry.addData("&[EVENT]", "[INIT OK.]");
                telemetry.update();


                waitForStart();


                Actions.runBlocking(DRIVE.actionBuilder(STARTING)

                        .strafeTo(new Vector2d(0, 0))

                        .strafeToLinearHeading(A1, 0)

                        .build()
                );

        }


        private void INIT_ODOMETRY_AND_MECANUM()
        {

                DRIVE = new MecanumDrive(hardwareMap, STARTING);

        }

}
