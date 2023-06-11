package dao.controle;

import anotacao.PersistentContext;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import servico.controle.JPAUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class DAOInteceptor implements MethodInterceptor {
    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {

        Field[] campos = method.getDeclaringClass().getDeclaredFields();
        for (Field campo : campos) {
            if (campo.isAnnotationPresent(PersistentContext.class)) {

                try {
                    campo.setAccessible(true);
                    campo.set(o, JPAUtil.getEntityManager());
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        Object object = methodProxy.invokeSuper(o,objects);
        return object;
    }

}
