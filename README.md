# Using Jcache SPI for using Hazelcast as L2C for Hibernate

This is an alternative approach of using `Hazelcast-hibernate` plugin to use Hazelcast as caching provider for Hibernate.
The plugin can be found [here](www.github.com/hazelcast/hazelcast/hibernate5)

## About Jcache

[JCache (JSR 107)](https://www.javadoc.io/doc/javax.cache/cache-api/1.1.1) is a standard caching API for Java. 
It provides an API for applications to be able to create and work with in-memory cache of objects. Hibernate has 
started to support Jcache implementations for L2C with version 5.2. Using only a CachingProvider implementation,
L2C can be used for SessionFactory objects in Hibernate. 

## Integrating Hazelcast

Include `hibernate-jcache` dependency in your program. The version must be same with
the `hibernate-core` version. 
        <dependency>
            <groupId>org.hibernate</groupId>
            <artifactId>hibernate-jcache</artifactId>
            <version>5.4.3.Final</version>
        </dependency>

### Hibernate Config

For Hibernate 5.2.x:
```xml
<property name="hibernate.cache.region.factory_class">org.hibernate.cache.jcache.JCacheRegionFactory</property>
```

For Hibernate 5.3.0 and higher:
```xml
    <property name="hibernate.cache.region.factory_class">jcache</property>
```
### Hazelcast Config


`CLIENT`: You can connect to an existing Hazelcast cluster and store the cache data there. Then you can scale the cluster
up or down free from your app. Data resides in L2C can be available even a SessionFactory is closed. So this makes L2C available
across SessionFactories. 
            
`EMBEDDED`: A new Hazelcast instance is created when your application is run. This instance cannot be scaled up or 
down unless you run another Hibernate application with the same server config. Latency of communicating with 
Hazelcast Cluster will be lower. 
           

- Using Native Client
```xml
<property name="hibernate.javax.cache.provider">com.hazelcast.client.cache.impl.HazelcastClientCachingProvider</property>
<property name="hibernate.javax.cache.uri">hazelcast-client.xml</property>
```

- Using Embedded Hazelcast Instance
```xml
<property name="hibernate.javax.cache.provider">com.hazelcast.cache.impl.HazelcastServerCachingProvider</property>
<property name="hibernate.javax.cache.uri">hazelcast.xml</property>
```


You can see the difference between client and embedded version [here](https://hazelcast.zendesk.com/hc/en-us/articles/115004441586-What-s-the-difference-between-client-server-vs-embedded-topologies-)

 ## Configuration
 
 ## etc. 

