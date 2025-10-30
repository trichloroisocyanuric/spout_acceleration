package com.example.spout_acceleration.mixin;

import com.simibubi.create.content.fluids.pump.PumpBlockEntity;
import com.simibubi.create.content.fluids.spout.SpoutBlockEntity;
import com.simibubi.create.content.kinetics.belt.behaviour.BeltProcessingBehaviour.ProcessingResult;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SpoutBlockEntity.class)
public abstract class SpoutBlockEntityMixin extends SmartBlockEntity {
    @Shadow
    public int processingTicks;

    private SpoutBlockEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Unique
    private void spout_acceleration$accelerateSpout() {
        if (this.processingTicks != SpoutBlockEntity.FILLING_TIME || level == null || level.isClientSide) return;
        BlockEntity be = level.getBlockEntity(worldPosition.above());
        if (be instanceof PumpBlockEntity pump) {
            float speed = Math.abs(pump.getSpeed());
            if (speed > 64.f) {
                processingTicks = Math.max((int) (SpoutBlockEntity.FILLING_TIME * 64.f / speed), 6);
            }
        }
    }

    @Inject(
            method = "whenItemHeld",
            at = @At(
                    value = "FIELD",
                    target = "Lcom/simibubi/create/content/fluids/spout/SpoutBlockEntity;processingTicks:I",
                    opcode = Opcodes.PUTFIELD,
                    shift = At.Shift.AFTER
            )
    )
    private void onWhenItemHeld(CallbackInfoReturnable<ProcessingResult> cir) {
        spout_acceleration$accelerateSpout();
    }


    @Inject(
            method = "tick",
            at = @At(
                    value = "FIELD",
                    target = "Lcom/simibubi/create/content/fluids/spout/SpoutBlockEntity;processingTicks:I",
//                    ordinal = 0,
                    opcode = Opcodes.PUTFIELD,
                    shift = At.Shift.AFTER
            )
    )
    private void onTick(CallbackInfo ci) {
        spout_acceleration$accelerateSpout();
    }
}
