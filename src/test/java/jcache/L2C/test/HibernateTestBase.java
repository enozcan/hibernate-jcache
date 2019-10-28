package jcache.L2C.test;

import jcache.L2C.test.entity.Item;
import jcache.L2C.test.entity.SubItem;
import org.hibernate.Session;
import org.junit.After;

import java.util.ArrayList;
import java.util.List;

public class HibernateTestBase {

    public final SessionFactoryUtil sfr;

    HibernateTestBase(boolean useHazelcastClient){
        sfr = new SessionFactoryUtil(useHazelcastClient);
        insertEntries();
    }

    public void insertEntries(){

        Item dpt = new Item("asdf",31);

        SubItem employee1 = new SubItem(1,"sub1",dpt);
        SubItem employee2 = new SubItem(2,"sub2",dpt);

        /*List<SubItem> employeeList = new ArrayList<SubItem>();
        employeeList.add(employee1);
        employeeList.add(employee2);
        dpt.setSubItems(employeeList);
        */

        dpt.addSubItem(employee1).addSubItem(employee2);
        Session session = sfr.getSession();
        session.save(dpt);

        /*
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

        Item item1 = new Item("item-1",1);//.addSubItem(subItem1);
        ArrayList<SubItem> str = new ArrayList<SubItem>();
        str.add(subItem4);
        str.add(subItem5);
        str.add(subItem6);
        item1.setSubItems(str);



        Item item2 = new Item("item-2",2);//.addSubItem(subItem2).addSubItem(subItem3);
        Item item3 = new Item("item-3",3).addSubItem(subItem4).addSubItem(subItem5).addSubItem(subItem6);
        Item item4 = new Item("item-4",4).addSubItem(subItem7).addSubItem(subItem8).addSubItem(subItem9).addSubItem(subItem10);

        subItem2.setItem(item2);
        subItem3.setItem(item3);
        subItem3.setItem(item3);

        Session session = sfr.getSession();

        subItem1.setItem(item1);
        session.save(item1);
        session.save(item2);
        session.save(item3);
        session.save(item4);*/

        sfr.commit();
        sfr.changeSession();
        sfr.evictCache();

    }

    @After
    public void evictCache(){
        sfr.evictCache();
    }


}
