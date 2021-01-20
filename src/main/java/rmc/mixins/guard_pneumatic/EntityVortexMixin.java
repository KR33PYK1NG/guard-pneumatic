package rmc.mixins.guard_pneumatic;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_16_R3.block.CraftBlock;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import me.desht.pneumaticcraft.common.entity.projectile.EntityVortex;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

@Mixin(value = EntityVortex.class)
public abstract class EntityVortexMixin
extends ThrowableEntity {

    @Inject(method = "Lme/desht/pneumaticcraft/common/entity/projectile/EntityVortex;tryCutPlants(Lnet/minecraft/util/math/BlockPos;)Z",
            cancellable = true,
            at = @At(value = "INVOKE",
                     target = "Lnet/minecraft/world/World;destroyBlock(Lnet/minecraft/util/math/BlockPos;Z)Z"))
    private void tryCutPlantsMixin(BlockPos pos, CallbackInfoReturnable<Boolean> mixin) {
        boolean fail = true;
        Entity shooter = this.func_234616_v_();
        if (shooter instanceof ServerPlayerEntity) {
            BlockBreakEvent event = new BlockBreakEvent(
                CraftBlock.at(this.getEntityWorld(), pos),
                ((ServerPlayerEntity) shooter).getBukkitEntity()
            );
            Bukkit.getPluginManager().callEvent(event);
            if (!event.isCancelled()) {
                fail = false;
            }
        }
        if (fail) {
            this.remove();
            mixin.setReturnValue(false);
        }
    }

    @Inject(method = "Lme/desht/pneumaticcraft/common/entity/projectile/EntityVortex;onImpact(Lnet/minecraft/util/math/RayTraceResult;)V",
            cancellable = true,
            locals = LocalCapture.CAPTURE_FAILSOFT,
            at = @At(value = "INVOKE",
                     target = "Lnet/minecraft/entity/Entity;setMotion(Lnet/minecraft/util/math/vector/Vector3d;)V"))
    private void onImpactMixin(RayTraceResult rtr, CallbackInfo mixin, Entity entity) {
        boolean fail = true;
        Entity shooter = this.func_234616_v_();
        if (shooter instanceof ServerPlayerEntity) {
            PlayerInteractEntityEvent event = new PlayerInteractEntityEvent(
                ((ServerPlayerEntity) shooter).getBukkitEntity(),
                entity.getBukkitEntity()
            );
            Bukkit.getPluginManager().callEvent(event);
            if (!event.isCancelled()) {
                fail = false;
            }
        }
        if (fail) {
            this.remove();
            mixin.cancel();
        }
    }

    private EntityVortexMixin(EntityType<? extends EntityVortex> type, World world) {
        super (type, world);
    }

}