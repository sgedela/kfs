/*
 * The Kuali Financial System, a comprehensive financial management system for higher education.
 * 
 * Copyright 2005-2014 The Kuali Foundation
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.kuali.kfs.sys.suite;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.Arrays;

import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * This annotation marks test classes or methods as members of the listed test suites. This reverses the normal direction of the
 * reference, from the test to the suite.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target( { ElementType.TYPE, ElementType.METHOD })
public @interface AnnotationTestSuite {

    public Class<? extends Superclass>[] value();

    /**
     * Test suites using the enclosing annotation must extend this nested class. They should have a static method named "suite" that
     * IDEs can run for convenience. That method should return getSuite() or getNegativeSuite() on an instance of the suite class.
     */
    public static abstract class Superclass {

        protected boolean pointsToThisClass(AnnotationTestSuite annotation) {
            if (annotation != null) {
                if (Arrays.asList(annotation.value()).contains(this.getClass().asSubclass(Superclass.class))) {
                    return true;
                }
            }
            return false;
        }

        /**
         * @return the suite of all test classes or methods listing this (sub)class in a AnnotationTestSuite annotation.
         * @throws java.io.IOException if the directory containing the test class files cannot be scanned
         */
        protected TestSuite getSuite() throws Exception {
            TestSuiteBuilder.ClassCriteria classCriteria = new TestSuiteBuilder.ClassCriteria() {
                public boolean includes(Class<? extends TestCase> testClass) {
                    return pointsToThisClass(testClass.getAnnotation(AnnotationTestSuite.class));
                }
            };
            TestSuiteBuilder.MethodCriteria methodCriteria = new TestSuiteBuilder.MethodCriteria() {
                public boolean includes(Method method) {
                    return pointsToThisClass(method.getAnnotation(AnnotationTestSuite.class));
                }
            };
            TestSuite suite = TestSuiteBuilder.build(classCriteria, methodCriteria);
            nameSuite(suite);
            return suite;
        }

        private void nameSuite(TestSuite suite) {
            suite.setName(this.getClass().getName());
        }

        /**
         * @return the suite of all test methods (including those within test class sub-suites) which are not in the suite returned
         *         by {@link #getSuite()}.
         * @throws java.io.IOException if the directory containing the test class files cannot be scanned
         */
        protected TestSuite getNegativeSuite() throws Exception {
            TestSuiteBuilder.MethodCriteria negativeMethodCriteria = new TestSuiteBuilder.MethodCriteria() {
                public boolean includes(Method method) {
                    AnnotationTestSuite testClassAnnotation = method.getDeclaringClass().getAnnotation(AnnotationTestSuite.class);
                    return !pointsToThisClass(testClassAnnotation) && !pointsToThisClass(method.getAnnotation(AnnotationTestSuite.class));
                }
            };
            TestSuite suite = TestSuiteBuilder.build(TestSuiteBuilder.NULL_CRITERIA, negativeMethodCriteria);
            nameSuite(suite);
            return suite;
        }
    }
}
