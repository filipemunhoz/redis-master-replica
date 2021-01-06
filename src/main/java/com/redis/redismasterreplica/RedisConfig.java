package com.redis.redismasterreplica;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStaticMasterReplicaConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

import io.lettuce.core.ReadFrom;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "redis")
public class RedisConfig {

    private RedisMasterProperties master;
    private List<RedisReplicaProperties> replicas;

    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {
        LettuceClientConfiguration clientConfig = LettuceClientConfiguration.builder()
                .readFrom(ReadFrom.REPLICA_PREFERRED)
                .build();
        final RedisStaticMasterReplicaConfiguration staticMasterReplicaConfiguration = new RedisStaticMasterReplicaConfiguration(master.getHost(), master.getPort());
        getReplicas().forEach(replica -> staticMasterReplicaConfiguration.addNode(replica.getHost(), replica.getPort()));
        return new LettuceConnectionFactory(staticMasterReplicaConfiguration, clientConfig);
    }

	@Getter
	@Setter
	private static class RedisMasterProperties {

	    private String host;
	    private int port;
	}
	
	@Getter
	@Setter
	private static class RedisReplicaProperties {

	    private String host;
	    private int port;
	}
}