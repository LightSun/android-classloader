package com.heaven7.android.classloader.app.javaassist;


/*mport javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.LoaderClassPath;

public class JATest {

    public void test() {
        try {
            ClassPool pool = ClassPool.getDefault();
            pool.appendClassPath(new LoaderClassPath(JATest.class.getClassLoader()));
            pool.insertClassPath(new ClassClassPath(TestUtil.class));
            pool.insertClassPath(new ClassClassPath(IHello.class));

            //TODO error findUrl
            CtClass helloClass = pool.get(IHello.class.getName());
            CtClass a = pool.makeClass("com.heaven7.test.javaassist.Hello");
            a.addInterface(helloClass);
            a.addMethod(CtMethod.make("public String hello(String in){" +
                    "return TestUtil.hello(in);" +
                    "}", a));
            System.out.println(a.toString());
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}*/
