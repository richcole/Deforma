package game.ui;

import game.events.AbstractEvent;

public class TreeNodeClickedEvent extends AbstractEvent {
	
	private TreeNode treeNode;
	private boolean checked;
	
	public TreeNodeClickedEvent(TreeNode treeNode, boolean checked) {
		super(treeNode);
		this.treeNode = treeNode;
		this.checked = checked;
	}

	public TreeNode getTreeNode() {
		return treeNode;
	}

	public boolean isChecked() {
		return checked;
	}
	
	

}
