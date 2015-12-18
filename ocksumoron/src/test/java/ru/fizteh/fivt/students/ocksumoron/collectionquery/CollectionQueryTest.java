package ru.fizteh.fivt.students.ocksumoron.collectionquery;

import com.sun.jdi.InvocationException;
import ru.fizteh.fivt.students.ocksumoron.collectionquery.impl.Tuple;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;


import static ru.fizteh.fivt.students.ocksumoron.collectionquery.Aggregates.avg;
import static ru.fizteh.fivt.students.ocksumoron.collectionquery.Aggregates.count;
import static ru.fizteh.fivt.students.ocksumoron.collectionquery.CollectionQueryTest.Student.student;
import static ru.fizteh.fivt.students.ocksumoron.collectionquery.Conditions.rlike;
import static ru.fizteh.fivt.students.ocksumoron.collectionquery.OrderByConditions.asc;
import static ru.fizteh.fivt.students.ocksumoron.collectionquery.OrderByConditions.desc;
import static ru.fizteh.fivt.students.ocksumoron.collectionquery.Sources.list;
import static ru.fizteh.fivt.students.ocksumoron.collectionquery.impl.FromStmt.from;

import org.junit.Assert;
import org.junit.Test;


public class CollectionQueryTest {

    @Test
    public void test1() throws InvocationTargetException,
            NoSuchMethodException, InstantiationException, IllegalAccessException {
        Iterable<Statistics> statistics =
                from(list(
                        student("ivanov", LocalDate.parse("1986-08-06"), "494"),
                        student("sidorov", LocalDate.parse("1986-08-06"), "495"),
                        student("smith", LocalDate.parse("1986-08-06"), "495"),
                        student("petrov", LocalDate.parse("2006-08-06"), "494")))
                        .select(Statistics.class, Student::getGroup, count(Student::getGroup), avg(Student::age))
                        .where(rlike(Student::getName, ".*ov").and(s -> s.age() > 20))
                        .groupBy(Student::getGroup)
                        .having(s -> s.getCount() > 0)
                        .orderBy(asc(Statistics::getGroup), desc(Statistics::getCount))
                        .limit(100)
                        .union()
                        .from(list(student("ivanov", LocalDate.parse("1985-08-06"), "494")))
                        .selectDistinct(Statistics.class, s->"all", count(s -> 1), avg(Student::age))
                        .execute();
        Iterable<Tuple<String, String>> mentorsByStudent =
                from(list(student("ivanov", LocalDate.parse("1985-08-06"), "494")))
                        .join(list(new Group("494", "mr.sidorov")))
                        .on((s, g) -> Objects.equals(s.getGroup(), g.getGroup()))
                        .select(sg -> sg.getFirst().getName(), sg -> sg.getSecond().getMentor())
                        .execute();
        Assert.assertEquals(statistics.toString(),
                "[Statistics{group='494', count=1, age=29.0}\n" +
                        ", Statistics{group='495', count=1, age=29.0}\n" +
                        ", Statistics{group='all', count=1, age=30.0}\n" +
                        "]");
        Assert.assertEquals(mentorsByStudent.toString(),
                "[Tuple{first=ivanov, second=mr.sidorov}" +
                        "]");
    }

    public static class Student {
        private final String name;

        private final LocalDate dateOfBirth;

        private final String group;

        public String getName() {
            return name;
        }

        public Student(String name, LocalDate dateOfBirth, String group) {
            this.name = name;
            this.dateOfBirth = dateOfBirth;
            this.group = group;
        }

        public LocalDate getDateOfBirth() {
            return dateOfBirth;
        }

        public String getGroup() {
            return group;
        }

        public Double age() {
            return (double) ChronoUnit.YEARS.between(getDateOfBirth(), LocalDateTime.now());
        }

        public static Student student(String name, LocalDate dateOfBirth, String group) {
            return new Student(name, dateOfBirth, group);
        }

        @Override
        public String toString() {
            StringBuilder result = new StringBuilder().append("Student{");
            if (group != null) {
                result.append("group='").append(group).append('\'');
            }
            if (name != null) {
                result.append(", name=").append(name);
            }
            if (dateOfBirth != null) {
                result.append(", age=").append(dateOfBirth);
            }
            result.append("}\n");
            return result.toString();
        }
    }

    public static class Group {
        private final String group;
        private final String mentor;

        public Group(String group, String mentor) {
            this.group = group;
            this.mentor = mentor;
        }

        public String getGroup() {
            return group;
        }

        public String getMentor() {
            return mentor;
        }

        @Override
        public String toString() {
            StringBuilder result = new StringBuilder().append("Student{");
            if (group != null) {
                result.append("group='").append(group).append('\'');
            }
            if (mentor != null) {
                result.append(", name=").append(mentor);
            }
            result.append("}\n");
            return result.toString();
        }
    }

    public static class Statistics {

        private final String group;
        private final Integer count;
        private final Double age;

        public String getGroup() {
            return group;
        }

        public Integer getCount() {
            return count;
        }

        public Statistics(String group) {
            this.group = group;
            this.count = null;
            this.age = null;
        }

        public Statistics(String group, Integer count) {
            this.group = group;
            this.count = count;
            this.age = null;
        }

        public Statistics(String group, Integer count, Double age) {
            this.group = group;
            this.count = count;
            this.age = age;
        }

        @Override
        public String toString() {
            StringBuilder result = new StringBuilder().append("Statistics{");
            if (group != null) {
                result.append("group='").append(group).append('\'');
            }
            if (count != null) {
                result.append(", count=").append(count);
            }
            if (age != null) {
                result.append(", age=").append(age);
            }
            result.append("}\n");
            return result.toString();
        }
    }
}