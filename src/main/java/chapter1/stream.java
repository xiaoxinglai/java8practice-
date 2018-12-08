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
