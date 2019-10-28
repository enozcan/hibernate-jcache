package jcache.L2C.test.entity;

import jcache.L2C.entity.Department;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cache;

import javax.persistence.*;


@Entity
@Cache(region = "SubItem-Cache",usage = CacheConcurrencyStrategy.READ_WRITE)
public class SubItem {

    @Id
    @Column(name = "SUBITEM_ID")
    private int id;

    @Column(name = "SUBITEM_NAME", nullable = false)
    private String name;


    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "item")
    private Item item;


    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public SubItem() {
    }

    public SubItem(String name, int id) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
