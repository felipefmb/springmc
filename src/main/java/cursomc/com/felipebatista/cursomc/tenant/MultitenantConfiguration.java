package cursomc.com.felipebatista.cursomc.tenant;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@AutoConfigureAfter(PersistenceConfiguration.class)
@ComponentScan(basePackageClasses = MultitenantConfiguration.class)
public class MultitenantConfiguration {

}
