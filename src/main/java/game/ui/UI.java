package game.ui;

import java.util.function.Consumer;

import game.Context;
import game.Registration;
import game.events.ApplicationCloseEvent;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBoxTreeItem;
import javafx.scene.control.CheckBoxTreeItem.TreeModificationEvent;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.CheckBoxTreeCell;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class UI extends Application {

  private CheckBoxTreeItem<String> rootItem;
  private Context context;

  public UI() {
    super();
  }

  @Override
  public void start(Stage primaryStage) {
    context = new Context(this);

    Button btn = new Button();
    btn.setText("Say 'Hello World'");
    btn.setOnAction((ActionEvent event) -> {
      System.out.println("Hello World!");
    });

    rootItem = new CheckBoxTreeItem<String>("Root");

    TreeView<String> treeView = new TreeView<String>(rootItem);
    treeView.setCellFactory(CheckBoxTreeCell.<String> forTreeView());

    StackPane root = new StackPane();
    root.getChildren().add(btn);

    root.getChildren().add(treeView);

    Scene scene = new Scene(root, 300, 250);

    primaryStage.setTitle("Hello World!");
    primaryStage.setScene(scene);
    primaryStage.show();

    context.getEventBus().onEventType(context, (ApplicationCloseEvent e) -> {
      Platform.exit();
    }, ApplicationCloseEvent.class);
  }

  public TreeNode newNode(String nodeText) {
    return newNode(rootItem, nodeText, false);
  }

  public TreeNode newNode(TreeNode parentNode, String nodeText,
      boolean isChecked) {
    return newNode(parentNode.treeItem, nodeText, isChecked);
  }

  private TreeNode newNode(CheckBoxTreeItem<String> parentNode,
      String nodeText, boolean isChecked) {
    TreeNode node = new TreeNode(new CheckBoxTreeItem<String>(nodeText));
    node.treeItem.setSelected(isChecked);
    node.treeItem.addEventHandler(
        CheckBoxTreeItem.<String> checkBoxSelectionChangedEvent(),
        (TreeModificationEvent<String> event) -> {
          boolean isItemChecked = node.treeItem.isSelected();
          context.getEventBus().post(
              new TreeNodeClickedEvent(node, isItemChecked));
        });
    parentNode.getChildren().add(node.treeItem);
    return node;
  }

  public Registration<TreeNodeClickedEvent> onTreeNodeClicked(TreeNode node,
      Consumer<TreeNodeClickedEvent> action) {
    return context.getEventBus().onEventType(node, action,
        TreeNodeClickedEvent.class);
  }

  public static void main(String[] args) {
    launch(args);
  }
}
