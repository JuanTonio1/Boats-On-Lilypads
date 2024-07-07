package net.juantonio.boats_on_lilypads.mixin;

import net.juantonio.boats_on_lilypads.events.inter.PlayerEnterBoatEvent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.util.ActionResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BoatEntity.class)
public class BoatMixin {
    @Inject(at = @At(value = "HEAD"), method = "updatePassengerPosition", cancellable = true)
    private void onUpdatePassengerPosition(Entity passenger, Entity.PositionUpdater positionUpdater,
                                           CallbackInfo ci) {
        if (passenger instanceof PlayerEntity) {
            ActionResult result = PlayerEnterBoatEvent.EVENT.invoker().onEnterBoat((PlayerEntity) passenger,
                    (BoatEntity) (Object) this);

            if (result == ActionResult.FAIL) {
                ci.cancel();
            }
        }
    }
}
