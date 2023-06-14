package em.controle;

import net.sf.cglib.proxy.Enhancer;

import javax.persistence.EntityManager;
import java.util.MissingResourceException;

public class FabricadeEntityManager {

    public static  EntityManager getEntityManager() {

        EntityManager proxy = (EntityManager) Enhancer.create(EntityManager.class,new EntityManagerInterceptor());
        return proxy;
    }
}
