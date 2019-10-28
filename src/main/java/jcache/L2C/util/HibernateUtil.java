package jcache.L2C.util;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
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
    }

    public static void closeFactory(){
        assert !sessionFactory.isClosed();
        sessionFactory.close();
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
