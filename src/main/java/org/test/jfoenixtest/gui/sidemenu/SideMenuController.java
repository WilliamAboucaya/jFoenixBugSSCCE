package org.test.jfoenixtest.gui.sidemenu;

import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXListView;
import org.test.jfoenixtest.gui.uicomponents.*;
import io.datafx.controller.ViewController;
import io.datafx.controller.flow.Flow;
import io.datafx.controller.flow.FlowException;
import io.datafx.controller.flow.FlowHandler;
import io.datafx.controller.flow.action.ActionTrigger;
import io.datafx.controller.flow.context.FXMLViewFlowContext;
import io.datafx.controller.flow.context.ViewFlowContext;
import io.datafx.controller.util.VetoException;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;

import javax.annotation.PostConstruct;
import java.util.Objects;

@ViewController(value = "/fxml/SideMenu.fxml", title = "Material Design Example")
public class SideMenuController {

    @FXMLViewFlowContext
    private ViewFlowContext context;
    @FXML
    @ActionTrigger("buttons")
    private Label button;
    @FXML
    @ActionTrigger("checkbox")
    private Label checkbox;
    @FXML
    @ActionTrigger("combobox")
    private Label combobox;
    @FXML
    private JFXListView<Label> sideList;

    /**
     * init fxml when loaded.
     */
    @PostConstruct
    public void init() {
        Objects.requireNonNull(context, "context");
        FlowHandler contentFlowHandler = (FlowHandler) context.getRegisteredObject("ContentFlowHandler");
        FlowHandler sideMenuFlowHandler = (FlowHandler) context.getRegisteredObject("SideMenuFlowHandler");
        sideList.propagateMouseEventsToParent();
        sideList.getSelectionModel().selectedItemProperty().addListener((o, oldVal, newVal) -> {
            new Thread(()->{
                Platform.runLater(()->{
                    if (newVal != null) {
                        try {
                            contentFlowHandler.handle(newVal.getId());
                            sideMenuFlowHandler.handle(newVal.getId());
                        } catch (VetoException exc) {
                            exc.printStackTrace();
                        } catch (FlowException exc) {
                            exc.printStackTrace();
                        }
                    }
                });
            }).start();
        });
        Flow contentFlow = (Flow) context.getRegisteredObject("ContentFlow");
        bindNodeToController(button, ButtonController.class, contentFlow, contentFlowHandler);
        bindNodeToController(checkbox, CheckboxController.class, contentFlow, contentFlowHandler);
        bindNodeToController(combobox, ComboBoxController.class, contentFlow, contentFlowHandler);
        
        Flow sideMenuFlow = (Flow) context.getRegisteredObject("SideMenuFlow");
        bindNodeToClosedMenu(button, sideMenuFlow, sideMenuFlowHandler);
        bindNodeToClosedMenu(checkbox, sideMenuFlow, sideMenuFlowHandler);
        bindNodeToClosedMenu(combobox, sideMenuFlow, sideMenuFlowHandler);
    }

    private void bindNodeToController(Node node, Class<?> controllerClass, Flow flow, FlowHandler flowHandler) {
        flow.withGlobalLink(node.getId(), controllerClass);
    }
    
    private void bindNodeToClosedMenu(Node node, Flow flow, FlowHandler flowHandler) {
    	flow.withGlobalTaskAction(node.getId(), () -> {
    		JFXDrawer drawer = (JFXDrawer) context.getRegisteredObject("Drawer");
    		
    		drawer.close();
    	});
    }

}
