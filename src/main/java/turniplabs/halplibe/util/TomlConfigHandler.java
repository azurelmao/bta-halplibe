package turniplabs.halplibe.util;

import net.fabricmc.loader.api.FabricLoader;
import turniplabs.halplibe.HalpLibe;
import turniplabs.halplibe.util.toml.Toml;
import turniplabs.halplibe.util.toml.TomlParser;

import java.io.*;

public class TomlConfigHandler {
    private static final String CONFIG_DIRECTORY = FabricLoader.getInstance().getGameDir().toString() + "/config/";
    private Toml defaults;
    private Toml config;
    private Toml rawParsed;
    private final String configFileName;
    private final File configFile;

    public TomlConfigHandler(String modID, Toml defaults) {
        this.configFileName = modID + ".cfg";
        this.configFile = new File(getFilePath());
        this.defaults = defaults;
        if (defaults.getComment().isPresent())
            this.config = new Toml(defaults.getComment().get());
        else this.config = new Toml();
        config.addMissing(defaults);
        create();
    }

    public TomlConfigHandler(String modID, Toml defaults, boolean create) {
        this.configFileName = modID + ".cfg";
        this.configFile = new File(getFilePath());
        this.defaults = defaults;
        if (defaults.getComment().isPresent())
            this.config = new Toml(defaults.getComment().get());
        else this.config = new Toml();
        config.addMissing(defaults);
        if(create) create();
    }

    //creates the actual config file
    public void create(){
        File configFile = new File(getFilePath());
        HalpLibe.LOGGER.info("Config file name: " + this.configFileName);
        HalpLibe.LOGGER.info("Config file path: " + configFile.getAbsolutePath());
        try {
            if (!configFile.exists()) {
                HalpLibe.LOGGER.info("Config file does not exist. Creating...");
                configFile.getParentFile().mkdirs();
                configFile.createNewFile();
                writeConfig();
            } else {
                // load only reads the entries in the file
                loadConfig();
                // ensure that new entries are written to the file
                writeConfig();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setDefaults(Toml defaults){
        this.defaults = defaults;
        config.addMissing(defaults);
    }

    public File getConfigFile() {
        return configFile;
    }

    public String getFilePath() {
        return CONFIG_DIRECTORY + configFileName;
    }

    public String getString(String key) {
        Object o = this.config.get(key);
        if (o == null) return null;
        return o.toString();
    }

    public int getInt(String key) {
        return this.config.get(key, Integer.class);
    }

    public long getLong(String key) {
        return this.config.get(key, Long.class);
    }

    public float getFloat(String key) {
        return this.config.get(key, Float.class);
    }

    public double getDouble(String key) {
        return this.config.get(key, Double.class);
    }

    public boolean getBoolean(String key) {
        return this.config.get(key, Boolean.class);
    }

    public void writeConfig() {
        File configFile = new File(getFilePath());

        // make sure the actual config has all the required entries
        config.merge(defaults);
        if (rawParsed != null) {
            // preserve undefined entries
            // used due to run config handler
            rawParsed.merge(true, config);
        } else rawParsed = config;

        // write the config
        try (OutputStream output = new FileOutputStream(configFile)) {
            output.write(rawParsed.toString().getBytes());
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadConfig() {
        File configFile = new File(getFilePath());
        // make sure the actual config has all the required entries
        config.merge(defaults);
        loadConfig(configFile, this.config);
    }

    private void loadConfig(File configFile, Toml properties) {
        try (InputStream input = new FileInputStream(configFile)) {
            // only loads the ones that it finds in the file
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            while (true) {
                byte[] buf = new byte[Math.max(2048, input.available())];
                int count = input.read(buf);
                if (count == -1) break;
                baos.write(buf, 0, count);
            }

            Toml parsed = TomlParser.parse(baos.toString());

            // TODO: system for specifying "greedy" categories?
            //       greedy categories would keep all entries that aren't sepcified in code but are specified in the config
            if (defaults.getComment().isPresent())  {
                rawParsed = new Toml(defaults.getComment().get());
                rawParsed.addMissing(parsed);
            } else rawParsed = parsed;

            properties.merge(true, rawParsed);

            input.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Toml getRawParsed() {
        return rawParsed;
    }
}