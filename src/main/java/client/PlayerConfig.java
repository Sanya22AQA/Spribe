package client;

import org.aeonbits.owner.Config;

@Config.Sources("classpath:config.properties")
public interface PlayerConfig extends Config {
    @Key("baseUrl")
    String baseUrl();

    @Key("loginOfSupervisor")
    String loginOfSupervisor();
}
