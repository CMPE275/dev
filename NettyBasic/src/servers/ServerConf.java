package servers;

import java.util.ArrayList;

public class ServerConf {
	
	int port;
	int nodeId;
	int mgmtport;
	String nodeName;
	ArrayList<AdjacentNode> adjacentNodes;

	public ArrayList<AdjacentNode> getAdjacentNodes() {
		return adjacentNodes;
	}

	public void setAdjacentNodes(ArrayList<AdjacentNode> adjacentNodes) {
		this.adjacentNodes = adjacentNodes;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getNodeId() {
		return nodeId;
	}

	public void setNodeId(int nodeId) {
		this.nodeId = nodeId;
	}

	public int getMgmtport() {
		return mgmtport;
	}

	public void setMgmtport(int mgmtport) {
		this.mgmtport = mgmtport;
	}

	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}
	
}
