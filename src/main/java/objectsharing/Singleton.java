package objectsharing;

import annotation.ThreadSafe;

@ThreadSafe
public class Singleton {
    private static volatile Singleton instance;//在Java5.0及更高版本中将instance声明为volatile类型才可以保证线程安全

    private Singleton() {
    }

    public static Singleton getInstance() {
        if (instance == null) {//instance非volatile时，存在可见性问题，线程可能看到一个仅被部分构造的对象
            synchronized (Singleton.class) {
                if (instance == null) {
                    instance = new Singleton();
                }
            }
        }
        return instance;
    }
}
