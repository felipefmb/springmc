package cursomc.com.felipebatista.cursomc.tenant;

import org.springframework.context.ApplicationContext;
import org.springframework.lang.Nullable;

import java.util.Objects;

public class ContextProvider {

    private static ApplicationContext context;

    ContextProvider() {
    }

    /**
     * Prove acesso ao contexto padrão da aplicação, possibilitando a localização e inicialização de objetos.
     *
     * @return o contexto padrão da aplicação
     */
    public static ApplicationContext get() {
        Objects.requireNonNull(context, "Contexto não inicializado");
        return context;
    }

    /**
     * Inicializa e retorna a mesma instancia do objeto fornecido
     *
     * @param bean objeto a ser inicializado
     * @param <T>  tipo do objeto
     * @return o objeto inicializado
     */
    public static <T> T autowire(T bean) {
        get().getAutowireCapableBeanFactory().autowireBean(bean);
        return bean;
    }

    public static Object getBean(String name) {
        return context.getBean(name);
    }

    public static <T> T getBean(String name, @Nullable Class<T> requiredType) {
        return context.getBean(name, requiredType);
    }

    public static Object getBean(String name, Object... args) {
        return context.getBean(name, args);
    }

    public static <T> T getBean(Class<T> requiredType) {
        return context.getBean(requiredType);
    }

    public static <T> T getBean(Class<T> requiredType, Object... args) {
        return context.getBean(requiredType, args);
    }

    public static void setContext(ApplicationContext context) {
        ContextProvider.context = context;
    }

    public static boolean hasContext() {
        return context != null;
    }
}