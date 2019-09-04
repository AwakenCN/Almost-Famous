# Spring Aop中的动态代理

    spring AOP的底层实现由俩种方式： 一种是JDK动态代理，另一种是CGLib动态代理。
    区别主要是jdk是代理接口，而cglib是代理类。
    
## 动态代理的前世今生
    
```
    自Java 1.3以后，Java提供了动态代理技术，允许开发者在运行期创建接口的代理实例，后来这项技术被用到了Spring的很多地方。
JDK动态代理主要涉及java.lang.reflect包下边的两个类：Proxy和InvocationHandler。其中，InvocationHandler是一个接口，可以通过实现该接口定义横切逻辑，并通过反射机制调用目标类的代码，动态地将横切逻辑和业务逻辑贬值在一起。
JDK动态代理的话，他有一个限制，就是它只能为接口创建代理实例，而对于没有通过接口定义业务方法的类，如何创建动态代理实例哪？答案就是CGLib。
CGLib采用底层的字节码技术，全称是：Code Generation Library，CGLib可以为一个类创建一个子类，在子类中采用方法拦截的技术拦截所有父类方法的调用并顺势织入横切逻辑。
```
        
## JDK动态代理

#### JDK动态代理原理详解

1. jdk的动态代理调用了Proxy.newProxyInstance(ClassLoader loader,Class<?>[] interfaces,InvocationHandler h) 方法。
2. 通过该方法生成字节码，动态的创建了一个代理类，interfaces参数是该动态类所继承的所有接口，而继承InvocationHandler 接口的类则是实现在调用代理接口方法前后的具体逻辑，下边是具体的实现：

#### JDK动态代理的源码分析

```java
/**   
 *    
 * JDK动态代理类   
 *    
 *   
 */    
public class JDKDynamicProxy implements InvocationHandler {    
    
    private Object targetObject;//需要代理的目标对象    
    
    public Object newProxy(Object targetObject) {//将目标对象传入进行代理    
        this.targetObject = targetObject;     
        return Proxy.newProxyInstance(targetObject.getClass().getClassLoader(),    
                targetObject.getClass().getInterfaces(), this);//返回代理对象    
    }    
    
    public Object invoke(Object proxy, Method method, Object[] args)//invoke方法    
            throws Throwable {    
        before();
        Object ret = null;      // 设置方法的返回值    
        ret  = method.invoke(targetObject, args);       //invoke调用需要代理的方法
        after();
        return ret;    
    }    
    
    private void before() {//方法执行前   
        System.out.println("方法执行前 !");    
    }    
    private void after() {//方法执行后    
        System.out.println("方法执行后");    
    }    
}  
```

## CGLib动态代理

#### CGLib动态代理原理详解

1. MethodProxy#invoke 对被调用的方法进行拦截
2. CglibAopProxy.DynamicAdvisedInterceptor#intercept 实现CGLib的动态代理实现

#### AOP中CGLib动态代理的源码分析

```java
package org.springframework.cglib.proxy;

/**
 * 当调用被拦截的方法时，
 * 由{org.springframework.cglib.proxy.Enhancer}生成的类将此对象
 * 传递给注册的{org.springframework.cglib.proxy.MethodInterceptor}对象。
 * 它既可以用于调用原始方法，也可以用于在相同类型的不同对象上调用相同的方法。
 */
public class MethodProxy {
    
    /**
     * 在相同类型的不同对象上调用原始方法。
     * @param obj 
     * @param args 
     * @throws Throwable 
     * org.springframework.cglib.proxy.MethodInterceptor#intercept
     */
    public Object invoke(Object obj, Object[] args) throws Throwable {
        try {
            // 初始化
            init();
            // DI 依赖注入
            FastClassInfo fci = fastClassInfo;
            // CGLib 动态代理
            return fci.f1.invoke(fci.i1, obj, args);
        }
        catch (InvocationTargetException ex) {
            throw ex.getTargetException();
        }
        catch (IllegalArgumentException ex) {
            if (fastClassInfo.i1 < 0)
                throw new IllegalArgumentException("Protected method: " + sig1);
            throw ex;
        }
    }
}

```        


```java
package org.springframework.aop.framework;

class CglibAopProxy implements AopProxy, Serializable {
    
    /**
     * 通用AOP回调。条件是当目标是动态的或代理没有被冻结。
     */
    private static class DynamicAdvisedInterceptor implements MethodInterceptor, Serializable {
    
        private final AdvisedSupport advised;
    
        public DynamicAdvisedInterceptor(AdvisedSupport advised) {
            this.advised = advised;
        }
    
        /**
         * CGLib 动态代理的实现
         * 
         * @author  noseparte
         * @date  2019/9/4 15:28
         * @Description
         */
        @Override
        @Nullable
        public Object intercept(Object proxy, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
            Object oldProxy = null;
            boolean setProxyContext = false;
            Object target = null;
            TargetSource targetSource = this.advised.getTargetSource();
            try {
                if (this.advised.exposeProxy) {
                    // 如果需要，使调用可用。
                    oldProxy = AopContext.setCurrentProxy(proxy);
                    setProxyContext = true;
                }
                // 尽可能晚到，以最小化我们“拥有”目标的时间，以防它来自一个池……
                target = targetSource.getTarget();
                Class<?> targetClass = (target != null ? target.getClass() : null);
                List<Object> chain = this.advised.getInterceptorsAndDynamicInterceptionAdvice(method, targetClass);
                Object retVal;
                // 对目标的反射调用。
                if (chain.isEmpty() && Modifier.isPublic(method.getModifiers())) {
                    // 我们可以跳过创建方法调用:直接调用目标。注意，最终的调用程序必须是InvokerInterceptor，
                    // 所以我们知道它只对目标执行反射操作，没有热交换或花哨的代理。
                    Object[] argsToUse = AopProxyUtils.adaptArgumentsIfNecessary(method, args);
                    retVal = methodProxy.invoke(target, argsToUse);
                }
                else {
                    // 我们需要创建一个方法调用…
                    retVal = new CglibMethodInvocation(proxy, target, method, args, targetClass, chain, methodProxy).proceed();
                }
                retVal = processReturnType(proxy, target, method, retVal);
                return retVal;
            }
            finally {
                if (target != null && !targetSource.isStatic()) {
                    targetSource.releaseTarget(target);
                }
                if (setProxyContext) {
                    // 恢复旧的代理。
                    AopContext.setCurrentProxy(oldProxy);
                }
            }
        }
    }
}
```