# Volatile Variables

> Refernce: Brian Goetz's [Java Concurrency in Practice ](https://www.amazon.com/Java-Concurrency-Practice-Brian-Goetz/dp/0321349601)

Java语言提供了一种稍弱的同步机制，即 `volatile` 变量，用来确保将变量的更新操作通知到其他线程。当把变量声明为 `volatile` 类型后，编译器与运行时都会注意到这个变量是共享的，因此不会将该变量上的操作与其他内存一起重排序。`volatile` 变量不会被缓存在寄存器或者对其他处理器不可见的地方，因此读取 `volatile` 类型的变量时总会返回最新写入的值。

`volatile` 变量对可见性的影响比 `volatile` 变量本身更为重要。当线程A首先写入一个 `volatile` 变量且线程B随后读取该变量时，在写入 `volatile` 变量之前对A可见的所有变量的值，在B读取了 `volatile` 变量后，对B也是可见的。从内存可见性的角度来看，写入 `volatile` 变量相当于退出同步代码块，而读取 `volatile` 变量就相当于进入同步代码块。

`volatile` 变量的正确使用方式包括：

*   确保它们自身状态的可见性；
*   确保它们所引用对象的状态的可见性；
*   标识一些重要的程序生命周期事件的发生（例如，初始化或关闭）。

[此处](./src/main/java/objectsharing/CountSleep.java)的代码给出了 `volatile` 变量的一种典型用法：检查某个状态标记以判断是否退出循环。

加锁机制既可以确保可见性又可以确保原子性，而 `volatile` 变量只能确保可见性。

当且仅当满足以下所有条件时，才应该使用 `volatile` 变量：

*   对变量的写入操作不依赖变量的当前值，或者能确保只有单个线程更新变量的值。
*   该变量不会与其他状态变量一起纳入不变性条件中。
*   在访问变量时不需要加锁。

> 以下内容引用自 <https://www.cnblogs.com/zhengbin/p/5654805.html>
>
> 当一个变量定义为 `volatile` 之后，将具备两种特性：
>
> *   保证此变量对所有的线程的可见性。“可见性”指当一个线程修改了这个变量的值，`volatile` 保证了新值能立即同步到主内存，以及每次使用前立即从主内存刷新。但普通变量做不到这点，普通变量的值在线程间传递均需要通过主内存来完成。
>
> *   禁止指令重排序优化。有 `volatile` 修饰的变量，赋值后多执行了一个“`load addl $0x0, (%esp)`”操作，这个操作相当于一个内存屏障（指令重排序时不能把后面的指令重排序到内存屏障之前的位置），只有一个CPU访问内存时，并不需要内存屏障；（什么是指令重排序：是指CPU采用了允许将多条指令不按程序规定的顺序分开发送给各相应电路单元处理）。

# 锁分段

> Refernce: Brian Goetz's [Java Concurrency in Practice ](https://www.amazon.com/Java-Concurrency-Practice-Brian-Goetz/dp/0321349601)

对一组独立对象上的锁进行分解即为锁分段。

# Java线程池饱和策略

> Reference: https://www.baeldung.com/java-rejectedexecutionhandler

通过传递一个 `RejectedExecutionException` 对象给执行器的构造器，我们可以修改执行器的饱和策略。

## 中止策略（Abort Policy）

中止策略是线程池的默认策略。中止策略使执行器抛出一个 `RejectedExecutionException` 异常。

## 调度者运行策略（Caller-Runs Policy）

该策略使调度者线程自己执行该任务。

## 丢弃策略（Discard Policy）

该策略在新任务提交失败时静默地丢弃新任务。

## 丢弃最老任务策略（Discard-Oldest Policy）

该策略先删除队列头中的任务，再重新提交新任务。

[这里](./src/main/java/threadpool/test/SaturationPolicyTest.java)的代码展示了四种策略的行为。