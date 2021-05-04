package co.za.bonk;

import java.io.File;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class JSONDatabase extends Database {

    JSONObject jsonObject;
    

    public JSONDatabase() {
        DiscordPaperPlugin plugin = DiscordPaperPlugin.getInstance();

        File databaseFile = new File(plugin.getDataFolder(), "database.json");
        if(!databaseFile.exists()) {
            databaseFile.getParentFile().mkdirs();
            plugin.saveResource("database.json", false);
        }

        try {
            FileReader fileReader = new FileReader(databaseFile);
            JSONParser jsonParser = new JSONParser();
            this.jsonObject = (JSONObject) jsonParser.parse(fileReader);
        } catch (Exception e) {
            plugin.getLogger().warning(e.toString());
        }
    }

    @Override
    public String NewFromDiscord(String discordName){

        String discordHash = Hashing.hashString(discordName);
        String uniqueHash = Hashing.hashString(discordName + Hashing.randomHex(16));

        //update JSON database:
        JSONObject registerObject = (JSONObject) this.jsonObject.get("register");
        
        if (!registerObject.containsKey(uniqueHash)) {
            JSONObject userObject = new JSONObject();
            userObject.put("discordName", discordName);
            userObject.put("discordHash", discordHash);
            registerObject.put(uniqueHash, userObject);
        }
        
        this.uploadNew();

        return uniqueHash;
    }

    @Override
    public int Register(String uniqueHash, String minecraftName, String minecraftUUID) {

        String minecraftHash = Hashing.hashString(minecraftName);
        
        JSONObject register = (JSONObject) this.jsonObject.get("register");
        JSONObject dTM = (JSONObject) this.jsonObject.get("discord_to_minecraft");
        JSONObject mTD = (JSONObject) this.jsonObject.get("minecraft_to_discord");

        if (register.containsKey(uniqueHash)) {
            JSONObject userRegister = (JSONObject) register.get(uniqueHash);

            String discordName = (String) userRegister.get("discordName");
            String discordHash = (String) userRegister.get("discordHash");

            JSONObject userDTM = new JSONObject();
            JSONObject userMTD = new JSONObject();

            userDTM.put("minecraftName", minecraftName);
            userDTM.put("minecraftUUID", minecraftUUID);

            userMTD.put("discordName", discordName);

            dTM.put(discordHash, userDTM);
            mTD.put(minecraftHash, userMTD);
            register.remove(uniqueHash);

            jsonObject.put("discord_to_minecraft", dTM);
            jsonObject.put("minecraft_to_discord", mTD);
            jsonObject.put("register", register);

            this.uploadNew();

            //succesfull
            return 0;
        }       
        //player not found on register list 
        return 1;
    }

    private void uploadNew() {
        DiscordPaperPlugin plugin = DiscordPaperPlugin.getInstance();

        try {
            File databaseFile = new File(plugin.getDataFolder(), "database.json");
            Files.write(Paths.get(databaseFile.toString()), this.jsonObject.toString().getBytes());
        } catch (Exception e) {
            e.getStackTrace();
        }
    }
    
}
