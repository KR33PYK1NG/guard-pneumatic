package rmc.mixins.guard_pneumatic;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import me.desht.pneumaticcraft.api.tileentity.IAirHandlerItem;
import me.desht.pneumaticcraft.common.entity.projectile.EntityVortex;
import me.desht.pneumaticcraft.common.item.ItemVortexCannon;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

@Mixin(value = ItemVortexCannon.class)
public abstract class ItemVortexCannonMixin {

    @Inject(method = "Lme/desht/pneumaticcraft/common/item/ItemVortexCannon;onItemRightClick(Lnet/minecraft/world/World;Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/Hand;)Lnet/minecraft/util/ActionResult;",
            locals = LocalCapture.CAPTURE_FAILSOFT,
            at = @At(value = "INVOKE",
                     target = "Lnet/minecraft/world/World;addEntity(Lnet/minecraft/entity/Entity;)Z"))
    private void onItemRightClickMixin(World world, PlayerEntity playerIn, Hand handIn, CallbackInfoReturnable<?> mixin, ItemStack iStack, IAirHandlerItem airHandler, float factor, EntityVortex vortex) {
        vortex.setShooter(playerIn);
    }

}