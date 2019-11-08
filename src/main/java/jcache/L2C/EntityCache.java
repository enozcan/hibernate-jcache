package jcache.L2C;

import com.hazelcast.core.Hazelcast;
import jcache.L2C.entity.Item;
import jcache.L2C.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class EntityCache {

    public static void main(String[] args){

        Session session1, session2, session3, session4;
        Transaction tx1;

        // Store an hazelcast.hibernate.jcache.L2C.Entity.Item with id = 1
        // L2C put is expected
        session1 = HibernateUtil.getSession();
        tx1 = session1.getTransaction();
        tx1.begin();
        session1.save(new Item("item-1",1));
        tx1.commit();
        session1.close();

        // Evict cache and clear statistics
        HibernateUtil.evictAllRegions();

        // Get recently stored value. Since the cache is evicted,
        // data is expected to be fetched from DB, not from the cache.
        // L2C miss & put are expected
        session2 = HibernateUtil.getSession();
        session2.beginTransaction();
        session2.get(Item.class,1);
        session2.close();
        HibernateUtil.printStats();

        // Get last accessed data from another session.
        // Data is expected to be fetched from L2 Cache, not from database.
        // L2C hit is expected
        session3 = HibernateUtil.getSession();
        session3.beginTransaction();
        session3.get(Item.class,1);
        session3.close();
        HibernateUtil.printStats();

        // Evict the cache and clear statistics
        HibernateUtil.evictAllRegions();

        // Data is expected to be fetched from database, not from L2C.
        // L2C miss & put are expected
        session4 = HibernateUtil.getSession();
        session4.beginTransaction();;
        session4.get(Item.class,1);
        session4.close();
        HibernateUtil.printStats();

        HibernateUtil.closeFactory();
        Hazelcast.shutdownAll();

    }


}
