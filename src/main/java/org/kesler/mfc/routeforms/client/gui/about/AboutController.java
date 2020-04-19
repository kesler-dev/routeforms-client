package org.kesler.mfc.routeforms.client.gui.about;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Window;
import org.kesler.mfc.routeforms.client.Version;
import org.kesler.mfc.routeforms.client.gui.AbstractController;


public class AboutController extends AbstractController {

    @FXML protected Label versionLabel;
    @FXML protected Label releaseDateLabel;


    @Override
    public void show(Window owner) {
        super.show(owner, "О программе");
    }

    @Override
    protected void updateContent() {
        versionLabel.setText(Version.getVersion());
        releaseDateLabel.setText(Version.getReleaseDate());
    }
}
