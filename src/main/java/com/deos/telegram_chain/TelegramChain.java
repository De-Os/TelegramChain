package com.deos.telegram_chain;

import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(TelegramChain.MODID)
public class TelegramChain {
    public static final String MODID = "telegram_chain";

    public static final Logger LOGGER = LoggerFactory.getLogger(TelegramChain.MODID);

    public static TelegramBot TELEGRAM_BOT;

    public TelegramChain(){
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    public TelegramChain(FMLJavaModLoadingContext context) {
        registerConfig(context);
    }

    public void registerConfig(FMLJavaModLoadingContext context){
        context.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }
}
