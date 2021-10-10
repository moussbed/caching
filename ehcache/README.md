# Caching 

 ## 1- Definition

Caching is a technique that involves the intermediate storage of data in very fast memory, usually. 
This means that this data can be made available much more quickly for subsequent requests 
since it does not have to be retrieved or recalculated from the primary and usually slower memory first.

Caching is particularly useful for the following scenarios:
  - The same data is requested again and again (so-called hot spots), which have to be loaded from the database anew with each request. This data can be cached in the main memory of the server application (RAM) or on the client (browser cache). These reduces access times and the number of data transfers since the server does not have to repeatedly request data from the database and send it to the client. 
  - Long-term or resource-intensive operations are often performed with specific parameters. Depending on the parameters, the result of the operation can be stored temporarily so that the server can send the result to the client without executing the operation.

## 2- Caching in Spring
Thanks to the implementation of JSR-107, some cache providers are fully compatible with the javax.cache API. Due to this compatibility, integration into Spring or Hibernate is very easy.

In Spring or Spring Boot it is very easy to add caching to an application. All you need to do is activate caching support via Annotation ***@EnableCaching***. As we are used to from Spring Boot, the entire caching infrastructure is configured for us.

Springs Caching Service is an abstraction and not an implementation. Therefore it is necessary to use a cache provider or cache implementation for caching. Spring supports a wide range of cache providers:
  - Ehcache 3 
  - Hazelcast
  - Infinispan
  - Couchbase
  - Redis 

A change of the cache provider has no effect on the existing code, as the developer only gets in touch with the abstract concepts.

If no cache provider is added, Spring Boot configures a very simple provider that caches in main memory using maps. This is sufficient for testing, but for applications in production, you should choose one of the above cache providers.

In this exchange we will use EhCache 3 and Hazelcast cache provider.

### 2-1 EhCache 
Ehcache is an open source, standards-based cache that boosts performance, offloads your database, and simplifies scalability. It's the most widely-used Java-based cache because it's robust, proven, full-featured, and integrates with other popular libraries and frameworks.

#### 2-1-1  Ehcache Caching Tiers
The memory areas supported by Ehcache include:
  - **On-Heap Store**: Uses the Java heap memory to store cache entries and shares the memory with the application. The cache is also scanned by the garbage collection. This memory is very fast, but also very limited.
  - **Off-Heap Store**: Uses the RAM to store cache entries. This memory is not subject to garbage collection. Still quite fast memory, but slower than the on-heap memory, because the cache entries have to be moved to the on-heap memory before they can be used.
  - **Disk Store**: Uses the hard disk to store cache entries. Much slower than RAM. It is recommended to use a dedicated SSD that is only used for caching.

#### 2-1-2  Ehcache Demo

In our demo project, we will use a three-tier cache with a disk store as an authority tier.

##### 2-1-2-1  Used Dependencies
For the Ehcache demo project we need the following dependencies in our Spring Boot based application:
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
<dependency>
   <groupId>org.springframework.boot</groupId>
   <artifactId>spring-boot-starter-cache</artifactId>
</dependency>
<dependency>
   <groupId>javax.cache</groupId>
   <artifactId>cache-api</artifactId>
</dependency>
<dependency>
   <groupId>org.ehcache</groupId>
   <artifactId>ehcache</artifactId>
   <version>3.7.1</version>
</dependency>
```
For caching we need spring-boot-starter-cache and cache-api dependency as well as the dependency ehcache as a cache provider.

##### 2-1-2-2  Enable Caching
To enable caching support in Spring Boot, we need a simple configuration class that must be annotated with @EnableCaching.

```java
@Configuration
@EnableCaching
public class EhcacheConfig {
}
```

##### 2-1-2-3  Ehcache Cache Configuration
Now the configuration of the Ehcache cache has to be done. The configuration is XML-based. We create the XML file ehcache.xml in the resource folder of our application.
- Cache Template

  First, we will define a cache template. This is especially advantageous if the application is to have more than one cache, but the configuration of the caches is largely the same. For our demo application it is conceivable, for example, that we want to cache the results of the circle area calculation and in another cache the results of a power calculation. For the cache template we use the following XML code:

```xml

<config
        xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance'
        xmlns='http://www.ehcache.org/v3'
        xsi:schemaLocation="
            http://www.ehcache.org/v3 
            http://www.ehcache.org/schema/ehcache-core-3.7.xsd">
  <! – Persistent cache directory – >
  <persistence directory="spring-boot-ehcache/cache"/>
  <! – Default cache template – >
  <cache-template name="default">
    <expiry>
      <ttl unit="hours">1</ttl>
    </expiry>
    <listeners>
      <listener>
        <class>main.java.com.mb.caching.config.CacheLogger</class>
        <event-firing-mode>ASYNCHRONOUS</event-firing-mode>
        <event-ordering-mode>UNORDERED</event-ordering-mode>
        <events-to-fire-on>CREATED</events-to-fire-on>
        <events-to-fire-on>EXPIRED</events-to-fire-on>
        <events-to-fire-on>EVICTED</events-to-fire-on>
      </listener>
    </listeners>
    <resources>
      <heap>1000</heap>
      <offheap unit="MB">10</offheap>
      <disk persistent="true" unit="MB">20</disk>
    </resources>
  </cache-template>
</config>
```

  In the **persistence tag**, we define the directory for a file-based cache on the hard disk (disk store). This is only the definition of the folder. Whether we really want to use a disk store or not will be configured later.

  In the **expiry tag**, we define a time to live (ttl) of 30 seconds. The time to live specifies how long a cache entry may remain in the cache independently of access. After the specified time has expired, the value is removed from the cache.

  It is also possible to define a time to idle (tti). The time to idle specifies how long the cache entry may exist in the cache without access. For example, if a value is not requested for more than 30 seconds, it is removed from the cache.

  In the listeners tag, we configure a CacheEventListener. The listener reacts to the following events:
       
    - A cache entry is placed in the cache(CREATED).
    - The validity of a cache entry has expired (EXPIRED).
    - A cache entry is evicted from the cache (EVICTED).
  The specified CacheLogger class only logs the occurred cache event on the console:
```java
public class CacheLogger implements CacheEventListener<Object, Object> {
  private final Logger LOG = LoggerFactory.getLogger(CacheLogger.class);
  @Override
  public void onEvent(CacheEvent<?, ?> cacheEvent) {
    LOG.info("Key: {} | EventType: {} | Old value: {} | New value: {}",
             cacheEvent.getKey(), cacheEvent.getType(), cacheEvent.getOldValue(), 
             cacheEvent.getNewValue());
  }
}
```
In the **resources tag**, we configure the tiers and capacities of our cache. We use a three-tier cache with a disk store as authority tier:
      
     - heap: For the on heap store we configure a capacity of 1,000 cache entries. This is the maximum number of entries before eviction starts.

     - offheap: For the off-heap store we configure a capacity of 10 MB.

     - disk: As disk cache, we configure 20 MB. Important: The disk cache must always have a higher memory capacity than the heap cache, otherwise the application throws an exception during application startup when parsing the XML file.

- Cache configuration

  Using the cache template we just created, we can now configure our cache. Thanks to the template we only have to define a name (alias) as well as the type of the cache key (key-type) and the type of the cached value (value-type):
```xml
<config ...>
    <! – Persistent cache directory – >
    ...
    <! – Default cache template – >
    ...
    <! – Cache configuration – >
    <cache alias="areaOfCircleCache" uses-template="default">
        <key-type>java.lang.Integer</key-type>
        <value-type>java.lang.Double</value-type>
    </cache>
</config>
```

- Wiring of ehcache.xml with application.properties

  Finally, we tell the application.properties file where our configuration file for Ehcache is located:
```properties
   spring.cache.jcache.config=classpath:ehcache.xml
```


##### 2-1-2-4  Caching Operations

- @Cacheable

Methods annotated with @Cacheable are not executed again if a value already exists in the cache for the cache key. If the value does not exist in the cache, then the method is executed and places its value in the cache.

We start our example with a simple service that calculates the area of a circle. The formula ***A = PI * radius²***   is used to calculate the area. The code is as follows:
```java
@Cacheable(value = "areaOfCircleCache", key = "#radius", condition = "#radius > 5")
public double areaOfCircle(int radius) {
  LOG.info("calculate the area of a circle with a radius of {}", radius);
  return Math.PI * Math.pow(radius, 2);
}
```
Each time this method is called with a radius greater than 5, the caching behavior is applied. This checks whether the method has already been called once for the specified parameter. If so, the result is returned from the cache and the method is not executed. If no, then the method is executed and the result is returned and stored in the cache.

- @CachePut

Now there is also the use case that we always want the method to be executed and its result to be placed in the cache. This is done using the @CachePut annotation, which has the same annotation parameters as @Cachable.

A possible scenario for using @CachePut is, for example, creating an entity object, as the following example shows:

```java
@CachePut(cacheNames = "studentCache", key = "#result.id")
public Student create(String firstName, String lastName, String courseOfStudies) {
  LOG.info("Creating student with firstName={}, lastName={} and courseOfStudies={}", 
           firstName, lastName, courseOfStudies);
  
  long newId = ID_CREATOR.incrementAndGet();
  Student newStudent = new Student(newId, firstName, lastName, courseOfStudies);      
  
  // persist in database
  return newStudent;
}
```

- @CacheEvict

A cache can become very large very quickly. The problem with large caches is that they occupy a lot of important main memory and mostly consist of stale data that is no longer needed.

To avoid inflated caches, you should, of course, have configured a meaningful eviction strategy. On the other hand, it is also possible to empty the cache based on requests. The following example shows how to remove all entries from the caches ***areaOfCircleCache*** and ***multiplyCache***.

```java
@CacheEvict(cacheNames = {"areaOfCircleCache", "multiplyCache"}, allEntries = true)
public void evictCache() {
  LOG.info("Evict all cache entries...");
}
```
