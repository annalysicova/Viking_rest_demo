package ru.mephi.vikingdemo.controller;

import org.springframework.stereotype.Component;
import ru.mephi.vikingdemo.gui.VikingDesktopFrame;
import ru.mephi.vikingdemo.model.Viking;
import ru.mephi.vikingdemo.service.VikingService;

import javax.swing.SwingUtilities;

@Component
public class VikingListener {

    private final VikingService service;
    private VikingDesktopFrame gui;

    public VikingListener(VikingService service) {
        this.service = service;
    }

    public void setGui(VikingDesktopFrame gui) {
        this.gui = gui;
    }

    public Viking testAdd() {
        Viking viking = service.createRandomViking();
        showViking(viking);
        return viking;
    }

    public void showViking(Viking viking) {
        if (gui != null) {
            SwingUtilities.invokeLater(() -> gui.addOrUpdateViking(viking));
        }
    }

    public void removeViking(int id) {
        if (gui != null) {
            SwingUtilities.invokeLater(() -> gui.removeViking(id));
        }
    }

    public void refreshGui() {
        if (gui != null) {
            SwingUtilities.invokeLater(gui::refreshVikings);
        }
    }
}
