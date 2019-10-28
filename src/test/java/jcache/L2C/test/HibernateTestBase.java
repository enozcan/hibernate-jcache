package jcache.L2C.test;

import jcache.L2C.test.entity.Item;
import jcache.L2C.test.entity.SubItem;
import org.hibernate.Session;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Rule;

public class HibernateTestBase {

    public final SessionFactoryRule sfr;

    HibernateTestBase(boolean useHazelcastClient){
        sfr = new SessionFactoryRule(useHazelcastClient);
        insertEntries();
    }

    public void insertEntries(){

        SubItem subItem1 = new SubItem("subitem-1",1);
        SubItem subItem2 = new SubItem("subitem-2",2);
        SubItem subItem3 = new SubItem("subitem-3",3);
        SubItem subItem4 = new SubItem("subitem-4",4);
        SubItem subItem5 = new SubItem("subitem-5",5);
        SubItem subItem6 = new SubItem("subitem-6",6);
        SubItem subItem7 = new SubItem("subitem-7",7);
        SubItem subItem8 = new SubItem("subitem-8",8);
        SubItem subItem9 = new SubItem("subitem-9",9);
        SubItem subItem10 = new SubItem("subitem-10",10);

        Item item1 = new Item("item-1",1).addSubItem(subItem1);
        Item item2 = new Item("item-2",2).addSubItem(subItem2).addSubItem(subItem3);
        Item item3 = new Item("item-3",3).addSubItem(subItem4).addSubItem(subItem5).addSubItem(subItem6);
        Item item4 = new Item("item-4",4).addSubItem(subItem7).addSubItem(subItem8).addSubItem(subItem9).addSubItem(subItem10);

        subItem1.setItem(item1);
        subItem2.setItem(item2);
        subItem3.setItem(item3);
        subItem3.setItem(item3);

        Session session = sfr.getSession();

        session.save(item1);
        session.save(item2);
        session.save(item3);
        session.save(item4);

        sfr.commit();
        session.getSessionFactory().getCache().evictAllRegions();
        sfr.changeSession();

    }

    @After
    public void evictCache(){
        sfr.evictCache();
    }


}
