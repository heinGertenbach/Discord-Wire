package co.za.bonk;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class JSONDatabase extends Database {

    JSONObject jsonObject;
    

    public JSONDatabase() {
        DiscordPaperPlugin plugin = DiscordPaperPlugin.getInstance();

        File databaseFile = new File(plugin.getDataFolder(), "secrets.json");
        if(!databaseFile.exists()) {
            databaseFile.getParentFile().mkdirs();
            plugin.saveResource("database.json", false);
        }

        try {
            FileReader fileReader = new FileReader(plugin.getDataFolder().getAbsolutePath()+"database.json");
            JSONParser jsonParser = new JSONParser();
            this.jsonObject = (JSONObject) jsonParser.parse(fileReader);
        } catch (Exception e) {
            e.getStackTrace();
        }
    }


    public String newFromDiscord(String username) throws Exception{

        String hashUsername = Hashing.hashString(username);
        String uniqueHash = Hashing.hashString(username + Hashing.randomHex(16));

        //update JSON database:
        JSONObject dTM = (JSONObject) jsonObject.get("discord_to_minecraft");
        
        if (!dTM.containsKey(hashUsername)) {
            JSONObject userObject = new JSONObject();
            userObject.put("UniqueHash", hashUsername);
            dTM.put("UserHash", userObject);
        }
        

        return uniqueHash;
    }

    public int Update(String hash, String key, String value){

        return 0;
    }

    public int Update(String hash, String[] keys, String[] values){

        return 0;
    }

    private void uploadNew() {
        DiscordPaperPlugin plugin = DiscordPaperPlugin.getInstance();

        try {
            Files.write(Paths.get(plugin.getDataFolder() + "database.json"), jsonObject.toString().getBytes());
        } catch (Exception e) {
            e.getStackTrace();
        }
    }
    
}
