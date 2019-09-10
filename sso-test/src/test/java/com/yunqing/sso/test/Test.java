package com.yunqing.sso.test;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j
public class Test {

    public static void main(String[] args) throws Exception {

        List<Person> list = Collections.synchronizedList(new ArrayList<>());
        List<Person> vector = new Vector<>();

        List<Person> linkedList = new LinkedList<>();
        list.add(new Person("yq", 11));
        list.add(new Person("11", 11));
        list.add(new Person("12", 11));

        /**
         * java8遍历
         */
        list.forEach(System.out::println);
        list.forEach(e -> System.out.println(e));
        /**
         * java8之前遍历
         */
        for (Person p : list) {
            System.out.println(p);
        }

        Map<String,Object> map = new HashMap<>();
        map.put("name", "yq");
        map.put("age", 11);
        /**
         * java8遍历
         */
        map.forEach((key, value) -> System.out.println("key = " + key + ",value = " + value));
        /**
         * java8之前遍历
         */
        for (String key : map.keySet()) {
            String value = map.get(key).toString();
            System.out.println("key = " + key + ",value = " + value);
        }



        Person person = new Person();
        person.setAge(11);
        person.setName("yq");


        //调用方法传入person对象

        test(person);
    }

    /**
     * java8 Optional的优雅使用方法
     * @param p
     * @return
     * @throws Exception
     */
    public static String test(Person p) throws Exception {
        /**
         * 以前的写法
         */
        if(p != null){
            String name = p.getName();
            if(name != null){
                return name.toUpperCase();
            }else{
                return null;
            }
        }else{
            return null;
        }

        /**
         * java8的写法更加优雅
         */
    }

    public static String test1(Person p) throws Exception {

        Optional<Person> op = Optional.ofNullable(p);

        /**
         * java8的写法更加优雅
         */
        return op.map(e -> e.getName())
                .map(s -> s.toUpperCase())
                .orElse(null);
    }
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class Person{

    private String name;
    private int age;
}
