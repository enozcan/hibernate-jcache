package jcache.sample.server.entity;

public class Item {

    private int id;

    private String name;

    private long price;

    public Item() {
    }

    public Item(int id, String name, long price) {
        this.id = id;
        this.name = name;
        this.price = price;
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

    public long getPrice() {
        return price;
    }


    public void setPrice(long price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "hazelcast.hibernate.jcache.Entity.Item{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                '}';
    }
}
