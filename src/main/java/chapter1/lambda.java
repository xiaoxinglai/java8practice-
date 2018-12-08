package chapter1;

import java.util.ArrayList;
import java.util.List;

/**
 * java 8
 *  方法引用 lambda表达式   流和默认方法
 *  传递代码
 */
public class lambda {

    //java8里面 :: 表示 方法引用

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

        //java8版本之后  使用方法引用
        filterApples(inventory, lambda::isGreenApple); //原理就是自动生成了一个内部类 然后用isGreenApple重写了test方法


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
