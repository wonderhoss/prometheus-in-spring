package horse.gargath.metricsexample;

import java.util.List;

import javax.sql.DataSource;

import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.spi.JdbiPlugin;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JdbiConfig {
    @Bean
    public Jdbi jdbi(final DataSource ds, final List<JdbiPlugin> jdbiPlugins, final List<RowMapper<?>> rowMappers) {
        final Jdbi jdbi = Jdbi.create("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1");
//          final Jdbi jdbi = Jdbi.create("jdbc:h2:file:./test.db");

jdbiPlugins.forEach(plugin -> jdbi.installPlugin(plugin));
        rowMappers.forEach(mapper -> jdbi.registerRowMapper(mapper));
        return jdbi;
    }
}