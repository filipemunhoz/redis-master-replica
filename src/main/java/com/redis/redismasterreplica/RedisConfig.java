package com.redis.redismasterreplica;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStaticMasterReplicaConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

import io.lettuce.core.ReadFrom;

@Configuration
@ConfigurationProperties(prefix = "redis")
public class RedisConfig {

    private RedisProperties master;
    private List<RedisProperties> replicas;

    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {
        LettuceClientConfiguration clientConfig = LettuceClientConfiguration.builder()
                .readFrom(ReadFrom.REPLICA_PREFERRED)
                .build();
        RedisStaticMasterReplicaConfiguration staticMasterReplicaConfiguration = new RedisStaticMasterReplicaConfiguration(master.getHost(), master.getPort());
        getReplicas().forEach(replica -> staticMasterReplicaConfiguration.addNode(replica.getHost(), replica.getPort()));
        return new LettuceConnectionFactory(staticMasterReplicaConfiguration, clientConfig);
    }

	public RedisProperties getMaster() {
		return master;
	}

	public void setMaster(RedisProperties master) {
		this.master = master;
	}

	public List<RedisProperties> getReplicas() {
		return replicas;
	}

	public void setReplicas(List<RedisProperties> replicas) {
		this.replicas = replicas;
	}
}