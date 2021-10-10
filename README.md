# Caching

## Definition

Caching is a technique that involves the intermediate storage of data in very fast memory, usually.
This means that this data can be made available much more quickly for subsequent requests
since it does not have to be retrieved or recalculated from the primary and usually slower memory first.

Caching is particularly useful for the following scenarios:
- The same data is requested again and again (so-called hot spots), which have to be loaded from the database anew with each request. This data can be cached in the main memory of the server application (RAM) or on the client (browser cache). These reduces access times and the number of data transfers since the server does not have to repeatedly request data from the database and send it to the client.
- Long-term or resource-intensive operations are often performed with specific parameters. Depending on the parameters, the result of the operation can be stored temporarily so that the server can send the result to the client without executing the operation.

## Caching in Spring
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
