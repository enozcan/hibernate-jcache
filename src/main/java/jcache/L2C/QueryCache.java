package jcache.L2C;

import com.hazelcast.core.Hazelcast;
import jcache.L2C.entity.Item;
import jcache.L2C.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.List;

public class QueryCache {


    private static final String QUERY_STRING = "select i from Item i where i.id < 102";
    private static final String QUERY_STRING2 = "select i from Item i where i.id > 101";
    private static final String QUERY_CACHE_REGION = "Item-Query-Cache";

    public static void main(String[] args){

        Session session1, session2, session3, session4;
        Transaction tx1;

        // Store Items with id = 100,101 and 102
        session1 = HibernateUtil.getSession();
        tx1 = session1.getTransaction();
        tx1.begin();
        session1.save(new Item("New Hazelcast Item-0",100));
        session1.save(new Item("New Hazelcast Item-1",101));
        session1.save(new Item("New Hazelcast Item-2",102));
        tx1.commit();
        session1.close();

        // Evict cache and clear statistics
        HibernateUtil.evictAllRegions();

        // Get recently stored values via query.
        // Data is expected to be fetched from database, not from L2C since evicted.
        // QueryCache miss & put are expected
        session2 = HibernateUtil.getSession();
        executeQuery(QUERY_STRING,session2);
        session2.close();
        HibernateUtil.printQueryCacheStats();

        // Execute last executed query again from another session.
        // Data is expected to be fetched from query Cache, not from database.
        // QueryCache hit is expected
        session3 = HibernateUtil.getSession();
        executeQuery(QUERY_STRING,session3);
        session3.close();
        HibernateUtil.printQueryCacheStats();

        // Execute last different query.
        // QueryCache miss & put are expected
        session4 = HibernateUtil.getSession();
        executeQuery(QUERY_STRING2,session4);
        session4.close();
        HibernateUtil.printQueryCacheStats();

        // Tear down
        HibernateUtil.closeFactory();
        Hazelcast.shutdownAll();
    }

    private static List<Item> executeQuery(String query, Session session){
        Transaction tx;
        tx = session.getTransaction();
        tx.begin();
        return (List<Item>) session.createQuery(query)
                .setMaxResults(10).setCacheable(true).setCacheRegion(QUERY_CACHE_REGION).list();
    }

}
