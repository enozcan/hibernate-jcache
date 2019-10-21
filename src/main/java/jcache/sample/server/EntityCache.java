package jcache.sample.server;

import com.hazelcast.core.Hazelcast;
import jcache.sample.server.entity.Item;
import jcache.sample.server.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class EntityCache {

    public static void main(String[] args){

        Session session1, session2, session3, session4;
        Transaction tx1, tx2, tx3, tx4;
        Item item;


        // Store an hazelcast.hibernate.jcache.Entity.Item with id = 1
        // L2C put is expected
        session1 = HibernateUtil.getSession();
        tx1 = session1.getTransaction();
        tx1.begin();
        session1.save(new Item(1,"New Hazelcast Item",12345L));
        tx1.commit();
        session1.close();
        HibernateUtil.evictAllRegions();

        // Get recently stored value
        // Data is expected to be fetched from L2 Cache, not from database.
        // L2C hit is expected
        session2 = HibernateUtil.getSession();
        tx2 = session2.getTransaction();
        tx2.begin();
        item = session2.get(Item.class,1);
        session2.close();
        System.out.println("Item fetched: "+ item);

        // Get last accessed data from another session.
        // Data is expected to be fetched from L2 Cache, not from database.
        // L2C hit is expected
        session3 = HibernateUtil.getSession();
        tx3 = session3.getTransaction();
        tx3.begin();
        item = session3.get(Item.class,1);
        System.out.println("Item fetched: "+ item);
        tx3.commit();
        session3.close();

        // Evict the cache
        HibernateUtil.evictAllRegions();

        // Data is expected to be fetched from database, not from L2C.
        // L2C miss & put are expected
        session4 = HibernateUtil.getSession();
        tx4 = session4.getTransaction();
        tx4.begin();
        item = session4.get(Item.class,1);
        System.out.println("Item fetched: "+ item);
        tx4.commit();
        session4.close();

        HibernateUtil.closeFactory();
        Hazelcast.shutdownAll();

    }


}
