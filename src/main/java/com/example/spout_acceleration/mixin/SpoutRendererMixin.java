package com.example.spout_acceleration.mixin;

import com.simibubi.create.content.fluids.spout.SpoutBlockEntity;
import com.simibubi.create.content.fluids.spout.SpoutRenderer;
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(SpoutRenderer.class)
public abstract class SpoutRendererMixin extends SafeBlockEntityRenderer<SpoutBlockEntity> {
    @ModifyConstant(
            method = "renderSafe(Lcom/simibubi/create/content/fluids/spout/SpoutBlockEntity;FLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;II)V",
            constant = @Constant(floatValue = 5, ordinal = 0)
    )
    private float modifyProcessingProgress(float constant) {
        return 2;
    }
}
