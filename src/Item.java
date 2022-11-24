public class Item {
    int x;
    int y;
    int type;
    Item(int x, int y) {
        this.x = x;
        this.y = y;
        this.type = (int)(Math.random()*3);
    }

    public void move() {
        x-= 4;
    }
}
