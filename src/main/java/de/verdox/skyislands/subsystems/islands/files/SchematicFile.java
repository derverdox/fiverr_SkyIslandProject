package de.verdox.skyislands.subsystems.islands.files;

import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class SchematicFile {
    private final String name;
    private final File file;
    private final Clipboard clipboard;

    SchematicFile(String name, File file){
        this.name = name;
        this.file = file;
        this.clipboard = loadClipboard();
        System.out.println("Loaded: "+this.name);
    }

    public File getFile() {
        return file;
    }

    public String getName() {
        return name;
    }

    public Clipboard getClipboard() {
        return clipboard;
    }

    private Clipboard loadClipboard (){

        ClipboardFormat format = ClipboardFormats.findByFile(file);
        try{
            ClipboardReader reader = format.getReader(new FileInputStream(file));
            Clipboard clipboard = reader.read();
            reader.close();
            return clipboard;
        }
        catch (IOException e){
            e.printStackTrace();
            return null;
        }
    }
}
