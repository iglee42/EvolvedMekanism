package fr.iglee42.evolvedmekanism.utils;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

public class EMMixinConfig implements IMixinConfigPlugin{


    @Override
    public void acceptTargets(Set<String> arg0, Set<String> arg1) {
    }

    @Override
    public List<String> getMixins() {
        return null;
    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public void onLoad(String arg0) {
    }

    @Override
    public void postApply(String arg0, ClassNode arg1, String arg2, IMixinInfo arg3) {
    }

    @Override
    public void preApply(String arg0, ClassNode arg1, String arg2, IMixinInfo arg3) {
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        System.out.println("Mixin " + mixinClassName + " -> " + targetClassName);
        return !ModsCompats.getAllMixinsClasses().contains(mixinClassName) || Arrays.stream(ModsCompats.values())
                .filter(m -> m.getMixinClasses().contains(mixinClassName))
                .allMatch(ModsCompats::isLoaded);
    }

}
