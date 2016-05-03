**Overview**

These are helper classes for some of our favourite test frameworks and
old good vanilla java as well. Use it as test dependency if you'll find 
it useful.

**Test frameworks dependencies**

* [AssertJ](https://github.com/joel-costigliola/assertj-core)

**Build status**

[![Build Status](https://travis-ci.org/nginate/commons-testing.svg?branch=master)](https://travis-ci.org/nginate/commons-testing)

**What's there in this bundle?**

* Conditions

AssertJ is great framework for making test assertions, but it requires
writing own conditions. These will allow to make long-chained assert
on a single test subject.

```java
    assertThat(obj)
        .isNotNull()
        .has(nullIn(Obj::getNullField))
        .has(empty(Obj::getCollectionField))
        ...
        .has(nonNullIn(Obj::getNonNullField))
```

* Unique value generation

In order to remove dependency on magic numbers in test, we often use 
just random values. But the problem is that we need test that are 
generating same output no matter how many times we run them. So the real 
goal for test value is to make it rather predictable and globally unique 
for whole build than just randomize each time.

```java
    Long long1 = uniqueLong();
    Long long2 = uniqueLong();
    
    assert long1 < long2;
    
    Short short1 = uniqueShort();
    
    assert short1 > 0;
    
    Date date1 = uniqueDate();
    Date date2 = uniqueDate();
    
    assert date2.isAfter(date1);
    assert date2.getTime() - date1.getTime() == 1000
```

**License**

<a href="http://www.wtfpl.net/"><img
       src="http://www.wtfpl.net/wp-content/uploads/2012/12/wtfpl-badge-4.png"
       width="80" height="15" alt="WTFPL" /></a>