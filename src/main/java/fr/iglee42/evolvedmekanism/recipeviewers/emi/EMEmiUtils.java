package fr.iglee42.evolvedmekanism.recipeviewers.emi;

import java.util.List;

import mekanism.client.gui.element.bar.GuiBar.IBarInfoHandler;
import mekanism.client.gui.element.progress.IProgressInfoHandler;

public final class EMEmiUtils {

    public static final IProgressInfoHandler CONSTANT_PROGRESS = () -> 1;
    public static final IBarInfoHandler FULL_BAR = () -> 1;

    private EMEmiUtils() {
    }

    public static IProgressInfoHandler progressHandler(int processTime) {
        int time = processTime * 50;
        return () -> {
            double subTime = System.currentTimeMillis() % time;
            return subTime / time;
        };
    }

    public static IBarInfoHandler barProgressHandler(int processTime) {
        int time = processTime * 50;
        return () -> {
            double subTime = System.currentTimeMillis() % time;
            return subTime / time;
        };
    }

    public static <T> T getCurrent(List<T> elements) {
        return elements.get((int) (System.currentTimeMillis() / 1000 % elements.size()));
    }
}
