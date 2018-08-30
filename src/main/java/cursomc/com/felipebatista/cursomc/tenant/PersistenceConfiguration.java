package cursomc.com.felipebatista.cursomc.tenant;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = PersistenceConfiguration.class)
public class PersistenceConfiguration {
    
}
