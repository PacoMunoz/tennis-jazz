package es.pmg.tennisjazz.config;

import java.time.Duration;

import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;

import org.hibernate.cache.jcache.ConfigSettings;
import io.github.jhipster.config.JHipsterProperties;

import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.*;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache =
            jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(
            CacheConfigurationBuilder.newCacheConfigurationBuilder(Object.class, Object.class,
                ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                .build());
    }

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(javax.cache.CacheManager cacheManager) {
        return hibernateProperties -> hibernateProperties.put(ConfigSettings.CACHE_MANAGER, cacheManager);
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            createCache(cm, es.pmg.tennisjazz.repository.UserRepository.USERS_BY_LOGIN_CACHE);
            createCache(cm, es.pmg.tennisjazz.repository.UserRepository.USERS_BY_EMAIL_CACHE);
            createCache(cm, es.pmg.tennisjazz.domain.User.class.getName());
            createCache(cm, es.pmg.tennisjazz.domain.Authority.class.getName());
            createCache(cm, es.pmg.tennisjazz.domain.User.class.getName() + ".authorities");
            createCache(cm, es.pmg.tennisjazz.domain.Tournament.class.getName());
            createCache(cm, es.pmg.tennisjazz.domain.Tournament.class.getName() + ".groups");
            createCache(cm, es.pmg.tennisjazz.domain.TournamentGroup.class.getName());
            createCache(cm, es.pmg.tennisjazz.domain.TournamentGroup.class.getName() + ".rounds");
            createCache(cm, es.pmg.tennisjazz.domain.TournamentGroup.class.getName() + ".players");
            createCache(cm, es.pmg.tennisjazz.domain.Player.class.getName());
            createCache(cm, es.pmg.tennisjazz.domain.Player.class.getName() + ".visitorMatches");
            createCache(cm, es.pmg.tennisjazz.domain.Player.class.getName() + ".localMatches");
            createCache(cm, es.pmg.tennisjazz.domain.Player.class.getName() + ".groups");
            createCache(cm, es.pmg.tennisjazz.domain.Round.class.getName());
            createCache(cm, es.pmg.tennisjazz.domain.Round.class.getName() + ".matchs");
            createCache(cm, es.pmg.tennisjazz.domain.Match.class.getName());
            createCache(cm, es.pmg.tennisjazz.domain.TournamentGroup.class.getName() + ".rankings");
            createCache(cm, es.pmg.tennisjazz.domain.Player.class.getName() + ".rankings");
            createCache(cm, es.pmg.tennisjazz.domain.Ranking.class.getName());
            // jhipster-needle-ehcache-add-entry
        };
    }

    private void createCache(javax.cache.CacheManager cm, String cacheName) {
        javax.cache.Cache<Object, Object> cache = cm.getCache(cacheName);
        if (cache != null) {
            cm.destroyCache(cacheName);
        }
        cm.createCache(cacheName, jcacheConfiguration);
    }
}
