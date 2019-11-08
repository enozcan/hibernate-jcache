package jcache.L2C.entity;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;


@Entity
@Cache(region = "SubItem-Cache",usage = CacheConcurrencyStrategy.READ_WRITE)
public class SubItem {

    @Id
    @Column(name = "SUBITEM_ID")
    private int id;

    @Column(name = "SUBITEM_NAME", nullable = false)
    private String name;


    @ManyToOne
    @JoinColumn(name = "ITEM_ID")
    private Item item;

    public SubItem(int id, String name, Item item) {
        this.id = id;
        this.name = name;
        this.item = item;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public SubItem() {
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


    @Override
    public String toString() {
        return "SubItem{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", item=" + item +
                '}';
    }
}
