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
