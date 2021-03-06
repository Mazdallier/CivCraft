package alexiil.mods.lib;

import java.io.File;
import java.lang.ref.WeakReference;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ConfigAccess {
    private static final String CATEGORY = "general";	// Configuration.CATAGORY_GENERAL
    
    // TODO: Change to minecraft forge's Configuration class
    public final Configuration cfg;
    // public final Config cfg;
    public final WeakReference<AlexIILMod> mod;
    
    public ConfigAccess(File file, AlexIILMod mod) {
        // cfg = new Config(file, mod);
        cfg = new Configuration(file);
        cfg.load();
        this.mod = new WeakReference<AlexIILMod>(mod);
        FMLCommonHandler.instance().bus().register(this);
    }
    
    @SubscribeEvent public void onConfig(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (mod.get().meta.modId.equals(event.modID))
            saveAll();
    }
    
    public void saveAll() {
        if (cfg.hasChanged())
            cfg.save();
    }
    
    public Property getProp(String key, boolean defaultValue) {
        return cfg.get(CATEGORY, key, defaultValue);
    }
    
    public Property getProp(String key, int defaultValue) {
        return cfg.get(CATEGORY, key, defaultValue);
    }
    
    public Property getProp(String key, byte defaultValue) {
        return cfg.get(CATEGORY, key, defaultValue);
    }
    
    public Property getProp(String key, double defaultValue) {
        return cfg.get(CATEGORY, key, defaultValue);
    }
    
    public Property getProp(String key, short defaultValue) {
        return cfg.get(CATEGORY, key, defaultValue);
    }
    
    public Property getProp(String key, String defaultValue) {
        return cfg.get(CATEGORY, key, defaultValue);
    }
}
