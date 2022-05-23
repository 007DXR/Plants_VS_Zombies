package core.game;
import com.alibaba.fastjson.*;
import core.State;
import core.Constants;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import core.component.GameMap;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.*;
class c extends Constants {}

public class Level extends State {
    private JSONObject gameInfo;
    private JSONObject persist;
    private int mapYLen;
    private int backgroundType;
    private GameMap map;
    private JSONObject mapData;
    /// work in map directory
    private Path rootDir = Paths.get("resources/data/map");
    private List<Path> pathManager;
    private File levelFile;
    
    public Level() {
        super();
        pathManager = new LinkedList<Path>();
        try {
			Files.walkFileTree(rootDir, new FindJavaVisitor(pathManager));
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
    }
    /// 初始化
    public void startUp(double currentTime, JSONObject persist) {
        gameInfo = persist;
        this.persist = persist;
        gameInfo.remove(c.CURRENT_TIME);
        gameInfo.put(c.CURRENT_TIME, currentTime);
        mapYLen = c.GRID_Y_LEN;
        map = new GameMap(c.GRID_X_LEN, mapYLen);
        mapData = new JSONObject();
        loadMap();
//        setupBackgroud();
//        initState();
    }
    /// 读入map文件信息
    public void loadMap() {
        Path filePath = pathManager.get((int)gameInfo.get(c.LEVEL_NUM));
        String data = readJsonFile(filePath.toString());
        mapData = JSONObject.parseObject(data);
    }
    public void setupBackgroud() {
    	int img_index = (int)mapData.get(c.BACKGROUND_TYPE);
    	
    }
    public void initState() {
        
    }
    public static void main() {
    	Level level = new Level();
    	level.loadMap();
    }
    /// 传入json文件名，传出String代表json文件内容
    private static String readJsonFile(String fileName) {
    	String jsonStr = "";
    	try {
	    	File jsonFile = new File(fileName);
	    	FileReader fileReader = new FileReader(jsonFile);
	    	Reader reader = new InputStreamReader(new FileInputStream(jsonFile),"utf-8");
	    	int ch = 0;
	    	StringBuffer sb = new StringBuffer();
	    	while ((ch = reader.read()) != -1) {
	    	sb.append((char) ch);
    	}
	    	fileReader.close();
	    	reader.close();
	    	jsonStr = sb.toString();
	    	return jsonStr;
    	} catch (IOException e) {
	    	e.printStackTrace();
	    	return null;
    	}
    }
}

class FindJavaVisitor extends SimpleFileVisitor<Path>{
    private List<Path> result;
    public FindJavaVisitor(List<Path> result){
        this.result = result;
    }
    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs){
        if(file.toString().endsWith(".json")){
            result.add(file.getFileName());
        }
        return FileVisitResult.CONTINUE;
    }
}
