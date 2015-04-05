package servers;

public class AdjacentNode {
	long port;
	long nodeId;
	long mgmtport;
	String nodeName;
	String host;
	
	public long getPort() {
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
