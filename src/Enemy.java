public class Enemy {    // 적 위치 파악 및 이동
    int x;
    int y;
    int hp;

    Enemy(int x, int y) {
        this.x = x;
        this.y = y;
        hp = 10;
    }

    public void move() {
        x-= 5;
    }
}
