package objectsharing;

class IntegerHolder {
    private static ThreadLocal<Integer> integerHolder = ThreadLocal.withInitial(() -> 0);

    static Integer get() {
        return integerHolder.get();
    }

    static void set(int val) {
        integerHolder.set(val);
    }
}
