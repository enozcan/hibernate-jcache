package jcache.L2C.util;

import com.hazelcast.cache.CacheStatistics;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.stat.CacheRegionStatistics;
import org.hibernate.stat.Statistics;

public class HibernateUtil {

    private static SessionFactory sessionFactory;

    private static Session currentSession;

    static {
        try {
            Configuration cfg = new Configuration();
            cfg.configure("hibernate.cfg.xml"); //default URI: "hibernate.cfg.xml"
            sessionFactory = cfg.buildSessionFactory();
        } catch (HibernateException e) {
            e.printStackTrace();
        }
    }

    public static Session getSession(){
        assert !sessionFactory.isClosed();
        currentSession = sessionFactory.getCurrentSession();
        return currentSession == null ? sessionFactory.openSession() : currentSession;
    }

    public static void evictAllRegions(){
        assert !sessionFactory.isClosed();
        sessionFactory.getCache().evictAllRegions();
        sessionFactory.getStatistics().clear();
    }

    public static void closeFactory(){
        assert !sessionFactory.isClosed();
        sessionFactory.close();
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void printStats(){
        System.out.println("[L2C Hits]: " + sessionFactory.getStatistics().getSecondLevelCacheHitCount());
        System.out.println("[L2C Miss]: " + sessionFactory.getStatistics().getSecondLevelCacheMissCount());
        System.out.println("[L2C Put]: " + sessionFactory.getStatistics().getSecondLevelCachePutCount());
    }

    public static void printQueryCacheStats(){
        System.out.println("[Query Cache Hits]: " + sessionFactory.getStatistics().getQueryCacheHitCount());
        System.out.println("[Query Cache Miss]: " + sessionFactory.getStatistics().getQueryCacheMissCount());
        System.out.println("[Query Cache Put]: " + sessionFactory.getStatistics().getQueryCachePutCount());
    }

    public static void printCollectionCacheStats(){
        CacheRegionStatistics cs = HibernateUtil.getSessionFactory().getStatistics().getCacheRegionStatistics("SubItems-Collection-Cache");
        System.out.println("[Collection Cache Hits]: " + cs.getHitCount());
        System.out.println("[Collection Cache Miss]: " + cs.getMissCount());
        System.out.println("[Collection Cache Put]: " + cs.getPutCount());


    }
}
