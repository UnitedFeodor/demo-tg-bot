package com.example.mydemobot.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;

import java.util.List;

@Configuration
@PropertySource("classpath:application-dev.properties")
@PropertySource("classpath:secret.properties")
@Data
@Slf4j
public class BotProperties {

    @Value("${bot.name}")
    String botName;

    List<BotCommand> commandList = List.of(
            new BotCommand("/start","start"),
            new BotCommand("/mydata","get data"),
            new BotCommand("/help","see help"),
            new BotCommand("/settings","configure"),
            new BotCommand("/mydata","see your data"),
            new BotCommand("/deletedata","delete your data")
    );

    @Bean
    @Qualifier("botToken")
    public String botToken( @Value("${bot.token}") String token){
        log.info("Token is {}",token);
        log.info("botName is {}",botName);
        return token;
    }
}
