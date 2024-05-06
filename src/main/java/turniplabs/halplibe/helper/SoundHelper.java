package turniplabs.halplibe.helper;

import net.minecraft.client.Minecraft;
import turniplabs.halplibe.HalpLibe;
import turniplabs.halplibe.util.DirectoryManager;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.Hashtable;


abstract public class SoundHelper {
    private static final Hashtable<String, String> fileCache = new Hashtable<>();
    public static final File appDirectory;
    public static final File soundDirectory;
    public static final File musicDirectory;
    public static final File streamingDirectory;
    public static final File caveMusicDirectory;
    static {
        if (!HalpLibe.isClient){
            appDirectory = null;
            soundDirectory = null;
            musicDirectory = null;
            streamingDirectory = null;
            caveMusicDirectory = null;
        } else {
            appDirectory = Minecraft.getAppDir("minecraft-bta");
            soundDirectory = new File(appDirectory.getAbsolutePath() + "/resources/mod/sound");
            musicDirectory = new File(appDirectory.getAbsolutePath() + "/resources/mod/music");
            streamingDirectory = new File(appDirectory.getAbsolutePath() + "/resources/mod/streaming");
            caveMusicDirectory = new File(appDirectory.getAbsolutePath() + "/resources/mod/cavemusic");
        }
    }

    /**
     * Place mod sounds in the <i>assets/modId/cavemusic/</i> directory for them to be seen.
     */
    public static void addCaveMusic(String MOD_ID, String soundSource){
        if (!HalpLibe.isClient) return;
        if (appDirectory == null) {
            HalpLibe.LOGGER.warn("Resource directory cannot be found, function is only intended to be ran on the Client!");
            return;
        }
        String destination = caveMusicDirectory.getPath();
        String source = (DirectoryManager.getCaveMusicDirectory(MOD_ID) + soundSource).replace("//", "/").trim();
        HalpLibe.LOGGER.info("File source: " + source);
        HalpLibe.LOGGER.info("File destination: " + destination);

        HalpLibe.LOGGER.info(extract(source, destination, soundSource) + " Added to sound directory");
    }
    /**
     * Place mod sounds in the <i>assets/modId/streaming/</i> directory for them to be seen.
     */
    public static void addStreaming(String MOD_ID, String soundSource){
        if (!HalpLibe.isClient) return;
        if (appDirectory == null) {
            HalpLibe.LOGGER.warn("Resource directory cannot be found, function is only intended to be ran on the Client!");
            return;
        }
        String destination = streamingDirectory.getPath();
        String source = (DirectoryManager.getStreamingDirectory(MOD_ID) + soundSource).replace("//", "/").trim();
        HalpLibe.LOGGER.info("File source: " + source);
        HalpLibe.LOGGER.info("File destination: " + destination);

        HalpLibe.LOGGER.info(extract(source, destination, soundSource) + " Added to sound directory");
    }
    /**
     * Place mod sounds in the <i>assets/modId/music/</i> directory for them to be seen.
     */
    public static void addMusic(String MOD_ID, String soundSource){
        if (!HalpLibe.isClient) return;
        if (appDirectory == null) {
            HalpLibe.LOGGER.warn("Resource directory cannot be found, function is only intended to be ran on the Client!");
            return;
        }
        String destination = musicDirectory.getPath();
        String source = (DirectoryManager.getMusicDirectory(MOD_ID) + soundSource).replace("//", "/").trim();
        HalpLibe.LOGGER.info("File source: " + source);
        HalpLibe.LOGGER.info("File destination: " + destination);

        HalpLibe.LOGGER.info(extract(source, destination, soundSource) + " Added to sound directory");
    }

    /**
     * Place mod sounds in the <i>assets/modId/sound/</i> directory for them to be seen.
     */
    public static void addSound(String MOD_ID, String soundSource){
        if (!HalpLibe.isClient) return;
        if (appDirectory == null) {
            HalpLibe.LOGGER.warn("Resource directory cannot be found, function is only intended to be ran on the Client!");
            return;
        }
        String destination = soundDirectory + ("/" + MOD_ID + "/").replace("//", "/");
        String source = (DirectoryManager.getSoundDirectory(MOD_ID) + soundSource).replace("//", "/").trim();
        HalpLibe.LOGGER.info("File source: " + source);
        HalpLibe.LOGGER.info("File destination: " + destination);

        HalpLibe.LOGGER.info(extract(source, destination, soundSource) + " Added to sound directory");
    }
    private static String extract(String jarFilePath, String destination, String soundSource){

        if(jarFilePath == null)
            return null;

        // See if we already have the file
        if(fileCache.contains(jarFilePath))
            return fileCache.get(jarFilePath);

        // Alright, we don't have the file, let's extract it
        try {
            // Read the file we're looking for
            InputStream fileStream = SoundHelper.class.getResourceAsStream(jarFilePath);

            // Was the resource found?
            if(fileStream == null)
                return null;

            File tempFile = new File(new File(destination), soundSource);
            tempFile.getParentFile().mkdirs();
            tempFile.delete();
            Files.createFile(tempFile.toPath());

            // Set this file to be deleted on VM exit
            tempFile.deleteOnExit();

            // Create an output stream to barf to the temp file
            OutputStream out = Files.newOutputStream(tempFile.toPath());

            // Write the file to the temp file
            byte[] buffer = new byte[1024];
            int len = fileStream.read(buffer);
            while (len != -1) {
                out.write(buffer, 0, len);
                len = fileStream.read(buffer);
            }

            // Store this file in the cache list
            fileCache.put(jarFilePath, tempFile.getAbsolutePath());

            // Close the streams
            fileStream.close();
            out.close();

            // Return the path of this sweet new file
            return tempFile.getAbsolutePath();

        } catch (IOException e) {
            HalpLibe.LOGGER.warn(e.toString());
            for (StackTraceElement element :e.getStackTrace()){
                HalpLibe.LOGGER.debug(element.toString());
            }
            return null;
        }
    }
}