# java8practice-
# java8练习

## 章节一 java8入门

### 使用lambda优化代码
```

/**
 * java 8
 *  方法引用 lambda表达式   流和默认方法
 *  传递代码
 */
public class chapter1 {

  
    //例如一个查找指定颜色为绿色要求的苹果的方法

    public static List<Apple> filterGreenApples(List<Apple> inventory){
        List<Apple> result=new ArrayList<Apple>();
        for (Apple apple : inventory) {
            if ("green".equals(apple.getColor())){ //条件为筛选颜色绿色的苹果
                result.add(apple);
            }

        }
        return  result;
    }


    //但是 如果 下次 出现了新的需求，要筛选出重量大于150g的苹果
    //又要写一个这样的方法 百分之80的代码都是重复的 只有一小部分条件不同
    public static List<Apple> filterWeightApples(List<Apple> inventory){
        List<Apple> result=new ArrayList<Apple>();
        for (Apple apple : inventory) {
            if (apple.getWeight()>150){ //条件为筛选重量大于150的苹果
                result.add(apple);
            }

        }
        return  result;
    }


    //上面那种实现 是项目中最常见的，我就见过一个项目 代码重复率高达60%的


    //优化方法
    //我们如果只写一个方法，但是把中间的条件代码作为参数 传递进去 不就行了吗？

    //判断条件单独抽出来做成函数 如下

    //判断是否是绿色苹果的代码
    public static boolean isGreenApple(Apple apple){
        return "green".equals(apple.getColor());
    }

    //判断是否满足重量要求的代码
    public static boolean isWeightApple(Apple apple){
        return apple.getWeight()>150;
    }

    //要将以上两段代码  作为参数传递给方法
    //首先 实现一个接口Predicate  该接口定义了一个test方法
    public interface Predicate<T>{
        boolean test(T t);
    }

    //然后重构刚刚那个筛选苹果的方法
    //java8之前 这种写法的用法，是要自己去实现Predicate<T>的实现类 然后重写其中的test的方法实现，把筛选逻辑写进去
    //这样每新增一个筛选逻辑 就要新增一个筛选条件的实现类
    //或者写成一个匿名内部类，对于只用一次的方法来说
    public static List<Apple> filterApples(List<Apple> inventory,Predicate<Apple> p){
        List<Apple> result=new ArrayList<Apple>();
        for (Apple apple : inventory) {
            if (p.test(apple)){ //使用p中的test方法筛选苹果，test的实现 由我们自己定义
                result.add(apple);
            }
        }
        return  result;
    }

    public static void main(String[] args) {

        //java8之前 这种写法的用法，是要自己去实现Predicate<T>的实现类 然后重写其中的test的方法实现，把筛选逻辑写进去
        //比如说 使用了匿名类的情况下
        List<Apple> inventory =new ArrayList<Apple>();
        filterApples(inventory,new Predicate(){
            public boolean test(Object o) {
                return  ((Apple)o).getWeight()>150;
            }
        });
        //java8之前 这种方式也是有的，比如说compare接口，就是要自己去实现一个
        
        //java8里面 :: 表示 方法引用
        //java8版本之后  使用方法引用
        filterApples(inventory,chapter1::isGreenApple); //原理就是自动生成了一个内部类 然后用isGreenApple重写了test方法


        //java 8版本之后 还可以用lambda来写，本质也是一种内部类   (参数)->返回值
        filterApples(inventory,(Apple a)->"green".equals(a.getColor()));  //这种写法 比之前的所有都方便
    }


     class Apple {

        private String color;//颜色
        private Integer weight; //重量

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }

        public Integer getWeight() {
            return weight;
        }

        public void setWeight(Integer weight) {
            this.weight = weight;
        }


    }

}


```

### 使用流筛选数据
```
package chapter1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;

public class stream {
    //比如说 你需要在一个列表中 筛选出所以重量大于150的苹果，然后按照颜色分组
    //按常规的筛选写法 就是在循环里面 迭代筛选

    public static void main(String[] args) {
        List<Apple> appleList = new ArrayList<>();

        //常规写法
        Map<String, List<Apple>> AppMap = new HashMap<>();
        for (Apple apple : appleList) {
            if (apple.getWeight() > 150) { //如果重量大于150
                if (AppMap.get(apple.getColor()) == null) { //该颜色还没分类
                    List<Apple> list = new ArrayList<>(); //新建该颜色的列表
                    list.add(apple);//将苹果放进去列表
                    AppMap.put(apple.getColor(),list);//将列表放到map中
                }else { //该颜色分类已存在
                    AppMap.get(apple.getColor()).add(apple);//该颜色分类已存在，则直接放进去即可
                }
            }
        }

        //如上方式 就可以筛选出来所有的150克大小以上的苹果，并按颜色分类



        //方式二 使用java8提供的流api实现 这种叫内部迭代
        Map<String, List<Apple>> AppMap2=appleList.stream().filter((Apple a)->a.getWeight()>150) //筛选出大于150的
                .collect(groupingBy(Apple::getColor)); //按颜色分组  最后得到map
        

    }


    class Apple {

        private String color;//颜色
        private Integer weight; //重量

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }

        public Integer getWeight() {
            return weight;
        }

        public void setWeight(Integer weight) {
            this.weight = weight;
        }


    }
}

```

### 并行流以及效率对比

```
package chapter1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;

public class parallelStream {

    //java8支持并行处理数据
    //并行就是 同时执行，传统的java程序，只能利用一个cpu ，如果电脑如果有八个cpu
    //比如说八核计算机，传统的java程序一次只能利用一个 ，这样就浪费了
    //可以使用多线程，这样提高效率，但是多线程程序编写需要谨慎 而且麻烦
    //一些数据处理，可以用java8的并行流来处理 原生支持利用计算机的所有的cpu


    //并行流 普通流，以及java普通迭代方式的时间效率比较


    public static void main(String[] args) {
        //初始化测试数据
        List<Apple> appleList = initAppleList();

        long startFor = System.currentTimeMillis();
        //常规写法
        Map<String, List<Apple>> AppMap = new HashMap<>();
        for (Apple apple : appleList) {
            if (apple.getWeight() > 150) { //如果重量大于150
                if (AppMap.get(apple.getColor()) == null) { //该颜色还没分类
                    List<Apple> list = new ArrayList<>(); //新建该颜色的列表
                    list.add(apple);//将苹果放进去列表
                    AppMap.put(apple.getColor(), list);//将列表放到map中
                } else { //该颜色分类已存在
                    AppMap.get(apple.getColor()).add(apple);//该颜色分类已存在，则直接放进去即可
                }
            }
        }
        long endFor = System.currentTimeMillis();
        System.out.println("普通迭代分类结束：用时:" + (endFor - startFor));


        long startStream = System.currentTimeMillis();
        //方式二 使用java8提供的流api实现 这种叫内部迭代
        Map<String, List<Apple>> AppMap2 = appleList.stream().filter((Apple a) -> a.getWeight() > 150) //筛选出大于150的
                .collect(groupingBy(Apple::getColor)); //按颜色分组  最后得到map
        long endStream = System.currentTimeMillis();
        System.out.println("普通流分类结束：用时:" + (endStream - startStream));


        //方式三 并行流
        //方式二 使用java8提供的流api实现 这种叫内部迭代
        long startPara = System.currentTimeMillis();
        Map<String, List<Apple>> AppMap3 = appleList.parallelStream().filter((Apple a) -> a.getWeight() > 150) //筛选出大于150的
                .collect(groupingBy(Apple::getColor)); //按颜色分组  最后得到map
        long endPara = System.currentTimeMillis();
        System.out.println("并行流分类结束：用时:" + (endPara - startPara));

        //1千个apple对象的时候
//        普通迭代分类结束：用时:3
//        普通流分类结束：用时:86
//        并行流分类结束：用时:6

//        1万个apple对象的时候
//        最终结果
//        普通迭代分类结束：用时:4
//        普通流分类结束：用时:80
//        并行流分类结束：用时:8
//
//        10万个apple对象的时候
//        最终结果
//       普通迭代分类结束：用时:16
//        普通流分类结束：用时:100
//        并行流分类结束：用时:87

//        100万个apple对象的时候
//        最终结果
//        普通迭代分类结束：用时:52
//        普通流分类结束：用时:184
//        并行流分类结束：用时:75

        //        1000万个apple对象的时候
//        普通迭代分类结束：用时:467
//        普通流分类结束：用时:547
//        并行流分类结束：用时:382

        //总结：使用lambda之后，运行效率变低了 ，1万条记录以下的时候 普通迭代和使用lambda大概有20倍的差距，并行流的差距为2倍，普通流和并行流的差距有10倍
        //然后随着数据量的上升 普通迭代和并行相距不大 ，继续上升数据流，并行流效率将比普通迭代和普通流快
        //因此 并行流适合处理大数据的情况下

    }


    //初始化测试数据
    public static List<Apple> initAppleList() {
        List<Apple> apples = new ArrayList<>();
        for (int i = 0; i < 1000000; i++) { //随机生成颜色和重量
            Apple apple = new Apple();
            int rad = (int) (Math.random() * 300);
            if (rad % 2 == 1) {
                apple.setColor("green");
            } else {
                apple.setColor("yellow");
            }
            apple.setWeight(rad);
            apples.add(apple);
        }
        return apples;
    }


    static class Apple {

        private String color;//颜色
        private Integer weight; //重量

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }

        public Integer getWeight() {
            return weight;
        }

        public void setWeight(Integer weight) {
            this.weight = weight;
        }
    }
}

```


### java8的接口默认方法

```aidl

public interface defaultInterface {

    default void test(){
        System.out.println("接口里面的实现类");
    }

}

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


```

