package game;

import game.mesh.CompiledMesh;
import game.mesh.CompiledMeshList;
import game.ui.TreeNode;
import game.ui.TreeNodeClickedEvent;
import game.ui.UI;

public class ModelController {

	public ModelController(CompiledMeshList meshList, UI ui) {
		TreeNode root = ui.newNode(meshList.toString());
		
		for(final CompiledMesh compiledMesh: meshList.getMeshList()) {
			TreeNode meshNode = ui.newNode(root, compiledMesh.getGeom().toString(), true);
			ui.onTreeNodeClicked(meshNode, (TreeNodeClickedEvent e) -> { treeNodeClicked(e, compiledMesh); });
		}
	}

	public void treeNodeClicked(TreeNodeClickedEvent e, CompiledMesh compiledMesh) {
		compiledMesh.setVisible(e.isChecked());
	}

}
