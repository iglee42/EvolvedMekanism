package fr.iglee42.evolvedmekanism.mixins.client;

import fr.iglee42.emgenerators.client.WindModelTexture;
import java.util.HashMap;
import java.util.Map;

import mekanism.generators.client.model.ModelWindGenerator;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = ModelWindGenerator.class, remap = false)
public abstract class ModelWindGeneratorMixin {

    @Shadow
    @Final
    private RenderType RENDER_TYPE;

    @Unique
    private static final Map<ResourceLocation, RenderType> evolvedmekanism$tierTypes = new HashMap<>();

    @Redirect(method = "render", at = @At(value = "FIELD", opcode = Opcodes.GETFIELD, target = "Lmekanism/generators/client/model/ModelWindGenerator;RENDER_TYPE:Lnet/minecraft/client/renderer/RenderType;"))
    private RenderType evolvedmekanism$tierTexture(ModelWindGenerator model) {
        ResourceLocation override = WindModelTexture.current();
        if (override == null) {
            return RENDER_TYPE;
        }
        return evolvedmekanism$tierTypes.computeIfAbsent(override, RenderType::entitySolid);
    }
}
