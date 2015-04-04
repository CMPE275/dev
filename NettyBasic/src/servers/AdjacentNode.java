package servers;

public class AdjacentNode {
	int port;
	int nodeId;
	int mgmtport;
	String nodeName;
	String host;
	
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public long getNodeId() {
		return nodeId;
	}
	public void setNodeId(int nodeId) {
		this.nodeId = nodeId;
	}
	public long getMgmtport() {
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
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	
	
}
