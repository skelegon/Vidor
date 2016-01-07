package org.steve.sulev;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.DirectoryIteratorException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/***
 * Class Utilities combines the utility methods, including tools for unit formatting, comparing and converting.
 * Here are also the tools for managing the config file and starting the VLC media player.
 */

public final class Utilities {
    private static PropertiesConfiguration config = new PropertiesConfiguration();
    private Utilities(){}

    // Loads the settings from config file. If no values defined, the deafult values are returned.
    public static Settings loadSettings(){
        config.setListDelimiter('|');
        Settings s = new Settings();
        try {
            config.load("vidor.config");
        } catch (ConfigurationException ex){
            ex.printStackTrace();
        }

        s.setVlcLocation(config.getString("vlclocation", ""));
        s.setThumbnailCount(config.getInt("thumbnailcount", 10));
        s.setThumbnailWidth(config.getInt("thumbnailwidth", 200));
        s.setThumbnailHighlightColor(config.getString("thumbnailhighlightcolor", ""));

        List<String> f = new ArrayList(Arrays.asList(config.getStringArray("folders")));
        if(!f.get(0).trim().isEmpty()) { //Bug fix - Empty folder check
            s.setFolders(f);
        }

        return s;
    }

    // Saves the settings to config file.
    public static void saveSettings(Settings settings) {
        config.setProperty("vlclocation", settings.getVlcLocation());
        config.setProperty("thumbnailcount", settings.getThumbnailCount());
        config.setProperty("thumbnailwidth", settings.getThumbnailWidth());
        config.setProperty("thumbnailhighlightcolor", settings.getThumbnailHighlightColor());
        config.setProperty("folders", String.join("|", settings.getFolders()));

        try {
            config.save("vidor.config");
        } catch (ConfigurationException ex){
            ex.printStackTrace();
        }
    }

    // Lists the files in the selected directories that match the predefined extensions.
    public static List<File> getFilesFromDisk(Path p) {
        List<File> result = new ArrayList<>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(p, "*.{wmv,mp4,flv,mov,avi,mkv}")) {
            for (Path entry : stream){
                result.add(new File(entry.getFileName().toString(), Files.size(entry), null));
            }
        } catch (IOException | DirectoryIteratorException ex){
            ex.printStackTrace();
        }
        return result;
    }

    // Formats seconds in hh:mm:ss
    public static String formatSeconds(int s){
        return String.format("%02d:%02d:%02d", s / 3600, (s % 3600) / 60, s % 60);
    }

    // Converts the byte array to javafx image format
    public static Image bytesToImage(byte[] bytes) throws IOException{
        return SwingFXUtils.toFXImage(ImageIO.read(new ByteArrayInputStream(bytes)), null);
    }

    // Starts the VLC media player with the video at the selected time.
    public static void startVid(String parameters, String vlcpath) {
        String[] args = parameters.split(",");
        try {
            new ProcessBuilder(vlcpath,
                    args[0],
                    args[1],
                    args[2]
            ).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Formats the file size to two places after the decimal
    public static String floatForm (double d)
    {
        return new DecimalFormat("#.##").format(d);
    }

    // converts the byte values to human readable format.
    // Reference: XXX, 9.04.2014, http://stackoverflow.com/a/22967188
    public static String bytesToHuman (long size)
    {
        if(size == 0) return "";

        long Kb = 1024;
        long Mb = Kb * 1024;
        long Gb = Mb * 1024;
        long Tb = Gb * 1024;
        long Pb = Tb * 1024;
        long Eb = Pb * 1024;

        if (size <  Kb)                 return floatForm(size) + " byte";
        if (size >= Kb && size < Mb)    return floatForm((double)size / Kb) + " Kb";
        if (size >= Mb && size < Gb)    return floatForm((double)size / Mb) + " Mb";
        if (size >= Gb && size < Tb)    return floatForm((double)size / Gb) + " Gb";
        if (size >= Tb && size < Pb)    return floatForm((double)size / Tb) + " Tb";
        if (size >= Pb && size < Eb)    return floatForm((double)size / Pb) + " Pb";
        if (size >= Eb)                 return floatForm((double)size / Eb) + " Eb";

        return "???";
    }

    // Compares the strings in the list with the given strings while ignoring the case sensitivity
    public static boolean containsCaseInsensitive(List<String> list, String strToCompare)
    {
        for(String str:list)
        {
            if(str.equalsIgnoreCase(strToCompare))
            {
                return true;
            }
        }
        return false;
    }

    //Opens default browser and loads this projects github page.
    public static void openGit(){
        try{
            Desktop.getDesktop().browse(new URL("https://github.com/skelegon/Vidor").toURI());
        } catch (URISyntaxException | IOException ex){
            ex.printStackTrace();
        }
    }
}
