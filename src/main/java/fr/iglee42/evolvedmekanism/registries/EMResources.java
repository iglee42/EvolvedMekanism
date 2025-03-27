package fr.iglee42.evolvedmekanism.registries;

import mekanism.common.resource.IResource;

public enum EMResources implements IResource {
    BETTER_GOLD("better_gold"),
    PLASLITHERITE("plaslitherite"),
    ;

    private final String registrySuffix;

    EMResources(String registrySuffix) {
        this.registrySuffix = registrySuffix;
    }

    @Override
    public String getRegistrySuffix() {
        return registrySuffix;
    }
}
