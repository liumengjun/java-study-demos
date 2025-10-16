
/**
 * Created by liumengjun on 2025-10-16.
 */
class FlexibleConstructorDemo {

    void main(String[] args) {
        IO.println(new Person("apple", 9));
//        IO.println(new Person("box", -1));
        IO.println(new Employee("cat", 29, "IT"));
//        IO.println(new Employee("dog", 9, "HR"));
    }

    class Person {

        String name;
        int age;

        Person(String name, int age) {
            if (age < 0)
                throw new IllegalArgumentException();
            this.name = name;
            this.age = age;
            show(); // 注意show实际执行时机
        }

        void show() {
            IO.println("Name: " + this.name + ", Age: " + this.age);
        }
    }

    class Employee extends Person {
        String officeID;

        Employee(String name, int age, String officeID) {

            if (age < 18 || age > 67)
                // Now fails fast!
                throw new IllegalArgumentException();
            this.officeID = officeID;
            // 后面才调用super()
            super(name, (age));
        }

        private static int verifyAge(int age) {
            if (age < 18 || age > 67)
                throw new IllegalArgumentException();
            return age;
        }

        @Override
        void show() {
            IO.println("Name: " + this.name + ", Age: " + this.age);
            IO.println("Office: " + this.officeID);
        }

    }
}
