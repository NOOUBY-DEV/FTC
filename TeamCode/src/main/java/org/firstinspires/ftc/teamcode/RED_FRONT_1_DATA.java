package org.firstinspires.ftc.teamcode;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;

import org.firstinspires.ftc.onbotjava.handlers.file.NewFile;

public class RED_FRONT_1_DATA
{

        public static Pose2d STARTING_POSE = new Pose2d(24, 24, Math.toRadians(45));

        public static Vector2d A1 = new Vector2d(0, -10);
        public static double A1_HEADING = Math.toRadians(2);


        public static Pose2d FLIP_POSE(final Pose2d POSE)
        {

                return new Pose2d(-POSE.position.x, POSE.position.y, -POSE.heading.toDouble());

        }

        public final Vector2d FLIP_VEC(final Vector2d VECTOR)
        {

                return new Vector2d(-VECTOR.x, VECTOR.y);

        }

}
