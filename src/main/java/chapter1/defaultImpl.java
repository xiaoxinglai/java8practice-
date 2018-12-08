package chapter1;

public class defaultImpl implements defaultInterface {

    public static void main(String[] args) {
        defaultImpl d=new defaultImpl();
        d.test();
        //输出 接口里面的实现类
    }

    //java8中加入默认方法 主要是为了支持库设计师
    // 这种只是为了帮助程序改进
    //比如说某个接口，如果新增了一个方法，然后它的所有实现类都必须实现这个接口
    //当这个接口是被很多子类实现的时候，将是一个灾难，所有子类都需要改动
    //使用default 关键字修饰之后，就是接口的默认方法 所有子类都可以使用
}
