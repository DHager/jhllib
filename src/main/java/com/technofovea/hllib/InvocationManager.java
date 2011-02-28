package com.technofovea.hllib;

import com.technofovea.hllib.methods.FullLibrary;
import com.technofovea.hllib.methods.ManagedCalls;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class InvocationManager implements InvocationHandler {

    private static final Logger logger = LoggerFactory.getLogger(InvocationManager.class);

    FullLibrary delegate;
    ManagedCallImpl lackey;

    InvocationManager(FullLibrary target) {
        super();
        this.delegate = target;
        lackey = new ManagedCallImpl(this.delegate);
    }

    @Override
    protected void finalize() throws Throwable {
        delegate.shutdown();
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (args == null) {
            args = new Object[0];
        }
        logger.trace("New invocation: "+method.getName());
        // Depending on what comes in, normally target the libarary instance or
        // else retarget to our managed call implementation.

        Object target = delegate;
        Object result = null;

        // Anything declared in ManagedLibrary or sub-interfaces is something we
        // must intercept and re-target.
        if (ManagedCalls.class.isAssignableFrom(method.getDeclaringClass())) {
            logger.trace("Is a managed call.");
            target = lackey;
        }else{
            logger.trace("Is not a managed call.");
        }


        try {
            boolean ok = preHook(proxy, method, args, target);
            if (!ok) {
                logger.trace("Pre-hook prevented method run");
                return null;
            }
            result = method.invoke(target, args);
            postHook(proxy, method, args, target, result);
        } catch (InvocationTargetException e) {
            throw e.getTargetException();
        } catch (Exception e) {
            throw new RuntimeException("unexpected invocation exception: " + e.getMessage(), e);
        } finally {
        }
        return result;
    }

    boolean preHook(Object proxy, Method method, Object[] args, Object target) {
 
            return true;
    }
    void postHook(Object proxy, Method method, Object[] args, Object target, Object result) {
        // Check bind, create, close, and delete for package
        if (method.getName().equals("createPackage") && (result instanceof HlPackage)) {
            logger.trace("Automatically rebinding package after creation");
            lackey.setCurrentPackage((HlPackage) result);
        } else if (method.getName().equals("bindPackage") && Boolean.TRUE.equals(result)) {
            HlPackage pkg = (HlPackage) args[0];
            lackey.addPackage(pkg);
            lackey.setCurrentPackage(pkg);

        }

    }
}
