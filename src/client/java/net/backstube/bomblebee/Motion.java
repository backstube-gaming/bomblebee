package net.backstube.bomblebee;

import net.minecraft.util.math.Vec3d;

public class Motion {

    public static final double DIAGONAL_MULTIPLIER = Math.sin((float) Math.toRadians(45));

    public static void doMotion(BombleBeeClientEntity cameraEntity, double hSpeed, double vSpeed) {
        float yaw = cameraEntity.getYaw();
        double velocityX = 0.0;
        double velocityY = 0.0;
        double velocityZ = 0.0;

        var forward = Vec3d.fromPolar(0, yaw);
        var side = Vec3d.fromPolar(0, yaw + 90);

        cameraEntity.input.tick(false, 0.3F);
        hSpeed = hSpeed * (cameraEntity.isSprinting() ? 1.5 : 1.0);

        boolean straight = false;
        if (cameraEntity.input.pressingForward) {
            velocityX += forward.x * hSpeed;
            velocityZ += forward.z * hSpeed;
            straight = true;
        }
        if (cameraEntity.input.pressingBack) {
            velocityX -= forward.x * hSpeed;
            velocityZ -= forward.z * hSpeed;
            straight = true;
        }

        boolean strafing = false;
        if (cameraEntity.input.pressingRight) {
            velocityZ += side.z * hSpeed;
            velocityX += side.x * hSpeed;
            strafing = true;
        }
        if (cameraEntity.input.pressingLeft) {
            velocityZ -= side.z * hSpeed;
            velocityX -= side.x * hSpeed;
            strafing = true;
        }

        if (straight && strafing) {
            velocityX *= DIAGONAL_MULTIPLIER;
            velocityZ *= DIAGONAL_MULTIPLIER;
        }

        if (cameraEntity.input.jumping) {
            velocityY += vSpeed;
        }
        if (cameraEntity.input.sneaking) {
            velocityY -= vSpeed;
        }

        cameraEntity.setVelocity(velocityX, velocityY, velocityZ);
    }
}
