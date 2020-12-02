package de.verdox.skyislands.subsystems.islands.files;

import com.sk89q.worldedit.bukkit.fastutil.Hash;
import de.verdox.skyislands.Core;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

public class FileManager {

    public static FileManager instance;

    public static FileManager getInstance() {
        if(instance == null)
            instance = new FileManager();
        return instance;
    }

    private File schematicDir;
    private final Map<String, SchematicFile> cache;

    FileManager(){
        initDirectories();
        cache = new HashMap<>();
        try { loadSchematicFiles(); } catch (IOException e) { e.printStackTrace(); }
    }

    private void initDirectories(){
        this.schematicDir = new File(Core.getInstance().getDataFolder()+"\\schematics");
        if(!this.schematicDir.exists())
            this.schematicDir.mkdirs();
    }

    private void loadSchematicFiles() throws IOException {
        Core.getVCoreInstance().consoleMessage("Loading SchematicFiles");
        Files.walk(this.schematicDir.toPath(),1).forEach(path -> {
            if(path.equals(this.schematicDir.toPath()))
                return;

            Core.getVCoreInstance().consoleMessage("Found: "+path);

            File schematicFile = path.toFile();
            if(!schematicFile.getAbsolutePath().endsWith(".schematic") & !schematicFile.getAbsolutePath().endsWith(".schem"))
                return;

            String schematicName = schematicFile.getName().replace(".schematic","").replace(".schem","");
            SchematicFile schemFile = new SchematicFile(schematicName,schematicFile);
            Core.getVCoreInstance().consoleMessage("Trying to save schematic File "+schematicName);
            cache.put(schematicName,schemFile);
        });
    }

    public SchematicFile getFile(String identifier){
        return cache.get(identifier);
    }

    public boolean exist(String identifier){
        return cache.containsKey(identifier);
    }
}
