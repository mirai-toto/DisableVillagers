package com.stratecide.disable.villagers.mixin;

import com.stratecide.disable.villagers.DisableVillagersMod;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityInteraction;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(VillagerEntity.class)
public abstract class VillagerMixin extends MerchantEntity {
    @Shadow private int experience;

    public VillagerMixin(EntityType<? extends MerchantEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    private void onVillagerTickInject(CallbackInfo ci) {
        if (DisableVillagersMod.isKillVillagers() && (!DisableVillagersMod.isSpareExperiencedVillagers() || experience == 0) && !this.isDead()) {
            this.kill();
            ci.cancel();
        }
    }

    @Inject(method = "isReadyToBreed", at = @At("HEAD"), cancellable = true)
    private void onIsReadyToBreedInject(CallbackInfoReturnable<Boolean> cir) {
        if (!DisableVillagersMod.isBreeding()) {
            cir.setReturnValue(false);
        }
    }
    
    // TODO: Trade cycling

    @Inject(method = "onInteractionWith", at=@At("HEAD"), cancellable = true)
    private void onInteractionWithInject(EntityInteraction interaction, Entity player, CallbackInfo ci) {
        if (DisableVillagersMod.getCuredZombieLoot() == null || interaction != EntityInteraction.ZOMBIE_VILLAGER_CURED) {
            return;
        }

        ServerWorld world = (ServerWorld) getWorld();
        DamageSource source = world.getDamageSources().generic();
        
        // When a zombie villager is cured, generate loot from the configured loot table
        // and kill the villager instead of converting it to a normal villager
        LootContextParameterSet.Builder builder = new LootContextParameterSet.Builder(world)
            .add(LootContextParameters.THIS_ENTITY, this)
            .add(LootContextParameters.ORIGIN, this.getPos())
            .add(LootContextParameters.DAMAGE_SOURCE, source)
            .addOptional(LootContextParameters.KILLER_ENTITY, source.getSource())
            .addOptional(LootContextParameters.DIRECT_KILLER_ENTITY, source.getSource());

        if (player instanceof PlayerEntity playerEntity) {
            builder.add(LootContextParameters.LAST_DAMAGE_PLAYER, playerEntity).luck(playerEntity.getLuck());
        }

        DisableVillagersMod.getCuredZombieLoot().generateLoot(builder.build(LootContextTypes.ENTITY), this::dropStack);
        
        this.kill();
        ci.cancel();
    }
}
