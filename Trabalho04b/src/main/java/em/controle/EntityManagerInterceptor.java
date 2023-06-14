package em.controle;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import servico.controle.JPAUtil;

import javax.persistence.EntityManager;
import java.lang.reflect.Method;

public class EntityManagerInterceptor implements MethodInterceptor {
    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        //  bota O entity Manager atual no em
        EntityManager em = JPAUtil.getEntityManager();
        // retorna com o invoke, passando o "em" e os "objects"(args)
        return method.invoke(em,objects);
    }
}
