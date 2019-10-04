package jcache.sample.server;

import jcache.sample.server.entity.Item;
import jcache.sample.server.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import static java.lang.System.exit;


// TODO: Cache region names
public class L2Cache {

    public static void main(String[] args){

        Session session1, session2, session3;
        Transaction tx1, tx2, tx3;
        Item item;


        // Store an hazelcast.hibernate.jcache.Entity.Item with id = 1
        // L2C miss and put are expected
        session1 = HibernateUtil.getSession();
        tx1 = session1.getTransaction();
        tx1.begin();
        session1.save(new Item(1,"New Hazelcast Item",12345L));
        tx1.commit();
        System.out.println("SESSION 1, Factory L2C stats:" + HibernateUtil.getSecondLevelCacheStats());
        session1.close();

        // Get recently stored value
        // Data is expected to be fetched from L2 Cache, not from database.
        // L2C hit is expected
        session2 = HibernateUtil.getSession();
        tx2 = session2.getTransaction();
        tx2.begin();
        item = session2.get(Item.class,1);
        System.out.println("Item fetched: "+ item);
        System.out.println("SESSION 2, Factory L2C stats:" + HibernateUtil.getSecondLevelCacheStats());
        session2.close();

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
        System.out.println("SESSION 3, Factory L2C stats:" + HibernateUtil.getSecondLevelCacheStats());



        HibernateUtil.closeFactory();
        exit(0);

    }


}
