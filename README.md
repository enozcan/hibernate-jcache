# Using Jcache SPI for using Hazelcast as L2C for Hibernate

This is an alternative approach to use Hazelcast as caching provider for Hibernate.
The plugin can be found [here](www.github.com/hazelcast/hazelcast/hibernate5)

## About Jcache

[JCache (JSR 107)](https://www.javadoc.io/doc/javax.cache/cache-api/1.1.1) is a standard caching API for Java. 
It provides an API for applications to be able to create and work with in-memory cache of objects. Hibernate has 
started to support Jcache implementations for L2C with version 5.2. Using only a CachingProvider implementation,
L2C can be used for SessionFactory objects in Hibernate. 

## Integrating Hazelcast

You can see the difference between the modes [here](https://hazelcast.zendesk.com/hc/en-us/articles/115004441586-What-s-the-difference-between-client-server-vs-embedded-topologies-)

* P2P Mode

Hazelcast Instance along with your Hibernate application.

- Low latency to get / put data to Hazelcast cache regions.
- Scalability is not independent from the application itself.

* Client Mode

- Relatively higher latency to get / put data to Hazelcast cache regions.
- Can be scaled independent from the application. 
- Data resides in L2C can be available even a SessionFactory is closed. So this makes L2C available
across SessionFactories.

 

