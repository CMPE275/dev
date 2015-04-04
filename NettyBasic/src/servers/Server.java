package servers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

//import org.codehaus.jackson.JsonGenerationException;
//import org.codehaus.jackson.map.JsonMappingException;
//import org.codehaus.jackson.map.ObjectMapper;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Server {

	public static ServerConf parseConf(File conf){

		ServerConf serverConf = new ServerConf();

		try {
			FileReader fileReader = new FileReader(conf);
			JSONParser jsonParser = new JSONParser();
			JSONObject jsonObject = (JSONObject) jsonParser.parse(fileReader);

		
			serverConf.setPort(Integer.parseInt(jsonObject.get("port").toString()));
			serverConf.setNodeId(Integer.parseInt(jsonObject.get("nodeId").toString()));
			serverConf.setMgmtport(Integer.parseInt(jsonObject.get("mgmtPort").toString()));
			serverConf.setNodeName(jsonObject.get("nodeName").toString());
			JSONArray jsonArray = (JSONArray) jsonObject.get("adjacentNodes");

			Iterator i = jsonArray.iterator();

			serverConf.adjacentNodes = new ArrayList<AdjacentNode>();

			int j = 0;
			while(i.hasNext()){
				AdjacentNode adjacentNode = new AdjacentNode();
				serverConf.adjacentNodes.add(adjacentNode);
				JSONObject innerObject = (JSONObject) i.next();
				serverConf.adjacentNodes.get(j).setHost((String)innerObject.get("host"));
				serverConf.adjacentNodes.get(j).setMgmtport(Integer.parseInt(innerObject.get("mgmtPort").toString()));
				serverConf.adjacentNodes.get(j).setNodeId(Integer.parseInt(innerObject.get("nodeId").toString()));
				serverConf.adjacentNodes.get(j).setNodeName((String)innerObject.get("nodeName"));
				serverConf.adjacentNodes.get(j).setPort(Integer.parseInt(innerObject.get("port").toString()));

				j++;
			}			

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return serverConf;

	}

	
	

}
